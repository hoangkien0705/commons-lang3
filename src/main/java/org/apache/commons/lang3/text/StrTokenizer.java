
package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


@Deprecated
public class StrTokenizer implements ListIterator<String>, Cloneable {

    private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE;
    private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE;
    static {
        CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
        CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
        CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
        CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
        CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
        CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
        CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);

        TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
        TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
        TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
        TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
        TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
        TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
        TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
    }

    
    private char chars[];
    
    private String tokens[];
    
    private int tokenPos;

    
    private StrMatcher delimMatcher = StrMatcher.splitMatcher();
    
    private StrMatcher quoteMatcher = StrMatcher.noneMatcher();
    
    private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();
    
    private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();

    
    private boolean emptyAsNull = false;
    
    private boolean ignoreEmptyTokens = true;

    //-----------------------------------------------------------------------

    
    private static StrTokenizer getCSVClone() {
        return (StrTokenizer) CSV_TOKENIZER_PROTOTYPE.clone();
    }

    
    public static StrTokenizer getCSVInstance() {
        return getCSVClone();
    }

    
    public static StrTokenizer getCSVInstance(final String input) {
        final StrTokenizer tok = getCSVClone();
        tok.reset(input);
        return tok;
    }

    
    public static StrTokenizer getCSVInstance(final char[] input) {
        final StrTokenizer tok = getCSVClone();
        tok.reset(input);
        return tok;
    }

    
    private static StrTokenizer getTSVClone() {
        return (StrTokenizer) TSV_TOKENIZER_PROTOTYPE.clone();
    }


    
    public static StrTokenizer getTSVInstance() {
        return getTSVClone();
    }

    
    public static StrTokenizer getTSVInstance(final String input) {
        final StrTokenizer tok = getTSVClone();
        tok.reset(input);
        return tok;
    }

    
    public static StrTokenizer getTSVInstance(final char[] input) {
        final StrTokenizer tok = getTSVClone();
        tok.reset(input);
        return tok;
    }

    //-----------------------------------------------------------------------
    
    public StrTokenizer() {
        super();
        this.chars = null;
    }

    
    public StrTokenizer(final String input) {
        super();
        if (input != null) {
            chars = input.toCharArray();
        } else {
            chars = null;
        }
    }

    
    public StrTokenizer(final String input, final char delim) {
        this(input);
        setDelimiterChar(delim);
    }

    
    public StrTokenizer(final String input, final String delim) {
        this(input);
        setDelimiterString(delim);
    }

    
    public StrTokenizer(final String input, final StrMatcher delim) {
        this(input);
        setDelimiterMatcher(delim);
    }

    
    public StrTokenizer(final String input, final char delim, final char quote) {
        this(input, delim);
        setQuoteChar(quote);
    }

    
    public StrTokenizer(final String input, final StrMatcher delim, final StrMatcher quote) {
        this(input, delim);
        setQuoteMatcher(quote);
    }

    
    public StrTokenizer(final char[] input) {
        super();
        this.chars = ArrayUtils.clone(input);
    }

    
    public StrTokenizer(final char[] input, final char delim) {
        this(input);
        setDelimiterChar(delim);
    }

    
    public StrTokenizer(final char[] input, final String delim) {
        this(input);
        setDelimiterString(delim);
    }

    
    public StrTokenizer(final char[] input, final StrMatcher delim) {
        this(input);
        setDelimiterMatcher(delim);
    }

    
    public StrTokenizer(final char[] input, final char delim, final char quote) {
        this(input, delim);
        setQuoteChar(quote);
    }

    
    public StrTokenizer(final char[] input, final StrMatcher delim, final StrMatcher quote) {
        this(input, delim);
        setQuoteMatcher(quote);
    }

    // API
    //-----------------------------------------------------------------------
    
    public int size() {
        checkTokenized();
        return tokens.length;
    }

    
    public String nextToken() {
        if (hasNext()) {
            return tokens[tokenPos++];
        }
        return null;
    }

    
    public String previousToken() {
        if (hasPrevious()) {
            return tokens[--tokenPos];
        }
        return null;
    }

    
    public String[] getTokenArray() {
        checkTokenized();
        return tokens.clone();
    }

    
    public List<String> getTokenList() {
        checkTokenized();
        final List<String> list = new ArrayList<>(tokens.length);
        list.addAll(Arrays.asList(tokens));
        return list;
    }

    
    public StrTokenizer reset() {
        tokenPos = 0;
        tokens = null;
        return this;
    }

    
    public StrTokenizer reset(final String input) {
        reset();
        if (input != null) {
            this.chars = input.toCharArray();
        } else {
            this.chars = null;
        }
        return this;
    }

    
    public StrTokenizer reset(final char[] input) {
        reset();
        this.chars = ArrayUtils.clone(input);
        return this;
    }

    // ListIterator
    //-----------------------------------------------------------------------
    
    @Override
    public boolean hasNext() {
        checkTokenized();
        return tokenPos < tokens.length;
    }

    
    @Override
    public String next() {
        if (hasNext()) {
            return tokens[tokenPos++];
        }
        throw new NoSuchElementException();
    }

    
    @Override
    public int nextIndex() {
        return tokenPos;
    }

    
    @Override
    public boolean hasPrevious() {
        checkTokenized();
        return tokenPos > 0;
    }

    
    @Override
    public String previous() {
        if (hasPrevious()) {
            return tokens[--tokenPos];
        }
        throw new NoSuchElementException();
    }

    
    @Override
    public int previousIndex() {
        return tokenPos - 1;
    }

    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is unsupported");
    }

    
    @Override
    public void set(final String obj) {
        throw new UnsupportedOperationException("set() is unsupported");
    }

    
    @Override
    public void add(final String obj) {
        throw new UnsupportedOperationException("add() is unsupported");
    }

    // Implementation
    //-----------------------------------------------------------------------
    
    private void checkTokenized() {
        if (tokens == null) {
            if (chars == null) {
                // still call tokenize as subclass may do some work
                final List<String> split = tokenize(null, 0, 0);
                tokens = split.toArray(new String[split.size()]);
            } else {
                final List<String> split = tokenize(chars, 0, chars.length);
                tokens = split.toArray(new String[split.size()]);
            }
        }
    }

    
    protected List<String> tokenize(final char[] srcChars, final int offset, final int count) {
        if (srcChars == null || count == 0) {
            return Collections.emptyList();
        }
        final StrBuilder buf = new StrBuilder();
        final List<String> tokenList = new ArrayList<>();
        int pos = offset;

        // loop around the entire buffer
        while (pos >= 0 && pos < count) {
            // find next token
            pos = readNextToken(srcChars, pos, count, buf, tokenList);

            // handle case where end of string is a delimiter
            if (pos >= count) {
                addToken(tokenList, StringUtils.EMPTY);
            }
        }
        return tokenList;
    }

    
    private void addToken(final List<String> list, String tok) {
        if (StringUtils.isEmpty(tok)) {
            if (isIgnoreEmptyTokens()) {
                return;
            }
            if (isEmptyTokenAsNull()) {
                tok = null;
            }
        }
        list.add(tok);
    }

    
    private int readNextToken(final char[] srcChars, int start, final int len, final StrBuilder workArea, final List<String> tokenList) {
        // skip all leading whitespace, unless it is the
        // field delimiter or the quote character
        while (start < len) {
            final int removeLen = Math.max(
                    getIgnoredMatcher().isMatch(srcChars, start, start, len),
                    getTrimmerMatcher().isMatch(srcChars, start, start, len));
            if (removeLen == 0 ||
                getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0 ||
                getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) {
                break;
            }
            start += removeLen;
        }

        // handle reaching end
        if (start >= len) {
            addToken(tokenList, StringUtils.EMPTY);
            return -1;
        }

        // handle empty token
        final int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
        if (delimLen > 0) {
            addToken(tokenList, StringUtils.EMPTY);
            return start + delimLen;
        }

        // handle found token
        final int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
        if (quoteLen > 0) {
            return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
        }
        return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
    }

    
    private int readWithQuotes(final char[] srcChars, final int start, final int len, final StrBuilder workArea,
                               final List<String> tokenList, final int quoteStart, final int quoteLen) {
        // Loop until we've found the end of the quoted
        // string or the end of the input
        workArea.clear();
        int pos = start;
        boolean quoting = quoteLen > 0;
        int trimStart = 0;

        while (pos < len) {
            // quoting mode can occur several times throughout a string
            // we must switch between quoting and non-quoting until we
            // encounter a non-quoted delimiter, or end of string
            if (quoting) {
                // In quoting mode

                // If we've found a quote character, see if it's
                // followed by a second quote.  If so, then we need
                // to actually put the quote character into the token
                // rather than end the token.
                if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
                        // matched pair of quotes, thus an escaped quote
                        workArea.append(srcChars, pos, quoteLen);
                        pos += quoteLen * 2;
                        trimStart = workArea.size();
                        continue;
                    }

                    // end of quoting
                    quoting = false;
                    pos += quoteLen;
                    continue;
                }

                // copy regular character from inside quotes
                workArea.append(srcChars[pos++]);
                trimStart = workArea.size();

            } else {
                // Not in quoting mode

                // check for delimiter, and thus end of token
                final int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
                if (delimLen > 0) {
                    // return condition when end of token found
                    addToken(tokenList, workArea.substring(0, trimStart));
                    return pos + delimLen;
                }

                // check for quote, and thus back into quoting mode
                if (quoteLen > 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    quoting = true;
                    pos += quoteLen;
                    continue;
                }

                // check for ignored (outside quotes), and ignore
                final int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
                if (ignoredLen > 0) {
                    pos += ignoredLen;
                    continue;
                }

                // check for trimmed character
                // don't yet know if its at the end, so copy to workArea
                // use trimStart to keep track of trim at the end
                final int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
                if (trimmedLen > 0) {
                    workArea.append(srcChars, pos, trimmedLen);
                    pos += trimmedLen;
                    continue;
                }

                // copy regular character from outside quotes
                workArea.append(srcChars[pos++]);
                trimStart = workArea.size();
            }
        }

        // return condition when end of string found
        addToken(tokenList, workArea.substring(0, trimStart));
        return -1;
    }

    
    private boolean isQuote(final char[] srcChars, final int pos, final int len, final int quoteStart, final int quoteLen) {
        for (int i = 0; i < quoteLen; i++) {
            if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i]) {
                return false;
            }
        }
        return true;
    }

    // Delimiter
    //-----------------------------------------------------------------------
    
    public StrMatcher getDelimiterMatcher() {
        return this.delimMatcher;
    }

    
    public StrTokenizer setDelimiterMatcher(final StrMatcher delim) {
        if (delim == null) {
            this.delimMatcher = StrMatcher.noneMatcher();
        } else {
            this.delimMatcher = delim;
        }
        return this;
    }

    
    public StrTokenizer setDelimiterChar(final char delim) {
        return setDelimiterMatcher(StrMatcher.charMatcher(delim));
    }

    
    public StrTokenizer setDelimiterString(final String delim) {
        return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
    }

    // Quote
    //-----------------------------------------------------------------------
    
    public StrMatcher getQuoteMatcher() {
        return quoteMatcher;
    }

    
    public StrTokenizer setQuoteMatcher(final StrMatcher quote) {
        if (quote != null) {
            this.quoteMatcher = quote;
        }
        return this;
    }

    
    public StrTokenizer setQuoteChar(final char quote) {
        return setQuoteMatcher(StrMatcher.charMatcher(quote));
    }

    // Ignored
    //-----------------------------------------------------------------------
    
    public StrMatcher getIgnoredMatcher() {
        return ignoredMatcher;
    }

    
    public StrTokenizer setIgnoredMatcher(final StrMatcher ignored) {
        if (ignored != null) {
            this.ignoredMatcher = ignored;
        }
        return this;
    }

    
    public StrTokenizer setIgnoredChar(final char ignored) {
        return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
    }

    // Trimmer
    //-----------------------------------------------------------------------
    
    public StrMatcher getTrimmerMatcher() {
        return trimmerMatcher;
    }

    
    public StrTokenizer setTrimmerMatcher(final StrMatcher trimmer) {
        if (trimmer != null) {
            this.trimmerMatcher = trimmer;
        }
        return this;
    }

    //-----------------------------------------------------------------------
    
    public boolean isEmptyTokenAsNull() {
        return this.emptyAsNull;
    }

    
    public StrTokenizer setEmptyTokenAsNull(final boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
        return this;
    }

    //-----------------------------------------------------------------------
    
    public boolean isIgnoreEmptyTokens() {
        return ignoreEmptyTokens;
    }

    
    public StrTokenizer setIgnoreEmptyTokens(final boolean ignoreEmptyTokens) {
        this.ignoreEmptyTokens = ignoreEmptyTokens;
        return this;
    }

    //-----------------------------------------------------------------------
    
    public String getContent() {
        if (chars == null) {
            return null;
        }
        return new String(chars);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public Object clone() {
        try {
            return cloneReset();
        } catch (final CloneNotSupportedException ex) {
            return null;
        }
    }

    
    Object cloneReset() throws CloneNotSupportedException {
        // this method exists to enable 100% test coverage
        final StrTokenizer cloned = (StrTokenizer) super.clone();
        if (cloned.chars != null) {
            cloned.chars = cloned.chars.clone();
        }
        cloned.reset();
        return cloned;
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        if (tokens == null) {
            return "StrTokenizer[not tokenized yet]";
        }
        return "StrTokenizer" + getTokenList();
    }

}
