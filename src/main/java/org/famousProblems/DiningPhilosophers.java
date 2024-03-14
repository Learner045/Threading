package org.famousProblems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

    private static List<Lock> forks = new ArrayList<>();
    public static void diningPhilosophers() {

        for (int i=0; i<5; i++) {
            forks.add(new ReentrantLock());
        }

        Semaphore semaphore = new Semaphore(4);

        for (int i=0; i<5; i++) {
            new Thread(new Philosopher(i, semaphore)).start();
        }

        // Deadlock solution - only limited philosophers can grab the fork - Semaphonre


    }

    static class Philosopher implements Runnable {

        private final int id;
        private final Semaphore semaphore;
        public Philosopher(int id, Semaphore semaphore){
            this.id = id;
            this.semaphore = semaphore;
        }
        @Override
        public void run() {
            while(true) {
                think();
                pick_forks();
                eat();
                put_forks();
            }
        }

        void pick_forks() {
            try {
                semaphore.acquire(); // Only 4 philosophers will be able to grab a fork, one will have to wait till of those give up the forks
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // First pick right fork and then left.
            forks.get(id).lock();
            System.out.println("Philosopher " + id + " picked the right fork");
            forks.get((id+1)%5).lock(); // since the table is circular
            System.out.println("Philosopher " + id + " picked the left fork");
        }

        void put_forks() {
            forks.get(id).unlock();
            forks.get((id+1)%5).unlock(); // since the table is circular

            semaphore.release();
        }

        void think() {
            System.out.println("Philosopher " + id + " thinks");
        }

        void eat() {
            System.out.println("Philosopher " + id + " eats");
        }
    }
}

/**
 * Deadlock
 * Philosopher 3thinks
 * Philosopher 1thinks
 * Philosopher 4thinks
 * Philosopher 0thinks
 * Philosopher 2thinks
 * Philosopher 3 picked the right fork
 * Philosopher 4 picked the right fork
 * Philosopher 1 picked the right fork
 * Philosopher 2 picked the right fork
 * Philosopher 0 picked the right fork
 */