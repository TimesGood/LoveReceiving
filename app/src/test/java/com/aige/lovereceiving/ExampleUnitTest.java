package com.aige.lovereceiving;


import com.aige.lovereceiving.bean.PackageBean;
import com.aige.lovereceiving.bean.PlanNoScanBean;
import com.aige.lovereceiving.mythread.ThreadCallbackTest;
import com.aige.lovereceiving.util.ServiceUtil;


import org.junit.Test;

import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static ThreadCallbackTest mThreadCallbackTest = new ThreadCallbackTest();
    @Test
    public void addition_isCorrect() {
        List<PackageBean> packageList = ServiceUtil.getPackageList("A4221040002", "A42藤县");
        System.out.println(packageList);
        List<PlanNoScanBean> planNoScan = ServiceUtil.getPlanNoScan("202108129534");
        System.out.println(planNoScan);
    }
}