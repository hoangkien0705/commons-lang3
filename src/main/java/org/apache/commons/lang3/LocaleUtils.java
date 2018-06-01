
package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LocaleUtils {

    
    private static final ConcurrentMap<String, List<Locale>> cLanguagesByCountry =
        new ConcurrentHashMap<>();

    
    private static final ConcurrentMap<String, List<Locale>> cCountriesByLanguage =
        new ConcurrentHashMap<>();

    
    public LocaleUtils() {
      super();
    }

    //-----------------------------------------------------------------------
    
    public static Locale toLocale(final String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) { // LANG-941 - JDK 8 introduced an empty locale where all fields are blank
            return new Locale(StringUtils.EMPTY, StringUtils.EMPTY);
        }
        if (str.contains("#")) { // LANG-879 - Cannot handle Java 7 script & extensions
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final int len = str.length();
        if (len < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch0 = str.charAt(0);
        if (ch0 == '_') {
            if (len < 3) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            final char ch1 = str.charAt(1);
            final char ch2 = str.charAt(2);
            if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 3) {
                return new Locale(StringUtils.EMPTY, str.substring(1, 3));
            }
            if (len < 5) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (str.charAt(3) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale(StringUtils.EMPTY, str.substring(1, 3), str.substring(4));
        }

        return parseLocale(str);
    }

    
    private static Locale parseLocale(final String str) {
        if (isISO639LanguageCode(str)) {
            return new Locale(str);
        }

        final String[] segments = str.split("_", -1);
        final String language = segments[0];
        if (segments.length == 2) {
            final String country = segments[1];
            if (isISO639LanguageCode(language) && isISO3166CountryCode(country) ||
                    isNumericAreaCode(country)) {
                return new Locale(language, country);
            }
        } else if (segments.length == 3) {
            final String country = segments[1];
            final String variant = segments[2];
            if (isISO639LanguageCode(language) &&
                    (country.length() == 0 || isISO3166CountryCode(country) || isNumericAreaCode(country)) &&
                    variant.length() > 0) {
                return new Locale(language, country, variant);
            }
        }
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    
    private static boolean isISO639LanguageCode(final String str) {
        return StringUtils.isAllLowerCase(str) && (str.length() == 2 || str.length() == 3);
    }

    
    private static boolean isISO3166CountryCode(final String str) {
        return StringUtils.isAllUpperCase(str) && str.length() == 2;
    }

    
    private static boolean isNumericAreaCode(final String str) {
        return StringUtils.isNumeric(str) && str.length() == 3;
    }

    //-----------------------------------------------------------------------
    
    public static List<Locale> localeLookupList(final Locale locale) {
        return localeLookupList(locale, locale);
    }

    //-----------------------------------------------------------------------
    
    public static List<Locale> localeLookupList(final Locale locale, final Locale defaultLocale) {
        final List<Locale> list = new ArrayList<>(4);
        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length() > 0) {
                list.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }
            if (locale.getCountry().length() > 0) {
                list.add(new Locale(locale.getLanguage(), StringUtils.EMPTY));
            }
            if (!list.contains(defaultLocale)) {
                list.add(defaultLocale);
            }
        }
        return Collections.unmodifiableList(list);
    }

    //-----------------------------------------------------------------------
    
    public static List<Locale> availableLocaleList() {
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    //-----------------------------------------------------------------------
    
    public static Set<Locale> availableLocaleSet() {
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    //-----------------------------------------------------------------------
    
    public static boolean isAvailableLocale(final Locale locale) {
        return availableLocaleList().contains(locale);
    }

    //-----------------------------------------------------------------------
    
    public static List<Locale> languagesByCountry(final String countryCode) {
        if (countryCode == null) {
            return Collections.emptyList();
        }
        List<Locale> langs = cLanguagesByCountry.get(countryCode);
        if (langs == null) {
            langs = new ArrayList<>();
            final List<Locale> locales = availableLocaleList();
            for (final Locale locale : locales) {
                if (countryCode.equals(locale.getCountry()) &&
                    locale.getVariant().isEmpty()) {
                    langs.add(locale);
                }
            }
            langs = Collections.unmodifiableList(langs);
            cLanguagesByCountry.putIfAbsent(countryCode, langs);
            langs = cLanguagesByCountry.get(countryCode);
        }
        return langs;
    }

    //-----------------------------------------------------------------------
    
    public static List<Locale> countriesByLanguage(final String languageCode) {
        if (languageCode == null) {
            return Collections.emptyList();
        }
        List<Locale> countries = cCountriesByLanguage.get(languageCode);
        if (countries == null) {
            countries = new ArrayList<>();
            final List<Locale> locales = availableLocaleList();
            for (final Locale locale : locales) {
                if (languageCode.equals(locale.getLanguage()) &&
                    locale.getCountry().length() != 0 &&
                    locale.getVariant().isEmpty()) {
                    countries.add(locale);
                }
            }
            countries = Collections.unmodifiableList(countries);
            cCountriesByLanguage.putIfAbsent(languageCode, countries);
            countries = cCountriesByLanguage.get(languageCode);
        }
        return countries;
    }

    //-----------------------------------------------------------------------
    // class to avoid synchronization (Init on demand)
    static class SyncAvoid {
        
        private static final List<Locale> AVAILABLE_LOCALE_LIST;
        
        private static final Set<Locale> AVAILABLE_LOCALE_SET;

        static {
            final List<Locale> list = new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));  // extra safe
            AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(list);
            AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet<>(list));
        }
    }

}
