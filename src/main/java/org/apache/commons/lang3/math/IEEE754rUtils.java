
package org.apache.commons.lang3.math;

import org.apache.commons.lang3.Validate;


public class IEEE754rUtils {

     
    public static double min(final double... array) {
        Validate.isTrue(array != null, "The Array must not be null");
        Validate.isTrue(array.length != 0, "Array cannot be empty.");

        // Finds and returns min
        double min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = min(array[i], min);
        }

        return min;
    }

    
    public static float min(final float... array) {
        Validate.isTrue(array != null, "The Array must not be null");
        Validate.isTrue(array.length != 0, "Array cannot be empty.");

        // Finds and returns min
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = min(array[i], min);
        }

        return min;
    }

    
    public static double min(final double a, final double b, final double c) {
        return min(min(a, b), c);
    }

    
    public static double min(final double a, final double b) {
        if(Double.isNaN(a)) {
            return b;
        } else
        if(Double.isNaN(b)) {
            return a;
        } else {
            return Math.min(a, b);
        }
    }

    
    public static float min(final float a, final float b, final float c) {
        return min(min(a, b), c);
    }

    
    public static float min(final float a, final float b) {
        if(Float.isNaN(a)) {
            return b;
        } else
        if(Float.isNaN(b)) {
            return a;
        } else {
            return Math.min(a, b);
        }
    }

    
    public static double max(final double... array) {
        Validate.isTrue(array != null, "The Array must not be null");
        Validate.isTrue(array.length != 0, "Array cannot be empty.");

        // Finds and returns max
        double max = array[0];
        for (int j = 1; j < array.length; j++) {
            max = max(array[j], max);
        }

        return max;
    }

    
    public static float max(final float... array) {
        Validate.isTrue(array != null, "The Array must not be null");
        Validate.isTrue(array.length != 0, "Array cannot be empty.");

        // Finds and returns max
        float max = array[0];
        for (int j = 1; j < array.length; j++) {
            max = max(array[j], max);
        }

        return max;
    }

    
    public static double max(final double a, final double b, final double c) {
        return max(max(a, b), c);
    }

    
    public static double max(final double a, final double b) {
        if(Double.isNaN(a)) {
            return b;
        } else
        if(Double.isNaN(b)) {
            return a;
        } else {
            return Math.max(a, b);
        }
    }

    
    public static float max(final float a, final float b, final float c) {
        return max(max(a, b), c);
    }

    
    public static float max(final float a, final float b) {
        if(Float.isNaN(a)) {
            return b;
        } else
        if(Float.isNaN(b)) {
            return a;
        } else {
            return Math.max(a, b);
        }
    }

}
