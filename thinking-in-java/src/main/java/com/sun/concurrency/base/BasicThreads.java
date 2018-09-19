package com.sun.concurrency.base;

public class BasicThreads {

    public static void main(String[] args) {
        Thread thread = new Thread(new LiftOff());

        thread.start();

        System.out.println("Waiting for LiftOff");
    }


}
