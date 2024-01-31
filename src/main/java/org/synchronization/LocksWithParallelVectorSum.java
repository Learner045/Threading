package org.synchronization;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LocksWithParallelVectorSum {

    public static int sum = 0;
    public static int[] arr = new int[10];

    public static Lock lockObj = new ReentrantLock();
    public static void parallelVectorSum() throws InterruptedException {
        for(int i=0; i<10; i++){
            arr[i] = 10;
        }

        final List<Thread> threadList = new ArrayList<>();
        int numThreads = 2;
        int splitSize = 10/numThreads; //  ( size of array / num of threads ) will give range such as (0,4) (5,9) ..

        for(int i=0; i< numThreads; i++){
            Thread t = new Thread(new WorkerThread((i*splitSize),((i+1)*splitSize)));
            t.start();
            threadList.add(t);
        }

        for(int i=0; i<numThreads; i++) {
            threadList.get(i).join();
        }

        System.out.println("Elements sum="+sum);

    }

    static class WorkerThread implements Runnable {

        private final int left;
        private final int right;

        public WorkerThread(int left, int right) {
            this.left = left;
            this.right = right;
        }
        @Override
        public void run() {
            for(int i=left; i<right; i++) {
                lockObj.lock();
                sum += arr[i];
                lockObj.unlock();
            }
        }
    }
}
