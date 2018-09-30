package com.sun.concurrency.se_concurrent6;

import com.sun.util.BasicGenerator;
import com.sun.util.Generator;

import java.util.List;
import java.util.concurrent.*;

class ExchangerProducer<T> implements Runnable {
    private Generator<T> generator;
    private Exchanger<List<T>> exchanger;
    private List<T> holder;

    public ExchangerProducer(Exchanger<List<T>> exchanger,Generator<T> generator,List<T> holder) {
        this.exchanger = exchanger;
        this.generator = generator;
        this.holder = holder;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                for(int i=0; i<ExchangerDemo.size;i++) {
                    holder.add(generator.next());
                }
                holder = exchanger.exchange(holder);
            }
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
        System.out.println("Finished ExchangerProducer");
    }
}

class ExchangerConsumer<T> implements Runnable {

    private Exchanger<List<T>> exchanger;
    private List<T> holder;
    private volatile T value;
    public ExchangerConsumer(Exchanger<List<T>> exchanger,List<T> holder) {
        this.exchanger = exchanger;
        this.holder = holder;
    }
    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                holder = exchanger.exchange(holder);
                for(T x : holder) {
                    value = x;
                    holder.remove(x);
                }
            }
        } catch (InterruptedException e) {
           // e.printStackTrace();
        }
        System.out.println("Final value: " + value);
    }
}

public class ExchangerDemo {

    static int size = 10;
    static int delay = 5;

    public static void main(String[] args) throws Exception{
        if(args.length > 0) {
            size = Integer.parseInt(args[0]);
        }
        if(args.length > 1) {
            delay = Integer.parseInt(args[1]);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        Exchanger<List<Fat>> xc = new Exchanger<>();
        List<Fat>
                producerList = new CopyOnWriteArrayList<>(),
                consumerList = new CopyOnWriteArrayList<>();
        exec.execute(new ExchangerProducer<>(xc, BasicGenerator.create(Fat.class),producerList));
        exec.execute(new ExchangerConsumer<>(xc,consumerList));
        TimeUnit.SECONDS.sleep(delay);
        exec.shutdownNow();
    }

}
