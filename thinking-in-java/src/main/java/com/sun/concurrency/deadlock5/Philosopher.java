package com.sun.concurrency.deadlock5;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {

    private Chopstick left;
    private Chopstick right;
    private final int id;
    private final int ponderFactor;
    private Random random = new Random(47);
    public Philosopher(Chopstick left,Chopstick right,int id, int ponderFactor) {
        this.left = left;
        this.right = right;
        this.id = id;
        this.ponderFactor = ponderFactor;
    }
    public void pause() throws InterruptedException {
        if(ponderFactor == 0) {
            return;
        }
        TimeUnit.MILLISECONDS.sleep(random.nextInt(ponderFactor*250));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " thinking");
                pause();
                System.out.println(this + "" + " grabbing right");
                right.take();
                System.out.println(this + "" + " grabbing left");
                left.take();
                System.out.println(this + "eating");
                pause();
                right.drop();
                left.drop();
            }
        } catch (Exception e) {
            System.out.println(this + " exiting via interrupt");
        }
    }

    @Override
    public String toString() {
        return "Philosopher " + id ;
    }
}
