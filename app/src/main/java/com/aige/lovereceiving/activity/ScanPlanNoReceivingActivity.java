package com.aige.lovereceiving.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.lovereceiving.R;

public class ScanPlanNoReceivingActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout title_bar;
    private LinearLayout tv_back;
    private TextView tv_main_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_plan_no_receiving);
        initUI();
    }
    //初始化界面
    private void initUI() {
        title_bar = findViewById(R.id.title_bar);
        title_bar.setBackground(getDrawable(R.color.blue));
        tv_back = findViewById(R.id.tv_back);
        tv_back.setBackground(getDrawable(R.drawable.ripple_button_blue));
        tv_back.setOnClickListener(this);
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("批次扫描");

    }
    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                ScanPlanNoReceivingActivity.this.finish();
        }

    }
}