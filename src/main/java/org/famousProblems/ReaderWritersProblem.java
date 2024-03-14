package org.famousProblems;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReaderWritersProblem {

    private static Semaphore writerLock = new Semaphore(1);  // We use semaphore instead of a lock because we want to allow only 1 thread
    private static int readers = 0;
    private static int counter = 0;

    private static Lock readerLock = new ReentrantLock(); // Protects readers var
    public static void readerWriterProblem() {

        for (int i=0; i< 4; i++) {
            new Thread((new ReaderThread())).start();
        }
        for (int i=0; i< 2; i++) {
            new Thread((new WriterThread())).start();
        }


    }

    static class ReaderThread implements Runnable {

        @Override
        public void run() {
            while(true) {
                // We cannot increment a var without ensuring exclusive access to it, so reader var should also be protected.
                readerLock.lock();
                readers++; // To keep track of how many reader threads are here.
                // This is required because when the first reader thread enters the CS, no writer thread should be in CS.
                if (readers == 1) {
                    try {
                        writerLock.acquire(); // Once a reader thread is in CS, no writer lock should enter CS so reader should acquire the writer lock.
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                readerLock.unlock();

                System.out.println("The value is " + counter + " thread ->" + Thread.currentThread().getName());
                readerLock.lock();
                readers--;
                if (readers == 0){
                    writerLock.release(); // last reader thread should release the writers lock
                }
                readerLock.unlock();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    static class WriterThread implements Runnable {

        @Override
        public void run() {
            while(true) { // This loop is added to make operation long running so that we can observe the behavior
                try {
                    writerLock.acquire();
                    counter++;
                    System.out.println("Writer thread updated the counter. Id -> " + Thread.currentThread().getName());
                    writerLock.release();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }
}
