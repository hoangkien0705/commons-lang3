

package org.apache.commons.lang3.builder;

// adapted from org.apache.axis.utils.IDKey


final class IDKey {
        private final Object value;
        private final int id;

        
        IDKey(final Object _value) {
            // This is the Object hash code
            id = System.identityHashCode(_value);
            // There have been some cases (LANG-459) that return the
            // same identity hash code for different objects.  So
            // the value is also added to disambiguate these cases.
            value = _value;
        }

        
        @Override
        public int hashCode() {
           return id;
        }

        
        @Override
        public boolean equals(final Object other) {
            if (!(other instanceof IDKey)) {
                return false;
            }
            final IDKey idKey = (IDKey) other;
            if (id != idKey.id) {
                return false;
            }
            // Note that identity equals is used.
            return value == idKey.value;
         }
}
