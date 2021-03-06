
package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;


public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number> {

    
    private static final long serialVersionUID = 62986528375L;

    
    private long value;

    
    public MutableLong() {
        super();
    }

    
    public MutableLong(final long value) {
        super();
        this.value = value;
    }

    
    public MutableLong(final Number value) {
        super();
        this.value = value.longValue();
    }

    
    public MutableLong(final String value) throws NumberFormatException {
        super();
        this.value = Long.parseLong(value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public Long getValue() {
        return Long.valueOf(this.value);
    }

    
    public void setValue(final long value) {
        this.value = value;
    }

    
    @Override
    public void setValue(final Number value) {
        this.value = value.longValue();
    }

    //-----------------------------------------------------------------------
    
    public void increment() {
        value++;
    }

    
    public long getAndIncrement() {
        final long last = value;
        value++;
        return last;
    }

    
    public long incrementAndGet() {
        value++;
        return value;
    }

    
    public void decrement() {
        value--;
    }

    
    public long getAndDecrement() {
        final long last = value;
        value--;
        return last;
    }

    
    public long decrementAndGet() {
        value--;
        return value;
    }

    //-----------------------------------------------------------------------
    
    public void add(final long operand) {
        this.value += operand;
    }

    
    public void add(final Number operand) {
        this.value += operand.longValue();
    }

    
    public void subtract(final long operand) {
        this.value -= operand;
    }

    
    public void subtract(final Number operand) {
        this.value -= operand.longValue();
    }

    
    public long addAndGet(final long operand) {
        this.value += operand;
        return value;
    }

    
    public long addAndGet(final Number operand) {
        this.value += operand.longValue();
        return value;
    }

    
    public long getAndAdd(final long operand) {
        final long last = value;
        this.value += operand;
        return last;
    }

    
    public long getAndAdd(final Number operand) {
        final long last = value;
        this.value += operand.longValue();
        return last;
    }

    //-----------------------------------------------------------------------
    // shortValue and byteValue rely on Number implementation
    
    @Override
    public int intValue() {
        return (int) value;
    }

    
    @Override
    public long longValue() {
        return value;
    }

    
    @Override
    public float floatValue() {
        return value;
    }

    
    @Override
    public double doubleValue() {
        return value;
    }

    //-----------------------------------------------------------------------
    
    public Long toLong() {
        return Long.valueOf(longValue());
    }

    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableLong) {
            return value == ((MutableLong) obj).longValue();
        }
        return false;
    }

    
    @Override
    public int hashCode() {
        return (int) (value ^ (value >>> 32));
    }

    //-----------------------------------------------------------------------
    
    @Override
    public int compareTo(final MutableLong other) {
        return NumberUtils.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
