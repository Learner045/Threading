package org.synchronizationTwo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Each thread processes one column, so lets say threads are starting from updating 2nd row .
 * Each thread will sum its previous row and then based on their column =Id assigned, they will update their column
 * Once everyone has updated one row, they can then move to another row.
 * But call await() to ensure that all of them updates their columns in a row before moving to next row
 */
public class MatrixWithCyclicBarrier {

    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(4, ()->System.out.println("Barrier was released"));
    private static int columns = 4;
    private static int rows=4;
    static int[][] arr = new int[][]{
            {1, 2, 2, 1},
            {2, 3, 3, 2},
            {3, 2, 1, 2},
            {4, 5, 5, 4}
    };

    public static void cyclicBarrier() {
        List<Thread> threadList = new ArrayList<>();

        for(int i=0; i<columns; i++){
            Thread t = new Thread(new WorkerThread(i)); // Each thread processes a column
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

        System.out.println("Final array \n" + Arrays.deepToString(arr));
    }

    static class WorkerThread implements Runnable {
        final int columnId;
        public WorkerThread(int columnId){
            this.columnId = columnId;
        }
        @Override
        public void run() {
            int sum=0;
            for(int i=1; i<rows; i++) {

                for(int j=0; j<columns; j++){ // calculating sum for previous row
                    sum +=  arr[i-1][j];
                }

                arr[i][columnId] += sum;
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
