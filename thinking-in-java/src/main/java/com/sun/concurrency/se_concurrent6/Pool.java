package com.sun.concurrency.se_concurrent6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Pool<T> {
    private int size;
    private List<T> items = new ArrayList<>();
    private volatile boolean[] checkedOut;
    private Semaphore available;
    public Pool(Class<T> classObject,int size) {
        this.size = size;
        checkedOut = new boolean[size];
        available = new Semaphore(size,true);
        for(int i=0; i<size; i++) {
            try {
                items.add(classObject.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public T checkout() throws InterruptedException {
        available.acquire();
        return getItem();
    }
    public void checkIn(T t) {
        if(releaseItem(t)) {
            available.release();
        }
    }
    private synchronized T getItem() {
        for(int i=0; i<size;i++) {
            if(!checkedOut[i]) {
                checkedOut[i] = true;
                return items.get(i);
            }
        }
        return null;
    }
    private synchronized boolean releaseItem(T t) {
        int index = items.indexOf(t);
        if(index == -1) {
            return false;
        }
        if(checkedOut[index]) {
            checkedOut[index] = false;
            return true;
        }
        return false;
    }

}
