
package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public abstract class BackgroundInitializer<T> implements
        ConcurrentInitializer<T> {
    
    private ExecutorService externalExecutor; // @GuardedBy("this")

    
    private ExecutorService executor; // @GuardedBy("this")

    
    private Future<T> future;  // @GuardedBy("this")

    
    protected BackgroundInitializer() {
        this(null);
    }

    
    protected BackgroundInitializer(final ExecutorService exec) {
        setExternalExecutor(exec);
    }

    
    public final synchronized ExecutorService getExternalExecutor() {
        return externalExecutor;
    }

    
    public synchronized boolean isStarted() {
        return future != null;
    }

    
    public final synchronized void setExternalExecutor(
            final ExecutorService externalExecutor) {
        if (isStarted()) {
            throw new IllegalStateException(
                    "Cannot set ExecutorService after start()!");
        }

        this.externalExecutor = externalExecutor;
    }

    
    public synchronized boolean start() {
        // Not yet started?
        if (!isStarted()) {

            // Determine the executor to use and whether a temporary one has to
            // be created
            ExecutorService tempExec;
            executor = getExternalExecutor();
            if (executor == null) {
                executor = tempExec = createExecutor();
            } else {
                tempExec = null;
            }

            future = executor.submit(createTask(tempExec));

            return true;
        }

        return false;
    }

    
    @Override
    public T get() throws ConcurrentException {
        try {
            return getFuture().get();
        } catch (final ExecutionException execex) {
            ConcurrentUtils.handleCause(execex);
            return null; // should not be reached
        } catch (final InterruptedException iex) {
            // reset interrupted state
            Thread.currentThread().interrupt();
            throw new ConcurrentException(iex);
        }
    }

    
    public synchronized Future<T> getFuture() {
        if (future == null) {
            throw new IllegalStateException("start() must be called first!");
        }

        return future;
    }

    
    protected final synchronized ExecutorService getActiveExecutor() {
        return executor;
    }

    
    protected int getTaskCount() {
        return 1;
    }

    
    protected abstract T initialize() throws Exception;

    
    private Callable<T> createTask(final ExecutorService execDestroy) {
        return new InitializationTask(execDestroy);
    }

    
    private ExecutorService createExecutor() {
        return Executors.newFixedThreadPool(getTaskCount());
    }

    private class InitializationTask implements Callable<T> {
        
        private final ExecutorService execFinally;

        
        InitializationTask(final ExecutorService exec) {
            execFinally = exec;
        }

        
        @Override
        public T call() throws Exception {
            try {
                return initialize();
            } finally {
                if (execFinally != null) {
                    execFinally.shutdown();
                }
            }
        }
    }
}
