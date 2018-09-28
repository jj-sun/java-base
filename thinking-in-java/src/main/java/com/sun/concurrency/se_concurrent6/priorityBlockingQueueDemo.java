package com.sun.concurrency.se_concurrent6;


import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class PrioritizedTask implements Runnable,Comparable<PrioritizedTask> {
    private Random random = new Random(47);
    private static int counter = 0;
    private final int id = counter++;
    private final int priority;
    protected static List<PrioritizedTask> sequence =
            new ArrayList<>();
    public PrioritizedTask(int priority) {
        this.priority = priority;
        sequence.add(this);
    }

    @Override
    public int compareTo(PrioritizedTask o) {
        return priority < o.priority ? 1 :
                (priority > o.priority ? -1 : 0);
    }

    @Override
    public void run() {
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(250));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this);
    }

    @Override
    public String toString() {
        return String.format("[%1$-3d]",priority) + " Task " + id;
    }
    public String summary() {
        return "(" + id + ":" + priority + ")";
    }

    public static class Endsentinel extends PrioritizedTask {
        private ExecutorService exec;
        public Endsentinel(ExecutorService exec) {
            super(-1);
            this.exec = exec;
        }

        @Override
        public void run() {
            int count = 0;
            for(PrioritizedTask pt : sequence) {
                System.out.println(pt.summary() + " ");
                if(++count % 5 == 0) {
                    System.out.println();
                }
            }
            System.out.println();
            System.out.println(this + " calling shutdown");
            exec.shutdownNow();
        }
    }
}

class PrioritizedTaskProducer implements Runnable {


    private Random random = new Random(47);
    private Queue<Runnable> queue;
    private ExecutorService exec;
    public PrioritizedTaskProducer(Queue<Runnable> q,
                                   ExecutorService exec) {
        this.queue = q;
        this.exec = exec;
    }

    @Override
    public void run() {
        try {
            for(int i=0; i<20; i++) {
                queue.add(new PrioritizedTask(random.nextInt(10)));
                Thread.yield();
            }
            for(int i=0; i<10; i++) {
                TimeUnit.MILLISECONDS.sleep(250);
                queue.add(new PrioritizedTask(10));
            }
            for(int i=0; i<10; i++) {
                queue.add(new PrioritizedTask(i));
            }
            queue.add(new PrioritizedTask.Endsentinel(exec));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("finished PrioritizedTaskProducer");
    }
}

public class priorityBlockingQueueDemo {

}
