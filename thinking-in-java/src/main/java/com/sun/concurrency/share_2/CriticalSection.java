package com.sun.concurrency.share_2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class Pair {
    private int x, y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair() {
        this(0, 0);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void incrementX() {
        x++;
    }

    public void incrementY() {
        y++;
    }

    @Override
    public String toString() {
        return "x: " + x +
                ", y: " + y;
    }

    public class PairValuesNotEqualException extends RuntimeException {
        public PairValuesNotEqualException() {
            super("Pair values not equal: " + Pair.this);
        }
    }

    public void checkState() {
        if (x != y) {
            throw new PairValuesNotEqualException();
        }
    }
}

abstract class PairManager {
    AtomicInteger checkCounter = new AtomicInteger(0);
    protected Pair p = new Pair();
    private List<Pair> storage = Collections.synchronizedList(new ArrayList<Pair>());
    public Pair getPair() {
        return new Pair(p.getX(),p.getY());
    }
    protected void store(Pair p) {
        storage.add(p);
        try {
            TimeUnit.MILLISECONDS.sleep(50   );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public abstract void increment();
}

class PairManager1 extends PairManager {
    @Override
    public synchronized void increment() {
        p.incrementX();
        p.incrementY();
        store(getPair());
    }
}
class PairManager2 extends PairManager {
    @Override
    public void increment() {
        Pair pair;
        synchronized (this) {
            p.incrementX();
            p.incrementY();
            pair = getPair();
        }
        store(pair);
    }
}

class PairManipulator implements Runnable {

    private PairManager pairManager;
    public PairManipulator(PairManager pairManager) {
        this.pairManager = pairManager;
    }
    @Override
    public void run() {
        while(true) {
            pairManager.increment();
        }
    }

    @Override
    public String toString() {
        return "Pair: " + pairManager.getPair() + " checkCounter: " + pairManager.checkCounter.get();
    }
}

class PairChecker implements Runnable {
    private PairManager pairManager;
    public PairChecker(PairManager pairManager) {
        this.pairManager = pairManager;
    }

    @Override
    public void run() {
        while (true) {
            pairManager.checkCounter.incrementAndGet();
            pairManager.getPair().checkState();
        }
    }
}

public class CriticalSection {

    static void testApproaches(PairManager pm1,PairManager pm2) {
        ExecutorService exec = Executors.newCachedThreadPool();
        PairManipulator
                pairManipulator1 = new PairManipulator(pm1),
                pairManipulator2 = new PairManipulator(pm2);
        PairChecker
                pairChecker1 = new PairChecker(pm1),
                pairChecker2 = new PairChecker(pm2);
        exec.execute(pairManipulator1);
        exec.execute(pairManipulator2);
        exec.execute(pairChecker1);
        exec.execute(pairChecker2);

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("pairManipulator1: " + pairManipulator1 + "\npairManipulator2: " + pairManipulator2);
        System.exit(0);
    }

    public static void main(String[] args) {
        PairManager
                pairManager1 = new PairManager1(),
                pairManager2 = new PairManager2();
        testApproaches(pairManager1,pairManager2);
    }

}
