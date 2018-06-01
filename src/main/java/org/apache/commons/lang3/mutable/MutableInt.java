
package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;


public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {

    
    private static final long serialVersionUID = 512176391864L;

    
    private int value;

    
    public MutableInt() {
        super();
    }

    
    public MutableInt(final int value) {
        super();
        this.value = value;
    }

    
    public MutableInt(final Number value) {
        super();
        this.value = value.intValue();
    }

    
    public MutableInt(final String value) throws NumberFormatException {
        super();
        this.value = Integer.parseInt(value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public Integer getValue() {
        return Integer.valueOf(this.value);
    }

    
    public void setValue(final int value) {
        this.value = value;
    }

    
    @Override
    public void setValue(final Number value) {
        this.value = value.intValue();
    }

    //-----------------------------------------------------------------------
    
    public void increment() {
        value++;
    }

    
    public int getAndIncrement() {
        final int last = value;
        value++;
        return last;
    }

    
    public int incrementAndGet() {
        value++;
        return value;
    }

    
    public void decrement() {
        value--;
    }

    
    public int getAndDecrement() {
        final int last = value;
        value--;
        return last;
    }

    
    public int decrementAndGet() {
        value--;
        return value;
    }

    //-----------------------------------------------------------------------
    
    public void add(final int operand) {
        this.value += operand;
    }

    
    public void add(final Number operand) {
        this.value += operand.intValue();
    }

    
    public void subtract(final int operand) {
        this.value -= operand;
    }

    
    public void subtract(final Number operand) {
        this.value -= operand.intValue();
    }

    
    public int addAndGet(final int operand) {
        this.value += operand;
        return value;
    }

    
    public int addAndGet(final Number operand) {
        this.value += operand.intValue();
        return value;
    }

    
    public int getAndAdd(final int operand) {
        final int last = value;
        this.value += operand;
        return last;
    }

    
    public int getAndAdd(final Number operand) {
        final int last = value;
        this.value += operand.intValue();
        return last;
    }

    //-----------------------------------------------------------------------
    // shortValue and byteValue rely on Number implementation
    
    @Override
    public int intValue() {
        return value;
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
    
    public Integer toInteger() {
        return Integer.valueOf(intValue());
    }

    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableInt) {
            return value == ((MutableInt) obj).intValue();
        }
        return false;
    }

    
    @Override
    public int hashCode() {
        return value;
    }

    //-----------------------------------------------------------------------
    
    @Override
    public int compareTo(final MutableInt other) {
        return NumberUtils.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
