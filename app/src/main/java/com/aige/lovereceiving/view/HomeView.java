package com.aige.lovereceiving.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.activity.ManualReceivingActivity;
import com.aige.lovereceiving.activity.ScanPlanNoReceivingActivity;
import com.aige.lovereceiving.activity.ScanReceivingActivity;

public class HomeView implements View.OnClickListener{
    private View view;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private LinearLayout scanReceiving,manualReceiving,scan_plan;


    public HomeView(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }
    public void initView() {
        view = layoutInflater.inflate(R.layout.homeview,null);
        scanReceiving = view.findViewById(R.id.scanReceiving);
        scanReceiving.setOnClickListener(this);

        manualReceiving = view.findViewById(R.id.manualReceiving);
        manualReceiving.setOnClickListener(this);

        scan_plan = view.findViewById(R.id.scan_plan);
        scan_plan.setOnClickListener(this);


    }

    public View getView() {
        if(view == null) {
            initView();
        }
        return view;
    }
    public void showView() {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.homeview,null);
        }
        view.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.scanReceiving:
                //跳转到扫描收货界面
                intent = new Intent(activity, ScanReceivingActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.manualReceiving:
                //跳转到扫描收货界面
                intent = new Intent(activity, ManualReceivingActivity.class);
                activity.startActivity(intent);
                break;
            case R.id.scan_plan:
                //跳转到批次扫描界面
                intent = new Intent(activity, ScanPlanNoReceivingActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}
