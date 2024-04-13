package ru.otus.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.threads.pool.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolDemo {
    private static final int NUMBER_OF_TASK_PROVIDERS = 2;
    private static final int NUMBER_OF_TASKS = 100;
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolDemo.class);

    public static void main(String[] args) {
        var threadPool = new ThreadPool(4);
        var threadFactory = Executors.defaultThreadFactory();
        var addedTaskCounter = new AtomicInteger();
        var completedTaskCounter = new AtomicInteger();
        var skippedTaskCounter = new AtomicInteger();

        var latch = new CountDownLatch(NUMBER_OF_TASK_PROVIDERS);

        var taskQueue = new ArrayBlockingQueue<Runnable>(NUMBER_OF_TASKS);
        for (var taskNumber = 1; taskNumber <= NUMBER_OF_TASKS; taskNumber++) {
            taskQueue.add(completedTaskCounter::incrementAndGet);
        }

        for (var providerNumber = 1; providerNumber <= NUMBER_OF_TASK_PROVIDERS; providerNumber++) {
            var taskProvider = threadFactory.newThread(() -> {
                        while (!taskQueue.isEmpty()) {
                            var task = taskQueue.poll();
                            var isAdded = threadPool.execute(task);
                            if (!isAdded) {
                                skippedTaskCounter.incrementAndGet();
                            } else {
                                addedTaskCounter.incrementAndGet();
                            }
                        }
                        latch.countDown();
                    }
            );
            taskProvider.setName("ProviderThread-%d".formatted(providerNumber));
            taskProvider.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            LOGGER.debug(e.getMessage(), e);
            e.printStackTrace();
        }

        threadPool.shutdown();

        LOGGER.info("added tasks %d".formatted(addedTaskCounter.get()));
        LOGGER.info("completed tasks %d".formatted(completedTaskCounter.get()));
        LOGGER.info("skipped tasks %d".formatted(skippedTaskCounter.get()));
    }
}
