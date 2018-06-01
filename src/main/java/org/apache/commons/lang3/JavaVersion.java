
package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;


public enum JavaVersion {

    
    JAVA_0_9(1.5f, "0.9"),

    
    JAVA_1_1(1.1f, "1.1"),

    
    JAVA_1_2(1.2f, "1.2"),

    
    JAVA_1_3(1.3f, "1.3"),

    
    JAVA_1_4(1.4f, "1.4"),

    
    JAVA_1_5(1.5f, "1.5"),

    
    JAVA_1_6(1.6f, "1.6"),

    
    JAVA_1_7(1.7f, "1.7"),

    
    JAVA_1_8(1.8f, "1.8"),

    
    @Deprecated
    JAVA_1_9(9.0f, "9"),

    
    JAVA_9(9.0f, "9"),

    
    JAVA_10(10.0f, "10"),

    
    JAVA_11(11.0f, "11"),

    
    JAVA_RECENT(maxVersion(), Float.toString(maxVersion()));

    
    private final float value;

    
    private final String name;

    
    JavaVersion(final float value, final String name) {
        this.value = value;
        this.name = name;
    }

    //-----------------------------------------------------------------------
    
    public boolean atLeast(final JavaVersion requiredVersion) {
        return this.value >= requiredVersion.value;
    }

    
    // helper for static importing
    static JavaVersion getJavaVersion(final String nom) {
        return get(nom);
    }

    
    static JavaVersion get(final String nom) {
        if ("0.9".equals(nom)) {
            return JAVA_0_9;
        } else if ("1.1".equals(nom)) {
            return JAVA_1_1;
        } else if ("1.2".equals(nom)) {
            return JAVA_1_2;
        } else if ("1.3".equals(nom)) {
            return JAVA_1_3;
        } else if ("1.4".equals(nom)) {
            return JAVA_1_4;
        } else if ("1.5".equals(nom)) {
            return JAVA_1_5;
        } else if ("1.6".equals(nom)) {
            return JAVA_1_6;
        } else if ("1.7".equals(nom)) {
            return JAVA_1_7;
        } else if ("1.8".equals(nom)) {
            return JAVA_1_8;
        } else if ("9".equals(nom)) {
            return JAVA_9;
        } else if ("10".equals(nom)) {
            return JAVA_10;
        } else if ("11".equals(nom)) {
            return JAVA_11;
        }
        if (nom == null) {
            return null;
        }
        final float v = toFloatVersion(nom);
        if ((v - 1.) < 1.) { // then we need to check decimals > .9
            final int firstComma = Math.max(nom.indexOf('.'), nom.indexOf(','));
            final int end = Math.max(nom.length(), nom.indexOf(',', firstComma));
            if (Float.parseFloat(nom.substring(firstComma + 1, end)) > .9f) {
                return JAVA_RECENT;
            }
        } else if (v > 10) {
            return JAVA_RECENT;
        }
        return null;
    }

    //-----------------------------------------------------------------------
    
    @Override
    public String toString() {
        return name;
    }

    
    private static float maxVersion() {
        final float v = toFloatVersion(System.getProperty("java.specification.version", "99.0"));
        if (v > 0) {
            return v;
        }
        return 99f;
    }

    
    private static float toFloatVersion(final String value) {
        final int defaultReturnValue = -1;
        if (value.contains(".")) {
            final String[] toParse = value.split("\\.");
            if (toParse.length >= 2) {
                return NumberUtils.toFloat(toParse[0] + '.' + toParse[1], defaultReturnValue);
            }
        } else {
            return NumberUtils.toFloat(value, defaultReturnValue);
        }
        return defaultReturnValue;
    }
}
