package com.aige.lovereceiving;

import com.aige.lovereceiving.api.AigeApi;
import com.aige.lovereceiving.bean.PackageBean;
import com.aige.lovereceiving.bean.ScanCodeBean;
import com.aige.lovereceiving.bean.UserBean;
import com.aige.lovereceiving.mythread.MyThread;
import com.aige.lovereceiving.mythread.ThreadCallback;
import com.aige.lovereceiving.mythread.ThreadCallbackTest;
import com.aige.lovereceiving.util.JsonUtil;
import com.aige.lovereceiving.util.ServiceUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static ThreadCallbackTest mThreadCallbackTest = new ThreadCallbackTest();
    @Test
    public void addition_isCorrect() {

    }
}