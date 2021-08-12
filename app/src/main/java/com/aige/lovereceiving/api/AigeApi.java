package com.aige.lovereceiving.api;


import com.aige.lovereceiving.util.JsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AigeApi {

    public static String Get(String urlPath,Map<String,String> map) throws IOException{
        StringBuffer stringBuffer = new StringBuffer();
        if(map != null) {
            String mapValue = JsonUtil.getMapValue(map);
            stringBuffer.append(urlPath).append("?").append(mapValue.substring(1));
        }else{
            stringBuffer.append(urlPath);
        }
        System.out.println("Get---------------->"+stringBuffer);
        InputStream input = null;
        BufferedReader in = null;
        try {
            URL url = new URL(stringBuffer.toString());
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            //延长超时时间
            connect.setRequestMethod("GET");
            connect.setReadTimeout(5000);
            connect.setConnectTimeout(5000);
            input = connect.getInputStream();
            in = new BufferedReader(new InputStreamReader(input));
            String line = null;
            System.out.println(connect.getResponseCode());
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("Get请求结果-------------->"+sb.toString());
            return sb.toString();
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    public static String post(String url, String json) throws IOException {
        final MediaType JSON
                = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        Date date = null;
        DateFormat df2 = DateFormat.getDateTimeInstance();
        System.out.println("==========================post请求1"+df2.format(new Date()));
        RequestBody body = RequestBody.create(JSON, json);
        System.out.println("==============================="+url+json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newBuilder().connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS).build();
        try (Response response = client.newCall(request).execute()) {
            response.header("Connection", "close");
            System.out.println("==========================post请求2"+df2.format(new Date()));
            String result = response.body().string();
            System.out.println("Post请求结果------------->"+result);
            return result;
        }
    }
    //通过post请求获取接口信息
//    public static String Post(String url, Map<String,String> map){
//        final HttpPost postRequest = new HttpPost(url);
//        // 设置文档中约定的Content-Type为请求的header
//        postRequest.setHeader("Content-Type", "application/json;charset=utf-8");
//        //map集合解析为json
//        ObjectMapper om = new ObjectMapper();
//        String json = "";
//        try {
//            json = om.writeValueAsString(map);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //Request Body
//        final StringEntity entity = new StringEntity(json, "UTF-8");
//        postRequest.setEntity(entity);
//        HttpEntity resultEntity = null;
//        JSONObject object = null;
//        String result = null;
//        try {
//            // 执行请求并获取返回值
//            final HttpClient httpClient = HttpClientBuilder.create().build();
//            final HttpResponse response = httpClient.execute(postRequest);
//
//            resultEntity = response.getEntity();
//            // 获取返回值
//            result = EntityUtils.toString(resultEntity, "UTF-8");
//            System.out.println(result);
//            //格式化json串
//            object = JSONObject.parseObject(result);
//            json = JSON.toJSONString(object, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
//                    SerializerFeature.WriteDateUseDateFormat);
//        } catch (final ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (final IOException e) {
//            e.printStackTrace();
//        } finally {
//            // 资源释放
//            postRequest.abort();
//            try {
//                EntityUtils.consume(resultEntity);
//            } catch (final IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
}
