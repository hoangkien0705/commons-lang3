

package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;


public class HashCodeBuilder implements Builder<Integer> {
    
    private static final int DEFAULT_INITIAL_VALUE = 17;

    
    private static final int DEFAULT_MULTIPLIER_VALUE = 37;

    
    private static final ThreadLocal<Set<IDKey>> REGISTRY = new ThreadLocal<>();

    

    
    static Set<IDKey> getRegistry() {
        return REGISTRY.get();
    }

    
    static boolean isRegistered(final Object value) {
        final Set<IDKey> registry = getRegistry();
        return registry != null && registry.contains(new IDKey(value));
    }

    
    private static void reflectionAppend(final Object object, final Class<?> clazz, final HashCodeBuilder builder, final boolean useTransients,
            final String[] excludeFields) {
        if (isRegistered(object)) {
            return;
        }
        try {
            register(object);
            final Field[] fields = clazz.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            for (final Field field : fields) {
                if (!ArrayUtils.contains(excludeFields, field.getName())
                    && !field.getName().contains("$")
                    && (useTransients || !Modifier.isTransient(field.getModifiers()))
                    && !Modifier.isStatic(field.getModifiers())
                    && !field.isAnnotationPresent(HashCodeExclude.class)) {
                    try {
                        final Object fieldValue = field.get(object);
                        builder.append(fieldValue);
                    } catch (final IllegalAccessException e) {
                        // this can't happen. Would get a Security exception instead
                        // throw a runtime exception in case the impossible happens.
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        } finally {
            unregister(object);
        }
    }

    
    public static int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber, final Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false, null);
    }

    
    public static int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber, final Object object,
            final boolean testTransients) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, testTransients, null);
    }

    
    public static <T> int reflectionHashCode(final int initialNonZeroOddNumber, final int multiplierNonZeroOddNumber, final T object,
            final boolean testTransients, final Class<? super T> reflectUpToClass, final String... excludeFields) {
        Validate.isTrue(object != null, "The object to build a hash code for must not be null");
        final HashCodeBuilder builder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Class<?> clazz = object.getClass();
        reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        while (clazz.getSuperclass() != null && clazz != reflectUpToClass) {
            clazz = clazz.getSuperclass();
            reflectionAppend(object, clazz, builder, testTransients, excludeFields);
        }
        return builder.toHashCode();
    }

    
    public static int reflectionHashCode(final Object object, final boolean testTransients) {
        return reflectionHashCode(DEFAULT_INITIAL_VALUE, DEFAULT_MULTIPLIER_VALUE, object,
                testTransients, null);
    }

    
    public static int reflectionHashCode(final Object object, final Collection<String> excludeFields) {
        return reflectionHashCode(object, ReflectionToStringBuilder.toNoNullStringArray(excludeFields));
    }

    // -------------------------------------------------------------------------

    
    public static int reflectionHashCode(final Object object, final String... excludeFields) {
        return reflectionHashCode(DEFAULT_INITIAL_VALUE, DEFAULT_MULTIPLIER_VALUE, object, false,
                null, excludeFields);
    }

    
    private static void register(final Object value) {
        Set<IDKey> registry = getRegistry();
        if (registry == null) {
            registry = new HashSet<>();
            REGISTRY.set(registry);
        }
        registry.add(new IDKey(value));
    }

    
    private static void unregister(final Object value) {
        final Set<IDKey> registry = getRegistry();
        if (registry != null) {
            registry.remove(new IDKey(value));
            if (registry.isEmpty()) {
                REGISTRY.remove();
            }
        }
    }

    
    private final int iConstant;

    
    private int iTotal = 0;

    
    public HashCodeBuilder() {
        iConstant = 37;
        iTotal = 17;
    }

    
    public HashCodeBuilder(final int initialOddNumber, final int multiplierOddNumber) {
        Validate.isTrue(initialOddNumber % 2 != 0, "HashCodeBuilder requires an odd initial value");
        Validate.isTrue(multiplierOddNumber % 2 != 0, "HashCodeBuilder requires an odd multiplier");
        iConstant = multiplierOddNumber;
        iTotal = initialOddNumber;
    }

    
    public HashCodeBuilder append(final boolean value) {
        iTotal = iTotal * iConstant + (value ? 0 : 1);
        return this;
    }

    
    public HashCodeBuilder append(final boolean[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final boolean element : array) {
                append(element);
            }
        }
        return this;
    }

    // -------------------------------------------------------------------------

    
    public HashCodeBuilder append(final byte value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    // -------------------------------------------------------------------------

    
    public HashCodeBuilder append(final byte[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final byte element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder append(final char value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    
    public HashCodeBuilder append(final char[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final char element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder append(final double value) {
        return append(Double.doubleToLongBits(value));
    }

    
    public HashCodeBuilder append(final double[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final double element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder append(final float value) {
        iTotal = iTotal * iConstant + Float.floatToIntBits(value);
        return this;
    }

    
    public HashCodeBuilder append(final float[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final float element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder append(final int value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    
    public HashCodeBuilder append(final int[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final int element : array) {
                append(element);
            }
        }
        return this;
    }

    
    // NOTE: This method uses >> and not >>> as Effective Java and
    //       Long.hashCode do. Ideally we should switch to >>> at
    //       some stage. There are backwards compat issues, so
    //       that will have to wait for the time being. cf LANG-342.
    public HashCodeBuilder append(final long value) {
        iTotal = iTotal * iConstant + ((int) (value ^ (value >> 32)));
        return this;
    }

    
    public HashCodeBuilder append(final long[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final long element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder append(final Object object) {
        if (object == null) {
            iTotal = iTotal * iConstant;

        } else {
            if (object.getClass().isArray()) {
                // factor out array case in order to keep method small enough
                // to be inlined
                appendArray(object);
            } else {
                iTotal = iTotal * iConstant + object.hashCode();
            }
        }
        return this;
    }

    
    private void appendArray(final Object object) {
        // 'Switch' on type of array, to dispatch to the correct handler
        // This handles multi dimensional arrays
        if (object instanceof long[]) {
            append((long[]) object);
        } else if (object instanceof int[]) {
            append((int[]) object);
        } else if (object instanceof short[]) {
            append((short[]) object);
        } else if (object instanceof char[]) {
            append((char[]) object);
        } else if (object instanceof byte[]) {
            append((byte[]) object);
        } else if (object instanceof double[]) {
            append((double[]) object);
        } else if (object instanceof float[]) {
            append((float[]) object);
        } else if (object instanceof boolean[]) {
            append((boolean[]) object);
        } else {
            // Not an array of primitives
            append((Object[]) object);
        }
    }

    
    public HashCodeBuilder append(final Object[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final Object element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder append(final short value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    
    public HashCodeBuilder append(final short[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (final short element : array) {
                append(element);
            }
        }
        return this;
    }

    
    public HashCodeBuilder appendSuper(final int superHashCode) {
        iTotal = iTotal * iConstant + superHashCode;
        return this;
    }

    
    public int toHashCode() {
        return iTotal;
    }

    
    @Override
    public Integer build() {
        return Integer.valueOf(toHashCode());
    }

    
    @Override
    public int hashCode() {
        return toHashCode();
    }

}
