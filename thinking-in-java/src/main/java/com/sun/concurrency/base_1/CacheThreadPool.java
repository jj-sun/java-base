package com.sun.concurrency.base_1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用ExecutorService
 */
public class CacheThreadPool {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0; i<5; i++) {
            executorService.execute(new LiftOff());
        }
        executorService.shutdown();
        System.out.println("Waiting for LiftOff");
    }

}
