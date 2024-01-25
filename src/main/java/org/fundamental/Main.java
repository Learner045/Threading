package org.fundamental;

import org.synchronization.SynchronizationInAction;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // ThreadCreation.createThreads();

        // ThreadPriorityAndStates.threadPriority();

        // ThreadPriorityAndStates.threadStates();

        // DaemonAndUserThreads.daemonThreads();

        // Exception handling in thread
        // handleException();

        // RaceConditionInAction.raceConditionInAction();

        // ParallelTextFileProcessingTry.parallelprocess();

        SynchronizationInAction.raceConditionInAction();

    }

    static void handleException() {

        Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e)->{
            // custom logic to handle exception
        });
       // We can also do thread1.setDefaultUncaughtExceptionHandler() and provide a lambda.
    }


}