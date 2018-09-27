package com.sun.concurrency.end_3;

import java.util.concurrent.TimeUnit;

class NeedCleanUp {
    private final int id;
    public NeedCleanUp(int id) {
        this.id = id;
        System.out.println("NeedCleanUp: " + this.id);
    }
    public void cleanup() {
        System.out.println("cleaning up : " + id);
    }

}

class Blocked3 implements Runnable {

    private volatile double d = 0.0d;

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                NeedCleanUp n1 = new NeedCleanUp(1);

                try {
                    System.out.println("Sleeping");
                    TimeUnit.SECONDS.sleep(1);
                    NeedCleanUp n2 = new NeedCleanUp(2);
                    try {
                        System.out.println("Calculating");

                        for(int i=1; i<2500000;i++) {
                            d = d + (Math.PI + Math.E) / d;
                        }
                        System.out.println("finished");

                    } finally {
                        n2.cleanup();
                    }
                } finally {
                    n1.cleanup();
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Exiting via InterruptedException");
        }
    }
}

public class InterruptingIdiom {

    public static void main(String[] args) throws Exception {
        /*if(args.length != 1) {
            System.out.println("usage: java interruptingIdiom delay-in-mS");
            System.exit(1);
        }*/
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.SECONDS.sleep(3);
        t.interrupt();
    }

}
