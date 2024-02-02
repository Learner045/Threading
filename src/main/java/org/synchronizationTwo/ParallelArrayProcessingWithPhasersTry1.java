package org.synchronizationTwo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class ParallelArrayProcessingWithPhasersTry1 {

    private static int arr[] = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private static int sum=0;

    private static int numOfThread = 3;

    private static Phaser phaser = new Phaser();

    public static void parallelProcessing() {
        List<Thread> threadList = new ArrayList<>();
        int slice = arr.length / numOfThread;
        for (int i=0; i<numOfThread; i++){
            Thread t = new Thread(new WorkerThread((i*slice), ((i+1)*slice)));
            phaser.register();
            t.start();
            threadList.add(t);
        }

        for(Thread t : threadList){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sum is "+sum);

    }


    private static class WorkerThread implements Runnable {

        final int left;
        final int right;
        WorkerThread(int left, int right){
            this.left = left;
            this.right = right;
        }
        @Override
        public void run() {
            doubleEl();

            phaser.arriveAndAwaitAdvance();

            addEl();

        }

        private void doubleEl(){
            for(int i=left; i<right; i++){
                arr[i] *=  2;
            }
        }

        private void addEl() {
            int s = 0;
            for(int i=left; i<right; i++){
                s += arr[i];
            }
            sum+=s;
        }
    }
}
