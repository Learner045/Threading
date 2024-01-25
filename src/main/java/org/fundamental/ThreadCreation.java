package org.fundamental;

public class ThreadCreation {

    public static void createThreads() throws InterruptedException{
        /*
         * 2 ways of creating threads in java
         * 1 - extends the Thread class
         * 2 - Use Runnable interface
         */

        MyThread myThread = new MyThread();
        myThread.start(); // IMP : to invoke start and not the run method. If we call run(), it will run in main thread
        myThread.join(); // ensures this thread completes before next set of operations are executed

        System.out.println("Current thread: " + Thread.currentThread().getName());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Current thread: " + Thread.currentThread().getName());
            }
        };
        Thread thread = new Thread(runnable); // runnable can also be a lambda
        thread.setName("MyRunnableThread");
        thread.start();

        System.out.println("Current thread: " + Thread.currentThread().getName());

        // IMP : order of prints is not guaranteed
    }

    static class MyThread extends Thread {

        @Override
        public void run() {
            setName("MyThread");
            System.out.println("Current thread: " + Thread.currentThread().getName());
        }
    }
}
