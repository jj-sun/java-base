package com.sun.concurrency.se_concurrent6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class DelayedTask implements Runnable, Delayed {
    private static int counter = 0;
    private final int id = counter++;
    private final int delta;
    private final long trigger;
    protected static List<DelayedTask> sequence = new ArrayList<>();
    public DelayedTask(int delta) {
        this.delta = delta;
        trigger = System.nanoTime() + TimeUnit.NANOSECONDS.convert(delta,TimeUnit.MILLISECONDS);
        sequence.add(this);
    }

    @Override
    public int compareTo(Delayed o) {
        DelayedTask delayedTask = (DelayedTask)o;
        if(trigger < delayedTask.trigger) {
            return -1;
        }
        if(trigger > delayedTask.trigger) {
            return 1;
        }
        return 0;
    }

    @Override
    public void run() {
        System.out.println(this + " ");
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(trigger - System.nanoTime(),TimeUnit.NANOSECONDS);
    }

    @Override
    public String toString() {
        return String.format("[%1$-4d] ",delta) +  " Task " + id ;
    }
    public String summery() {
        return "(" + id + ":" + delta + ")";
    }


    public static class Endsentinel extends DelayedTask {
        private ExecutorService exec;
        public Endsentinel(int delay,ExecutorService exec) {
            super(delay);
            this.exec = exec;
        }

        @Override
        public void run() {
            for(DelayedTask delayedTask : sequence) {
                System.out.println(delayedTask.summery() + " ");
            }
            System.out.println();
            System.out.println(this + " calling shutdown");
            exec.shutdownNow();
        }
    }

}

class DelayTaskConsumer implements Runnable {

    private DelayQueue<DelayedTask> d;
    public DelayTaskConsumer(DelayQueue d) {
        this.d = d;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                d.take().run();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished delayTaskConsumer");
    }
}

public class DelayQueueDemo {

    public static void main(String[] args) {
        Random random = new Random(47);
        ExecutorService exec = Executors.newCachedThreadPool();
        DelayQueue<DelayedTask> delayedTasks = new DelayQueue<>();
        for(int i=0; i<20; i++) {
            delayedTasks.put(new DelayedTask(random.nextInt(5000)));
        }
        delayedTasks.add(new DelayedTask.Endsentinel(5000,exec));
        exec.execute(new DelayTaskConsumer(delayedTasks));
    }

}
