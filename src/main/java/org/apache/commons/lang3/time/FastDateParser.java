/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.time;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FastDateParser implements DateParser, Serializable {

    
    private static final long serialVersionUID = 3L;

    static final Locale JAPANESE_IMPERIAL = new Locale("ja","JP","JP");

    // defining fields
    private final String pattern;
    private final TimeZone timeZone;
    private final Locale locale;
    private final int century;
    private final int startYear;

    // derived fields
    private transient List<StrategyAndWidth> patterns;

    // comparator used to sort regex alternatives
    // alternatives should be ordered longer first, and shorter last. ('february' before 'feb')
    // all entries must be lowercase by locale.
    private static final Comparator<String> LONGER_FIRST_LOWERCASE = new Comparator<String>() {
        @Override
        public int compare(final String left, final String right) {
            return right.compareTo(left);
        }
    };

    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale) {
        this(pattern, timeZone, locale, null);
    }

    
    protected FastDateParser(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
        this.pattern = pattern;
        this.timeZone = timeZone;
        this.locale = locale;

        final Calendar definingCalendar = Calendar.getInstance(timeZone, locale);

        int centuryStartYear;
        if(centuryStart!=null) {
            definingCalendar.setTime(centuryStart);
            centuryStartYear= definingCalendar.get(Calendar.YEAR);
        } else if(locale.equals(JAPANESE_IMPERIAL)) {
            centuryStartYear= 0;
        } else {
            // from 80 years ago to 20 years from now
            definingCalendar.setTime(new Date());
            centuryStartYear= definingCalendar.get(Calendar.YEAR)-80;
        }
        century= centuryStartYear / 100 * 100;
        startYear= centuryStartYear - century;

        init(definingCalendar);
    }

    
    private void init(final Calendar definingCalendar) {
        patterns = new ArrayList<>();

        final StrategyParser fm = new StrategyParser(definingCalendar);
        for(;;) {
            final StrategyAndWidth field = fm.getNextStrategy();
            if(field==null) {
                break;
            }
            patterns.add(field);
        }
    }

    // helper classes to parse the format string
    //-----------------------------------------------------------------------

    
    private static class StrategyAndWidth {
        final Strategy strategy;
        final int width;

        StrategyAndWidth(final Strategy strategy, final int width) {
            this.strategy = strategy;
            this.width = width;
        }

        int getMaxWidth(final ListIterator<StrategyAndWidth> lt) {
            if(!strategy.isNumber() || !lt.hasNext()) {
                return 0;
            }
            final Strategy nextStrategy = lt.next().strategy;
            lt.previous();
            return nextStrategy.isNumber() ?width :0;
       }
    }

    
    private class StrategyParser {
        private final Calendar definingCalendar;
        private int currentIdx;

        StrategyParser(final Calendar definingCalendar) {
            this.definingCalendar = definingCalendar;
        }

        StrategyAndWidth getNextStrategy() {
            if (currentIdx >= pattern.length()) {
                return null;
            }

            final char c = pattern.charAt(currentIdx);
            if (isFormatLetter(c)) {
                return letterPattern(c);
            }
            return literal();
        }

        private StrategyAndWidth letterPattern(final char c) {
            final int begin = currentIdx;
            while (++currentIdx < pattern.length()) {
                if (pattern.charAt(currentIdx) != c) {
                    break;
                }
            }

            final int width = currentIdx - begin;
            return new StrategyAndWidth(getStrategy(c, width, definingCalendar), width);
        }

        private StrategyAndWidth literal() {
            boolean activeQuote = false;

            final StringBuilder sb = new StringBuilder();
            while (currentIdx < pattern.length()) {
                final char c = pattern.charAt(currentIdx);
                if (!activeQuote && isFormatLetter(c)) {
                    break;
                } else if (c == '\'' && (++currentIdx == pattern.length() || pattern.charAt(currentIdx) != '\'')) {
                    activeQuote = !activeQuote;
                    continue;
                }
                ++currentIdx;
                sb.append(c);
            }

            if (activeQuote) {
                throw new IllegalArgumentException("Unterminated quote");
            }

            final String formatField = sb.toString();
            return new StrategyAndWidth(new CopyQuotedStrategy(formatField), formatField.length());
        }
    }

    private static boolean isFormatLetter(final char c) {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    // Accessors
    //-----------------------------------------------------------------------
    
    @Override
    public String getPattern() {
        return pattern;
    }

    
    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    
    @Override
    public Locale getLocale() {
        return locale;
    }


    // Basics
    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDateParser)) {
            return false;
        }
        final FastDateParser other = (FastDateParser) obj;
        return pattern.equals(other.pattern)
            && timeZone.equals(other.timeZone)
            && locale.equals(other.locale);
    }

    
    @Override
    public int hashCode() {
        return pattern.hashCode() + 13 * (timeZone.hashCode() + 13 * locale.hashCode());
    }

    
    @Override
    public String toString() {
        return "FastDateParser[" + pattern + "," + locale + "," + timeZone.getID() + "]";
    }

    // Serializing
    //-----------------------------------------------------------------------
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        final Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
        init(definingCalendar);
    }

    
    @Override
    public Object parseObject(final String source) throws ParseException {
        return parse(source);
    }

    
    @Override
    public Date parse(final String source) throws ParseException {
        final ParsePosition pp = new ParsePosition(0);
        final Date date= parse(source, pp);
        if (date == null) {
            // Add a note re supported date range
            if (locale.equals(JAPANESE_IMPERIAL)) {
                throw new ParseException(
                        "(The " +locale + " locale does not support dates before 1868 AD)\n" +
                                "Unparseable date: \""+source, pp.getErrorIndex());
            }
            throw new ParseException("Unparseable date: "+source, pp.getErrorIndex());
        }
        return date;
    }

    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return parse(source, pos);
    }

    
    @Override
    public Date parse(final String source, final ParsePosition pos) {
        // timing tests indicate getting new instance is 19% faster than cloning
        final Calendar cal= Calendar.getInstance(timeZone, locale);
        cal.clear();

        return parse(source, pos, cal) ? cal.getTime() : null;
    }

    
    @Override
    public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
        final ListIterator<StrategyAndWidth> lt = patterns.listIterator();
        while (lt.hasNext()) {
            final StrategyAndWidth strategyAndWidth = lt.next();
            final int maxWidth = strategyAndWidth.getMaxWidth(lt);
            if (!strategyAndWidth.strategy.parse(this, calendar, source, pos, maxWidth)) {
                return false;
            }
        }
        return true;
    }

    // Support for strategies
    //-----------------------------------------------------------------------

    private static StringBuilder simpleQuote(final StringBuilder sb, final String value) {
        for (int i = 0; i < value.length(); ++i) {
            final char c = value.charAt(i);
            switch (c) {
            case '\\':
            case '^':
            case '$':
            case '.':
            case '|':
            case '?':
            case '*':
            case '+':
            case '(':
            case ')':
            case '[':
            case '{':
                sb.append('\\');
            default:
                sb.append(c);
            }
        }
        return sb;
    }

    
    private static Map<String, Integer> appendDisplayNames(final Calendar cal, final Locale locale, final int field, final StringBuilder regex) {
        final Map<String, Integer> values = new HashMap<>();

        final Map<String, Integer> displayNames = cal.getDisplayNames(field, Calendar.ALL_STYLES, locale);
        final TreeSet<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);
        for (final Map.Entry<String, Integer> displayName : displayNames.entrySet()) {
            final String key = displayName.getKey().toLowerCase(locale);
            if (sorted.add(key)) {
                values.put(key, displayName.getValue());
            }
        }
        for (final String symbol : sorted) {
            simpleQuote(regex, symbol).append('|');
        }
        return values;
    }

    
    private int adjustYear(final int twoDigitYear) {
        final int trial = century + twoDigitYear;
        return twoDigitYear >= startYear ? trial : trial + 100;
    }

    
    private abstract static class Strategy {
        
        boolean isNumber() {
            return false;
        }

        abstract boolean parse(FastDateParser parser, Calendar calendar, String source, ParsePosition pos, int maxWidth);
    }

    
    private abstract static class PatternStrategy extends Strategy {

        private Pattern pattern;

        void createPattern(final StringBuilder regex) {
            createPattern(regex.toString());
        }

        void createPattern(final String regex) {
            this.pattern = Pattern.compile(regex);
        }

        
        @Override
        boolean isNumber() {
            return false;
        }

        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            final Matcher matcher = pattern.matcher(source.substring(pos.getIndex()));
            if (!matcher.lookingAt()) {
                pos.setErrorIndex(pos.getIndex());
                return false;
            }
            pos.setIndex(pos.getIndex() + matcher.end(1));
            setCalendar(parser, calendar, matcher.group(1));
            return true;
        }

        abstract void setCalendar(FastDateParser parser, Calendar cal, String value);
    }

    
    private Strategy getStrategy(final char f, final int width, final Calendar definingCalendar) {
        switch(f) {
        default:
            throw new IllegalArgumentException("Format '"+f+"' not supported");
        case 'D':
            return DAY_OF_YEAR_STRATEGY;
        case 'E':
            return getLocaleSpecificStrategy(Calendar.DAY_OF_WEEK, definingCalendar);
        case 'F':
            return DAY_OF_WEEK_IN_MONTH_STRATEGY;
        case 'G':
            return getLocaleSpecificStrategy(Calendar.ERA, definingCalendar);
        case 'H':  // Hour in day (0-23)
            return HOUR_OF_DAY_STRATEGY;
        case 'K':  // Hour in am/pm (0-11)
            return HOUR_STRATEGY;
        case 'M':
            return width>=3 ?getLocaleSpecificStrategy(Calendar.MONTH, definingCalendar) :NUMBER_MONTH_STRATEGY;
        case 'S':
            return MILLISECOND_STRATEGY;
        case 'W':
            return WEEK_OF_MONTH_STRATEGY;
        case 'a':
            return getLocaleSpecificStrategy(Calendar.AM_PM, definingCalendar);
        case 'd':
            return DAY_OF_MONTH_STRATEGY;
        case 'h':  // Hour in am/pm (1-12), i.e. midday/midnight is 12, not 0
            return HOUR12_STRATEGY;
        case 'k':  // Hour in day (1-24), i.e. midnight is 24, not 0
            return HOUR24_OF_DAY_STRATEGY;
        case 'm':
            return MINUTE_STRATEGY;
        case 's':
            return SECOND_STRATEGY;
        case 'u':
            return DAY_OF_WEEK_STRATEGY;
        case 'w':
            return WEEK_OF_YEAR_STRATEGY;
        case 'y':
        case 'Y':
            return width>2 ?LITERAL_YEAR_STRATEGY :ABBREVIATED_YEAR_STRATEGY;
        case 'X':
            return ISO8601TimeZoneStrategy.getStrategy(width);
        case 'Z':
            if (width==2) {
                return ISO8601TimeZoneStrategy.ISO_8601_3_STRATEGY;
            }
            //$FALL-THROUGH$
        case 'z':
            return getLocaleSpecificStrategy(Calendar.ZONE_OFFSET, definingCalendar);
        }
    }

    @SuppressWarnings("unchecked") // OK because we are creating an array with no entries
    private static final ConcurrentMap<Locale, Strategy>[] caches = new ConcurrentMap[Calendar.FIELD_COUNT];

    
    private static ConcurrentMap<Locale, Strategy> getCache(final int field) {
        synchronized (caches) {
            if (caches[field] == null) {
                caches[field] = new ConcurrentHashMap<>(3);
            }
            return caches[field];
        }
    }

    
    private Strategy getLocaleSpecificStrategy(final int field, final Calendar definingCalendar) {
        final ConcurrentMap<Locale, Strategy> cache = getCache(field);
        Strategy strategy = cache.get(locale);
        if (strategy == null) {
            strategy = field == Calendar.ZONE_OFFSET
                    ? new TimeZoneStrategy(locale)
                    : new CaseInsensitiveTextStrategy(field, definingCalendar, locale);
            final Strategy inCache = cache.putIfAbsent(locale, strategy);
            if (inCache != null) {
                return inCache;
            }
        }
        return strategy;
    }

    
    private static class CopyQuotedStrategy extends Strategy {

        private final String formatField;

        
        CopyQuotedStrategy(final String formatField) {
            this.formatField = formatField;
        }

        
        @Override
        boolean isNumber() {
            return false;
        }

        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            for (int idx = 0; idx < formatField.length(); ++idx) {
                final int sIdx = idx + pos.getIndex();
                if (sIdx == source.length()) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
                if (formatField.charAt(idx) != source.charAt(sIdx)) {
                    pos.setErrorIndex(sIdx);
                    return false;
                }
            }
            pos.setIndex(formatField.length() + pos.getIndex());
            return true;
        }
    }

    
     private static class CaseInsensitiveTextStrategy extends PatternStrategy {
        private final int field;
        final Locale locale;
        private final Map<String, Integer> lKeyValues;

        
        CaseInsensitiveTextStrategy(final int field, final Calendar definingCalendar, final Locale locale) {
            this.field = field;
            this.locale = locale;

            final StringBuilder regex = new StringBuilder();
            regex.append("((?iu)");
            lKeyValues = appendDisplayNames(definingCalendar, locale, field, regex);
            regex.setLength(regex.length()-1);
            regex.append(")");
            createPattern(regex);
        }

        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            final Integer iVal = lKeyValues.get(value.toLowerCase(locale));
            cal.set(field, iVal.intValue());
        }
    }


    
    private static class NumberStrategy extends Strategy {
        private final int field;

        
        NumberStrategy(final int field) {
             this.field= field;
        }

        
        @Override
        boolean isNumber() {
            return true;
        }

        @Override
        boolean parse(final FastDateParser parser, final Calendar calendar, final String source, final ParsePosition pos, final int maxWidth) {
            int idx = pos.getIndex();
            int last = source.length();

            if (maxWidth == 0) {
                // if no maxWidth, strip leading white space
                for (; idx < last; ++idx) {
                    final char c = source.charAt(idx);
                    if (!Character.isWhitespace(c)) {
                        break;
                    }
                }
                pos.setIndex(idx);
            } else {
                final int end = idx + maxWidth;
                if (last > end) {
                    last = end;
                }
            }

            for (; idx < last; ++idx) {
                final char c = source.charAt(idx);
                if (!Character.isDigit(c)) {
                    break;
                }
            }

            if (pos.getIndex() == idx) {
                pos.setErrorIndex(idx);
                return false;
            }

            final int value = Integer.parseInt(source.substring(pos.getIndex(), idx));
            pos.setIndex(idx);

            calendar.set(field, modify(parser, value));
            return true;
        }

        
        int modify(final FastDateParser parser, final int iValue) {
            return iValue;
        }

    }

    private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(Calendar.YEAR) {
        
        @Override
        int modify(final FastDateParser parser, final int iValue) {
            return iValue < 100 ? parser.adjustYear(iValue) : iValue;
        }
    };

    
    static class TimeZoneStrategy extends PatternStrategy {
        private static final String RFC_822_TIME_ZONE = "[+-]\\d{4}";
        private static final String GMT_OPTION = TimeZones.GMT_ID + "[+-]\\d{1,2}:\\d{2}";

        private final Locale locale;
        private final Map<String, TzInfo> tzNames= new HashMap<>();

        private static class TzInfo {
            TimeZone zone;
            int dstOffset;

            TzInfo(final TimeZone tz, final boolean useDst) {
                zone = tz;
                dstOffset = useDst ?tz.getDSTSavings() :0;
            }
        }

        
        private static final int ID = 0;

        
        TimeZoneStrategy(final Locale locale) {
            this.locale = locale;

            final StringBuilder sb = new StringBuilder();
            sb.append("((?iu)" + RFC_822_TIME_ZONE + "|" + GMT_OPTION );

            final Set<String> sorted = new TreeSet<>(LONGER_FIRST_LOWERCASE);

            final String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
            for (final String[] zoneNames : zones) {
                // offset 0 is the time zone ID and is not localized
                final String tzId = zoneNames[ID];
                if (tzId.equalsIgnoreCase(TimeZones.GMT_ID)) {
                    continue;
                }
                final TimeZone tz = TimeZone.getTimeZone(tzId);
                // offset 1 is long standard name
                // offset 2 is short standard name
                final TzInfo standard = new TzInfo(tz, false);
                TzInfo tzInfo = standard;
                for (int i = 1; i < zoneNames.length; ++i) {
                    switch (i) {
                    case 3: // offset 3 is long daylight savings (or summertime) name
                            // offset 4 is the short summertime name
                        tzInfo = new TzInfo(tz, true);
                        break;
                    case 5: // offset 5 starts additional names, probably standard time
                        tzInfo = standard;
                        break;
                    default:
                        break;
                    }
                    if (zoneNames[i] != null) {
                        final String key = zoneNames[i].toLowerCase(locale);
                        // ignore the data associated with duplicates supplied in
                        // the additional names
                        if (sorted.add(key)) {
                            tzNames.put(key, tzInfo);
                        }
                    }
                }
            }
            // order the regex alternatives with longer strings first, greedy
            // match will ensure longest string will be consumed
            for (final String zoneName : sorted) {
                simpleQuote(sb.append('|'), zoneName);
            }
            sb.append(")");
            createPattern(sb);
        }

        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String timeZone) {
            final TimeZone tz = FastTimeZone.getGmtTimeZone(timeZone);
            if (tz != null) {
                cal.setTimeZone(tz);
            } else {
                final TzInfo tzInfo = tzNames.get(timeZone.toLowerCase(locale));
                cal.set(Calendar.DST_OFFSET, tzInfo.dstOffset);
                cal.set(Calendar.ZONE_OFFSET, tzInfo.zone.getRawOffset());
            }
        }
    }

    private static class ISO8601TimeZoneStrategy extends PatternStrategy {
        // Z, +hh, -hh, +hhmm, -hhmm, +hh:mm or -hh:mm

        
        ISO8601TimeZoneStrategy(final String pattern) {
            createPattern(pattern);
        }

        
        @Override
        void setCalendar(final FastDateParser parser, final Calendar cal, final String value) {
            cal.setTimeZone(FastTimeZone.getGmtTimeZone(value));
        }

        private static final Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
        private static final Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
        private static final Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");

        
        static Strategy getStrategy(final int tokenLen) {
            switch(tokenLen) {
            case 1:
                return ISO_8601_1_STRATEGY;
            case 2:
                return ISO_8601_2_STRATEGY;
            case 3:
                return ISO_8601_3_STRATEGY;
            default:
                throw new IllegalArgumentException("invalid number of X");
            }
        }
    }

    private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(Calendar.MONTH) {
        @Override
        int modify(final FastDateParser parser, final int iValue) {
            return iValue-1;
        }
    };

    private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(Calendar.YEAR);
    private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(Calendar.WEEK_OF_YEAR);
    private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(Calendar.WEEK_OF_MONTH);
    private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(Calendar.DAY_OF_YEAR);
    private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(Calendar.DAY_OF_MONTH);
    private static final Strategy DAY_OF_WEEK_STRATEGY = new NumberStrategy(Calendar.DAY_OF_WEEK) {
        @Override
        int modify(final FastDateParser parser, final int iValue) {
            return iValue != 7 ? iValue + 1 : Calendar.SUNDAY;
        }
    };

    private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(Calendar.DAY_OF_WEEK_IN_MONTH);
    private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(Calendar.HOUR_OF_DAY);
    private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(Calendar.HOUR_OF_DAY) {
        @Override
        int modify(final FastDateParser parser, final int iValue) {
            return iValue == 24 ? 0 : iValue;
        }
    };

    private static final Strategy HOUR12_STRATEGY = new NumberStrategy(Calendar.HOUR) {
        @Override
        int modify(final FastDateParser parser, final int iValue) {
            return iValue == 12 ? 0 : iValue;
        }
    };

    private static final Strategy HOUR_STRATEGY = new NumberStrategy(Calendar.HOUR);
    private static final Strategy MINUTE_STRATEGY = new NumberStrategy(Calendar.MINUTE);
    private static final Strategy SECOND_STRATEGY = new NumberStrategy(Calendar.SECOND);
    private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(Calendar.MILLISECOND);
}
