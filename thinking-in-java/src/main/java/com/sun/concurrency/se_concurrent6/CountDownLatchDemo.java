package com.sun.concurrency.se_concurrent6;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TaskPortion implements Runnable {

    private static int counter = 0;

    private final int id = counter++;

    private static Random random = new Random();

    private final CountDownLatch countDownLatch;

    public TaskPortion(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            doWork();
            countDownLatch.countDown();
        } catch (InterruptedException e) {
            //
        }
    }

    public void doWork() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(random.nextInt(2000));
        System.out.println(this + " completed");
    }

    @Override
    public String toString() {
        return String.format("%1$-3d ",id);
    }
}

class WaitingTask implements Runnable {

    private static int counter = 0;
    private final int id = counter++;
    private final CountDownLatch countDownLatch;

    public WaitingTask(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            System.out.println("Latch barrier passed for " + this);
        } catch (InterruptedException e) {
            System.err.println(this + "interrupted");
        }
    }
    @Override
    public String toString() {
        return String.format("WaitingTask %1$-3d ",id);
    }
}

public class CountDownLatchDemo {

    static int SIZE = 100;

    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);
        for(int i=0; i<10; i++) {
            exec.execute(new WaitingTask(countDownLatch));
        }
        for(int i=0; i<SIZE;i++) {
            exec.execute(new TaskPortion(countDownLatch));
        }
        System.out.println("Latched all tasks");
        exec.shutdown();
    }

}
