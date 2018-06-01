
package org.apache.commons.lang3.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;


public class DiffBuilder implements Builder<DiffResult> {

    private final List<Diff<?>> diffs;
    private final boolean objectsTriviallyEqual;
    private final Object left;
    private final Object right;
    private final ToStringStyle style;

    
    public DiffBuilder(final Object lhs, final Object rhs,
            final ToStringStyle style, final boolean testTriviallyEqual) {

        Validate.isTrue(lhs != null, "lhs cannot be null");
        Validate.isTrue(rhs != null, "rhs cannot be null");

        this.diffs = new ArrayList<>();
        this.left = lhs;
        this.right = rhs;
        this.style = style;

        // Don't compare any fields if objects equal
        this.objectsTriviallyEqual = testTriviallyEqual && (lhs == rhs || lhs.equals(rhs));
    }

    
    public DiffBuilder(final Object lhs, final Object rhs,
            final ToStringStyle style) {

            this(lhs, rhs, style, true);
    }

    
    public DiffBuilder append(final String fieldName, final boolean lhs,
            final boolean rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs != rhs) {
            diffs.add(new Diff<Boolean>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Boolean getLeft() {
                    return Boolean.valueOf(lhs);
                }

                @Override
                public Boolean getRight() {
                    return Boolean.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final boolean[] lhs,
            final boolean[] rhs) {
        validateFieldNameNotNull(fieldName);
        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Boolean[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Boolean[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Boolean[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final byte lhs,
            final byte rhs) {
        validateFieldNameNotNull(fieldName);
        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs != rhs) {
            diffs.add(new Diff<Byte>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Byte getLeft() {
                    return Byte.valueOf(lhs);
                }

                @Override
                public Byte getRight() {
                    return Byte.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final byte[] lhs,
            final byte[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Byte[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Byte[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Byte[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final char lhs,
            final char rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs != rhs) {
            diffs.add(new Diff<Character>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Character getLeft() {
                    return Character.valueOf(lhs);
                }

                @Override
                public Character getRight() {
                    return Character.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final char[] lhs,
            final char[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Character[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Character[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Character[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final double lhs,
            final double rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (Double.doubleToLongBits(lhs) != Double.doubleToLongBits(rhs)) {
            diffs.add(new Diff<Double>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Double getLeft() {
                    return Double.valueOf(lhs);
                }

                @Override
                public Double getRight() {
                    return Double.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final double[] lhs,
            final double[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Double[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Double[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Double[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final float lhs,
            final float rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (Float.floatToIntBits(lhs) != Float.floatToIntBits(rhs)) {
            diffs.add(new Diff<Float>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Float getLeft() {
                    return Float.valueOf(lhs);
                }

                @Override
                public Float getRight() {
                    return Float.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final float[] lhs,
            final float[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Float[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Float[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Float[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final int lhs,
            final int rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs != rhs) {
            diffs.add(new Diff<Integer>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Integer getLeft() {
                    return Integer.valueOf(lhs);
                }

                @Override
                public Integer getRight() {
                    return Integer.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final int[] lhs,
            final int[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Integer[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Integer[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Integer[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final long lhs,
            final long rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs != rhs) {
            diffs.add(new Diff<Long>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Long getLeft() {
                    return Long.valueOf(lhs);
                }

                @Override
                public Long getRight() {
                    return Long.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final long[] lhs,
            final long[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Long[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Long[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Long[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final short lhs,
            final short rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs != rhs) {
            diffs.add(new Diff<Short>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Short getLeft() {
                    return Short.valueOf(lhs);
                }

                @Override
                public Short getRight() {
                    return Short.valueOf(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final short[] lhs,
            final short[] rhs) {
        validateFieldNameNotNull(fieldName);

        if (objectsTriviallyEqual) {
            return this;
        }
        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Short[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Short[] getLeft() {
                    return ArrayUtils.toObject(lhs);
                }

                @Override
                public Short[] getRight() {
                    return ArrayUtils.toObject(rhs);
                }
            });
        }
        return this;
    }

    
    public DiffBuilder append(final String fieldName, final Object lhs,
            final Object rhs) {
        validateFieldNameNotNull(fieldName);
        if (objectsTriviallyEqual) {
            return this;
        }
        if (lhs == rhs) {
            return this;
        }

        Object objectToTest;
        if (lhs != null) {
            objectToTest = lhs;
        } else {
            // rhs cannot be null, as lhs != rhs
            objectToTest = rhs;
        }

        if (objectToTest.getClass().isArray()) {
            if (objectToTest instanceof boolean[]) {
                return append(fieldName, (boolean[]) lhs, (boolean[]) rhs);
            }
            if (objectToTest instanceof byte[]) {
                return append(fieldName, (byte[]) lhs, (byte[]) rhs);
            }
            if (objectToTest instanceof char[]) {
                return append(fieldName, (char[]) lhs, (char[]) rhs);
            }
            if (objectToTest instanceof double[]) {
                return append(fieldName, (double[]) lhs, (double[]) rhs);
            }
            if (objectToTest instanceof float[]) {
                return append(fieldName, (float[]) lhs, (float[]) rhs);
            }
            if (objectToTest instanceof int[]) {
                return append(fieldName, (int[]) lhs, (int[]) rhs);
            }
            if (objectToTest instanceof long[]) {
                return append(fieldName, (long[]) lhs, (long[]) rhs);
            }
            if (objectToTest instanceof short[]) {
                return append(fieldName, (short[]) lhs, (short[]) rhs);
            }

            return append(fieldName, (Object[]) lhs, (Object[]) rhs);
        }

        // Not array type
        if (lhs != null && lhs.equals(rhs)) {
            return this;
        }

        diffs.add(new Diff<Object>(fieldName) {
            private static final long serialVersionUID = 1L;

            @Override
            public Object getLeft() {
                return lhs;
            }

            @Override
            public Object getRight() {
                return rhs;
            }
        });

        return this;
    }

    
    public DiffBuilder append(final String fieldName, final Object[] lhs,
            final Object[] rhs) {
        validateFieldNameNotNull(fieldName);
        if (objectsTriviallyEqual) {
            return this;
        }

        if (!Arrays.equals(lhs, rhs)) {
            diffs.add(new Diff<Object[]>(fieldName) {
                private static final long serialVersionUID = 1L;

                @Override
                public Object[] getLeft() {
                    return lhs;
                }

                @Override
                public Object[] getRight() {
                    return rhs;
                }
            });
        }

        return this;
    }

    
    public DiffBuilder append(final String fieldName,
            final DiffResult diffResult) {
        validateFieldNameNotNull(fieldName);
        Validate.isTrue(diffResult != null, "Diff result cannot be null");
        if (objectsTriviallyEqual) {
            return this;
        }

        for (final Diff<?> diff : diffResult.getDiffs()) {
            append(fieldName + "." + diff.getFieldName(),
                   diff.getLeft(), diff.getRight());
        }

        return this;
    }

    
    @Override
    public DiffResult build() {
        return new DiffResult(left, right, diffs, style);
    }

    private void validateFieldNameNotNull(final String fieldName) {
        Validate.isTrue(fieldName != null, "Field name cannot be null");
    }

}
