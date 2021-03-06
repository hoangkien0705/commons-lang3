
package org.apache.commons.lang3.mutable;


public class MutableDouble extends Number implements Comparable<MutableDouble>, Mutable<Number> {

    
    private static final long serialVersionUID = 1587163916L;

    
    private double value;

    
    public MutableDouble() {
        super();
    }

    
    public MutableDouble(final double value) {
        super();
        this.value = value;
    }

    
    public MutableDouble(final Number value) {
        super();
        this.value = value.doubleValue();
    }

    
    public MutableDouble(final String value) throws NumberFormatException {
        super();
        this.value = Double.parseDouble(value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public Double getValue() {
        return Double.valueOf(this.value);
    }

    
    public void setValue(final double value) {
        this.value = value;
    }

    
    @Override
    public void setValue(final Number value) {
        this.value = value.doubleValue();
    }

    //-----------------------------------------------------------------------
    
    public boolean isNaN() {
        return Double.isNaN(value);
    }

    
    public boolean isInfinite() {
        return Double.isInfinite(value);
    }

    //-----------------------------------------------------------------------
    
    public void increment() {
        value++;
    }

    
    public double getAndIncrement() {
        final double last = value;
        value++;
        return last;
    }

    
    public double incrementAndGet() {
        value++;
        return value;
    }

    
    public void decrement() {
        value--;
    }

    
    public double getAndDecrement() {
        final double last = value;
        value--;
        return last;
    }

    
    public double decrementAndGet() {
        value--;
        return value;
    }

    //-----------------------------------------------------------------------
    
    public void add(final double operand) {
        this.value += operand;
    }

    
    public void add(final Number operand) {
        this.value += operand.doubleValue();
    }

    
    public void subtract(final double operand) {
        this.value -= operand;
    }

    
    public void subtract(final Number operand) {
        this.value -= operand.doubleValue();
    }

    
    public double addAndGet(final double operand) {
        this.value += operand;
        return value;
    }

    
    public double addAndGet(final Number operand) {
        this.value += operand.doubleValue();
        return value;
    }

    
    public double getAndAdd(final double operand) {
        final double last = value;
        this.value += operand;
        return last;
    }

    
    public double getAndAdd(final Number operand) {
        final double last = value;
        this.value += operand.doubleValue();
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
        return (long) value;
    }

    
    @Override
    public float floatValue() {
        return (float) value;
    }

    
    @Override
    public double doubleValue() {
        return value;
    }

    //-----------------------------------------------------------------------
    
    public Double toDouble() {
        return Double.valueOf(doubleValue());
    }

    //-----------------------------------------------------------------------
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableDouble
            && Double.doubleToLongBits(((MutableDouble) obj).value) == Double.doubleToLongBits(value);
    }

    
    @Override
    public int hashCode() {
        final long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ bits >>> 32);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public int compareTo(final MutableDouble other) {
        return Double.compare(this.value, other.value);
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
