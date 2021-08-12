package com.aige.lovereceiving.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * json节点查找
 */
public class JsonUtil {
    /**
     * 用递归算法解析Json文件指定层级的是否符合要求
     * 用于解析层级是一组的Json字符串
     * @param json      需要解析的字符串
     * @param attrs     指定字符串层级，以层级1.层级2.层级3...规则写入
     * @param condition 如果需要在层级paramModel中指定键值对属性的值，以键:值规则写入
     * @param jsonList  返回指定层级的List集合
     * @param domain    json映射对象，自己创建一个对应节点字段的domain
     */
    public static void getJsonSingleValue(JSONObject json, String attrs, String condition, List<?> jsonList, Class domain) {
        JSONObject jsonObject = null;
        int index1 = attrs.indexOf('.');
        if(json == null) {
            System.out.println("Json串为空，请检查传入的Json串");
            return;
        }
        //最终层
        if (index1 == -1) {
            jsonObject = json.getJSONObject(attrs);
            if (!(null == jsonObject)) {
                System.out.println(jsonObject);
                jsonList.addAll(JSONObject.parseArray("[" + jsonObject.toString() + "]", domain));
            }
        } else {//头节点
            String s1 = attrs.substring(0, index1);
            String s2 = attrs.substring(index1 + 1);
            jsonObject = json.getJSONObject(s1);
            if (!(null == jsonObject)) {
                getJsonSingleValue(jsonObject, s2, "", jsonList, domain);
            }
        }
        return;
    }
    /**
     * 用递归算法解析Json文件指定层级的是否符合要求
     * 用于解析层级是多组的JSON对象
     * @param json 需要解析的字符串
     * @param attrs 指定字符串层级，以层级1.层级2.层级3...规则写入
     * @param condition 如果需要在层级paramModel中指定键值对属性的值，以键:值规则写入
     * @param jsonList 返回指定层级的List集合
     * @param domain json映射对象，自己创建一个对应节点字段的domain
     */
    public static void getJsonNodeValue(JSONObject json, String attrs,String condition,List<?> jsonList,Class domain) {
        JSONArray jsonArray = null;
        int index1 = attrs.indexOf('.');
        if(json == null) {
            System.out.println("Json串为空，请检查传入的Json串");
            return;
        }
        //最终层
        if (index1 == -1) {
            jsonArray = json.getJSONArray(attrs);
            if(!(null==jsonArray)) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    //把指定层级的json储存在List集合中
                    jsonList.addAll(JSONObject.parseArray("[" + jsonObject.toString() + "]", domain));
                }
            }
        }else {//头节点
            String s1 = attrs.substring(0, index1);
            String s2 = attrs.substring(index1 + 1);
            jsonArray = json.getJSONArray(s1);
            if(!(null==jsonArray)) {
                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                    if(condition.length()>0) {
                        int index2 = condition.indexOf(":");
                        String c1 = condition.substring(0,index2);
                        String c2 = condition.substring(index2+1);
                        String subStr = jsonObject.getString(c1) == null?"":jsonObject.getString(c1);
                        if(subStr.contains(c2)) {
                            getJsonNodeValue(jsonObject,s2,"",jsonList,domain);
                        }else {
                            continue;
                        }
                    }else {
                        getJsonNodeValue(jsonObject,s2,"",jsonList,domain);
                    }
                }
            }
        }
        return;
    }
    //解析Map集合
    public static String getMapValue(Map<String,String> map) {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            stringBuffer.append("&"+next.getKey()+"="+next.getValue());
        }
        return stringBuffer.toString();
    }
}