
package org.apache.commons.lang3;

import java.util.regex.Pattern;


public class RegExUtils {

    
    public static String removeAll(final String text, final Pattern regex) {
        return replaceAll(text, regex, StringUtils.EMPTY);
    }

    
    public static String removeAll(final String text, final String regex) {
        return replaceAll(text, regex, StringUtils.EMPTY);
    }

    
    public static String removeFirst(final String text, final Pattern regex) {
        return replaceFirst(text, regex, StringUtils.EMPTY);
    }

    
    public static String removeFirst(final String text, final String regex) {
        return replaceFirst(text, regex, StringUtils.EMPTY);
    }

    
    public static String removePattern(final String text, final String regex) {
        return replacePattern(text, regex, StringUtils.EMPTY);
    }

    
    public static String replaceAll(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return regex.matcher(text).replaceAll(replacement);
    }

    
    public static String replaceAll(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return text.replaceAll(regex, replacement);
    }

    
    public static String replaceFirst(final String text, final Pattern regex, final String replacement) {
        if (text == null || regex == null|| replacement == null ) {
            return text;
        }
        return regex.matcher(text).replaceFirst(replacement);
    }

    
    public static String replaceFirst(final String text, final String regex, final String replacement) {
        if (text == null || regex == null|| replacement == null ) {
            return text;
        }
        return text.replaceFirst(regex, replacement);
    }

    
    public static String replacePattern(final String text, final String regex, final String replacement) {
        if (text == null || regex == null || replacement == null) {
            return text;
        }
        return Pattern.compile(regex, Pattern.DOTALL).matcher(text).replaceAll(replacement);
    }

}
