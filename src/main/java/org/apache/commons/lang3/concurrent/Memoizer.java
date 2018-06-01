
package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


public class Memoizer<I, O> implements Computable<I, O> {

    private final ConcurrentMap<I, Future<O>> cache = new ConcurrentHashMap<>();
    private final Computable<I, O> computable;
    private final boolean recalculate;

    
    public Memoizer(final Computable<I, O> computable) {
        this(computable, false);
    }

    
    public Memoizer(final Computable<I, O> computable, final boolean recalculate) {
        this.computable = computable;
        this.recalculate = recalculate;
    }

    
    @Override
    public O compute(final I arg) throws InterruptedException {
        while (true) {
            Future<O> future = cache.get(arg);
            if (future == null) {
                final Callable<O> eval = new Callable<O>() {

                    @Override
                    public O call() throws InterruptedException {
                        return computable.compute(arg);
                    }
                };
                final FutureTask<O> futureTask = new FutureTask<>(eval);
                future = cache.putIfAbsent(arg, futureTask);
                if (future == null) {
                    future = futureTask;
                    futureTask.run();
                }
            }
            try {
                return future.get();
            } catch (final CancellationException e) {
                cache.remove(arg, future);
            } catch (final ExecutionException e) {
                if (recalculate) {
                    cache.remove(arg, future);
                }

                throw launderException(e.getCause());
            }
        }
    }

    
    private RuntimeException launderException(final Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new IllegalStateException("Unchecked exception", throwable);
        }
    }
}
