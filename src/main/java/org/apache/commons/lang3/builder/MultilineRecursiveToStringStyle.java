

package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ClassUtils;


public class MultilineRecursiveToStringStyle extends RecursiveToStringStyle {

    
    private static final long serialVersionUID = 1L;

    
    private static final int INDENT = 2;

    
    private int spaces = 2;

    
    public MultilineRecursiveToStringStyle() {
        super();
        resetIndent();
    }

    
    private void resetIndent() {
        setArrayStart("{" + System.lineSeparator() + spacer(spaces));
        setArraySeparator("," + System.lineSeparator() + spacer(spaces));
        setArrayEnd(System.lineSeparator() + spacer(spaces - INDENT) + "}");

        setContentStart("[" + System.lineSeparator() + spacer(spaces));
        setFieldSeparator("," + System.lineSeparator() + spacer(spaces));
        setContentEnd(System.lineSeparator() + spacer(spaces - INDENT) + "]");
    }

    
    private StringBuilder spacer(final int spaces) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            sb.append(" ");
        }
        return sb;
    }

    @Override
    public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
        if (!ClassUtils.isPrimitiveWrapper(value.getClass()) && !String.class.equals(value.getClass())
                && accept(value.getClass())) {
            spaces += INDENT;
            resetIndent();
            buffer.append(ReflectionToStringBuilder.toString(value, this));
            spaces -= INDENT;
            resetIndent();
        } else {
            super.appendDetail(buffer, fieldName, value);
        }
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final Object[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void reflectionAppendArrayDetail(final StringBuffer buffer, final String fieldName, final Object array) {
        spaces += INDENT;
        resetIndent();
        super.reflectionAppendArrayDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final long[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final int[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final short[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final byte[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final char[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final double[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final float[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final boolean[] array) {
        spaces += INDENT;
        resetIndent();
        super.appendDetail(buffer, fieldName, array);
        spaces -= INDENT;
        resetIndent();
    }

}
