package com.sun.concurrency.share;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EvenChecker implements Runnable {

    private IntGenerator generator;
    private int id;

    public EvenChecker(IntGenerator g , int id) {
        this.generator = g;
        this.id = id;
    }

    @Override
    public void run() {
        while(!generator.isCanceled()) {
            int val = generator.next();
            if(val % 2 != 0) {
                System.out.println(val + " not even");
                generator.cancel();
            }
        }
    }
    public static void test(IntGenerator ig, int count) {
        System.out.println("Press Control + C exit");
        ExecutorService exec = Executors.newCachedThreadPool();
        for(int i=0; i<count; i++) {
            exec.execute(new EvenChecker(ig,i));
        }
        exec.shutdown();
    }
    public static void test(IntGenerator intGenerator) {
        test(intGenerator,10);
    }
}
