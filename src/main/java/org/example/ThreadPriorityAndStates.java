package org.example;

public class ThreadPriorityAndStates {

    public static void threadPriority() throws  InterruptedException {

        // IMP : thread priority is not always honored and program correctness should not be based on it.

        Thread thread1 = new Thread(()->{
            Thread currentThread = Thread.currentThread();
            System.out.println(currentThread.getName()+" priority:" + currentThread.getPriority());
        });

        thread1.setPriority(Thread.MAX_PRIORITY);
        thread1.setName("Thread1");

        Thread thread2 = new Thread(()->{
            Thread currentThread = Thread.currentThread();
            System.out.println(currentThread.getName()+" priority:" + currentThread.getPriority());
        });

        thread2.setPriority(Thread.MIN_PRIORITY);
        thread2.setName("Thread2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }

    public static void threadStates() throws  InterruptedException {
        Thread thread1 = new Thread(()->{
            Thread currentThread = Thread.currentThread();
            // IMP : here it will be in runnable state as it is simply executing the body.
            System.out.println("State: " + currentThread.getState());
        });

        System.out.println("State: " + thread1.getState());

        thread1.start();

        System.out.println("State: " + thread1.getState());

        thread1.join();

        System.out.println("State: " + thread1.getState());
    }
}
