
package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;


public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {

    
    private static final long serialVersionUID = -2135791679L;

    
    private short value;

    
    public MutableShort() {
        super();
    }

    
    public MutableShort(final short value) {
        super();
        this.value = value;
    }

    
    public MutableShort(final Number value) {
        super();
        this.value = value.shortValue();
    }

    
    public MutableShort(final String value) throws NumberFormatException {
        super();
        this.value = Short.parseShort(value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public Short getValue() {
        return Short.valueOf(this.value);
    }

    
    public void setValue(final short value) {
        this.value = value;
    }

    
    @Override
    public void setValue(final Number value) {
        this.value = value.shortValue();
    }

    //-----------------------------------------------------------------------
    
    public void increment() {
        value++;
    }

    
    public short getAndIncrement() {
        final short last = value;
        value++;
        return last;
    }

    
    public short incrementAndGet() {
        value++;
        return value;
    }

    
    public void decrement() {
        value--;
    }

    
    public short getAndDecrement() {
        final short last = value;
        value--;
        return last;
    }

    
    public short decrementAndGet() {
        value--;
        return value;
    }

    //-----------------------------------------------------------------------
    
    public void add(final short operand) {
        this.value += operand;
    }

    
    public void add(final Number operand) {
        this.value += operand.shortValue();
    }

    
    public void subtract(final short operand) {
        this.value -= operand;
    }

    
    public void subtract(final Number operand) {
        this.value -= operand.shortValue();
    }

    
    public short addAndGet(final short operand) {
        this.value += operand;
        return value;
    }

    
    public short addAndGet(final Number operand) {
        this.value += operand.shortValue();
        return value;
    }

    
    public short getAndAdd(final short operand) {
        final short last = value;
        this.value += operand;
        return last;
    }

    
    public short getAndAdd(final Number operand) {
        final short last = value;
        this.value += operand.shortValue();
        return last;
    }

    //-----------------------------------------------------------------------
    // byteValue relies on Number implementation
    
    @Override
    public short shortValue() {
        return value;
    }

    
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
    
    public Short toShort() {
        return Short.valueOf(shortValue());
    }

    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableShort) {
            return value == ((MutableShort) obj).shortValue();
        }
        return false;
    }

    
    @Override
    public int hashCode() {
        return value;
    }

    //-----------------------------------------------------------------------
    
    @Override
    public int compareTo(final MutableShort other) {
        return NumberUtils.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
