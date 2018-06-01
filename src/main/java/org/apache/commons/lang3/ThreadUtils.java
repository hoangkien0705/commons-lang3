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
package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ThreadUtils {
    public static Thread findThreadById(final long threadId, final ThreadGroup threadGroup) {
        Validate.isTrue(threadGroup != null, "The thread group must not be null");
        final Thread thread = findThreadById(threadId);
        if(thread != null && threadGroup.equals(thread.getThreadGroup())) {
            return thread;
        }
        return null;
    }

    
    public static Thread findThreadById(final long threadId, final String threadGroupName) {
        Validate.isTrue(threadGroupName != null, "The thread group name must not be null");
        final Thread thread = findThreadById(threadId);
        if(thread != null && thread.getThreadGroup() != null && thread.getThreadGroup().getName().equals(threadGroupName)) {
            return thread;
        }
        return null;
    }

    
    public static Collection<Thread> findThreadsByName(final String threadName, final ThreadGroup threadGroup) {
        return findThreads(threadGroup, false, new NamePredicate(threadName));
    }

    
    public static Collection<Thread> findThreadsByName(final String threadName, final String threadGroupName) {
        Validate.isTrue(threadName != null, "The thread name must not be null");
        Validate.isTrue(threadGroupName != null, "The thread group name must not be null");

        final Collection<ThreadGroup> threadGroups = findThreadGroups(new NamePredicate(threadGroupName));

        if(threadGroups.isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<Thread> result = new ArrayList<>();
        final NamePredicate threadNamePredicate = new NamePredicate(threadName);
        for(final ThreadGroup group : threadGroups) {
            result.addAll(findThreads(group, false, threadNamePredicate));
        }
        return Collections.unmodifiableCollection(result);
    }

    
    public static Collection<ThreadGroup> findThreadGroupsByName(final String threadGroupName) {
        return findThreadGroups(new NamePredicate(threadGroupName));
    }

    
    public static Collection<ThreadGroup> getAllThreadGroups() {
        return findThreadGroups(ALWAYS_TRUE_PREDICATE);
    }

    
    public static ThreadGroup getSystemThreadGroup() {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        while(threadGroup.getParent() != null) {
            threadGroup = threadGroup.getParent();
        }
        return threadGroup;
    }

    
    public static Collection<Thread> getAllThreads() {
        return findThreads(ALWAYS_TRUE_PREDICATE);
    }

    
    public static Collection<Thread> findThreadsByName(final String threadName) {
        return findThreads(new NamePredicate(threadName));
    }

    
    public static Thread findThreadById(final long threadId) {
        final Collection<Thread> result = findThreads(new ThreadIdPredicate(threadId));
        return result.isEmpty() ? null : result.iterator().next();
    }

    
    public ThreadUtils() {
        super();
    }

    
    //if java minimal version for lang becomes 1.8 extend this interface from java.util.function.Predicate
    public interface ThreadPredicate {

        
        boolean test(Thread thread);
    }

    
    //if java minimal version for lang becomes 1.8 extend this interface from java.util.function.Predicate
    public interface ThreadGroupPredicate {

        
        boolean test(ThreadGroup threadGroup);
    }

    
    public static final AlwaysTruePredicate ALWAYS_TRUE_PREDICATE = new AlwaysTruePredicate();

    
    private static final class AlwaysTruePredicate implements ThreadPredicate, ThreadGroupPredicate{

        private AlwaysTruePredicate() {
        }

        @Override
        public boolean test(final ThreadGroup threadGroup) {
            return true;
        }

        @Override
        public boolean test(final Thread thread) {
            return true;
        }
    }

    
    public static class NamePredicate implements ThreadPredicate, ThreadGroupPredicate {

        private final String name;

        
        public NamePredicate(final String name) {
            super();
            Validate.isTrue(name != null, "The name must not be null");
            this.name = name;
        }

        @Override
        public boolean test(final ThreadGroup threadGroup) {
            return threadGroup != null && threadGroup.getName().equals(name);
        }

        @Override
        public boolean test(final Thread thread) {
            return thread != null && thread.getName().equals(name);
        }
    }

    
    public static class ThreadIdPredicate implements ThreadPredicate {

        private final long threadId;

        
        public ThreadIdPredicate(final long threadId) {
            super();
            if (threadId <= 0) {
                throw new IllegalArgumentException("The thread id must be greater than zero");
            }
            this.threadId = threadId;
        }

        @Override
        public boolean test(final Thread thread) {
            return thread != null && thread.getId() == threadId;
        }
    }

    
    public static Collection<Thread> findThreads(final ThreadPredicate predicate){
        return findThreads(getSystemThreadGroup(), true, predicate);
    }

    
    public static Collection<ThreadGroup> findThreadGroups(final ThreadGroupPredicate predicate){
        return findThreadGroups(getSystemThreadGroup(), true, predicate);
    }

    
    public static Collection<Thread> findThreads(final ThreadGroup group, final boolean recurse, final ThreadPredicate predicate) {
        Validate.isTrue(group != null, "The group must not be null");
        Validate.isTrue(predicate != null, "The predicate must not be null");

        int count = group.activeCount();
        Thread[] threads;
        do {
            threads = new Thread[count + (count / 2) + 1]; //slightly grow the array size
            count = group.enumerate(threads, recurse);
            //return value of enumerate() must be strictly less than the array size according to javadoc
        } while (count >= threads.length);

        final List<Thread> result = new ArrayList<>(count);
        for (int i = 0; i < count; ++i) {
            if (predicate.test(threads[i])) {
                result.add(threads[i]);
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    
    public static Collection<ThreadGroup> findThreadGroups(final ThreadGroup group, final boolean recurse, final ThreadGroupPredicate predicate){
        Validate.isTrue(group != null, "The group must not be null");
        Validate.isTrue(predicate != null, "The predicate must not be null");

        int count = group.activeGroupCount();
        ThreadGroup[] threadGroups;
        do {
            threadGroups = new ThreadGroup[count + (count / 2) + 1]; //slightly grow the array size
            count = group.enumerate(threadGroups, recurse);
            //return value of enumerate() must be strictly less than the array size according to javadoc
        } while(count >= threadGroups.length);

        final List<ThreadGroup> result = new ArrayList<>(count);
        for(int i = 0; i < count; ++i) {
            if(predicate.test(threadGroups[i])) {
                result.add(threadGroups[i]);
            }
        }
        return Collections.unmodifiableCollection(result);
    }
}
