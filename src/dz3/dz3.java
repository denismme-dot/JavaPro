package dz3;

import java.util.LinkedList;
import java.util.Queue;

public class dz3 {

    private final Queue<Runnable> taskQueue = new LinkedList<>();
    private final Thread[] workers;
    private volatile boolean isShutdown = false;
    private final Object lock = new Object();
    private int activeTasks = 0;

    public dz3(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        workers = new Thread[capacity];

        for (int i = 0; i < capacity; i++) {
            workers[i] = new Thread(new Worker(), "dz3-worker-" + i);
            workers[i].start();
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException("Task must not be null");
        }

        synchronized (lock) {
            if (isShutdown) {
                throw new IllegalStateException("Pool is shut down, cannot accept new tasks");
            }
            taskQueue.offer(task);
            lock.notify();
        }
    }

    public void shutdown() {
        synchronized (lock) {
            isShutdown = true;
            lock.notifyAll();
        }
    }

    public void awaitTermination() {
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (true) {
                Runnable task;

                synchronized (lock) {
                    while (taskQueue.isEmpty() && !isShutdown) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    if (taskQueue.isEmpty() && isShutdown) {
                        return;
                    }

                    task = taskQueue.poll();
                    activeTasks++;
                }

                try {
                    task.run();
                } catch (RuntimeException e) {
                    System.err.println("Task execution failed: " + e.getMessage());
                } finally {
                    synchronized (lock) {
                        activeTasks--;
                        lock.notifyAll();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        dz3 pool = new dz3(3);

        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.execute(() -> {
                System.out.println("Task " + taskId + " started by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " finished by " + Thread.currentThread().getName());
            });
        }

        pool.shutdown();
        pool.awaitTermination();
        System.out.println("All tasks completed, pool terminated.");
    }
}