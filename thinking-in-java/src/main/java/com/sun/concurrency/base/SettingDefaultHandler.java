package com.sun.concurrency.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 设置全局的处理器
 */
public class SettingDefaultHandler {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHanlder());
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new ExceptionThread2());
        exec.shutdown();
    }
}
