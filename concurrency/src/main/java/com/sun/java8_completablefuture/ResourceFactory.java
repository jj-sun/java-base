package com.sun.java8_completablefuture;

/**
 * 延迟初始化占位类模式
 */
public class ResourceFactory {

    private static class ResourceHolder {
        public static Object resource = new Object();
    }
    public static Object getInstance() {
        return ResourceHolder.resource;
    }

}
