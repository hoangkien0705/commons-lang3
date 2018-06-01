
package org.apache.commons.lang3.text;

import java.util.Map;


@Deprecated
public abstract class StrLookup<V> {

    
    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<>(null);

    
    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    //-----------------------------------------------------------------------
    
    public static StrLookup<?> noneLookup() {
        return NONE_LOOKUP;
    }

    
    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    
    public static <V> StrLookup<V> mapLookup(final Map<String, V> map) {
        return new MapStrLookup<>(map);
    }

    //-----------------------------------------------------------------------
    
    protected StrLookup() {
        super();
    }

    
    public abstract String lookup(String key);

    //-----------------------------------------------------------------------
    
    static class MapStrLookup<V> extends StrLookup<V> {

        
        private final Map<String, V> map;

        
        MapStrLookup(final Map<String, V> map) {
            this.map = map;
        }

        
        @Override
        public String lookup(final String key) {
            if (map == null) {
                return null;
            }
            final Object obj = map.get(key);
            if (obj == null) {
                return null;
            }
            return obj.toString();
        }
    }

    //-----------------------------------------------------------------------
    
    private static class SystemPropertiesStrLookup extends StrLookup<String> {
        
        @Override
        public String lookup(final String key) {
            if (key.length() > 0) {
                try {
                    return System.getProperty(key);
                } catch (final SecurityException scex) {
                    // Squelched. All lookup(String) will return null.
                }
            }
            return null;
        }
    }
}
