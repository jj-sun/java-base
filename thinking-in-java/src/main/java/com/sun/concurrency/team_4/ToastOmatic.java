package com.sun.concurrency.team_4;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 吐司BlockingQueue
 */

class Toast {
    public enum Status {DRY, BUTTERED, JAMMED}

    private Status status = Status.DRY;
    private final int id;

    public Toast(int id) {
        this.id = id;
    }

    public void buffer() {
        status = Status.BUTTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Toast " + id + ": " + status;
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {
}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;
    private Random random = new Random(47);

    public Toaster(ToastQueue toasts) {
        this.toastQueue = toasts;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(100 + random.nextInt(500));
                Toast toast = new Toast(count++);
                System.out.println(toast);
                toastQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.err.println("Toaster interrupted");
        }
        System.out.println("Toaster off");
    }
}

//apply butter to toaster
class Butter implements Runnable {

    private ToastQueue dryQueue, butteredQueue;

    public Butter(ToastQueue dryQueue, ToastQueue butteredQueue) {
        this.dryQueue = dryQueue;
        this.butteredQueue = butteredQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast toast = dryQueue.take();
                toast.buffer();
                System.out.println(toast);
                butteredQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.err.println("Butter interrupted");
        }
        System.out.println("Butter off");
    }
}

//Apply jammer to buffer toast
class Jammer implements Runnable {
    ToastQueue bufferedQueue, jammeredQueue;

    public Jammer(ToastQueue bufferedQueue, ToastQueue jammeredQueue) {
        this.bufferedQueue = bufferedQueue;
        this.jammeredQueue = jammeredQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast toast = bufferedQueue.take();
                toast.jam();
                System.out.println(toast);
                jammeredQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.err.println("Jammer interrupted");
        }
        System.out.println("Jammered off");
    }
}

//Consume the toast
class Eater implements Runnable {

    private ToastQueue finishedQueue;
    private int counter = 0;

    public Eater(ToastQueue finishedQueue) {
        this.finishedQueue = finishedQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                Toast toast = finishedQueue.take();
                if (toast.getId() != counter++ || toast.getStatus() != Toast.Status.JAMMED) {
                    System.out.println(">>> Error " + toast);
                    System.exit(1);
                } else {
                    System.out.println(toast);
                }
            }
        } catch (InterruptedException e) {
            System.err.println("Eater interrupted");
        }
        System.out.println("Eater off");
    }
}

public class ToastOmatic {

    public static void main(String[] args) throws Exception {
        ToastQueue dryQueue = new ToastQueue(),
                bufferedQueue = new ToastQueue(),
                finished = new ToastQueue();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new Toaster(dryQueue));
        exec.execute(new Butter(dryQueue, bufferedQueue));
        exec.execute(new Jammer(bufferedQueue, finished));
        exec.execute(new Eater(finished));
        TimeUnit.SECONDS.sleep(5);
        exec.shutdownNow();
    }

}
