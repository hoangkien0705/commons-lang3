
package org.apache.commons.lang3.concurrent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.atomic.AtomicReference;


public abstract class AbstractCircuitBreaker<T> implements CircuitBreaker<T> {
    
    public static final String PROPERTY_NAME = "open";

    
    protected final AtomicReference<State> state = new AtomicReference<>(State.CLOSED);

    
    private final PropertyChangeSupport changeSupport;

    
    public AbstractCircuitBreaker() {
        changeSupport = new PropertyChangeSupport(this);
    }

    
    @Override
    public boolean isOpen() {
        return isOpen(state.get());
    }

    
    @Override
    public boolean isClosed() {
        return !isOpen();
    }

    
    @Override
    public abstract boolean checkState();

    
    @Override
    public abstract boolean incrementAndCheckState(T increment);

    
    @Override
    public void close() {
        changeState(State.CLOSED);
    }

    
    @Override
    public void open() {
        changeState(State.OPEN);
    }

    
    protected static boolean isOpen(final State state) {
        return state == State.OPEN;
    }

    
    protected void changeState(final State newState) {
        if (state.compareAndSet(newState.oppositeState(), newState)) {
            changeSupport.firePropertyChange(PROPERTY_NAME, !isOpen(newState), isOpen(newState));
        }
    }

    
    public void addChangeListener(final PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    
    public void removeChangeListener(final PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    
    protected enum State {
        CLOSED {
            
            @Override
            public State oppositeState() {
                return OPEN;
            }
        },

        OPEN {
            
            @Override
            public State oppositeState() {
                return CLOSED;
            }
        };

        
        public abstract State oppositeState();
    }

}
