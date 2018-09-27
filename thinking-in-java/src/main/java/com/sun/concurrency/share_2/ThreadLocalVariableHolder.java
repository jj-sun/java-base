package com.sun.concurrency.share_2;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 线程本地存储
 */
class Accessor implements Runnable {
    private final int id;
    public Accessor(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            ThreadLocalVariableHolder.increment();
            System.out.println(this);
            Thread.yield();
        }
    }

    @Override
    public String toString() {
        return "#" + id +
                ": " + ThreadLocalVariableHolder.get();
    }
}

public class ThreadLocalVariableHolder {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
      private Random random = new Random(47);
      protected synchronized Integer initialValue() {
          return random.nextInt(10000);
      }
    };
    public static void increment() {
        threadLocal.set(threadLocal.get() + 1);
    }
    public static int get() {
        return threadLocal.get();
    }

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++) {
            exec.execute(new Accessor(i));
        }
        TimeUnit.SECONDS.sleep(3);
        exec.shutdown();
    }

}
