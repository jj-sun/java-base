package com.sun.concurrency.base_1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 从任务中产生返回值
 * Future
 * isDone() 可用来查询Future是否已经完成
 * get()将阻塞直至结果准备就绪
 */
public class CallableDemo {

    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        List<Future<String>> results = new ArrayList<>();

        for(int i=0; i<10; i++) {
            results.add(exec.submit(new TaskWithResult(i)));
        }
        results.forEach(res -> {
            try {
                if(res.isDone()) {
                    System.out.println(res.get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }finally {
                exec.shutdown();
            }
        });

    }

}

class TaskWithResult implements Callable {

    private int id;

    public TaskWithResult(int id) {
        this.id = id;
    }

    @Override
    public Object call() throws Exception {
        return "Result of TaskWithResult " + id;
    }
}