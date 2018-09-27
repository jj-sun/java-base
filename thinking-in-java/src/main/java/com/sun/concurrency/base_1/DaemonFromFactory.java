package com.sun.concurrency.base_1;

import com.sun.util.DaemonThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DaemonFromFactory implements Runnable {

    @Override
    public void run() {
        while(true) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.println(Thread.currentThread() + " " + this);
            } catch (InterruptedException e) {
                System.err.println("interrupted");
            }

        }
    }

    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for(int i=0; i<10; i++) {
            exec.execute(new DaemonFromFactory());
        }
        System.out.println("All daemons started");
        TimeUnit.MILLISECONDS.sleep(1000);
    }
}
