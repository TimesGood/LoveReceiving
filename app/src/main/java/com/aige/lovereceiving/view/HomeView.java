package com.aige.lovereceiving.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.activity.ManualReceivingActivity;
import com.aige.lovereceiving.activity.ScanReceivingActivity;

public class HomeView {
    private View view;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private LinearLayout scanReceiving,manualReceiving;

    public HomeView(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }
    public void initView() {
        view = layoutInflater.inflate(R.layout.homeview,null);
        scanReceiving = view.findViewById(R.id.scanReceiving);
        scanReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到扫描收货界面
                Intent intent = new Intent(activity, ScanReceivingActivity.class);
                activity.startActivity(intent);
            }
        });
        manualReceiving = view.findViewById(R.id.manualReceiving);
        manualReceiving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到手动收货界面
                Intent intent = new Intent(activity, ManualReceivingActivity.class);
                activity.startActivity(intent);
            }
        });
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


}
