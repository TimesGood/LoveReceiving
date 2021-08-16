package com.aige.lovereceiving.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.bean.PlanNoReceivingBean;
import com.aige.lovereceiving.bean.PlanNoScanBean;
import com.aige.lovereceiving.bean.ReceivingBean;
import com.aige.lovereceiving.bean.ScanCodeBean;
import com.aige.lovereceiving.mydailog.MyDialog;
import com.aige.lovereceiving.util.AnalysisUtils;
import com.aige.lovereceiving.util.BeeAndVibrateManagerUtil;
import com.aige.lovereceiving.util.ServiceUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlanNoScanAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlanNoScanBean> list;
    public PlanNoScanAdapter() {

    }
    public PlanNoScanAdapter(Context context) {
        this.context = context;

    }
    public void setDate(ArrayList<PlanNoScanBean> list) {
        this.list = (ArrayList<PlanNoScanBean>) list.clone();
        notifyDataSetChanged();//在更改数据之后自动更新
    }
    //取得列数
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }
    //获取列
    @Override
    public PlanNoScanBean getItem(int position) {
        return list == null ? null : list.get(position);
    }
    //获取列id
    @Override
    public long getItemId(int position) {
        return position;
    }
    //position：列Id，convertView：列View，
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //为避免ListView出现复用情况，创建集合储存数据
        Map mHashMap = new HashMap();
        final ViewHolder vh;
        if (mHashMap.get(position) == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.receiving_item,parent,false);
            vh.orderId_text = convertView.findViewById(R.id.orderId_text);
            vh.detailName_text = convertView.findViewById(R.id.detailName_text);
            vh.package_text = convertView.findViewById(R.id.package_text);
            vh.receivingDate_text = convertView.findViewById(R.id.receivingDate_text);
            vh.receivingDate_layout = convertView.findViewById(R.id.receivingDate_layout);
            convertView.setTag(vh);//把自定义的View设置进ListView的View中
        } else {
            //当该列数据已经存在于HashMap中，取HashMap中的数据赋予convertView
            convertView = (View) mHashMap.get(position);
            vh = (ViewHolder) convertView.getTag();
        }
        //获取列
        final PlanNoScanBean bean = getItem(position);
        //设置该列的值
        if(bean != null){
            vh.orderId_text.setText(bean.getPackageCode().substring(0,13));
            vh.detailName_text.setText(bean.getSolutionName());
            String packageCode = bean.getPackageCode();
            vh.package_text.setText(packageCode.substring(packageCode.length()-5));
            vh.receivingDate_text.setText(bean.getDeliveryDate());

        }
        return convertView;
    }
    static class ViewHolder {
        public TextView orderId_text,detailName_text,package_text,receivingDate_text;
        public LinearLayout receivingDate_layout;
    }

}
