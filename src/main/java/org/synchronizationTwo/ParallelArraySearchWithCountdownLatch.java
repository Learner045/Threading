package org.synchronizationTwo;

import java.util.concurrent.CountDownLatch;

/**
 * Generally useful for one shot problems that can be split ito multiple problems and only ran once.
 */
public class ParallelArraySearchWithCountdownLatch {

    static int arr[] = new int[]{1, 2, 300, 4, 50, 6, 7, 8, 9, 10};
    static int foundPosition = -1;
    static int numOfThreads = 2;

    private static CountDownLatch countDownLatch = new CountDownLatch(numOfThreads);

    public static void parallelArraySearch() throws InterruptedException {

        int searchNum = 2;

        int slice = arr.length / numOfThreads;

        for(int i=0; i<numOfThreads; i++){
            Thread t = new Thread(new WorkerThread((i*slice),((i+1)*slice), searchNum));
            t.start();
        }

        countDownLatch.await(); // Main thread will wait for all threads to be ready, if we don't add this then the thread will resume execution even if threads are still doing their job

        System.out.println("Number found at index:" + foundPosition);
    }

    static class WorkerThread implements Runnable {
        final int left;
        final int right;

        final int searchNum;

        public WorkerThread(int left, int right, int searchNum) {
            this.left = left;
            this.right = right;
            this.searchNum = searchNum;
        }
        @Override
        public void run() {
            for(int i=left; i<right; i++) {
                if(arr[i] == searchNum) {
                    foundPosition = i;
                    break;
                }
            }

            countDownLatch.countDown(); // This indicates that this particular thread finished its job of searching
        }
    }
}


