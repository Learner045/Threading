package org.synchronization;

import java.util.ArrayList;
import java.util.List;

public class SynchronizationInAction {

    // global variables or class-level variables, allocated on heap
    private static int globalCounter = 0;
    private static final Object obj = new Object(); // obj has monitor which is a key to critical section.

    public static void raceConditionInAction() {
        List<Thread> threads = new ArrayList<>();
        ThreadGroup threadGroup = new ThreadGroup("Group1");

        for (int i=1; i<=100; i++) {
            Thread t = new Thread(threadGroup, new MyThread());
            t.start();
            threads.add(t);
        }

        threadGroup.interrupt(); // When we call this, all threads blocked in sleeping state will be unblocked and throw InterruptedException.

        threads.forEach((t)-> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Total = "+globalCounter);
    }

    static class MyThread implements Runnable {
        @Override
        public void run() {
            /* In this case, there's some overhead in starting the thread and very little overhead
             in increment operation and hence, most threads are able to complete their run before
             next threads start so we don't see race condition in action here. Idea is multiple threads
             are executing this operation in same time.
             */
            // globalCounter++;

            /* If we unwrap it, it has 3 states.
            int localCounter = globalCounter;
            localCounter = localCounter + 1;
            globalCounter = localCounter;
            */

            // So if we have 10 threads, it is possible that 2 threads read the same value of global counter
            // They will increment the value to same value and then update the globalcounter.

            // To see the globalCounter race in action, we need to make sure that all threads reach the counter in same time.

            try{
                Thread.sleep(99999);
            } catch (InterruptedException e) {

            }

            /* Incorrect use of synchronization, each thread would use a new object ( monitor) to enter cs and this would kill the purpose of using synchronized.
             *  synchronized (new Object()) {
             *                 globalCounter++; // putting our critical section in synchronized block
             *             }
             */
            synchronized (obj) {
                globalCounter++; // putting our critical section in synchronized block
            }

        }
    }

}
