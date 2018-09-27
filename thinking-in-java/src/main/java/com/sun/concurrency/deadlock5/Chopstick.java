package com.sun.concurrency.deadlock5;

public class Chopstick {
    private boolean take = false;
    public synchronized void take() throws Exception {
        while (take) {
            wait();
        }
        take = true;
    }
    public synchronized void drop() {
        take = false;
        notifyAll();
    }
}
