
package org.apache.commons.lang3.builder;

import java.util.Collection;

import org.apache.commons.lang3.ClassUtils;


public class RecursiveToStringStyle extends ToStringStyle {

    
    private static final long serialVersionUID = 1L;

    
    public RecursiveToStringStyle() {
        super();
    }

    @Override
    public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
        if (!ClassUtils.isPrimitiveWrapper(value.getClass()) &&
            !String.class.equals(value.getClass()) &&
            accept(value.getClass())) {
            buffer.append(ReflectionToStringBuilder.toString(value, this));
        } else {
            super.appendDetail(buffer, fieldName, value);
        }
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final Collection<?> coll) {
        appendClassName(buffer, coll);
        appendIdentityHashCode(buffer, coll);
        appendDetail(buffer, fieldName, coll.toArray());
    }

    
    protected boolean accept(final Class<?> clazz) {
        return true;
    }
}
