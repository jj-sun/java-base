package com.sun.concurrency.se_concurrent6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class CheckoutTask<T> implements Runnable {

    private static int counter = 0;
    private final int id = counter++;

    private Pool<T> pool;
    public CheckoutTask(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void run() {

        try {
            T item = pool.checkout();
            System.out.println(this + "checked out " + item);

            TimeUnit.SECONDS.sleep(1);
            System.out.println(this + "checking in " + item);
            pool.checkIn(item);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CheckoutTask{" +
                "id=" + id +
                '}';
    }
}

public class SemaphoreDemo {
    final static int SIZE = 25;

    public static void main(String[] args) throws Exception {
        final Pool<Fat> pool = new Pool<>(Fat.class,SIZE);
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<SIZE; i++) {
            exec.execute(new CheckoutTask<Fat>(pool));
        }
        System.out.println("All checkoutTask created");

        List<Fat> list = new ArrayList<>();
        for(int i=0; i<SIZE;i++) {
            Fat f = pool.checkout();
            f.operation();
            list.add(f);
        }
        Future<?> blocked = exec.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    pool.checkout();
                } catch (InterruptedException e) {
                    System.err.println("checkout interrupted");
                }
            }
        });
        TimeUnit.SECONDS.sleep(2);
        blocked.cancel(true);
        System.out.println("checking in object in " + list);
        for(Fat f : list) {
            pool.checkIn(f);
        }
        for(Fat f : list) {
            pool.checkIn(f);
        }
        exec.shutdown();

    }
}
