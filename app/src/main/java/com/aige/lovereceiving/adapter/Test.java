//package com.aige.lovereceiving.activity;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.aige.lovereceiving.R;
//import com.aige.lovereceiving.view.HomeView;
//import com.aige.lovereceiving.view.UserView;
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener{
//    //导航栏
//    private RelativeLayout title_bar;
//    private TextView tv_main_title;
//    //中间内容
//    private LinearLayout main_body;
//    //底部组件
//    private LinearLayout bottom_LLayout,tv_back;
//    private RelativeLayout home_RLayout,user_RLayout;
//    private TextView home_text,user_text;
//    private ImageView home_image,user_image;
//    //View界面对象
//    private HomeView homeView;
//    private UserView userView;
//    private View home_bottom_view,user_bottom_view;
//    @Override
//    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        initUI();
//    }
//    private void initUI() {
//        home_RLayout = findViewById(R.id.home_RLayout);
//        user_RLayout = findViewById(R.id.user_RLayout);
//        bottom_LLayout = findViewById(R.id.bottom_LLayout);
//        //注册底部组件监听事件
//        for(int i=0;i<bottom_LLayout.getChildCount();i++) {
//            bottom_LLayout.getChildAt(i).setOnClickListener(this);
//        }
//        title_bar = findViewById(R.id.title_bar);
//        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
//        tv_back = findViewById(R.id.tv_back);
//        tv_back.setVisibility(View.GONE);
//        tv_main_title = findViewById(R.id.tv_main_title);
//        home_bottom_view = findViewById(R.id.home_bottom_view);
//        home_image = findViewById(R.id.home_image);
//        user_bottom_view = findViewById(R.id.user_bottom_view);
//        user_image = findViewById(R.id.user_image);
//
//        createView(0);
//    }
//    //底部按钮点击事件集
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.home_RLayout:
//                createView(0);
//                break;
//            case R.id.user_RLayout:
//                createView(1);
//                break;
//        }
//    }
//    //隐藏当前页面所有组件
//    private void removeAllView() {
//        main_body = findViewById(R.id.main_body);
//        for(int i=0;i<main_body.getChildCount();i++) {
//            main_body.getChildAt(i).setVisibility(View.GONE);
//        }
//    }
//    //清除底部所有组件的状态
//    private void clearStatus() {
//        home_text = findViewById(R.id.home_text);
//        home_text.setTextColor(Color.parseColor("#000000"));
//        home_bottom_view.setBackgroundColor(Color.parseColor("#ffffff"));
//        home_image.setImageDrawable(getResources().getDrawable((R.drawable.home_image)));
//        user_text = findViewById(R.id.user_text);
//        user_text.setTextColor(Color.parseColor("#000000"));
//        user_bottom_view.setBackgroundColor(Color.parseColor("#ffffff"));
//        user_image.setImageDrawable(getResources().getDrawable((R.drawable.user_image)));
//    }
//    //设置底部组件状态
//    private void setStatus(int status) {
//        clearStatus();
//        switch (status) {
//            case 0:
//                tv_main_title.setText("首页");
//                home_text.setTextColor(Color.parseColor("#30B4FF"));
//                home_bottom_view.setBackgroundColor(Color.parseColor("#30B4FF"));
//                home_image.setImageDrawable(getResources().getDrawable((R.drawable.home_image_on)));
//                break;
//            case 1:
//                tv_main_title.setText("个人中心");
//                user_text.setTextColor(Color.parseColor("#30B4FF"));
//                user_bottom_view.setBackgroundColor(Color.parseColor("#30B4FF"));
//                user_image.setImageDrawable(getResources().getDrawable((R.drawable.user_image_on)));
//        }
//    }
//    //界面切换
//    private void createView(int index) {
//        switch (index) {
//            //首页
//            case 0 :
//                removeAllView();
//                setStatus(0);
//                if (homeView == null) {
//                    homeView = new HomeView(this);
//                    main_body.addView(homeView.getView());
//                }else {
//                    homeView.getView();
//                }
//                homeView.showView();
//                break;
//            //个人中心
//            case 1 :
//                removeAllView();
//                setStatus(1);
//                if (userView == null) {
//
//                    userView = new UserView(this);
//                    main_body.addView(userView.getView());
//                }else {
//                    userView.getView();
//                }
//                userView.showView();
//                break;
//            default:
//                break;
//        }
//    }
//    //双击返回手机返回键，关闭软件
//    protected long exitTime;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if((System.currentTimeMillis() - exitTime) > 2000) {
//                Toast.makeText(MainActivity.this,"再按一次退出爱收货",Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            }else {
//                MainActivity.this.finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode,event);
//    }
//
//}
