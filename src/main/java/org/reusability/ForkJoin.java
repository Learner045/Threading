package org.reusability;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveAction;

public class ForkJoin {

    /**
     *  Works only for problems which can be divided into smaller problems and can be achieved through divide and conquer
     */
    private static int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
    public static void forkJoin() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoin = new ForkJoinPool(2);

        Future<?> future = forkJoin.submit(new IncrementTask(0, 8));
        future.get();

        System.out.println("The array is: "+ Arrays.toString(arr));
    }


    static class IncrementTask extends RecursiveAction {

        private final int left;
        private final int right;

        public IncrementTask(int left, int right) {
            this.left = left;
            this.right = right;
        }
        @Override
        protected void compute() {
            if (right - left < 2) { // Works same way if this range is changed to < 3.
                for (int i=left; i< right; i++) {
                    arr[i] = arr[i] +1;
                }
            } else {
                int mid = (left + right)/2;
                invokeAll(new IncrementTask(left, mid), new IncrementTask(mid, right)); // This method will call fork() on the task which will push the task in work queue of the current thread.
                // After it calls fork, it also calls join() so the invokeAll() call also acts like a barrier. So when the tasks complete execution then only this call will return.

            }
        }
    }
}
