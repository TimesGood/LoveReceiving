package com.aige.lovereceiving.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.aige.lovereceiving.R;
import com.aige.lovereceiving.util.AnalysisUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private TextView tv_version;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
    }
    private void initUI() {
        tv_version = findViewById(R.id.tv_version);
        //try {
        //    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);
        //    tv_version.setText("V"+info.versionName);
        //} catch (PackageManager.NameNotFoundException e) {
        //    e.printStackTrace();
        //    tv_version.setText("V");//获取不到程序包信息时，直接显示V
        //}
        Timer timer = new Timer();//延迟类
        //延迟目标
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(AnalysisUtils.getBoolean(SplashActivity.this,"loginInfo","isLogin")) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

            }
        };
        timer.schedule(task,3000);//task延迟3秒

    }
}
