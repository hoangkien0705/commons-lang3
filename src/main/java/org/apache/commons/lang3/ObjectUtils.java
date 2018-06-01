
package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.StrBuilder;


//@Immutable
@SuppressWarnings("deprecation") // deprecated class StrBuilder is imported
// because it is part of the signature of deprecated methods
public class ObjectUtils {

    private static final char AT_SIGN = '@';

    
    public static final Null NULL = new Null();

    
    public ObjectUtils() {
        super();
    }

    // Defaulting
    //-----------------------------------------------------------------------
    
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return object != null ? object : defaultValue;
    }

    
    @SafeVarargs
    public static <T> T firstNonNull(final T... values) {
        if (values != null) {
            for (final T val : values) {
                if (val != null) {
                    return val;
                }
            }
        }
        return null;
    }

    
    public static boolean anyNotNull(final Object... values) {
        return firstNonNull(values) != null;
    }

    
    public static boolean allNotNull(final Object... values) {
        if (values == null) {
            return false;
        }

        for (final Object val : values) {
            if (val == null) {
                return false;
            }
        }

        return true;
    }

    // Null-safe equals/hashCode
    //-----------------------------------------------------------------------
    
    @Deprecated
    public static boolean equals(final Object object1, final Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    
    public static boolean notEqual(final Object object1, final Object object2) {
        return !ObjectUtils.equals(object1, object2);
    }

    
    @Deprecated
    public static int hashCode(final Object obj) {
        // hashCode(Object) retained for performance, as hash code is often critical
        return obj == null ? 0 : obj.hashCode();
    }

    
    @Deprecated
    public static int hashCodeMulti(final Object... objects) {
        int hash = 1;
        if (objects != null) {
            for (final Object object : objects) {
                final int tmpHash = ObjectUtils.hashCode(object);
                hash = hash * 31 + tmpHash;
            }
        }
        return hash;
    }

    // Identity ToString
    //-----------------------------------------------------------------------
    
    public static String identityToString(final Object object) {
        if (object == null) {
            return null;
        }
        final String name = object.getClass().getName();
        final String hexString = Integer.toHexString(System.identityHashCode(object));
        final StringBuilder builder = new StringBuilder(name.length() + 1 + hexString.length());
        // @formatter:off
        builder.append(name)
              .append(AT_SIGN)
              .append(hexString);
        // @formatter:off
        return builder.toString();
    }

    
    public static void identityToString(final Appendable appendable, final Object object) throws IOException {
        Validate.notNull(object, "Cannot get the toString of a null object");
        appendable.append(object.getClass().getName())
              .append(AT_SIGN)
              .append(Integer.toHexString(System.identityHashCode(object)));
    }

    
    @Deprecated
    public static void identityToString(final StrBuilder builder, final Object object) {
        Validate.notNull(object, "Cannot get the toString of a null object");
        final String name = object.getClass().getName();
        final String hexString = Integer.toHexString(System.identityHashCode(object));
        builder.ensureCapacity(builder.length() +  name.length() + 1 + hexString.length());
        builder.append(name)
              .append(AT_SIGN)
              .append(hexString);
    }

    
    public static void identityToString(final StringBuffer buffer, final Object object) {
        Validate.notNull(object, "Cannot get the toString of a null object");
        final String name = object.getClass().getName();
        final String hexString = Integer.toHexString(System.identityHashCode(object));
        buffer.ensureCapacity(buffer.length() + name.length() + 1 + hexString.length());
        buffer.append(name)
              .append(AT_SIGN)
              .append(hexString);
    }

    
    public static void identityToString(final StringBuilder builder, final Object object) {
        Validate.notNull(object, "Cannot get the toString of a null object");
        final String name = object.getClass().getName();
        final String hexString = Integer.toHexString(System.identityHashCode(object));
        builder.ensureCapacity(builder.length() +  name.length() + 1 + hexString.length());
        builder.append(name)
              .append(AT_SIGN)
              .append(hexString);
    }

    // ToString
    //-----------------------------------------------------------------------
    
    @Deprecated
    public static String toString(final Object obj) {
        return obj == null ? StringUtils.EMPTY : obj.toString();
    }

    
    @Deprecated
    public static String toString(final Object obj, final String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }

    // Comparable
    //-----------------------------------------------------------------------
    
    @SafeVarargs
    public static <T extends Comparable<? super T>> T min(final T... values) {
        T result = null;
        if (values != null) {
            for (final T value : values) {
                if (compare(value, result, true) < 0) {
                    result = value;
                }
            }
        }
        return result;
    }

    
    @SafeVarargs
    public static <T extends Comparable<? super T>> T max(final T... values) {
        T result = null;
        if (values != null) {
            for (final T value : values) {
                if (compare(value, result, false) > 0) {
                    result = value;
                }
            }
        }
        return result;
    }

    
    public static <T extends Comparable<? super T>> int compare(final T c1, final T c2) {
        return compare(c1, c2, false);
    }

    
    public static <T extends Comparable<? super T>> int compare(final T c1, final T c2, final boolean nullGreater) {
        if (c1 == c2) {
            return 0;
        } else if (c1 == null) {
            return nullGreater ? 1 : -1;
        } else if (c2 == null) {
            return nullGreater ? -1 : 1;
        }
        return c1.compareTo(c2);
    }

    
    @SafeVarargs
    public static <T extends Comparable<? super T>> T median(final T... items) {
        Validate.notEmpty(items);
        Validate.noNullElements(items);
        final TreeSet<T> sort = new TreeSet<>();
        Collections.addAll(sort, items);
        @SuppressWarnings("unchecked") //we know all items added were T instances
        final T result = (T) sort.toArray()[(sort.size() - 1) / 2];
        return result;
    }

    
    @SafeVarargs
    public static <T> T median(final Comparator<T> comparator, final T... items) {
        Validate.notEmpty(items, "null/empty items");
        Validate.noNullElements(items);
        Validate.notNull(comparator, "null comparator");
        final TreeSet<T> sort = new TreeSet<>(comparator);
        Collections.addAll(sort, items);
        @SuppressWarnings("unchecked") //we know all items added were T instances
        final
        T result = (T) sort.toArray()[(sort.size() - 1) / 2];
        return result;
    }

    // Mode
    //-----------------------------------------------------------------------
    
    @SafeVarargs
    public static <T> T mode(final T... items) {
        if (ArrayUtils.isNotEmpty(items)) {
            final HashMap<T, MutableInt> occurrences = new HashMap<>(items.length);
            for (final T t : items) {
                final MutableInt count = occurrences.get(t);
                if (count == null) {
                    occurrences.put(t, new MutableInt(1));
                } else {
                    count.increment();
                }
            }
            T result = null;
            int max = 0;
            for (final Map.Entry<T, MutableInt> e : occurrences.entrySet()) {
                final int cmp = e.getValue().intValue();
                if (cmp == max) {
                    result = null;
                } else if (cmp > max) {
                    max = cmp;
                    result = e.getKey();
                }
            }
            return result;
        }
        return null;
    }

    // cloning
    //-----------------------------------------------------------------------
    
    public static <T> T clone(final T obj) {
        if (obj instanceof Cloneable) {
            final Object result;
            if (obj.getClass().isArray()) {
                final Class<?> componentType = obj.getClass().getComponentType();
                if (!componentType.isPrimitive()) {
                    result = ((Object[]) obj).clone();
                } else {
                    int length = Array.getLength(obj);
                    result = Array.newInstance(componentType, length);
                    while (length-- > 0) {
                        Array.set(result, length, Array.get(obj, length));
                    }
                }
            } else {
                try {
                    final Method clone = obj.getClass().getMethod("clone");
                    result = clone.invoke(obj);
                } catch (final NoSuchMethodException e) {
                    throw new CloneFailedException("Cloneable type "
                        + obj.getClass().getName()
                        + " has no clone method", e);
                } catch (final IllegalAccessException e) {
                    throw new CloneFailedException("Cannot clone Cloneable type "
                        + obj.getClass().getName(), e);
                } catch (final InvocationTargetException e) {
                    throw new CloneFailedException("Exception cloning Cloneable type "
                        + obj.getClass().getName(), e.getCause());
                }
            }
            @SuppressWarnings("unchecked") // OK because input is of type T
            final T checked = (T) result;
            return checked;
        }

        return null;
    }

    
    public static <T> T cloneIfPossible(final T obj) {
        final T clone = clone(obj);
        return clone == null ? obj : clone;
    }

    // Null
    //-----------------------------------------------------------------------
    
    public static class Null implements Serializable {
        
        private static final long serialVersionUID = 7092611880189329093L;

        
        Null() {
            super();
        }

        
        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }


    // Constants (LANG-816):
    


    
    public static boolean CONST(final boolean v) {
        return v;
    }

    
    public static byte CONST(final byte v) {
        return v;
    }

    
    public static byte CONST_BYTE(final int v) throws IllegalArgumentException {
        if (v < Byte.MIN_VALUE || v > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -128 and 127: [" + v + "]");
        }
        return (byte) v;
    }

    
    public static char CONST(final char v) {
        return v;
    }

    
    public static short CONST(final short v) {
        return v;
    }

    
    public static short CONST_SHORT(final int v) throws IllegalArgumentException {
        if (v < Short.MIN_VALUE || v > Short.MAX_VALUE) {
            throw new IllegalArgumentException("Supplied value must be a valid byte literal between -32768 and 32767: [" + v + "]");
        }
        return (short) v;
    }


    
    public static int CONST(final int v) {
        return v;
    }

    
    public static long CONST(final long v) {
        return v;
    }

    
    public static float CONST(final float v) {
        return v;
    }

    
    public static double CONST(final double v) {
        return v;
    }

    
    public static <T> T CONST(final T v) {
        return v;
    }

}
