package com.sun.concurrency.end_3;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlocked implements Runnable {
    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException");
        }
        System.out.println("exiting SleepBlocked.run()");
    }
}
class IOBlocked implements Runnable {
    private InputStream in;
    public IOBlocked(InputStream is) {
        this.in = is;
    }
    @Override
    public void run() {
        System.out.println("Please input: ");
        try {
            in.read();
        } catch (IOException e) {
           if(Thread.currentThread().isInterrupted()) {
               System.err.println("IOBlocked interrupted");
           }else {
               throw new RuntimeException();
           }
        }
        System.out.println("exiting IOBlocked.run()");
    }
}

class SynchronizedBlocked implements Runnable {
    public synchronized void f() {
        while(true) {
            Thread.yield();
        }
    }
    public SynchronizedBlocked() {
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }
    @Override
    public void run() {
        System.out.println("Trying to call f()");
        f();
        System.out.println("exiting SynchronizedBlocked.run()");
    }
}

public class Interrupting {

    private static ExecutorService exec = Executors.newCachedThreadPool();
    static void test(Runnable runnable) throws Exception {
        Future<?> future = exec.submit(runnable);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("Interrupting " + runnable.getClass().getName());
        future.cancel(true); //中断线程
        System.out.println("interrupt send to  " + runnable.getClass().getName());
    }

    public static void main(String[] args) throws Exception {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("exit(0)");
        System.exit(0);
    }

}
