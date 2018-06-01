
package org.apache.commons.lang3.concurrent;


public abstract class LazyInitializer<T> implements ConcurrentInitializer<T> {

    private static final Object NO_INIT = new Object();

    @SuppressWarnings("unchecked")
    
    private volatile T object = (T) NO_INIT;

    
    @Override
    public T get() throws ConcurrentException {
        // use a temporary variable to reduce the number of reads of the
        // volatile field
        T result = object;

        if (result == NO_INIT) {
            synchronized (this) {
                result = object;
                if (result == NO_INIT) {
                    object = result = initialize();
                }
            }
        }

        return result;
    }

    
    protected abstract T initialize() throws ConcurrentException;
}
