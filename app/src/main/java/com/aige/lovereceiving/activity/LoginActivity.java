package com.aige.lovereceiving.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.aige.lovereceiving.R;
import com.aige.lovereceiving.bean.UserBean;
import com.aige.lovereceiving.util.AnalysisUtils;
import com.aige.lovereceiving.util.JsonUtil;
import com.aige.lovereceiving.util.ServiceUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private EditText editUserName,editPassword;
    private Button loginBtn;
    private LinearLayout tv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        initUI();
    }
    private void initUI() {
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_back = findViewById(R.id.tv_back);
        loginBtn = findViewById(R.id.loginBtn);
        editUserName = findViewById(R.id.editUserName);
        editPassword = findViewById(R.id.editPassword);
        tv_back.setVisibility(View.GONE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
    private void login() {
        //获取文本信息
        String usernameStr = editUserName.getText().toString().trim();
        String passworkStr = editPassword.getText().toString().trim();
        if(TextUtils.isEmpty(usernameStr)) {//如果用户名控件内没有元素
            Toast.makeText(LoginActivity.this,"请输入用户名",Toast.LENGTH_LONG).show();
            return;
        }else if(TextUtils.isEmpty(passworkStr)) {//如果密码1控件内没有元素
            Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }else {
            new Thread() {
                @Override
                public void run() {
                    String text = ServiceUtil.login(usernameStr,passworkStr);
                    if("1".equals(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "超时！请检查网络", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else if("2".equals(text)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        JSONObject jsonObject = JSONObject.parseObject(text);
                        if("true".equals(jsonObject.getString("ret"))) {
                            List<UserBean> userList = new ArrayList<>();
                            JsonUtil.getJsonObjectValue(jsonObject,"data","",userList, UserBean.class);
                            UserBean next = userList.iterator().next();
                            String username = next.getUsername();
                            if (null != username) {
                                //储存用户信息、登录状态以及其他信息
                                saveRegisterInfo(username,true,next.getOrderpre());
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                LoginActivity.this.finish();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else if("登录失败,该用户不是经销商".equals(next.getUsername())) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"登录失败,该用户不是经销商",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,"此用户不存在",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }.start();
        }
    }

    //储存一些数据在SharedPreferences
    private void saveRegisterInfo(String userName,boolean start,String orderpre) {
        AnalysisUtils.saveSetting(this,"loginInfo","userName",userName);
        AnalysisUtils.saveSetting(this,"loginInfo","isLogin",start);
        AnalysisUtils.saveSetting(this,"loginInfo","orderpre",orderpre);
    }
}
