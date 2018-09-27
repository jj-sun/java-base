package com.sun.concurrency.end_3;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class NIOBlocked implements Runnable {

    private final SocketChannel sc;

    public NIOBlocked(SocketChannel sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for read() in " + this);
            sc.read(ByteBuffer.allocate(1));
        } catch (ClosedByInterruptException e) {
            System.err.println("ClosedByInterruptException");
        }catch (AsynchronousCloseException e) {
            System.err.println("AsynchronousCloseException");
        }catch (IOException e) {
            throw new RuntimeException();
        }
        System.out.println("Exiting NIOBlocked " + this);
    }
}

public class NIOInterruption {

    public static void main(String[] args) throws Exception {
        ExecutorService exec = Executors.newCachedThreadPool();
        ServerSocket sc = new ServerSocket(8080);
        InetSocketAddress sca = new InetSocketAddress("localhost",8080);

        SocketChannel sc1 = SocketChannel.open(sca);
        SocketChannel sc2 = SocketChannel.open(sca);

        Future<?> future = exec.submit(new NIOBlocked(sc1));
        exec.execute(new NIOBlocked(sc2));
        exec.shutdown();
        TimeUnit.SECONDS.sleep(1);
        future.cancel(true);
        TimeUnit.SECONDS.sleep(1);
        sc2.close();

    }

}
