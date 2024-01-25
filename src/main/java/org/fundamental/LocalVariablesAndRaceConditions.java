package org.fundamental;

public class LocalVariablesAndRaceConditions {

    // global variables or class-level variables, allocated on heap
    private static int globalCounter = 0;

    /* ThreadLocal is a wrapper to a value and will be private to each thread even though it is a class field.
     * We can also provide an initial value ThreadLocal.withInitial(()-> "initValue");
     */
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    static void localVarsAndRaceConditions() {
        Thread t1 = new Thread(new MyThread());
        Thread t2 = new Thread(new MyThread());

        t1.start();
        t2.start();
    }

    static class MyThread implements Runnable {
        @Override
        public void run() {
            // private variables for a thread which would reside in thread's stack
            int counter = 0;
            threadLocal.set("myValue"); // private to each thread
        }
    }
}
