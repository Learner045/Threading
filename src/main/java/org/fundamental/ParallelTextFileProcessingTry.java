package org.fundamental;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Requirements --
 *  1. Periodically scans the ./src/main/resources directory and watches for new files
 *  2. For each file found in this directory, a new thread should take care of its processing.
 *  3. Processing = each line of the file will be hashed using a hashing algorithm
 *  4. The program will create new files, marked _output , and will place them into ./src/main/resources/output
 *  5. Throw an exception if a line is empty
 */
public class ParallelTextFileProcessingTry {

    private final static String resourcesPath = "src/main/resources/output"; // global var only used for lookup

    static void parallelprocess() throws InterruptedException {

        // only accessed by scanner thread
        List<Thread> fileProcessignThreads = new ArrayList<>();
        Map<String, Boolean> alreadyProcessed = new HashMap<>();
        File dir = new File("src/main/resources");

        Thread scannerThread = new Thread( () -> {
            while(true) {
                // scan the directory and do the processing

                try {

                    for(File file : dir.listFiles()) {
                        if (file.isFile()) {
                            // to avoid processing if files are already processed once
                            if (alreadyProcessed.containsKey(file.getName())) {
                                continue;
                            }
                            System.out.println("Creating processor thread to process "+file.getPath());
                            Thread processorThread = new Thread(new ProcessingThread(file.getPath()));
                            fileProcessignThreads.add(processorThread);
                            alreadyProcessed.put(file.getName(), true);
                            processorThread.start();
                        }
                    }
                    Thread.sleep(5000);

                } catch (InterruptedException e) {

                } finally {
                    for (Thread pro : fileProcessignThreads) {
                        try {
                            if (pro.isAlive()) {
                                pro.join();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        scannerThread.start();

    }


    static class ProcessingThread implements Runnable {

        final String filePath;

        ProcessingThread(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void run() {
            BufferedWriter writer = null;
            try {

                String filename = String.valueOf(Path.of(filePath).getFileName());
                Path outputFilePath = Path.of(resourcesPath+"/"+filename+"_output");

                writer = new BufferedWriter(new FileWriter(outputFilePath.toFile()));
                List<String> lines =  Files.readAllLines(Path.of(filePath));
                for (String line: lines) {
                    if (line.isEmpty()) {
                        throw new IllegalArgumentException("Line is empty for file : "+ filename);
                    }
                    writer.write(line.hashCode()+"\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}

/*
 * Output
 * Creating processor thread to process src/main/resources/sample1.txt
 * Creating processor thread to process src/main/resources/sample2.txt
 * File created true
 * File created true
 * Creating processor thread to process src/main/resources/saple3.txt
 * File created true
 */
