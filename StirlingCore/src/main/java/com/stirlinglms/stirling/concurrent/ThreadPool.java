package com.stirlinglms.stirling.concurrent;

import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ThreadPool implements Executor {

    private static final Map<PoolSpec, ThreadPool> POOLS = Maps.newConcurrentMap();

    private final ExecutorService service;

    private ThreadPool(ExecutorService service) {
        this.service = service;
    }

    public static ThreadPool createForSpec(PoolSpec spec) {
        return POOLS.computeIfAbsent(spec, func -> {
            int max = spec.getMaxThreads();

            return new ThreadPool(spec.isStealing() ? new ForkJoinPool(max, spec, null, true) :
              new ThreadPoolExecutor(1, max, 60L, TimeUnit.SECONDS, Queues.newLinkedBlockingQueue(), spec));
        });
    }

    public static void closePools() {
        POOLS.forEach((k, v) -> v.shutdown());
    }

    public void shutdown() {
        this.service.shutdown();
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.service.submit(task);
    }

    public Future<?> submit(Runnable task) {
        return this.service.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return this.service.submit(task, result);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.service.invokeAll(tasks);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.service.invokeAll(tasks, timeout, unit);
    }


    @Override
    public void execute(Runnable command) {
        Executors.newCachedThreadPool();
    }
}
