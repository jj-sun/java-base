package com.sun.concurrency.se_concurrent6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

class Horse implements Runnable {

    private static int counter = 0;
    private final int id = counter++;
    private int strides = 0;

    private static Random random = new Random(47);

    private final CyclicBarrier cyclicBarrier;

    public Horse(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }
    public synchronized int getStrides() {
        return strides;
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                synchronized (this) {
                    strides += random.nextInt(3);
                }
                cyclicBarrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Horse : " + id + " ";
    }
    public String tracks() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i=0; i<getStrides(); i++) {
            stringBuilder.append("*");
        }
        stringBuilder.append(id);
        return stringBuilder.toString();
    }
}



public class HorseRace {

    static final int FINISH_LINE = 75;

    private List<Horse> horses = new ArrayList<>();

    private ExecutorService exec = Executors.newCachedThreadPool();

    private CyclicBarrier cyclicBarrier;

    public HorseRace(int nHorses,final int pause) {
        cyclicBarrier = new CyclicBarrier(nHorses, new Runnable() {

            @Override
            public void run() {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i=0; i<FINISH_LINE; i++) {
                    stringBuilder.append("=");
                }
                System.out.println(stringBuilder);
                for(Horse horse : horses) {
                    System.out.println(horse.tracks());
                }
                for(Horse horse : horses) {
                    if(horse.getStrides() >= FINISH_LINE) {
                        System.out.println(horse + "won!");
                        exec.shutdownNow();
                        return;
                    }
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(pause);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for(int i=0;i<nHorses;i++) {
            Horse horse = new Horse(cyclicBarrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 200;
        new HorseRace(nHorses,pause);
    }

}
