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

public class ReceivingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ReceivingBean> list;
    public ReceivingAdapter() {

    }
    public ReceivingAdapter(Context context) {
        this.context = context;

    }
    public void setDate(ArrayList<ReceivingBean> list) {
        this.list = (ArrayList<ReceivingBean>) list.clone();
        notifyDataSetChanged();//在更改数据之后自动更新
    }
    //取得列数
    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }
    //获取列
    @Override
    public ReceivingBean getItem(int position) {
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
        final ReceivingBean bean = getItem(position);
        //设置该列的值
        if(bean != null){
            vh.orderId_text.setText(bean.getOrderId());
            vh.detailName_text.setText(bean.getDetailName());
            String packageCode = bean.getPackageCode();
            vh.package_text.setText(packageCode.substring(packageCode.length()-5));
            if(bean.getReceivingDate() == null && "1".equals(bean.getScanType())) {
                vh.receivingDate_text.setVisibility(View.GONE);
                Button button = new Button(context);
                button.setText(bean.getScanStatus());
                button.setTextColor(Color.parseColor("#ffffff"));
                button.setBackground(convertView.getResources().getDrawable(R.drawable.ripple_button_yellow));
                //点击收货事件
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getStaticCount() >= 3) {
                            Toast.makeText(context,"只允许同时收货三个包装",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        bean.setScanStatus("收货中...");
                        notifyDataSetChanged();
                        new Thread() {
                            @Override
                            public void run() {
                                System.out.println(bean.getPackageCode());
                                if(bean.getOrderId().substring(0,11).equals(bean.getPackageCode().substring(0,11))) {
                                    String userName = AnalysisUtils.getValue(context, "loginInfo", "userName");
                                    String salesOrderId = AnalysisUtils.getValue(context, "loginInfo", "salesOrderId");
                                    List<ScanCodeBean> scanCodeBeans = ServiceUtil.scanPackage(userName, bean.getPackageCode(),salesOrderId);
                                    Iterator<ScanCodeBean> iterator = scanCodeBeans.iterator();
                                    while (iterator.hasNext()) {
                                        ScanCodeBean next = iterator.next();
                                        if("on".equals(next.getStatuss())) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context, "包装"+packageCode+"收货成功", Toast.LENGTH_SHORT).show();
                                                    Date date = new Date();
                                                    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                                    String format = format1.format(date);
                                                    button.setVisibility(View.GONE);
                                                    vh.receivingDate_text.setText(format);
                                                    vh.receivingDate_text.setVisibility(View.VISIBLE);
                                                    bean.setReceivingDate(format);
                                                    bean.setScanStatus("确认收货");
                                                    BeeAndVibrateManagerUtil.playTone(context,R.raw.ok);
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }else if("off".equals(next.getStatuss())) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MyDialog.setDialog(context,"订单状态已收货，勿重复扫描","请检查");
                                                }
                                            });
                                        }else if("1".equals(next.getStatuss())) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MyDialog.setDialog(context,"收货超时","请确认包装码"+packageCode);
                                                    bean.setScanStatus("重新收货");
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }else if("2".equals(next.getStatuss())) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    MyDialog.setDialog(context,"系统异常","");
                                                    bean.setScanStatus("重新收货");
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }else{
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            MyDialog.setDialog(context,"包装码和销售单不一致","请检查");
                                            bean.setScanStatus("重新收货");
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            }
                        }.start();
                    }
                });
                vh.receivingDate_layout.addView(button);
            }else{
                if(bean.getReceivingDate() != null) {
                    vh.receivingDate_text.setText(bean.getReceivingDate());
                }else{
                    vh.receivingDate_text.setText("未扫描收货");
                }

            }
        }
        return convertView;
    }
    static class ViewHolder {
        public TextView orderId_text,detailName_text,package_text,receivingDate_text;
        public LinearLayout receivingDate_layout;
    }
    public int getStaticCount() {
        int i = 0;
        Iterator<ReceivingBean> iterator = list.iterator();
        while(iterator.hasNext()) {
            ReceivingBean next = iterator.next();
            if("收货中...".equals(next.getScanStatus())) {
                i++;
            }
        }
        return i;
    }
}
