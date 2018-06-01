
package org.apache.commons.lang3.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.apache.commons.lang3.Validate;


public abstract class TypeLiteral<T> implements Typed<T> {

    @SuppressWarnings("rawtypes")
    private static final TypeVariable<Class<TypeLiteral>> T = TypeLiteral.class.getTypeParameters()[0];

    
    public final Type value;

    private final String toString;

    
    protected TypeLiteral() {
        this.value =
            Validate.notNull(TypeUtils.getTypeArguments(getClass(), TypeLiteral.class).get(T),
                "%s does not assign type parameter %s", getClass(), TypeUtils.toLongString(T));

        this.toString = String.format("%s<%s>", TypeLiteral.class.getSimpleName(), TypeUtils.toString(value));
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TypeLiteral)) {
            return false;
        }
        final TypeLiteral<?> other = (TypeLiteral<?>) obj;
        return TypeUtils.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return 37 << 4 | value.hashCode();
    }

    @Override
    public String toString() {
        return toString;
    }

    @Override
    public Type getType() {
        return value;
    }
}
