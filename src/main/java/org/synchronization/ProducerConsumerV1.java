package org.synchronization;

import java.util.LinkedList;
import java.util.Queue;

/**
 * More traditional approach. w
 */
public class ProducerConsumerV1 {

    public static void producerConsumerProblem() {
        Queue<String> queue = new LinkedList<>(); // to exchange data between threads
        Thread producer = new Thread(new Producer(queue));
        Thread consumer = new Thread(new Consumer(queue));

        producer.start();
        consumer.start();

    }


    static class Producer implements Runnable {
        private Queue<String> queue;

        public Producer(Queue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    produceData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void produceData() throws InterruptedException {
            // We have synchronized on the common object that we have for both producer and consumer.
            synchronized (queue) {
                if (queue.size() == 10) {
                    System.out.println("In producer, waiting...");
                    queue.wait();
                }
                Thread.sleep(1000); // added to slow down the producer-consumer threads

                System.out.println("Producing data with id" + queue.size());
                queue.add("element_"+queue.size());
                // notify() This is a no-op if there is no waiting thread.
                if (queue.size() == 1) {
                    queue.notify(); // unlocking the consumers
                }

            }
        }
    }

    static class Consumer implements Runnable {
        private Queue<String> queue;

        public Consumer(Queue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    while(true) {
                        consumeData();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void consumeData() throws InterruptedException {
            synchronized (queue) {
                if(queue.isEmpty()) {
                    System.out.println("Queue is empty, waiting for data in queue to consume");
                    queue.wait();
                }
                Thread.sleep(1000); // added to slow down the producer-consumer threads
                String data = queue.remove();
                System.out.println("Consuming "+data);

                // When the queue is full, producer will be waiting for consumer to consume data so
                // after consuming data ( queue.remove() ), we unlock the producer threads
                if(queue.size() == 9) {
                    queue.notify();
                }
            }
        }
    }
}

// output
/*
Consuming element_5
Consuming element_6
Consuming element_7
Consuming element_8
Consuming element_9
Queue is empty, waiting for data in queue to consume
Producing data with id0
Producing data with id1
Consuming element_0
Consuming element_1
Queue is empty, waiting for data in queue to consume
Producing data with id0
Producing data with id1
Producing data with id2
Producing data with id3
Producing data with id4

 */
