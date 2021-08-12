package com.aige.lovereceiving.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.activity.LoginActivity;
import com.aige.lovereceiving.activity.MyLoadingActivity;
import com.aige.lovereceiving.activity.TestActivity;
import com.aige.lovereceiving.util.AnalysisUtils;

public class UserView implements View.OnClickListener{
    private View view;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private ImageView image_user;
    private RelativeLayout logout;
    private LinearLayout user_list_LLayout;
    private TextView textUser_text,dealer_text,orderId_prefix_text;

    public UserView(Activity activity) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
    }
    public void initView() {
        view = layoutInflater.inflate(R.layout.userview,null);
        image_user = view.findViewById(R.id.image_user);
        image_user.setOnClickListener(this);
        logout = view.findViewById(R.id.logout);
        //注册监听
        user_list_LLayout = view.findViewById(R.id.user_list_LLayout);
        for(int i = 0;i < user_list_LLayout.getChildCount();i++) {
            user_list_LLayout.getChildAt(i).setOnClickListener(this);
        }
        textUser_text = view.findViewById(R.id.textUser_text);
        textUser_text.setText(AnalysisUtils.getValue(activity,"loginInfo","userName"));
        orderId_prefix_text = view.findViewById(R.id.orderId_prefix_text);
        orderId_prefix_text.setText(AnalysisUtils.getValue(activity,"loginInfo","orderpre"));
    }
    //取得VIew界面
    public View getView() {
        if(view == null) {
            initView();
        }
        return view;
    }
    //显示View界面
    public void showView() {
        if(view == null) {
            view = layoutInflater.inflate(R.layout.userview,null);
        }
        view.setVisibility(View.VISIBLE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_user:
                //跳转到登录界面
                if(AnalysisUtils.getBoolean(activity,"loginInfo","isLogin")) {
                    Toast.makeText(activity,"您已登录，如需更换请退出当前账号",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
                break;
            case R.id.logout:
                //退出登录
                activity.finish();
                AnalysisUtils.saveSetting(activity,"loginInfo","isLogin",false);
                Intent intent1 = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent1);
                break;
            case R.id.layout_text:
                Intent intent = new Intent(activity, MyLoadingActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}
