package com.aige.lovereceiving.mythread;

import com.aige.lovereceiving.bean.PackageBean;
import com.aige.lovereceiving.util.ServiceUtil;

import java.util.List;
import java.util.Map;

public class MyThread implements Runnable {
    private List<PackageBean> packageBeans;
    private ThreadCallback mThreadCallback;
    public MyThread(ThreadCallback threadCallback){
        this.mThreadCallback = threadCallback;
    }
    @Override
    public void run() {
        mThreadCallback.threadStartLisener();
        packageBeans = ServiceUtil.getPackageList("A4221040002","A42藤县");

        mThreadCallback.threadEndLisener();
    }
    private List<PackageBean> Test() {
        return packageBeans;
    }
}
