package com.sun.core_skill.t3;

import java.util.concurrent.TimeUnit;

public class MyThread extends Thread {

    private int count = 5;

    public MyThread(String name) {
        super();
        this.setName(name);
    }

    @Override
    public void run() {
        super.run();
        while(count > 0) {
            count--;
            System.out.println("Run " + Thread.currentThread().getName() + ":" + count);
        }
    }
}
