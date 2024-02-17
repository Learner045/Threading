package org.parallelAlgorithms;

import java.util.Arrays;
import java.util.concurrent.*;

public class QuickSort {

    private static int arr[] = new int[] {3, 4, 7, 1, 10, 6, 9, 2, 8, 4};
    private static int arrSize = 10;

    private static ForkJoinPool forkJoinPool = new ForkJoinPool();
    public static void applyQuickSort()throws ExecutionException, InterruptedException {

        long start = System.nanoTime();
        Future<?> future = forkJoinPool.submit(new SortTask(0, arrSize-1));
        future.get();

        long end = System.nanoTime();
        System.out.println(Arrays.toString(arr));
        System.out.println("Took "+(end-start)+"ns"); // Greater the size of the array, more the improvement with parallelism.
    }

    private static int partition(int low, int high) {
        int pivot = arr[high];
        int pIndex = low;
        for(int i=low; i<high; i++) {
            if (arr[i] <= pivot) {
                swap(i, pIndex);
                pIndex ++;
            }
        }

        swap(pIndex, high);
        return pIndex;
    }

    public static void swap(int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


    static class SortTask extends RecursiveAction{
        private final int low;
        private final int high;

        public SortTask(int low, int high) {
            this.low = low;
            this.high = high;
        }

        @Override
        protected void compute() {
           if (low < high) {
               int pIndex = partition(low, high);
               invokeAll(new SortTask(low, pIndex-1), new SortTask(pIndex+1, high));
           }

        }
    }


}
