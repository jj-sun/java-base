package com.sun.concurrency.end_3;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CloseResource {

    public static void main(String[] args) throws Exception {

        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(8080);
        InputStream in = new Socket("localhost",8080).getInputStream();
        exec.execute(new IOBlocked(in));
        exec.execute(new IOBlocked(System.in));
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("shutting down all Threads");
        exec.shutdownNow();
        System.out.println("Closing " + in.getClass().getName());
        TimeUnit.SECONDS.sleep(1);
        in.close();
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Closing  " + System.in.getClass().getName());
        System.in.close();
    }

}
