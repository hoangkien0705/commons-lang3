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
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class FastDateFormat extends Format implements DateParser, DatePrinter {

    
    private static final long serialVersionUID = 2L;

    

    public static final int FULL = DateFormat.FULL;

    
    public static final int LONG = DateFormat.LONG;

    
    public static final int MEDIUM = DateFormat.MEDIUM;

    
    public static final int SHORT = DateFormat.SHORT;

    private static final FormatCache<FastDateFormat> cache= new FormatCache<FastDateFormat>() {
        @Override
        protected FastDateFormat createInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
            return new FastDateFormat(pattern, timeZone, locale);
        }
    };

    private final FastDatePrinter printer;
    private final FastDateParser parser;

    //-----------------------------------------------------------------------
    
    public static FastDateFormat getInstance() {
        return cache.getInstance();
    }

    
    public static FastDateFormat getInstance(final String pattern) {
        return cache.getInstance(pattern, null, null);
    }

    
    public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone) {
        return cache.getInstance(pattern, timeZone, null);
    }

    
    public static FastDateFormat getInstance(final String pattern, final Locale locale) {
        return cache.getInstance(pattern, null, locale);
    }

    
    public static FastDateFormat getInstance(final String pattern, final TimeZone timeZone, final Locale locale) {
        return cache.getInstance(pattern, timeZone, locale);
    }

    //-----------------------------------------------------------------------
    
    public static FastDateFormat getDateInstance(final int style) {
        return cache.getDateInstance(style, null, null);
    }

    
    public static FastDateFormat getDateInstance(final int style, final Locale locale) {
        return cache.getDateInstance(style, null, locale);
    }

    
    public static FastDateFormat getDateInstance(final int style, final TimeZone timeZone) {
        return cache.getDateInstance(style, timeZone, null);
    }

    
    public static FastDateFormat getDateInstance(final int style, final TimeZone timeZone, final Locale locale) {
        return cache.getDateInstance(style, timeZone, locale);
    }

    //-----------------------------------------------------------------------
    
    public static FastDateFormat getTimeInstance(final int style) {
        return cache.getTimeInstance(style, null, null);
    }

    
    public static FastDateFormat getTimeInstance(final int style, final Locale locale) {
        return cache.getTimeInstance(style, null, locale);
    }

    
    public static FastDateFormat getTimeInstance(final int style, final TimeZone timeZone) {
        return cache.getTimeInstance(style, timeZone, null);
    }

    
    public static FastDateFormat getTimeInstance(final int style, final TimeZone timeZone, final Locale locale) {
        return cache.getTimeInstance(style, timeZone, locale);
    }

    //-----------------------------------------------------------------------
    
    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, null, null);
    }

    
    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final Locale locale) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, null, locale);
    }

    
    public static FastDateFormat getDateTimeInstance(final int dateStyle, final int timeStyle, final TimeZone timeZone) {
        return getDateTimeInstance(dateStyle, timeStyle, timeZone, null);
    }
    
    public static FastDateFormat getDateTimeInstance(
            final int dateStyle, final int timeStyle, final TimeZone timeZone, final Locale locale) {
        return cache.getDateTimeInstance(dateStyle, timeStyle, timeZone, locale);
    }

    // Constructor
    //-----------------------------------------------------------------------
    
    protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale) {
        this(pattern, timeZone, locale, null);
    }

    // Constructor
    //-----------------------------------------------------------------------
    
    protected FastDateFormat(final String pattern, final TimeZone timeZone, final Locale locale, final Date centuryStart) {
        printer= new FastDatePrinter(pattern, timeZone, locale);
        parser= new FastDateParser(pattern, timeZone, locale, centuryStart);
    }

    // Format methods
    //-----------------------------------------------------------------------
    
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        return toAppendTo.append(printer.format(obj));
    }

    
    @Override
    public String format(final long millis) {
        return printer.format(millis);
    }

    
    @Override
    public String format(final Date date) {
        return printer.format(date);
    }

    
    @Override
    public String format(final Calendar calendar) {
        return printer.format(calendar);
    }

    
    @Deprecated
    @Override
    public StringBuffer format(final long millis, final StringBuffer buf) {
        return printer.format(millis, buf);
    }

    
    @Deprecated
    @Override
    public StringBuffer format(final Date date, final StringBuffer buf) {
        return printer.format(date, buf);
    }

    
    @Deprecated
    @Override
    public StringBuffer format(final Calendar calendar, final StringBuffer buf) {
        return printer.format(calendar, buf);
    }

    
    @Override
    public <B extends Appendable> B format(final long millis, final B buf) {
        return printer.format(millis, buf);
    }

    
    @Override
    public <B extends Appendable> B format(final Date date, final B buf) {
        return printer.format(date, buf);
    }

    
    @Override
    public <B extends Appendable> B format(final Calendar calendar, final B buf) {
        return printer.format(calendar, buf);
    }

    // Parsing
    //-----------------------------------------------------------------------


    
    @Override
    public Date parse(final String source) throws ParseException {
        return parser.parse(source);
    }

    
    @Override
    public Date parse(final String source, final ParsePosition pos) {
        return parser.parse(source, pos);
    }

    
    @Override
    public boolean parse(final String source, final ParsePosition pos, final Calendar calendar) {
        return parser.parse(source, pos, calendar);
    }

    
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        return parser.parseObject(source, pos);
    }

    // Accessors
    //-----------------------------------------------------------------------
    
    @Override
    public String getPattern() {
        return printer.getPattern();
    }

    
    @Override
    public TimeZone getTimeZone() {
        return printer.getTimeZone();
    }

    
    @Override
    public Locale getLocale() {
        return printer.getLocale();
    }

    
    public int getMaxLengthEstimate() {
        return printer.getMaxLengthEstimate();
    }

    // Basics
    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDateFormat)) {
            return false;
        }
        final FastDateFormat other = (FastDateFormat) obj;
        // no need to check parser, as it has same invariants as printer
        return printer.equals(other.printer);
    }

    
    @Override
    public int hashCode() {
        return printer.hashCode();
    }

    
    @Override
    public String toString() {
        return "FastDateFormat[" + printer.getPattern() + "," + printer.getLocale() + "," + printer.getTimeZone().getID() + "]";
    }

    
    @Deprecated
    protected StringBuffer applyRules(final Calendar calendar, final StringBuffer buf) {
        return printer.applyRules(calendar, buf);
    }
}
