package com.sun.concurrency.share;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomciIntegerTest implements Runnable {
    private AtomicInteger a1 = new AtomicInteger(0);

    public int getValue() {
        return a1.get();
    }

    public void evenIncrement() {
        a1.addAndGet(2);
    }

    @Override
    public void run() {
        while (true) {
            evenIncrement();
        }
    }

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Aborting");
                System.exit(0);
            }
        }, 5000);

        ExecutorService exec = Executors.newCachedThreadPool();
        AtomciIntegerTest a1 = new AtomciIntegerTest();
        exec.execute(a1);
        while(true) {
            int val = a1.getValue();
            if(val % 2 != 0) {
                System.out.println(val);
                System.exit(0);
            }
        }
    }
}
