
package org.apache.commons.lang3.concurrent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.apache.commons.lang3.Validate;


public class MultiBackgroundInitializer
        extends
        BackgroundInitializer<MultiBackgroundInitializer.MultiBackgroundInitializerResults> {
    
    private final Map<String, BackgroundInitializer<?>> childInitializers =
        new HashMap<>();

    
    public MultiBackgroundInitializer() {
        super();
    }

    
    public MultiBackgroundInitializer(final ExecutorService exec) {
        super(exec);
    }

    
    public void addInitializer(final String name, final BackgroundInitializer<?> init) {
        Validate.isTrue(name != null, "Name of child initializer must not be null!");
        Validate.isTrue(init != null, "Child initializer must not be null!");

        synchronized (this) {
            if (isStarted()) {
                throw new IllegalStateException(
                        "addInitializer() must not be called after start()!");
            }
            childInitializers.put(name, init);
        }
    }

    
    @Override
    protected int getTaskCount() {
        int result = 1;

        for (final BackgroundInitializer<?> bi : childInitializers.values()) {
            result += bi.getTaskCount();
        }

        return result;
    }

    
    @Override
    protected MultiBackgroundInitializerResults initialize() throws Exception {
        Map<String, BackgroundInitializer<?>> inits;
        synchronized (this) {
            // create a snapshot to operate on
            inits = new HashMap<>(
                    childInitializers);
        }

        // start the child initializers
        final ExecutorService exec = getActiveExecutor();
        for (final BackgroundInitializer<?> bi : inits.values()) {
            if (bi.getExternalExecutor() == null) {
                // share the executor service if necessary
                bi.setExternalExecutor(exec);
            }
            bi.start();
        }

        // collect the results
        final Map<String, Object> results = new HashMap<>();
        final Map<String, ConcurrentException> excepts = new HashMap<>();
        for (final Map.Entry<String, BackgroundInitializer<?>> e : inits.entrySet()) {
            try {
                results.put(e.getKey(), e.getValue().get());
            } catch (final ConcurrentException cex) {
                excepts.put(e.getKey(), cex);
            }
        }

        return new MultiBackgroundInitializerResults(inits, results, excepts);
    }

    
    public static class MultiBackgroundInitializerResults {
        
        private final Map<String, BackgroundInitializer<?>> initializers;

        
        private final Map<String, Object> resultObjects;

        
        private final Map<String, ConcurrentException> exceptions;

        
        private MultiBackgroundInitializerResults(
                final Map<String, BackgroundInitializer<?>> inits,
                final Map<String, Object> results,
                final Map<String, ConcurrentException> excepts) {
            initializers = inits;
            resultObjects = results;
            exceptions = excepts;
        }

        
        public BackgroundInitializer<?> getInitializer(final String name) {
            return checkName(name);
        }

        
        public Object getResultObject(final String name) {
            checkName(name);
            return resultObjects.get(name);
        }

        
        public boolean isException(final String name) {
            checkName(name);
            return exceptions.containsKey(name);
        }

        
        public ConcurrentException getException(final String name) {
            checkName(name);
            return exceptions.get(name);
        }

        
        public Set<String> initializerNames() {
            return Collections.unmodifiableSet(initializers.keySet());
        }

        
        public boolean isSuccessful() {
            return exceptions.isEmpty();
        }

        
        private BackgroundInitializer<?> checkName(final String name) {
            final BackgroundInitializer<?> init = initializers.get(name);
            if (init == null) {
                throw new NoSuchElementException(
                        "No child initializer with name " + name);
            }

            return init;
        }
    }
}
