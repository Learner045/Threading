package org.reusability;

import java.util.concurrent.*;

public class ThreadReusability {

    public static void reusability() throws ExecutionException, InterruptedException {

        /*
         * It takes workQueue which is collection of runnables. Instance of workQueue is a BlockingQueue<Runnable>.
         * Which blocks when queue is full, and we try to add elements in queue
         * Also, blocks the thread if queue is empty, and it is trying to fetch from the queue.
         * Blocking Queue has multiple implementations.
         * Constructor also takes corePoolSize which is number of worker threads.
         * If there is a spike in tasks added to the queue and all threads are busy, then the executor will try to add more threads
         * based on maxPoolSize that we provide. If some threads become idle, then those will be destroyed. KeepAliveTime will be time
         * till which those threads will be kept alive after which they will be destroyed.
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                3,
                5,
                1,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(3)
                );

        // There can be 2 types of execution, one where no values are returned in future and one where values are returned.
        threadPoolExecutor.execute(()->System.out.println("Task 1")); // This will provide worker threads a task to  execute.
        threadPoolExecutor.execute(()->System.out.println("Task 2"));

        System.out.println(threadPoolExecutor.getPoolSize());

        // Even after executing the above 2 tasks, the program does not terminate. This is because Java application waits for user level threads
        // to destroy by either completing their execution or interrupted.
        // So we need to destroy all threads in threadpool.
        // We use these methods to destroy threads in the thread pool.
        threadPoolExecutor.shutdown();
        threadPoolExecutor.awaitTermination(    3, TimeUnit.MINUTES);



        /*
        // if you want to return value from task, then use threadPoolExecutor.submit(<callable>)
        Future<Integer> futureResult =  threadPoolExecutor.submit(new CallableTask());

        // can do other stuff

        Integer result = futureResult.get(); // It will check if a result is available and if not, then it will wait until the result is made available.
         */




    }

    static class CallableTask implements Callable<Integer> {

        @Override
        public Integer call() {
            return 4;
        }
    }
}
