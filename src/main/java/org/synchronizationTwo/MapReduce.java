package org.synchronizationTwo;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class MapReduce {

    /**
     * -> Intermediate result :
     * [ friend: 1,
     *   need: 1
     * ]
     * -> Reducers Input: It will contain a list with other lists, one list will contain record of same keys
     * [
     *  [ friend: 1,
     *    friend: 1
     *  ],
     *  [ a:1,
     *    a:1
     *  ],
     *  [
     *    need:1
     *  ]
     * ]
     * -> Reducer result:
     * [
     *   friend: 2,
     *   a: 2,
     *   need: 1
     * ]
     */
    private static final String input  = "a friend in need is a friend indeed.";

    /** This list will be updated by multiple mapper threads to generate intermediate result, hence we create a synchronized list. All accesses to this list will be thread safe. */
    private static final List<Map.Entry<String, Integer>> intermediateResult = Collections.synchronizedList(new ArrayList<>());

    private static final List<List<Map.Entry<String, Integer>>> reduceInput = Collections.synchronizedList(new ArrayList<>());

    private static final CountDownLatch countDownLatch = new CountDownLatch(2); /** we have 2 mapper threads which need to finish before the partitioner thread performs shuffle on intermediate result */

    private static final List<Map.Entry<String, Integer>> finalResult = Collections.synchronizedList(new ArrayList<>());

    public static void mapreduce() throws InterruptedException {

        List<String> inputList = List.of(input.split(" "));

        Thread t1 = new Thread(new Mapper(inputList.subList(0, inputList.size()/2)));
        t1.start();
        Thread t2 = new Thread(new Mapper(inputList.subList(inputList.size()/2, inputList.size())));
        t2.start();

        // if we don't use count down latch, we will need to rely on join() here for t1 and t2

        Thread t3 = new Thread(new Partitioner()); // IMP: This will be a single thread, we can't split this task into multiple threads
        t3.start();

        t3.join();

        // Each reducer thread performs summation for their set of lists.
        final List<Thread> list = new ArrayList<>();
        for(int i=0; i<reduceInput.size(); i++){
            Thread t = new Thread(new Reducer(reduceInput.get(i)));
            t.start();
            list.add(t);
        }

        for(Thread t: list){
            t.join();
        }

        System.out.println("Test");


    }

    static class Mapper implements Runnable {

        private final List<String> input;
        Mapper(List<String> input) {
            this.input = input;
        }
        @Override
        public void run() {
            for (String word: input) {
                intermediateResult.add(Map.entry(word, 1)); // adds an entry of <word, 1>
            }

            countDownLatch.countDown();
        }
    }

    static class Partitioner implements Runnable {

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final Map<String, List<Map.Entry<String, Integer>>> map = new HashMap<>(); // word : List<<word,1> <word, 1>>

            for(Map.Entry<String, Integer> entry: intermediateResult) {
                if(map.containsKey(entry.getKey())) {
                    List<Map.Entry<String, Integer>> list = map.get(entry.getKey());
                    list.add(entry);
                } else {
                    List<Map.Entry<String, Integer>> list = new ArrayList<>();
                    list.add(entry);
                    map.put(entry.getKey(), list);
                }
            }

            reduceInput.addAll(map.values()); // Add all the lists .

            // ** course uses stream api, to first collect all unique words in list of entries. Second, for each unique word, it creates a list of entries specific to that word using streams and adds it to main list.

        }
    }

    static class Reducer implements Runnable {

        final List<Map.Entry<String, Integer>> reducerInput;

        Reducer(final List<Map.Entry<String, Integer>> reducerInput) {
            this.reducerInput = reducerInput;
        }

        @Override
        public void run() {

            int sum = 0;
            String word = reducerInput.get(0).getKey();
            for(Map.Entry<String, Integer> entry: reducerInput) {
                sum+=entry.getValue();
            }
            finalResult.add(Map.entry(word, sum));

        }
    }
}
