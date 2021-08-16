package com.aige.lovereceiving.util;

import com.aige.lovereceiving.api.AigeApi;
import com.aige.lovereceiving.bean.PackageBean;
import com.aige.lovereceiving.bean.PlanNoReceivingBean;
import com.aige.lovereceiving.bean.PlanNoScanBean;
import com.aige.lovereceiving.bean.ScanCodeBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceUtil {
    //http://192.168.10.9:8900
    //http://192.168.10.75:8090
    //http://192.168.10.75:8091
    //http://117.141.133.214:8900
    private static final String head = "http://192.168.10.75:8091";
    private static DateFormat df2;
    public static String login(String userName,String passWork) {
        String sign = MD5Utils.md5("LoveProduction2021"+userName);
        Map<String,String> map = new HashMap<>();
        map.put("userName",userName);
        //map.put("sign",sign);
        ObjectMapper om = new ObjectMapper();
        String json = "";
        String get = null;
        try {
            json = om.writeValueAsString(map);
            get = AigeApi.post(head+"/api/User/Login", json);
        }catch (SocketTimeoutException e) {
            get = "1";
            return get;
        } catch (IOException e) {
            get = "2";
            e.printStackTrace();
        }
        return get;
    }
    //获取包装列表
    public static List<PackageBean> getPackageList(String salesOrderId, String userName) {
        Map<String,String> map = new HashMap<>();
        map.put("SalesOrderId",salesOrderId);
        map.put("UserName", URLEncoder.encode(userName));
        List<PackageBean> list = new ArrayList<>();
        PackageBean bean = null;
        try {
            String get = AigeApi.Get(head+"/api/Orders/GetOrderPackageBySalesOrderId", map);
            JSONObject jsonObject = JSON.parseObject(get);
            if("true".equals(jsonObject.get("ret")+"")) {
                JsonUtil.getJsonArrayValue(jsonObject,"data","",list,PackageBean.class);
            }else{
                bean = new PackageBean();
                bean.setStatus("0");
                list.add(bean);
                return list;
            }
        }catch (SocketTimeoutException e) {
            bean = new PackageBean();
            bean.setStatus("1");
            list.add(bean);
            return list;
        } catch (IOException e) {
            bean = new PackageBean();
            bean.setStatus("2");
            list.add(bean);
            e.printStackTrace();
            return list;
        }
        return list;
    }
    //确认收货
    public static List<ScanCodeBean> scanPackage(String userName,String packageCode,String salesOrderId) {
        Map<String,String> map = new HashMap<>();
        map.put("userName",userName);
        map.put("packageCode",packageCode);
        map.put("salesOrderId",salesOrderId);
        ObjectMapper om = new ObjectMapper();
        String json;
        String post = "";
        List<ScanCodeBean> list = new ArrayList<>();
        ScanCodeBean bean = null;
        try {
            json = om.writeValueAsString(map);
            post = AigeApi.post(head + "/api/Orders/SanCode", json);
            //获取列表状态
            if(!"".equals(post)) {
                JSONObject jsonObject = JSONObject.parseObject(post);
                //收货成功
                if("true".equals(jsonObject.getString("ret"))) {
                    bean = new ScanCodeBean();
                    bean.setStatuss("on");
                    list.add(bean);
                }else{
                    //收货失败
                    bean = new ScanCodeBean();
                    bean.setStatuss("off");
                    list.add(bean);
                }
            }else{
                bean = new ScanCodeBean();
                bean.setStatuss("3");
                list.add(bean);
                return list;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }catch (SocketTimeoutException e) {
            //超时
            bean = new ScanCodeBean();
            bean.setStatuss("1");
            list.add(bean);
            return list;
        } catch (IOException e) {
            //其他异常
            e.printStackTrace();
            bean = new ScanCodeBean();
            bean.setStatuss("2");
            list.add(bean);
            return list;
        }
        return list;
    }
    //批次扫描获取包装列表
    public static List<PlanNoScanBean> getPlanNoScan(String batchNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("BatchNo",batchNo);
        Map<String, Object> network_get = AigeApi.getRequest("http://192.168.10.75:8093/api/Receiving/GetOrderPackageListByBatchNo", map, "NETWORK_GET");
        int responseCode = (int) network_get.get("responseCode");
        List<PlanNoScanBean> list = new ArrayList<>();
        PlanNoScanBean bean = new PlanNoScanBean();
        if(responseCode==200) {
            byte[] responseBody = (byte[]) network_get.get("responseBody");
            try {
                String body = new String(responseBody,"UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(body);
                JsonUtil.getJsonArrayValue(jsonObject,"data","",list, PlanNoScanBean.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            bean.setResponseCode(responseCode);
            list.add(bean);
        }
        return list;
    }
    //批次扫描包装结果
    public static List<PlanNoReceivingBean> getPlanNoScanReceiving(String packageCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("PackageCode",packageCode);
        Map<String, Object> network_get = AigeApi.getRequest("http://192.168.10.75:8093/api/Receiving/ScanCodeReceivingByPackageCode", map, "NETWORK_GET");
        int responseCode = (int) network_get.get("responseCode");
        List<PlanNoReceivingBean> list = new ArrayList<>();
        PlanNoReceivingBean bean = new PlanNoReceivingBean();
        if(200 == responseCode) {
            byte[] responseBody = (byte[]) network_get.get("responseBody");
            try {
                String body = new String(responseBody,"UTF-8");
                JSONObject jsonObject = JSONObject.parseObject(body);
                JsonUtil.getJsonObjectValue(jsonObject,"","",list, PlanNoReceivingBean.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            bean.setResponseCode(responseCode);
            list.add(bean);
        }
        return list;
    }
}
