package com.aige.lovereceiving.activity;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.adapter.MyPagerAdapter;
import com.aige.lovereceiving.view.HomeView;
import com.aige.lovereceiving.view.UserView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    //导航栏
    private RelativeLayout title_bar;
    private TextView tv_main_title;
    //底部组件
    private LinearLayout bottom_LLayout,tv_back;
    private RelativeLayout home_RLayout,user_RLayout;
    private TextView home_text,user_text;
    private ImageView home_image,user_image;
    private View home_bottom_view,user_bottom_view;
    private ViewPager vpager_four;
    private ArrayList<View> listViews;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }
    private void initUI() {
        vpager_four = findViewById(R.id.main_body1);
        home_RLayout = findViewById(R.id.home_RLayout);
        user_RLayout = findViewById(R.id.user_RLayout);
        bottom_LLayout = findViewById(R.id.bottom_LLayout);
        title_bar = findViewById(R.id.title_bar);
        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        home_bottom_view = findViewById(R.id.home_bottom_view);
        home_image = findViewById(R.id.home_image);
        home_text = findViewById(R.id.home_text);
        user_bottom_view = findViewById(R.id.user_bottom_view);
        user_image = findViewById(R.id.user_image);
        //注册底部组件监听事件
        for(int i=0;i<bottom_LLayout.getChildCount();i++) {
            bottom_LLayout.getChildAt(i).setOnClickListener(this);
        }
        //初始界面状态
        title_bar.setBackground(getDrawable(R.color.blue));
        tv_back.setVisibility(View.GONE);
        tv_main_title.setText("首页");
        home_text.setTextColor(Color.parseColor("#30B4FF"));
        home_bottom_view.setBackgroundColor(Color.parseColor("#30B4FF"));
        home_image.setImageDrawable(getDrawable((R.drawable.home_image_on)));
        //载入页面
        HomeView homeView = new HomeView(this);
        UserView userView = new UserView(this);
        listViews = new ArrayList<View>();
        listViews.add(homeView.getView());
        listViews.add(userView.getView());
        vpager_four.setAdapter(new MyPagerAdapter(listViews));
        vpager_four.setCurrentItem(0);          //设置ViewPager当前页，从0开始算
        vpager_four.addOnPageChangeListener(this);
    }
    //底部按钮点击事件集
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_RLayout:
                vpager_four.setCurrentItem(0);
                break;
            case R.id.user_RLayout:
                vpager_four.setCurrentItem(1);
                break;
        }
    }
    //清除底部所有组件的状态
    private void clearStatus() {
        home_text.setTextColor(Color.parseColor("#000000"));
        home_bottom_view.setBackgroundColor(Color.parseColor("#ffffff"));
        home_image.setImageDrawable(getResources().getDrawable((R.drawable.home_image)));
        user_text = findViewById(R.id.user_text);
        user_text.setTextColor(Color.parseColor("#000000"));
        user_bottom_view.setBackgroundColor(Color.parseColor("#ffffff"));
        user_image.setImageDrawable(getResources().getDrawable((R.drawable.user_image)));
    }
    //双击返回手机返回键，关闭软件
    protected long exitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this,"再按一次退出爱收货",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }
    @Override
    public void onPageSelected(int position) {
        clearStatus();
        switch (position) {
            case 0:

                tv_main_title.setText("首页");
                home_text.setTextColor(Color.parseColor("#30B4FF"));
                home_bottom_view.setBackgroundColor(Color.parseColor("#30B4FF"));
                home_image.setImageDrawable(getResources().getDrawable((R.drawable.home_image_on)));
                break;
            case 1:
                tv_main_title.setText("个人中心");
                user_text.setTextColor(Color.parseColor("#30B4FF"));
                user_bottom_view.setBackgroundColor(Color.parseColor("#30B4FF"));
                user_image.setImageDrawable(getResources().getDrawable((R.drawable.user_image_on)));
                break;
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
