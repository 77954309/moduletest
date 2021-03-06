package com.lm.concurrent.actuator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Classname TestNewFixedThreadPool
 * @Description TODO
 * @Date 2020/9/24 18:51
 * @Created by limeng
 */
public class TestNewFixedThreadPool {
    public static void main(String[] args) throws InterruptedException {
        //ExecutorService pool = ThreadPool.newCachedThreadPool(2,"TestNewFixedThreadPool-",false);
        ExecutorService pool = Executors.newSingleThreadExecutor();

        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("ccc");
            }
        });
//
//        // 创建线程
//        Thread t1 = new MyThread();
//        Thread t2 = new MyThread();
//        Thread t3 = new MyThread();
//        Thread t4 = new MyThread();
//        Thread t5 = new MyThread();
//        // 将线程放入池中进行执行
//
//        pool.execute(t1);
//        pool.execute(t2);
//        pool.execute(t3);
//        pool.execute(t4);
//        pool.execute(t5);
//        // 关闭线程池
//        pool.shutdown();
    }
}
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "正在执行。。。");
    }
}