
package org.apache.commons.lang3.concurrent;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


public class EventCountCircuitBreaker extends AbstractCircuitBreaker<Integer> {

    
    private static final Map<State, StateStrategy> STRATEGY_MAP = createStrategyMap();

    
    private final AtomicReference<CheckIntervalData> checkIntervalData;

    
    private final int openingThreshold;

    
    private final long openingInterval;

    
    private final int closingThreshold;

    
    private final long closingInterval;

    
    public EventCountCircuitBreaker(final int openingThreshold, final long openingInterval,
                                    final TimeUnit openingUnit, final int closingThreshold, final long closingInterval,
                                    final TimeUnit closingUnit) {
        super();
        checkIntervalData = new AtomicReference<>(new CheckIntervalData(0, 0));
        this.openingThreshold = openingThreshold;
        this.openingInterval = openingUnit.toNanos(openingInterval);
        this.closingThreshold = closingThreshold;
        this.closingInterval = closingUnit.toNanos(closingInterval);
    }

    
    public EventCountCircuitBreaker(final int openingThreshold, final long checkInterval, final TimeUnit checkUnit,
                                    final int closingThreshold) {
        this(openingThreshold, checkInterval, checkUnit, closingThreshold, checkInterval,
                checkUnit);
    }

    
    public EventCountCircuitBreaker(final int threshold, final long checkInterval, final TimeUnit checkUnit) {
        this(threshold, checkInterval, checkUnit, threshold);
    }

    
    public int getOpeningThreshold() {
        return openingThreshold;
    }

    
    public long getOpeningInterval() {
        return openingInterval;
    }

    
    public int getClosingThreshold() {
        return closingThreshold;
    }

    
    public long getClosingInterval() {
        return closingInterval;
    }

    
    @Override
    public boolean checkState() {
        return performStateCheck(0);
    }

    
    @Override
    public boolean incrementAndCheckState(final Integer increment)
            throws CircuitBreakingException {
        return performStateCheck(increment);
    }

    
    public boolean incrementAndCheckState() {
        return incrementAndCheckState(1);
    }

    
    @Override
    public void open() {
        super.open();
        checkIntervalData.set(new CheckIntervalData(0, now()));
    }

    
    @Override
    public void close() {
        super.close();
        checkIntervalData.set(new CheckIntervalData(0, now()));
    }

    
    private boolean performStateCheck(final int increment) {
        CheckIntervalData currentData;
        CheckIntervalData nextData;
        State currentState;

        do {
            final long time = now();
            currentState = state.get();
            currentData = checkIntervalData.get();
            nextData = nextCheckIntervalData(increment, currentData, currentState, time);
        } while (!updateCheckIntervalData(currentData, nextData));

        // This might cause a race condition if other changes happen in between!
        // Refer to the header comment!
        if (stateStrategy(currentState).isStateTransition(this, currentData, nextData)) {
            currentState = currentState.oppositeState();
            changeStateAndStartNewCheckInterval(currentState);
        }
        return !isOpen(currentState);
    }

    
    private boolean updateCheckIntervalData(final CheckIntervalData currentData,
            final CheckIntervalData nextData) {
        return currentData == nextData
                || checkIntervalData.compareAndSet(currentData, nextData);
    }

    
    private void changeStateAndStartNewCheckInterval(final State newState) {
        changeState(newState);
        checkIntervalData.set(new CheckIntervalData(0, now()));
    }

    
    private CheckIntervalData nextCheckIntervalData(final int increment,
            final CheckIntervalData currentData, final State currentState, final long time) {
        CheckIntervalData nextData;
        if (stateStrategy(currentState).isCheckIntervalFinished(this, currentData, time)) {
            nextData = new CheckIntervalData(increment, time);
        } else {
            nextData = currentData.increment(increment);
        }
        return nextData;
    }

    
    long now() {
        return System.nanoTime();
    }

    
    private static StateStrategy stateStrategy(final State state) {
        return STRATEGY_MAP.get(state);
    }

    
    private static Map<State, StateStrategy> createStrategyMap() {
        final Map<State, StateStrategy> map = new EnumMap<>(State.class);
        map.put(State.CLOSED, new StateStrategyClosed());
        map.put(State.OPEN, new StateStrategyOpen());
        return map;
    }

    
    private static class CheckIntervalData {
        
        private final int eventCount;

        
        private final long checkIntervalStart;

        
        CheckIntervalData(final int count, final long intervalStart) {
            eventCount = count;
            checkIntervalStart = intervalStart;
        }

        
        public int getEventCount() {
            return eventCount;
        }

        
        public long getCheckIntervalStart() {
            return checkIntervalStart;
        }

        
        public CheckIntervalData increment(final int delta) {
            return (delta != 0) ? new CheckIntervalData(getEventCount() + delta,
                    getCheckIntervalStart()) : this;
        }
    }

    
    private abstract static class StateStrategy {
        
        public boolean isCheckIntervalFinished(final EventCountCircuitBreaker breaker,
                final CheckIntervalData currentData, final long now) {
            return now - currentData.getCheckIntervalStart() > fetchCheckInterval(breaker);
        }

        
        public abstract boolean isStateTransition(EventCountCircuitBreaker breaker,
                CheckIntervalData currentData, CheckIntervalData nextData);

        
        protected abstract long fetchCheckInterval(EventCountCircuitBreaker breaker);
    }

    
    private static class StateStrategyClosed extends StateStrategy {

        
        @Override
        public boolean isStateTransition(final EventCountCircuitBreaker breaker,
                final CheckIntervalData currentData, final CheckIntervalData nextData) {
            return nextData.getEventCount() > breaker.getOpeningThreshold();
        }

        
        @Override
        protected long fetchCheckInterval(final EventCountCircuitBreaker breaker) {
            return breaker.getOpeningInterval();
        }
    }

    
    private static class StateStrategyOpen extends StateStrategy {
        
        @Override
        public boolean isStateTransition(final EventCountCircuitBreaker breaker,
                final CheckIntervalData currentData, final CheckIntervalData nextData) {
            return nextData.getCheckIntervalStart() != currentData
                    .getCheckIntervalStart()
                    && currentData.getEventCount() < breaker.getClosingThreshold();
        }

        
        @Override
        protected long fetchCheckInterval(final EventCountCircuitBreaker breaker) {
            return breaker.getClosingInterval();
        }
    }

}
