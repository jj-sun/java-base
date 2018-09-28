package com.sun.concurrency.deadlock5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 解决死锁的方法之一
 * 没看明白，感觉也没解决,还是处于死锁中
 */
public class FixedDiningPhilosophers {
    public static void main(String[] args) throws Exception {
        int ponder = 5;
        if(args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }
        int size = 1;
        if(args.length > 1) {
            size = Integer.parseInt(args[1]);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];
        for(int i=0;i<size;i++) {
            sticks[i] = new Chopstick();
        }
        for(int i=0; i<size;i++) {
            if(i< (size-1)) {
                exec.execute(new Philosopher(sticks[i],sticks[i+1],i,ponder));
            }else {
                exec.execute(new Philosopher(sticks[0],sticks[i],i,ponder));
            }

        }

        if(args.length == 3 && args[2].equals("timeout")) {
            TimeUnit.SECONDS.sleep(5);
        }else {
            System.out.println("Press 'Enter to quit'");
            System.in.read();
        }
        exec.shutdownNow();
    }
}
