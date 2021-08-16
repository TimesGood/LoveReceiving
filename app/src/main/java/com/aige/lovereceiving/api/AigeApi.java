package com.aige.lovereceiving.api;


import android.content.Context;
import android.content.res.AssetManager;

import com.aige.lovereceiving.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AigeApi {
    //NETWORK_GET表示发送GET请求
    public static final String NETWORK_GET = "NETWORK_GET";
    //NETWORK_POST_KEY_VALUE表示用POST发送键值对数据
    public static final String NETWORK_POST = "NETWORK_POST";
    //NETWORK_POST_XML表示用POST发送XML数据
    public static final String NETWORK_POST_XML = "NETWORK_POST_XML";
    //NETWORK_POST_JSON表示用POST发送JSON数据
    public static final String NETWORK_POST_JSON = "NETWORK_POST_JSON";
    private static URL url = null;//请求的URL地址
    private static HttpURLConnection conn = null;
    private static Context context;
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
    //通过传入参数进行请求
    public static Map<String, Object> getRequest(String urlpath, Map<String, Object> map, String action) {
        Map<String, Object> result = new HashMap<>();
        String requestHeader = null;//请求头
        byte[] requestBody = null;//请求体
        String responseHeader = null;//响应头
        byte[] responseBody = null;//响应体
        int responseCode = 0;//响应码
        StringBuffer buffer = null;
        try {
            if (NETWORK_GET.equals(action)) {
                //发送GET请求
                //判断Get请求有参还是无参
                if (map != null) {
                    buffer = new StringBuffer();
                    buffer.append(urlpath + "?").append(getMapValue(map,"key"));
                    System.out.println("---------------"+buffer.toString());
                    url = new URL(buffer.toString());
                } else {
                    url = new URL(urlpath);
                }
                conn = (HttpURLConnection) url.openConnection();
                //HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
                conn.setRequestMethod("GET");
                //HttpURLConnection默认也支持从服务端读取结果流，所以下面的setDoInput也可以省略
                conn.setDoInput(true);
                //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                conn.setRequestProperty("action", NETWORK_GET);
                //禁用网络缓存
                conn.setUseCaches(false);
                //设置超时时间
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                //在对各种参数配置完成后，通过调用connect方法建立TCP连接，但是并未真正获取数据
                //conn.connect()方法不必显式调用，当调用conn.getInputStream()方法时内部也会自动调用connect方法
                conn.connect();
                responseCode = conn.getResponseCode();
                //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
                InputStream is = conn.getInputStream();
                //将InputStream转换成byte数组,getBytesByInputStream会关闭输入流
                responseBody = getBytesByInputStream(is);
                //获得响应头
                responseHeader = getResponseHeader(conn);

            } else if (NETWORK_POST.equals(action)) {
                //用POST发送键值对数据
                url = new URL(urlpath);
                conn = (HttpURLConnection) url.openConnection();
                //通过setRequestMethod将conn设置成POST方法
                conn.setRequestMethod("POST");
                //调用conn.setDoOutput()方法以显式开启请求体
                conn.setDoOutput(true);
                //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                conn.setRequestProperty("action", NETWORK_POST);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("accept", "application/json");
                //设置超时时间
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                //获取请求头
                requestHeader = getReqeustHeader(conn);
                //获取conn的输出流
                OutputStream os = conn.getOutputStream();
                //获取Json字符串字节数组，将该字节数组作为请求体
                String mapJson = getMapValue(map,"json");
                requestBody = mapJson.getBytes("UTF-8");
                //将请求体写入到conn的输出流中
                os.write(requestBody);
                //记得调用输出流的flush方法
                os.flush();
                //关闭输出流
                os.close();
                //获取响应码
                responseCode = conn.getResponseCode();
                //当调用getInputStream方法时才真正将请求体数据上传至服务器
                InputStream is = conn.getInputStream();
                //获得响应体的字节数组
                responseBody = getBytesByInputStream(is);
                //获得响应头
                responseHeader = getResponseHeader(conn);
            }
        }catch (SocketTimeoutException e) {
            responseCode = 408;
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //最后将conn断开连接
            if (conn != null) {
                conn.disconnect();
            }
        }
        //储存请求获取的数据
        result.put("url", url.toString());
        result.put("action", action);
        result.put("requestHeader", requestHeader);
        result.put("requestBody", requestBody);
        result.put("responseHeader", responseHeader);
        result.put("responseBody", responseBody);
        result.put("responseCode",responseCode);
        return result;
    }
    //通过传入文件进行请求,文件在assets,可传入xml、json文件
    public Map<String, Object> getRequestFile(String urlpath, String filename, String action) {
        Map<String, Object> result = new HashMap<>();
        String requestHeader = null;//请求头
        byte[] requestBody = null;//请求体
        String responseHeader = null;//响应头
        byte[] responseBody = null;//响应体
        int responseCode = 0;//响应码
        StringBuffer buffer = null;
        try {
            if (NETWORK_POST_XML.equals(action)) {
                //用POST发送XML数据
                url = new URL(urlpath);
                conn = (HttpURLConnection) url.openConnection();
                //通过setRequestMethod将conn设置成POST方法
                conn.setRequestMethod("POST");
                //调用conn.setDoOutput()方法以显式开启请求体
                conn.setDoOutput(true);
                //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                conn.setRequestProperty("action", NETWORK_POST_XML);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("accept", "application/json");
                //设置超时时间
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                //获取请求头
                requestHeader = getReqeustHeader(conn);
                //获取conn的输出流
                OutputStream os = conn.getOutputStream();
                //读取assets目录下的person.xml文件，将其字节数组作为请求体
                requestBody = getBytesFromAssets(filename);
                //将请求体写入到conn的输出流中
                os.write(requestBody);
                //记得调用输出流的flush方法
                os.flush();
                //关闭输出流
                os.close();
                //获取响应码
                responseCode = conn.getResponseCode();
                //当调用getInputStream方法时才真正将请求体数据上传至服务器
                InputStream is = conn.getInputStream();
                //获得响应体的字节数组
                responseBody = getBytesByInputStream(is);
                //获得响应头
                responseHeader = getResponseHeader(conn);
            } else if (NETWORK_POST_JSON.equals(action)) {
                //用POST发送JSON数据
                url = new URL(urlpath);
                conn = (HttpURLConnection) url.openConnection();
                //通过setRequestMethod将conn设置成POST方法
                conn.setRequestMethod("POST");
                //调用conn.setDoOutput()方法以显式开启请求体
                conn.setDoOutput(true);
                //用setRequestProperty方法设置一个自定义的请求头:action，由于后端判断
                conn.setRequestProperty("action", NETWORK_POST_JSON);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("accept", "application/json");
                //设置超时时间
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                //获取请求头
                requestHeader = getReqeustHeader(conn);
                //获取conn的输出流
                OutputStream os = conn.getOutputStream();
                //读取assets目录下的person.json文件，将其字节数组作为请求体
                requestBody = getBytesFromAssets(filename);
                //将请求体写入到conn的输出流中
                os.write(requestBody);
                //记得调用输出流的flush方法
                os.flush();
                //关闭输出流
                os.close();
                //获取响应码
                responseCode = conn.getResponseCode();
                //当调用getInputStream方法时才真正将请求体数据上传至服务器
                InputStream is = conn.getInputStream();
                //获得响应体的字节数组
                responseBody = getBytesByInputStream(is);
                //获得响应头
                responseHeader = getResponseHeader(conn);
            }
        } catch (SocketTimeoutException e) {
            responseCode = 408;
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //最后将conn断开连接
            if (conn != null) {
                conn.disconnect();
            }
        }
        //储存请求获取的数据
        result.put("url", url.toString());
        result.put("action", action);
        result.put("requestHeader", requestHeader);
        result.put("requestBody", requestBody);
        result.put("responseHeader", responseHeader);
        result.put("responseBody", responseBody);
        result.put("responseCode",responseCode);
        return result;
    }

    //读取请求头
    private static String getReqeustHeader(HttpURLConnection conn) {
        Map<String, List<String>> requestHeaderMap = conn.getRequestProperties();
        Iterator<String> requestHeaderIterator = requestHeaderMap.keySet().iterator();
        StringBuilder sbRequestHeader = new StringBuilder();
        while (requestHeaderIterator.hasNext()) {
            String requestHeaderKey = requestHeaderIterator.next();
            String requestHeaderValue = conn.getRequestProperty(requestHeaderKey);
            sbRequestHeader.append(requestHeaderKey);
            sbRequestHeader.append(":");
            sbRequestHeader.append(requestHeaderValue);
            sbRequestHeader.append("\n");
        }
        return sbRequestHeader.toString();
    }

    //读取响应头
    private static String getResponseHeader(HttpURLConnection conn) {
        Map<String, List<String>> responseHeaderMap = conn.getHeaderFields();
        int size = responseHeaderMap.size();
        StringBuilder sbResponseHeader = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String responseHeaderKey = conn.getHeaderFieldKey(i);
            String responseHeaderValue = conn.getHeaderField(i);
            sbResponseHeader.append(responseHeaderKey);
            sbResponseHeader.append(":");
            sbResponseHeader.append(responseHeaderValue);
            sbResponseHeader.append("\n");
        }
        return sbResponseHeader.toString();
    }

    //获取流中的数据
    private static byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    //根据文件名，从asserts目录中读取文件的字节数组
    private byte[] getBytesFromAssets(String fileName) {
        byte[] bytes = null;
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(fileName);
            bytes = getBytesByInputStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //解析Map集合
    public static String getMapValue(Map<String, Object> map,String type) {
        StringBuffer stringBuffer = new StringBuffer();
        if("key".equals(type)) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                stringBuffer.append("&" + next.getKey() + "=" + next.getValue())
                        .toString().substring(1);
            }
        }else if ("json".equals(type)){
            ObjectMapper om = new ObjectMapper();
            try {
                stringBuffer.append(om.writeValueAsString(map));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
}
