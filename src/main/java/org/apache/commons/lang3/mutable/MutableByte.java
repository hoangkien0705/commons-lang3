
package org.apache.commons.lang3.mutable;

import org.apache.commons.lang3.math.NumberUtils;


public class MutableByte extends Number implements Comparable<MutableByte>, Mutable<Number> {

    
    private static final long serialVersionUID = -1585823265L;

    
    private byte value;

    
    public MutableByte() {
        super();
    }

    
    public MutableByte(final byte value) {
        super();
        this.value = value;
    }

    
    public MutableByte(final Number value) {
        super();
        this.value = value.byteValue();
    }

    
    public MutableByte(final String value) throws NumberFormatException {
        super();
        this.value = Byte.parseByte(value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public Byte getValue() {
        return Byte.valueOf(this.value);
    }

    
    public void setValue(final byte value) {
        this.value = value;
    }

    
    @Override
    public void setValue(final Number value) {
        this.value = value.byteValue();
    }

    //-----------------------------------------------------------------------
    
    public void increment() {
        value++;
    }

    
    public byte getAndIncrement() {
        final byte last = value;
        value++;
        return last;
    }

    
    public byte incrementAndGet() {
        value++;
        return value;
    }

    
    public void decrement() {
        value--;
    }

    
    public byte getAndDecrement() {
        final byte last = value;
        value--;
        return last;
    }

    
    public byte decrementAndGet() {
        value--;
        return value;
    }

    //-----------------------------------------------------------------------
    
    public void add(final byte operand) {
        this.value += operand;
    }

    
    public void add(final Number operand) {
        this.value += operand.byteValue();
    }

    
    public void subtract(final byte operand) {
        this.value -= operand;
    }

    
    public void subtract(final Number operand) {
        this.value -= operand.byteValue();
    }

    
    public byte addAndGet(final byte operand) {
        this.value += operand;
        return value;
    }

    
    public byte addAndGet(final Number operand) {
        this.value += operand.byteValue();
        return value;
    }

    
    public byte getAndAdd(final byte operand) {
        final byte last = value;
        this.value += operand;
        return last;
    }

    
    public byte getAndAdd(final Number operand) {
        final byte last = value;
        this.value += operand.byteValue();
        return last;
    }

    //-----------------------------------------------------------------------
    // shortValue relies on Number implementation
    
    @Override
    public byte byteValue() {
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
    
    public Byte toByte() {
        return Byte.valueOf(byteValue());
    }

    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MutableByte) {
            return value == ((MutableByte) obj).byteValue();
        }
        return false;
    }

    
    @Override
    public int hashCode() {
        return value;
    }

    //-----------------------------------------------------------------------
    
    @Override
    public int compareTo(final MutableByte other) {
        return NumberUtils.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
