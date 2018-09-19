package com.sun.concurrency.base;

public class MoreBasicThreads {

    public static void main(String[] args) {
        for(int i=0; i<5; i++) {
            Thread thread = new Thread(new LiftOff());
            thread.start();
        }
        System.out.println("Waiting for LiftOff");
    }

}
