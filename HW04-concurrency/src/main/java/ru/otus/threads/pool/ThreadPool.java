package ru.otus.threads.pool;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    private final Queue<Runnable> taskQueue;
    private final ThreadFactory treadFactory;
    private final Lock lock;
    private final AtomicBoolean isShutDown;
    private final AtomicInteger addedTaskCounter;
    private final AtomicInteger runningTaskCounter;

    public ThreadPool(int capacity) {
        checkCapacity(capacity);
        taskQueue = new LinkedList<>();
        treadFactory = Executors.defaultThreadFactory();
        lock = new ReentrantLock();
        isShutDown = new AtomicBoolean(false);
        addedTaskCounter = new AtomicInteger();
        runningTaskCounter = new AtomicInteger();
        runExecutors(capacity);
    }

    public boolean execute(Runnable task) {
        requireNonNull(task, "task must not be null");
        if (isShutDown.get()) {
            throw new IllegalStateException("cannot add a task after the pool is shut down");
        }
        lock.lock();
        try {
            if (taskQueue.add(task)) {
                LOGGER.info("%s add task %d".formatted(getThreadName(), addedTaskCounter.incrementAndGet()));
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        LOGGER.info("ThreadPool is shut down ");
        isShutDown.set(true);
    }

    private void runExecutors(int capacity) {
        LOGGER.info("ThreadPool is running ");
        for (var threadNumber = 1; threadNumber <= capacity; threadNumber++) {
            var executor = initExecutor();
            executor.setName("PooledThread-%d".formatted(threadNumber));
            executor.start();
        }
    }

    private Thread initExecutor() {
        return treadFactory.newThread(() -> {
            while (!isShutDown.get() || !taskQueue.isEmpty()) {
                if (!lock.tryLock()) {
                    continue;
                }
                try {
                    var task = taskQueue.poll();
                    if (nonNull(task)) {
                        task.run();
                        LOGGER.info("%s run task %d".formatted(getThreadName(), runningTaskCounter.incrementAndGet()));
                    }
                } finally {
                    lock.unlock();
                }
            }
        });
    }

    private String getThreadName() {
        return Thread.currentThread().getName();
    }

    private void checkCapacity(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity value %d is less then 1".formatted(capacity));
        }
    }
}
