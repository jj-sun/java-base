package com.sun.concurrency.base;


import java.util.concurrent.TimeUnit;

class ADaemon implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Starting ADaemon");
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.err.println("Exiting via InterruptedException");
        } finally {
            System.err.println("This should always run?");
        }
    }
}
public class DaemonDontRunFinally {

    public static void main(String[] args) throws Exception {
        Thread t = new Thread(new ADaemon());
        t.setDaemon(true);
        t.start();
        TimeUnit.MILLISECONDS.sleep(1);
    }

}
