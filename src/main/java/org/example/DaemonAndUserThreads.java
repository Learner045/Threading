package org.example;

public class DaemonAndUserThreads {

    public static void daemonThreads() {
        Thread t1 = new Thread(new MyThread(10), "T1");
        Thread t2 = new Thread(new MyThread(3), "T2");
        t1.setDaemon(true);

        t1.start();
        t2.start();

        // T1 starts execution, T2 starts execution and sleep for 3s but after it completes execution,
        // the program terminates instead of completing T1
        /*
         *  Sleeping for 1s T1
            Sleeping for 1s T2
            Sleeping for 1s T2
            Sleeping for 1s T1
            Sleeping for 1s T1
            Sleeping for 1s T2
         */

        // t1.join(); Works the same as user-level thread and will wait for thread completion

    }

    static class MyThread implements Runnable {
        private final int numOfSecs;

        public MyThread(int numOfSecs) {
            this.numOfSecs = numOfSecs;
        }

        @Override
        public void run() {
            for (int i=0; i< numOfSecs; i++) {
                try {
                    System.out.println("Sleeping for 1s " + Thread.currentThread().getName());
                    Thread.sleep(1000);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
