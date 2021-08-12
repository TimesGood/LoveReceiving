package com.aige.lovereceiving.mythread;

public class ThreadCallbackTest implements ThreadCallback {
    private static ThreadCallbackTest mThreadCallbackTest = new ThreadCallbackTest();

    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + " 开始");
        new Thread(new MyThread(mThreadCallbackTest)).start();
    }

    @Override
    public void threadStartLisener() {
        System.out.println(Thread.currentThread().getName() + " 线程，知道SubRunnable线程开始执行任务了");
    }

    @Override
    public void threadEndLisener() {
        System.out.println(Thread.currentThread().getName() + " 线程，知道SubRunnable线程任务执行结束了");
    }
}