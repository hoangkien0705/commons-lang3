
package org.apache.commons.lang3.math;

import java.math.BigInteger;

import org.apache.commons.lang3.Validate;


public final class Fraction extends Number implements Comparable<Fraction> {

    
    private static final long serialVersionUID = 65382027393090L;

    
    public static final Fraction ZERO = new Fraction(0, 1);
    
    public static final Fraction ONE = new Fraction(1, 1);
    
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);


    
    private final int numerator;
    
    private final int denominator;

    
    private transient int hashCode = 0;
    
    private transient String toString = null;
    
    private transient String toProperString = null;

    
    private Fraction(final int numerator, final int denominator) {
        super();
        this.numerator = numerator;
        this.denominator = denominator;
    }

    
    public static Fraction getFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (denominator < 0) {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        return new Fraction(numerator, denominator);
    }

    
    public static Fraction getFraction(final int whole, final int numerator, final int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (denominator < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        }
        if (numerator < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        }
        long numeratorValue;
        if (whole < 0) {
            numeratorValue = whole * (long) denominator - numerator;
        } else {
            numeratorValue = whole * (long) denominator + numerator;
        }
        if (numeratorValue < Integer.MIN_VALUE || numeratorValue > Integer.MAX_VALUE) {
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
        }
        return new Fraction((int) numeratorValue, denominator);
    }

    
    public static Fraction getReducedFraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        }
        if (numerator == 0) {
            return ZERO; // normalize zero.
        }
        // allow 2^k/-2^31 as a valid fraction (where k>0)
        if (denominator == Integer.MIN_VALUE && (numerator & 1) == 0) {
            numerator /= 2;
            denominator /= 2;
        }
        if (denominator < 0) {
            if (numerator == Integer.MIN_VALUE || denominator == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: can't negate");
            }
            numerator = -numerator;
            denominator = -denominator;
        }
        // simplify fraction.
        final int gcd = greatestCommonDivisor(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        return new Fraction(numerator, denominator);
    }

    
    public static Fraction getFraction(double value) {
        final int sign = value < 0 ? -1 : 1;
        value = Math.abs(value);
        if (value > Integer.MAX_VALUE || Double.isNaN(value)) {
            throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
        }
        final int wholeNumber = (int) value;
        value -= wholeNumber;

        int numer0 = 0; // the pre-previous
        int denom0 = 1; // the pre-previous
        int numer1 = 1; // the previous
        int denom1 = 0; // the previous
        int numer2 = 0; // the current, setup in calculation
        int denom2 = 0; // the current, setup in calculation
        int a1 = (int) value;
        int a2 = 0;
        double x1 = 1;
        double x2 = 0;
        double y1 = value - a1;
        double y2 = 0;
        double delta1, delta2 = Double.MAX_VALUE;
        double fraction;
        int i = 1;
        do {
            delta1 = delta2;
            a2 = (int) (x1 / y1);
            x2 = y1;
            y2 = x1 - a2 * y1;
            numer2 = a1 * numer1 + numer0;
            denom2 = a1 * denom1 + denom0;
            fraction = (double) numer2 / (double) denom2;
            delta2 = Math.abs(value - fraction);
            a1 = a2;
            x1 = x2;
            y1 = y2;
            numer0 = numer1;
            denom0 = denom1;
            numer1 = numer2;
            denom1 = denom2;
            i++;
        } while (delta1 > delta2 && denom2 <= 10000 && denom2 > 0 && i < 25);
        if (i == 25) {
            throw new ArithmeticException("Unable to convert double to fraction");
        }
        return getReducedFraction((numer0 + wholeNumber * denom0) * sign, denom0);
    }

    
    public static Fraction getFraction(String str) {
        Validate.isTrue(str != null, "The string must not be null");
        // parse double format
        int pos = str.indexOf('.');
        if (pos >= 0) {
            return getFraction(Double.parseDouble(str));
        }

        // parse X Y/Z format
        pos = str.indexOf(' ');
        if (pos > 0) {
            final int whole = Integer.parseInt(str.substring(0, pos));
            str = str.substring(pos + 1);
            pos = str.indexOf('/');
            if (pos < 0) {
                throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
            }
            final int numer = Integer.parseInt(str.substring(0, pos));
            final int denom = Integer.parseInt(str.substring(pos + 1));
            return getFraction(whole, numer, denom);
        }

        // parse Y/Z format
        pos = str.indexOf('/');
        if (pos < 0) {
            // simple whole number
            return getFraction(Integer.parseInt(str), 1);
        }
        final int numer = Integer.parseInt(str.substring(0, pos));
        final int denom = Integer.parseInt(str.substring(pos + 1));
        return getFraction(numer, denom);
    }

    // Accessors
    //-------------------------------------------------------------------

    
    public int getNumerator() {
        return numerator;
    }

    
    public int getDenominator() {
        return denominator;
    }

    
    public int getProperNumerator() {
        return Math.abs(numerator % denominator);
    }

    
    public int getProperWhole() {
        return numerator / denominator;
    }

    // Number methods
    //-------------------------------------------------------------------

    
    @Override
    public int intValue() {
        return numerator / denominator;
    }

    
    @Override
    public long longValue() {
        return (long) numerator / denominator;
    }

    
    @Override
    public float floatValue() {
        return (float) numerator / (float) denominator;
    }

    
    @Override
    public double doubleValue() {
        return (double) numerator / (double) denominator;
    }

    // Calculations
    //-------------------------------------------------------------------

    
    public Fraction reduce() {
        if (numerator == 0) {
            return equals(ZERO) ? this : ZERO;
        }
        final int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
        if (gcd == 1) {
            return this;
        }
        return Fraction.getFraction(numerator / gcd, denominator / gcd);
    }

    
    public Fraction invert() {
        if (numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        }
        if (numerator==Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: can't negate numerator");
        }
        if (numerator<0) {
            return new Fraction(-denominator, -numerator);
        }
        return new Fraction(denominator, numerator);
    }

    
    public Fraction negate() {
        // the positive range is one smaller than the negative range of an int.
        if (numerator==Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: too large to negate");
        }
        return new Fraction(-numerator, denominator);
    }

    
    public Fraction abs() {
        if (numerator >= 0) {
            return this;
        }
        return negate();
    }

    
    public Fraction pow(final int power) {
        if (power == 1) {
            return this;
        } else if (power == 0) {
            return ONE;
        } else if (power < 0) {
            if (power == Integer.MIN_VALUE) { // MIN_VALUE can't be negated.
                return this.invert().pow(2).pow(-(power / 2));
            }
            return this.invert().pow(-power);
        } else {
            final Fraction f = this.multiplyBy(this);
            if (power % 2 == 0) { // if even...
                return f.pow(power / 2);
            }
            return f.pow(power / 2).multiplyBy(this);
        }
    }

    
    private static int greatestCommonDivisor(int u, int v) {
        // From Commons Math:
        if (u == 0 || v == 0) {
            if (u == Integer.MIN_VALUE || v == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow: gcd is 2^31");
            }
            return Math.abs(u) + Math.abs(v);
        }
        // if either operand is abs 1, return 1:
        if (Math.abs(u) == 1 || Math.abs(v) == 1) {
            return 1;
        }
        // keep u and v negative, as negative integers range down to
        // -2^31, while positive numbers can only be as large as 2^31-1
        // (i.e. we can't necessarily negate a negative number without
        // overflow)
        if (u > 0) {
            u = -u;
        } // make u negative
        if (v > 0) {
            v = -v;
        } // make v negative
        // B1. [Find power of 2]
        int k = 0;
        while ((u & 1) == 0 && (v & 1) == 0 && k < 31) { // while u and v are both even...
            u /= 2;
            v /= 2;
            k++; // cast out twos.
        }
        if (k == 31) {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
        // B2. Initialize: u and v have been divided by 2^k and at least
        // one is odd.
        int t = (u & 1) == 1 ? v : -(u / 2);
        // t negative: u was odd, v may be even (t replaces v)
        // t positive: u was even, v is odd (t replaces u)
        do {
            
            // B4/B3: cast out twos from t.
            while ((t & 1) == 0) { // while t is even..
                t /= 2; // cast out twos
            }
            // B5 [reset max(u,v)]
            if (t > 0) {
                u = -t;
            } else {
                v = t;
            }
            // B6/B3. at this point both u and v should be odd.
            t = (v - u) / 2;
            // |u| larger: t positive (replace u)
            // |v| larger: t negative (replace v)
        } while (t != 0);
        return -u * (1 << k); // gcd is u*2^k
    }

    // Arithmetic
    //-------------------------------------------------------------------

    
    private static int mulAndCheck(final int x, final int y) {
        final long m = (long) x * (long) y;
        if (m < Integer.MIN_VALUE || m > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: mul");
        }
        return (int) m;
    }

    
    private static int mulPosAndCheck(final int x, final int y) {
        
        final long m = (long) x * (long) y;
        if (m > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: mulPos");
        }
        return (int) m;
    }

    
    private static int addAndCheck(final int x, final int y) {
        final long s = (long) x + (long) y;
        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return (int) s;
    }

    
    private static int subAndCheck(final int x, final int y) {
        final long s = (long) x - (long) y;
        if (s < Integer.MIN_VALUE || s > Integer.MAX_VALUE) {
            throw new ArithmeticException("overflow: add");
        }
        return (int) s;
    }

    
    public Fraction add(final Fraction fraction) {
        return addSub(fraction, true );
    }

    
    public Fraction subtract(final Fraction fraction) {
        return addSub(fraction, false );
    }

    
    private Fraction addSub(final Fraction fraction, final boolean isAdd) {
        Validate.isTrue(fraction != null, "The fraction must not be null");
        // zero is identity for addition.
        if (numerator == 0) {
            return isAdd ? fraction : fraction.negate();
        }
        if (fraction.numerator == 0) {
            return this;
        }
        // if denominators are randomly distributed, d1 will be 1 about 61%
        // of the time.
        final int d1 = greatestCommonDivisor(denominator, fraction.denominator);
        if (d1 == 1) {
            // result is ( (u*v' +/- u'v) / u'v')
            final int uvp = mulAndCheck(numerator, fraction.denominator);
            final int upv = mulAndCheck(fraction.numerator, denominator);
            return new Fraction(isAdd ? addAndCheck(uvp, upv) : subAndCheck(uvp, upv), mulPosAndCheck(denominator,
                    fraction.denominator));
        }
        // the quantity 't' requires 65 bits of precision; see knuth 4.5.1
        // exercise 7. we're going to use a BigInteger.
        // t = u(v'/d1) +/- v(u'/d1)
        final BigInteger uvp = BigInteger.valueOf(numerator).multiply(BigInteger.valueOf(fraction.denominator / d1));
        final BigInteger upv = BigInteger.valueOf(fraction.numerator).multiply(BigInteger.valueOf(denominator / d1));
        final BigInteger t = isAdd ? uvp.add(upv) : uvp.subtract(upv);
        // but d2 doesn't need extra precision because
        // d2 = gcd(t,d1) = gcd(t mod d1, d1)
        final int tmodd1 = t.mod(BigInteger.valueOf(d1)).intValue();
        final int d2 = tmodd1 == 0 ? d1 : greatestCommonDivisor(tmodd1, d1);

        // result is (t/d2) / (u'/d1)(v'/d2)
        final BigInteger w = t.divide(BigInteger.valueOf(d2));
        if (w.bitLength() > 31) {
            throw new ArithmeticException("overflow: numerator too large after multiply");
        }
        return new Fraction(w.intValue(), mulPosAndCheck(denominator / d1, fraction.denominator / d2));
    }

    
    public Fraction multiplyBy(final Fraction fraction) {
        Validate.isTrue(fraction != null, "The fraction must not be null");
        if (numerator == 0 || fraction.numerator == 0) {
            return ZERO;
        }
        // knuth 4.5.1
        // make sure we don't overflow unless the result *must* overflow.
        final int d1 = greatestCommonDivisor(numerator, fraction.denominator);
        final int d2 = greatestCommonDivisor(fraction.numerator, denominator);
        return getReducedFraction(mulAndCheck(numerator / d1, fraction.numerator / d2),
                mulPosAndCheck(denominator / d2, fraction.denominator / d1));
    }

    
    public Fraction divideBy(final Fraction fraction) {
        Validate.isTrue(fraction != null, "The fraction must not be null");
        if (fraction.numerator == 0) {
            throw new ArithmeticException("The fraction to divide by must not be zero");
        }
        return multiplyBy(fraction.invert());
    }

    // Basics
    //-------------------------------------------------------------------

    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Fraction)) {
            return false;
        }
        final Fraction other = (Fraction) obj;
        return getNumerator() == other.getNumerator() && getDenominator() == other.getDenominator();
    }

    
    @Override
    public int hashCode() {
        if (hashCode == 0) {
            // hash code update should be atomic.
            hashCode = 37 * (37 * 17 + getNumerator()) + getDenominator();
        }
        return hashCode;
    }

    
    @Override
    public int compareTo(final Fraction other) {
        if (this == other) {
            return 0;
        }
        if (numerator == other.numerator && denominator == other.denominator) {
            return 0;
        }

        // otherwise see which is less
        final long first = (long) numerator * (long) other.denominator;
        final long second = (long) other.numerator * (long) denominator;
        if (first == second) {
            return 0;
        } else if (first < second) {
            return -1;
        } else {
            return 1;
        }
    }

    
    @Override
    public String toString() {
        if (toString == null) {
            toString = getNumerator() + "/" + getDenominator();
        }
        return toString;
    }

    
    public String toProperString() {
        if (toProperString == null) {
            if (numerator == 0) {
                toProperString = "0";
            } else if (numerator == denominator) {
                toProperString = "1";
            } else if (numerator == -1 * denominator) {
                toProperString = "-1";
            } else if ((numerator > 0 ? -numerator : numerator) < -denominator) {
                // note that we do the magnitude comparison test above with
                // NEGATIVE (not positive) numbers, since negative numbers
                // have a larger range. otherwise numerator==Integer.MIN_VALUE
                // is handled incorrectly.
                final int properNumerator = getProperNumerator();
                if (properNumerator == 0) {
                    toProperString = Integer.toString(getProperWhole());
                } else {
                    toProperString = getProperWhole() + " " + properNumerator + "/" + getDenominator();
                }
            } else {
                toProperString = getNumerator() + "/" + getDenominator();
            }
        }
        return toProperString;
    }
}
