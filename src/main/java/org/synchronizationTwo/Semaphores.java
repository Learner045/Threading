package org.synchronizationTwo;

import java.util.concurrent.Semaphore;

public class Semaphores {

    static Semaphore semaphore = new Semaphore(2);
    public static void semaphores() throws InterruptedException {

        Executor executor = new Executor();
        executor.submit(new Job(4000));
        executor.submit(new Job(5000));
        executor.submit(new Job(3000));

        /**
         * Summary:
         * At a time, as we have 2 permits, 2 jobs will be able to acquire locks and execute CS
         * Job 3 will have to wait till one lock is released i.e. Job 1 is finished.
         */
    }

    static class Executor {
        public static void submit(Job job) throws InterruptedException {

            System.out.println("Launching job with id: "+ job.getWork());
            semaphore.acquire();
            Thread t = new Thread(()->{

                System.out.println("Executing job with id: "+ job.getWork());
                try {
                    Thread.sleep(job.getWork()); // simulating job

                    System.out.println("Job finished with id: "+ job.getWork());

                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
            t.start();


        }
    }

    static class Job {
        final int work;
        public Job(int work) {
            this.work = work;
        }

        public int getWork() {
            return work;
        }
    }
}
