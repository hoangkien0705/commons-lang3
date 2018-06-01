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

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.Validate;


// TODO: Before making public move from getDateTimeInstance(Integer,...) to int; or some other approach.
abstract class FormatCache<F extends Format> {

    
    static final int NONE= -1;

    private final ConcurrentMap<MultipartKey, F> cInstanceCache
        = new ConcurrentHashMap<>(7);

    private static final ConcurrentMap<MultipartKey, String> cDateTimeInstanceCache
        = new ConcurrentHashMap<>(7);

    
    public F getInstance() {
        return getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, TimeZone.getDefault(), Locale.getDefault());
    }

    
    public F getInstance(final String pattern, TimeZone timeZone, Locale locale) {
        Validate.notNull(pattern, "pattern must not be null");
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        if (locale == null) {
            locale = Locale.getDefault();
        }
        final MultipartKey key = new MultipartKey(pattern, timeZone, locale);
        F format = cInstanceCache.get(key);
        if (format == null) {
            format = createInstance(pattern, timeZone, locale);
            final F previousValue= cInstanceCache.putIfAbsent(key, format);
            if (previousValue != null) {
                // another thread snuck in and did the same work
                // we should return the instance that is in ConcurrentMap
                format= previousValue;
            }
        }
        return format;
    }

    
    protected abstract F createInstance(String pattern, TimeZone timeZone, Locale locale);

    
    // This must remain private, see LANG-884
    private F getDateTimeInstance(final Integer dateStyle, final Integer timeStyle, final TimeZone timeZone, Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        final String pattern = getPatternForStyle(dateStyle, timeStyle, locale);
        return getInstance(pattern, timeZone, locale);
    }

    
    // package protected, for access from FastDateFormat; do not make public or protected
    F getDateTimeInstance(final int dateStyle, final int timeStyle, final TimeZone timeZone, final Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), Integer.valueOf(timeStyle), timeZone, locale);
    }

    
    // package protected, for access from FastDateFormat; do not make public or protected
    F getDateInstance(final int dateStyle, final TimeZone timeZone, final Locale locale) {
        return getDateTimeInstance(Integer.valueOf(dateStyle), null, timeZone, locale);
    }

    
    // package protected, for access from FastDateFormat; do not make public or protected
    F getTimeInstance(final int timeStyle, final TimeZone timeZone, final Locale locale) {
        return getDateTimeInstance(null, Integer.valueOf(timeStyle), timeZone, locale);
    }

    
    // package protected, for access from test code; do not make public or protected
    static String getPatternForStyle(final Integer dateStyle, final Integer timeStyle, final Locale locale) {
        final MultipartKey key = new MultipartKey(dateStyle, timeStyle, locale);

        String pattern = cDateTimeInstanceCache.get(key);
        if (pattern == null) {
            try {
                DateFormat formatter;
                if (dateStyle == null) {
                    formatter = DateFormat.getTimeInstance(timeStyle.intValue(), locale);
                } else if (timeStyle == null) {
                    formatter = DateFormat.getDateInstance(dateStyle.intValue(), locale);
                } else {
                    formatter = DateFormat.getDateTimeInstance(dateStyle.intValue(), timeStyle.intValue(), locale);
                }
                pattern = ((SimpleDateFormat)formatter).toPattern();
                final String previous = cDateTimeInstanceCache.putIfAbsent(key, pattern);
                if (previous != null) {
                    // even though it doesn't matter if another thread put the pattern
                    // it's still good practice to return the String instance that is
                    // actually in the ConcurrentMap
                    pattern= previous;
                }
            } catch (final ClassCastException ex) {
                throw new IllegalArgumentException("No date time pattern for locale: " + locale);
            }
        }
        return pattern;
    }

    // ----------------------------------------------------------------------
    
    private static class MultipartKey {
        private final Object[] keys;
        private int hashCode;

        
        MultipartKey(final Object... keys) {
            this.keys = keys;
        }

        
        @Override
        public boolean equals(final Object obj) {
            // Eliminate the usual boilerplate because
            // this inner static class is only used in a generic ConcurrentHashMap
            // which will not compare against other Object types
            return Arrays.equals(keys, ((MultipartKey)obj).keys);
        }

        
        @Override
        public int hashCode() {
            if(hashCode==0) {
                int rc= 0;
                for(final Object key : keys) {
                    if(key!=null) {
                        rc= rc*7 + key.hashCode();
                    }
                }
                hashCode= rc;
            }
            return hashCode;
        }
    }

}
