package com.github.skyisbule.grab;

import com.github.skyisbule.grab.thread.GThread;


public class Run {

    public static void main(String[] args){
        new GThread("test.properties").start();
        new GThread("test.properties").start();
    }

}
