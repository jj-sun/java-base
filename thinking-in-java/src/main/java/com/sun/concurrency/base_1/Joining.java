package com.sun.concurrency.base_1;

/**
 * join
 * 一个线程可以在其他线程上调用join()方法，其效果是等待一段时间直到第二个线程结束才继续执行。
 */

class Sleeper extends Thread {

    private int duration;

    public Sleeper(String name,int duration) {
        super(name);
        this.duration = duration;
        start();
    }
    @Override
    public void run() {
        try {
            sleep(duration);
        } catch (InterruptedException e) {
            System.err.println(getName() + "is interrupted."
            + "isInterrupted(): " +isInterrupted());
        }
        System.out.println(getName() + " has awakened");
    }
}
class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name,Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    @Override
    public void run() {
        try {
            sleeper.join();
        } catch (InterruptedException e) {
            System.err.println("Interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}

public class Joining {

    public static void main(String[] args) {
        Sleeper
                sleepy = new Sleeper("Sleepy",1500),
                grumpy = new Sleeper("Grumpy",1500);
        Joiner
                dopey = new Joiner("Dopey",sleepy),
                doc = new Joiner("Doc",grumpy);
        grumpy.isInterrupted();
    }

}
