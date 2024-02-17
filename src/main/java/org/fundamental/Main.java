package org.fundamental;

import org.parallelAlgorithms.QuickSort;
import org.reusability.ForkJoin;
import org.reusability.ThreadReusability;
import org.synchronizationTwo.MapReduce;
import org.synchronizationTwo.ParallelArrayProcessingWithPhasersSolution;
import org.synchronizationTwo.ParallelArrayProcessingWithPhasersTry1;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // ThreadCreation.createThreads();

        // ThreadPriorityAndStates.threadPriority();

        // ThreadPriorityAndStates.threadStates();

        // DaemonAndUserThreads.daemonThreads();

        // Exception handling in thread
        // handleException();

        // RaceConditionInAction.raceConditionInAction();

        // ParallelTextFileProcessingTry.parallelprocess();

        // SynchronizationInAction.raceConditionInAction();

        // ProducerConsumerV1.producerConsumerProblem();

        // LocksWithParallelVectorSum.parallelVectorSum();

        // ReadWriteLocks.readWriteLock();

        // Semaphores.semaphores();

        // ParallelArraySearchWithCountdownLatch.parallelArraySearch();

        // MatrixWithCyclicBarrier.cyclicBarrier();

        // ParallelArrayProcessingWithPhasersTry1.parallelProcessing();

        // ParallelArrayProcessingWithPhasersSolution.parallelProcessing();

        // MapReduce.mapreduce();

        // ThreadReusability.reusability();

        // ForkJoin.forkJoin();

        QuickSort.applyQuickSort();

    }

    static void handleException() {

        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e)->{
            // custom logic to handle exception
        });
       // We can also do thread1.setDefaultUncaughtExceptionHandler() and provide a lambda.
    }


}