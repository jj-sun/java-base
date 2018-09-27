package com.sun.concurrency.team_4;

import com.sun.concurrency.base_1.LiftOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

class LiftOffRunner implements Runnable {

    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunner(BlockingQueue<LiftOff> deque) {
        this.rockets = deque;
    }

    public void add(LiftOff liftOff) {
        try {
            rockets.add(liftOff);
        } catch (Exception e) {
            System.err.println("interrupt during put()");
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run();  //use this thread
            }
        } catch (InterruptedException e) {
            System.err.println("waking from take()");
        }
        System.out.println("Exiting LiftOffRunner!");
    }
}

public class TestBlockingQueues {
    static void getKey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static void getKey(String message) {
        System.out.println(message);
        getKey();
    }
    static void test(String msg, BlockingQueue<LiftOff> deque) {
        System.out.println(msg);
        LiftOffRunner runner = new LiftOffRunner(deque);
        Thread thread = new Thread(runner);
        thread.start();
        for(int i=0; i<5; i++) {
            runner.add(new LiftOff(5));
        }
        getKey("Press 'Enter' (" + msg + ")");
        thread.interrupt();
        System.out.println("Finished " + msg + "test");
    }

    public static void main(String[] args) throws Exception {
        test("linded", new LinkedBlockingQueue<LiftOff>());
        test("arrays", new ArrayBlockingQueue<LiftOff>(3));
        test("sync", new SynchronousQueue<LiftOff>());

    }
}
