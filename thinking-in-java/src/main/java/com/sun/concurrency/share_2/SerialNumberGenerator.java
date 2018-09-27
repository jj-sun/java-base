package com.sun.concurrency.share_2;

public class SerialNumberGenerator {

    private static int nextSerialNumber = 0;

    public static int nextSerialNumber() {
        return ++nextSerialNumber;  //Not Thread safe
    }

}
