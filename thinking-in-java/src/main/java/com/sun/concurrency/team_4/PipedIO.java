package com.sun.concurrency.team_4;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 任务间使用管道进行输入/输出
 */
class Sender implements Runnable {
    private PipedWriter out = new PipedWriter();
    private Random random = new Random(47);

    public PipedWriter getPipedWriter() {
        return out;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for(char c = 'A'; c < 'z'; c++) {
                    out.write(c);
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(500));
                }
            }
        } catch (IOException e) {
            System.err.println(e + "sender IOException");
        } catch (InterruptedException e) {
            System.err.println(e + "sender InterruptedException");
        }
    }
}

class Receiver implements Runnable {
    private PipedReader in;
    public Receiver(Sender sender) throws IOException {
        in = new PipedReader(sender.getPipedWriter());
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("Read " + (char)in.read() + ", ");
            }
        } catch (IOException e) {
            System.err.println(e + "receiver exception");
        }
    }
}

public class PipedIO {

    public static void main(String[] args) throws Exception {
        Sender sender = new Sender();
        Receiver receiver = new Receiver(sender);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receiver);
        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }

}
