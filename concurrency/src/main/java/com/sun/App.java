package com.sun;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );


        ReadWriteLock lock = new ReentrantReadWriteLock();
        lock.readLock();
        lock.writeLock();

    }
}


