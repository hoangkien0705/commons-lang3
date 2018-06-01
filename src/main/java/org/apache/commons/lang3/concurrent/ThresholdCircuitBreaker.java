
package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicLong;


public class ThresholdCircuitBreaker extends AbstractCircuitBreaker<Long> {
    
    private static final long INITIAL_COUNT = 0L;

    
    private final long threshold;

    
    private final AtomicLong used;

    
    public ThresholdCircuitBreaker(final long threshold) {
        super();
        this.used = new AtomicLong(INITIAL_COUNT);
        this.threshold = threshold;
    }

    
    public long getThreshold() {
        return threshold;
    }

    
    @Override
    public boolean checkState() throws CircuitBreakingException {
        return isOpen();
    }

    
    @Override
    public void close() {
        super.close();
        this.used.set(INITIAL_COUNT);
    }

    
    @Override
    public boolean incrementAndCheckState(final Long increment) throws CircuitBreakingException {
        if (threshold == 0) {
            open();
        }

        final long used = this.used.addAndGet(increment);
        if (used > threshold) {
            open();
        }

        return checkState();
    }

}
