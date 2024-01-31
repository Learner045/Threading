package org.synchronization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLocks {

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static final Lock readLock = readWriteLock.readLock();
    static final Lock writeLock = readWriteLock.writeLock();

    static final List<Integer> list = new ArrayList<>();


   public static void readWriteLock() {
        Thread writer = new Thread(new WriterThread());

        Thread reader1 = new Thread(new ReaderThread());
        Thread reader2 = new Thread(new ReaderThread());
        Thread reader3 = new Thread(new ReaderThread());
        Thread reader4 = new Thread(new ReaderThread());

        writer.start();
        reader1.start();
        reader2.start();
        reader3.start();
        reader4.start();


    }

    static class WriterThread implements Runnable {

        @Override
        public void run() {
            try {
                while(true) {
                    writeData();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void writeData() throws InterruptedException {
            Thread.sleep(10000);

            writeLock.lock();
            int value = (int)( Math.random() * 10);

            System.out.println("Producing data: "+value);
            Thread.sleep(3000);
            list.add(value);
            writeLock.unlock();

        }
    }

    static class ReaderThread implements Runnable {

        @Override
        public void run() {
            try {
                while(true) {
                    readData();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void readData() throws InterruptedException {
            Thread.sleep(3000);

            // readLock.lock(); // puts current thread in waiting state if lock not avail


            // this is spinlock. It runs this while continuously trying to acquire the lock
            // spinlock does not block the thread, keeps the thread in runnable state until lock is able to be acquired.
             while(true) {
                 boolean lockValue = readLock.tryLock(); // checks if we can acquire, if yes then rets true
                 if (lockValue){
                     break;
                 }else{
                     System.out.println("Waiting for read lock");
                 }
             }

            System.out.println("Consuming data: "+list);
            readLock.unlock();

        }
    }
}
