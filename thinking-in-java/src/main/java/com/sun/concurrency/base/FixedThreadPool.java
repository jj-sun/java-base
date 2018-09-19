package com.sun.concurrency.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0; i<5; i++) {
            executorService.execute(new LiftOff());
        }
        executorService.shutdown();
        System.out.println("Waiting for LiftOff");
    }
}
