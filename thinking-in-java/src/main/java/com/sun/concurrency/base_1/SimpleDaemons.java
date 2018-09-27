package com.sun.concurrency.base_1;

import java.util.concurrent.TimeUnit;

/**
 * 后台线程
 */
public class SimpleDaemons implements Runnable {

    @Override
    public void run() {
        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            } catch (InterruptedException e) {
                System.err.println("sleep() interrupted");
            }

        }
    }

    public static void main(String[] args) throws Exception {
        for(int i=0; i<10; i++) {
            Thread thread = new Thread(new SimpleDaemons());
            thread.setDaemon(true);
            thread.start();
        }

        System.out.println("All daemons started");
        TimeUnit.MILLISECONDS.sleep(121);
    }
}
