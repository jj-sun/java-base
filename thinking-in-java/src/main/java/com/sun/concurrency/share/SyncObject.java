package com.sun.concurrency.share;

/**
 * 同步代码块
 * 在其他对象上进行同步
 */
class DualSynch {
    private Object syncObject = new Object();
    public synchronized void f() {
        for(int i=0; i<5; i++) {
            System.out.println("f()");
            Thread.yield();
        }
    }
    public void g() {
        synchronized (syncObject) {
            for(int i=0; i<5; i++) {
                System.out.println("g()");
                Thread.yield();
            }
        }
    }
}

public class SyncObject {
    public static void main(String[] args) {
        final DualSynch dualSynch = new DualSynch();
        new Thread(() -> dualSynch.f()).start();
        dualSynch.g();
    }
}
