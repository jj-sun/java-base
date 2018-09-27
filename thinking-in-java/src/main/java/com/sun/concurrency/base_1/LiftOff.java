package com.sun.concurrency.base_1;

import java.util.concurrent.TimeUnit;

public class LiftOff implements Runnable {
    protected int countDown = 10;
    private static int taskCount = 0;
    private final int id = taskCount++;

    public LiftOff() {}
    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" + (countDown > 0 ? countDown : "LiftOff!") + ").";
    }

    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.println(status());
            //Thread.yield();  //让出cpu给其他线程使用，但不保证

            //Old style
            //Thread.sleep(100)
            // java SE-5/6 style
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
