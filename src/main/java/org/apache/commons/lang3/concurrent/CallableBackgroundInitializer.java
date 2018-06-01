
package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.Validate;


public class CallableBackgroundInitializer<T> extends BackgroundInitializer<T> {
    
    private final Callable<T> callable;

    
    public CallableBackgroundInitializer(final Callable<T> call) {
        checkCallable(call);
        callable = call;
    }

    
    public CallableBackgroundInitializer(final Callable<T> call, final ExecutorService exec) {
        super(exec);
        checkCallable(call);
        callable = call;
    }

    
    @Override
    protected T initialize() throws Exception {
        return callable.call();
    }

    
    private void checkCallable(final Callable<T> call) {
        Validate.isTrue(call != null, "Callable must not be null!");
    }
}
