package com.sun.concurrency.share_2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class AttemptLocking {

    private ReentrantLock lock = new ReentrantLock();

    public void untimed() {
        boolean capture = lock.tryLock();
        try {
            System.out.println("tryLock(): " +capture);
        } finally {
            if(capture) {
                lock.unlock();
            }
        }
    }
    public void timed() {
        boolean captured = false;

        try {
            captured = lock.tryLock(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        try {
            System.out.println("lock.tryLock(2,TimeUnit.SECONDS): " + captured);

        } finally {
            if(captured) {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final AttemptLocking attemptLocking = new AttemptLocking();
        attemptLocking.untimed();
        attemptLocking.timed();
        new Thread() {
            {
                setDaemon(true);
            }
            @Override
            public void run() {
                attemptLocking.lock.lock();
                System.out.println("acquired");
            }
        }.start();
        Thread.yield();
        TimeUnit.MILLISECONDS.sleep(200);
        attemptLocking.untimed();
        attemptLocking.timed();

    }

}
