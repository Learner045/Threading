package org.synchronizationTwo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

public class ParallelArrayProcessingWithPhasersSolution {

    private static int arr[] = new int[] {1, 2, 3, 4, 5, 6, 7, 8};

    private static int sum=0;

    private static Phaser phaser = new Phaser(1); // this is main thread

    public static void parallelProcessing() {

        for (int i=0; i<arr.length; i++){ // 8 threads
            Thread t = new Thread(new WorkerThread(i));
            t.start();
        }

        phaser.arriveAndAwaitAdvance(); // main thread is reaching this barrier, it starts all the threads and reaches here
        phaser.arriveAndAwaitAdvance(); // this waits for thread 0 to complete

        System.out.println("Sum is "+sum);

    }


    private static class WorkerThread implements Runnable {

        final int threadIndex;
        WorkerThread(int threadIndex){
            this.threadIndex = threadIndex;
            phaser.register(); // each thread registers with phaser
        }
        @Override
        public void run() {
           arr[threadIndex] *= 2;
           phaser.arriveAndAwaitAdvance();

           // only 1 thread computes the sum of doubled array
           if (threadIndex == 0){
               for(int i=0; i<arr.length; i++){
                   sum+= arr[i];
               }
               phaser.arriveAndAwaitAdvance(); // 2nd phase. We put this here inorder to synchronize with main thread so that we know that we have done our execution
           }else{
               phaser.arriveAndDeregister();
               // All other threads deregister with phaser as their job is done
           }
        }
    }
}
