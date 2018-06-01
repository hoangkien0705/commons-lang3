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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class DateFormatUtils {

    
    private static final TimeZone UTC_TIME_ZONE = FastTimeZone.getGmtTimeZone();

    
    public static final FastDateFormat ISO_8601_EXTENDED_DATETIME_FORMAT
            = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss");

    
    @Deprecated
    public static final FastDateFormat ISO_DATETIME_FORMAT = ISO_8601_EXTENDED_DATETIME_FORMAT;

    
    public static final FastDateFormat ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT
            = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZZ");

    
    @Deprecated
    public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT;

    
    public static final FastDateFormat ISO_8601_EXTENDED_DATE_FORMAT
            = FastDateFormat.getInstance("yyyy-MM-dd");

    
    @Deprecated
    public static final FastDateFormat ISO_DATE_FORMAT = ISO_8601_EXTENDED_DATE_FORMAT;

    
    @Deprecated
    public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT
            = FastDateFormat.getInstance("yyyy-MM-ddZZ");

    
    @Deprecated
    public static final FastDateFormat ISO_TIME_FORMAT
            = FastDateFormat.getInstance("'T'HH:mm:ss");

    
    @Deprecated
    public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT
            = FastDateFormat.getInstance("'T'HH:mm:ssZZ");

    
    public static final FastDateFormat ISO_8601_EXTENDED_TIME_FORMAT
            = FastDateFormat.getInstance("HH:mm:ss");

    
    @Deprecated
    public static final FastDateFormat ISO_TIME_NO_T_FORMAT = ISO_8601_EXTENDED_TIME_FORMAT;

    
    public static final FastDateFormat ISO_8601_EXTENDED_TIME_TIME_ZONE_FORMAT
            = FastDateFormat.getInstance("HH:mm:ssZZ");

    
    @Deprecated
    public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = ISO_8601_EXTENDED_TIME_TIME_ZONE_FORMAT;

    
    public static final FastDateFormat SMTP_DATETIME_FORMAT
            = FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    //-----------------------------------------------------------------------
    
    public DateFormatUtils() {
        super();
    }

    
    public static String formatUTC(final long millis, final String pattern) {
        return format(new Date(millis), pattern, UTC_TIME_ZONE, null);
    }

    
    public static String formatUTC(final Date date, final String pattern) {
        return format(date, pattern, UTC_TIME_ZONE, null);
    }

    
    public static String formatUTC(final long millis, final String pattern, final Locale locale) {
        return format(new Date(millis), pattern, UTC_TIME_ZONE, locale);
    }

    
    public static String formatUTC(final Date date, final String pattern, final Locale locale) {
        return format(date, pattern, UTC_TIME_ZONE, locale);
    }

    
    public static String format(final long millis, final String pattern) {
        return format(new Date(millis), pattern, null, null);
    }

    
    public static String format(final Date date, final String pattern) {
        return format(date, pattern, null, null);
    }

    
    public static String format(final Calendar calendar, final String pattern) {
        return format(calendar, pattern, null, null);
    }

    
    public static String format(final long millis, final String pattern, final TimeZone timeZone) {
        return format(new Date(millis), pattern, timeZone, null);
    }

    
    public static String format(final Date date, final String pattern, final TimeZone timeZone) {
        return format(date, pattern, timeZone, null);
    }

    
    public static String format(final Calendar calendar, final String pattern, final TimeZone timeZone) {
        return format(calendar, pattern, timeZone, null);
    }

    
    public static String format(final long millis, final String pattern, final Locale locale) {
        return format(new Date(millis), pattern, null, locale);
    }

    
    public static String format(final Date date, final String pattern, final Locale locale) {
        return format(date, pattern, null, locale);
    }

    
    public static String format(final Calendar calendar, final String pattern, final Locale locale) {
        return format(calendar, pattern, null, locale);
    }

    
    public static String format(final long millis, final String pattern, final TimeZone timeZone, final Locale locale) {
        return format(new Date(millis), pattern, timeZone, locale);
    }

    
    public static String format(final Date date, final String pattern, final TimeZone timeZone, final Locale locale) {
        final FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(date);
    }

    
    public static String format(final Calendar calendar, final String pattern, final TimeZone timeZone, final Locale locale) {
        final FastDateFormat df = FastDateFormat.getInstance(pattern, timeZone, locale);
        return df.format(calendar);
    }

}
