
package org.apache.commons.lang3.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;


public class DiffResult implements Iterable<Diff<?>> {

    
    public static final String OBJECTS_SAME_STRING = "";

    private static final String DIFFERS_STRING = "differs from";

    private final List<Diff<?>> diffs;
    private final Object lhs;
    private final Object rhs;
    private final ToStringStyle style;

    
    DiffResult(final Object lhs, final Object rhs, final List<Diff<?>> diffs,
            final ToStringStyle style) {
        Validate.isTrue(lhs != null, "Left hand object cannot be null");
        Validate.isTrue(rhs != null, "Right hand object cannot be null");
        Validate.isTrue(diffs != null, "List of differences cannot be null");

        this.diffs = diffs;
        this.lhs = lhs;
        this.rhs = rhs;

        if (style == null) {
            this.style = ToStringStyle.DEFAULT_STYLE;
        } else {
            this.style = style;
        }
    }

    
    public List<Diff<?>> getDiffs() {
        return Collections.unmodifiableList(diffs);
    }

    
    public int getNumberOfDiffs() {
        return diffs.size();
    }

    
    public ToStringStyle getToStringStyle() {
        return style;
    }

    
    @Override
    public String toString() {
        return toString(style);
    }

    
    public String toString(final ToStringStyle style) {
        if (diffs.size() == 0) {
            return OBJECTS_SAME_STRING;
        }

        final ToStringBuilder lhsBuilder = new ToStringBuilder(lhs, style);
        final ToStringBuilder rhsBuilder = new ToStringBuilder(rhs, style);

        for (final Diff<?> diff : diffs) {
            lhsBuilder.append(diff.getFieldName(), diff.getLeft());
            rhsBuilder.append(diff.getFieldName(), diff.getRight());
        }

        return String.format("%s %s %s", lhsBuilder.build(), DIFFERS_STRING,
                rhsBuilder.build());
    }

    
    @Override
    public Iterator<Diff<?>> iterator() {
        return diffs.iterator();
    }
}
