package com.stirlinglms.stirling.concurrent;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ThreadFactory;

public class PoolSpec implements ThreadFactory, ForkJoinPool.ForkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler {

    public static final PoolSpec EMAIL = new PoolSpec("Stirling - Email", true, 5);
    public static final PoolSpec INTEGRATION = new PoolSpec("Stirling - Integration", true, 8);
    public static final PoolSpec PAYMENT = new PoolSpec("Stirling - Payment", true, 5);

    private final String name;
    private final boolean stealing;
    private final int maxThreads;

    /**
     * Creates a representation of a ThreadPool.
     *
     * @param name Name of the pool.
     * @param stealing Whether the pool should attempt to complete queued tasks in other threads if it is idle.
     * @param maxThreads The maximum usage percentage of each pool.
     */
    public PoolSpec(String name, boolean stealing, int maxThreads) throws IllegalArgumentException {
        this.name = name;
        this.stealing = stealing;
        this.maxThreads = maxThreads;
    }

    /**
     * @return Name of the thread pool.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return {@code true} If the pool should attempt to 'steal' tasks from other pools if it is idle.
     */
    public boolean isStealing() {
        return this.stealing;
    }

    /**
     * @return The maximum amount of threads that the pool should use.
     */
    public int getMaxThreads() {
        return this.maxThreads;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, this.name);

        thread.setUncaughtExceptionHandler(this);

        return thread;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
    }

    @Override
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);

        thread.setName(this.name + "[" + thread.getPoolIndex() + "]");
        thread.setUncaughtExceptionHandler(this);

        return thread;
    }
}
