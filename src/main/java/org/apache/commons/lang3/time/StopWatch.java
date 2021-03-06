/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.lang3.time;

import java.util.concurrent.TimeUnit;


public class StopWatch {

    private static final long NANO_2_MILLIS = 1000000L;


    
    public static StopWatch createStarted() {
        final StopWatch sw = new StopWatch();
        sw.start();
        return sw;
    }

    
    private enum State {

        UNSTARTED {
            @Override
            boolean isStarted() {
                return false;
            }
            @Override
            boolean isStopped() {
                return true;
            }
            @Override
            boolean isSuspended() {
                return false;
            }
        },
        RUNNING {
            @Override
            boolean isStarted() {
                return true;
            }
            @Override
            boolean isStopped() {
                return false;
            }
            @Override
            boolean isSuspended() {
                return false;
            }
        },
        STOPPED {
            @Override
            boolean isStarted() {
                return false;
            }
            @Override
            boolean isStopped() {
                return true;
            }
            @Override
            boolean isSuspended() {
                return false;
            }
        },
        SUSPENDED {
            @Override
            boolean isStarted() {
                return true;
            }
            @Override
            boolean isStopped() {
                return false;
            }
            @Override
            boolean isSuspended() {
                return true;
            }
        };

        
        abstract boolean isStarted();

        
        abstract boolean isStopped();

        
        abstract boolean isSuspended();
    }

    
    private enum SplitState {
        SPLIT,
        UNSPLIT
    }
    
    private State runningState = State.UNSTARTED;

    
    private SplitState splitState = SplitState.UNSPLIT;

    
    private long startTime;

    
    private long startTimeMillis;

    
    private long stopTime;

    
    public StopWatch() {
        super();
    }

    
    public void start() {
        if (this.runningState == State.STOPPED) {
            throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
        }
        if (this.runningState != State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch already started. ");
        }
        this.startTime = System.nanoTime();
        this.startTimeMillis = System.currentTimeMillis();
        this.runningState = State.RUNNING;
    }


    
    public void stop() {
        if (this.runningState != State.RUNNING && this.runningState != State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        if (this.runningState == State.RUNNING) {
            this.stopTime = System.nanoTime();
        }
        this.runningState = State.STOPPED;
    }

    
    public void reset() {
        this.runningState = State.UNSTARTED;
        this.splitState = SplitState.UNSPLIT;
    }

    
    public void split() {
        if (this.runningState != State.RUNNING) {
            throw new IllegalStateException("Stopwatch is not running. ");
        }
        this.stopTime = System.nanoTime();
        this.splitState = SplitState.SPLIT;
    }

    
    public void unsplit() {
        if (this.splitState != SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch has not been split. ");
        }
        this.splitState = SplitState.UNSPLIT;
    }

    
    public void suspend() {
        if (this.runningState != State.RUNNING) {
            throw new IllegalStateException("Stopwatch must be running to suspend. ");
        }
        this.stopTime = System.nanoTime();
        this.runningState = State.SUSPENDED;
    }

    
    public void resume() {
        if (this.runningState != State.SUSPENDED) {
            throw new IllegalStateException("Stopwatch must be suspended to resume. ");
        }
        this.startTime += System.nanoTime() - this.stopTime;
        this.runningState = State.RUNNING;
    }

    
    public long getTime() {
        return getNanoTime() / NANO_2_MILLIS;
    }

    
    public long getTime(final TimeUnit timeUnit) {
        return timeUnit.convert(getNanoTime(), TimeUnit.NANOSECONDS);
    }

    
    public long getNanoTime() {
        if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED) {
            return this.stopTime - this.startTime;
        } else if (this.runningState == State.UNSTARTED) {
            return 0;
        } else if (this.runningState == State.RUNNING) {
            return System.nanoTime() - this.startTime;
        }
        throw new RuntimeException("Illegal running state has occurred.");
    }

    
    public long getSplitTime() {
        return getSplitNanoTime() / NANO_2_MILLIS;
    }
    
    public long getSplitNanoTime() {
        if (this.splitState != SplitState.SPLIT) {
            throw new IllegalStateException("Stopwatch must be split to get the split time. ");
        }
        return this.stopTime - this.startTime;
    }

    
    public long getStartTime() {
        if (this.runningState == State.UNSTARTED) {
            throw new IllegalStateException("Stopwatch has not been started");
        }
        // System.nanoTime is for elapsed time
        return this.startTimeMillis;
    }

    
    @Override
    public String toString() {
        return DurationFormatUtils.formatDurationHMS(getTime());
    }

    
    public String toSplitString() {
        return DurationFormatUtils.formatDurationHMS(getSplitTime());
    }

    
    public boolean isStarted() {
        return runningState.isStarted();
    }

    
    public boolean isSuspended() {
        return runningState.isSuspended();
    }

    
    public boolean isStopped() {
        return runningState.isStopped();
    }

}
