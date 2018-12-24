package com.sun.core_skill.t3;

public class Run {

    public static void main(String[] args) {
        MyThread a  = new MyThread("A");
        MyThread b  = new MyThread("b");
        MyThread c  = new MyThread("c");

        a.start();
        b.start();
        c.start();

    }

}
