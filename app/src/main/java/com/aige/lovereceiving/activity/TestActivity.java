package com.aige.lovereceiving.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.adapter.MyAdapter;
import com.aige.lovereceiving.bean.TestListBean;
import com.aige.lovereceiving.myanimated.MinSoftLoadingView;
import com.aige.lovereceiving.myanimated.MyAnimated;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    private LinearLayout test_view;
    private ListView listview_test;
    private List<TestListBean> beans;
    private MyAdapter<TestListBean> myAdapter1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listview_test = findViewById(R.id.listview_test);
        beans = new ArrayList<TestListBean>();
        beans.add(new TestListBean("条目1","百度"));
        beans.add(new TestListBean("条目2","豆瓣"));
        beans.add(new TestListBean("条目3","支付宝"));
        //Adapter初始化
        myAdapter1 = new MyAdapter<TestListBean>((ArrayList)beans,R.layout.text_view) {
            @Override
            public void bindView(ViewHolder holder, TestListBean obj) {
                holder.setText(R.id.list_name,obj.getItem1());
                holder.setText(R.id.list_phone,obj.getItem2());
            }
        };
        myAdapter1.add(1,new TestListBean("33","ddd"));
        listview_test.setAdapter(myAdapter1);

    }

}