package com.sun.concurrency.team_4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Car1 {

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private boolean waxOn = false;

    public void waxed() {
        lock.lock();
        try {
            waxOn = true; //ready to buffer
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void buffed() {
        lock.unlock();
        try {
            waxOn = false; //ready for another coat of wax
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void waitForWaxing() throws InterruptedException {
        lock.unlock();
        try {
            while(waxOn == false) {
               condition.await();
            }
        } finally {
            lock.unlock();
        }
    }
    public synchronized void waitForBuffing() throws InterruptedException {
        lock.unlock();
        try {
            while(waxOn == true) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }
}

class WaxOn1 implements Runnable {
    private Car car;

    public WaxOn1(Car car) {
        this.car = car;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                System.out.println("Wax On !");
                TimeUnit.MILLISECONDS.sleep(200);
                car.waxed();
                car.waitForBuffing();
            }
        } catch (InterruptedException e) {
            System.err.println("Exiting via InterruptedException");
        }
        System.out.println("Ending wax on task");
    }
}
class WaxOff1 implements Runnable {

    private Car car;
    public WaxOff1(Car car) {
        this.car = car;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                car.waitForWaxing();
                System.out.println("Wax off !");
                TimeUnit.MILLISECONDS.sleep(200);
                car.buffed();
            }
        } catch (InterruptedException e) {
            System.err.println("Exiting via InterruptedException");
        }
        System.out.println("Ending wax off task");
    }
}

public class WaxOMatic2 {
    public static void main(String[] args) throws Exception {
        Car car = new Car();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new WaxOn(car));
        exec.execute(new WaxOff(car));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }
}
