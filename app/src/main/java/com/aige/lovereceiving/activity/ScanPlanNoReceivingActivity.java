package com.aige.lovereceiving.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.adapter.PlanNoScanAdapter;
import com.aige.lovereceiving.bean.PlanNoScanBean;
import com.aige.lovereceiving.util.ServiceUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanPlanNoReceivingActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout title_bar;
    private LinearLayout tv_back,find_title_bar;
    private TextView tv_main_title,find_edit;
    private ImageView camera_scan_img;
    private Button find_btn;
    private PlanNoScanAdapter adapter;
    private ListView receiving_list;


    //线程
    ExecutorService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_plan_no_receiving);
        initUI();
    }
    //初始化界面
    private void initUI() {
        title_bar = findViewById(R.id.title_bar);
        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        find_title_bar = findViewById(R.id.find_title_bar);
        find_edit = findViewById(R.id.find_edit);
        camera_scan_img = findViewById(R.id.camera_scan_img);
        find_btn = findViewById(R.id.find_btn);
        receiving_list = findViewById(R.id.receiving_list);

        title_bar.setBackground(getDrawable(R.color.blue));
        tv_back.setBackground(getDrawable(R.drawable.ripple_button_blue));
        tv_back.setOnClickListener(this);
        tv_main_title.setText("批次扫描");
        camera_scan_img.setOnClickListener(this);
        find_btn.setOnClickListener(this);

        service = Executors.newCachedThreadPool();

    }
    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                ScanPlanNoReceivingActivity.this.finish();
                break;
            case R.id.find_btn:
                String edit = find_edit.getText().toString();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        getScanList(edit);
                    }
                });
        }
    }
    private void getScanList(String batchNo) {
        List<PlanNoScanBean> planNoScan = ServiceUtil.getPlanNoScan(batchNo);
        Iterator<PlanNoScanBean> iterator = planNoScan.iterator();
        PlanNoScanBean next = iterator.next();
        int responseCode = next.getResponseCode();
        adapter = new PlanNoScanAdapter(this);
        if(200 == responseCode) {
            adapter.setDate((ArrayList<PlanNoScanBean>) planNoScan);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                receiving_list.setAdapter(adapter);
            }
        });

    }
}