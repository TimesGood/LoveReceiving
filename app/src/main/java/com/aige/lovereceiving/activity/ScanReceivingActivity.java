package com.aige.lovereceiving.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.adapter.ReceivingAdapter;
import com.aige.lovereceiving.bean.PackageBean;
import com.aige.lovereceiving.bean.ReceivingBean;
import com.aige.lovereceiving.bean.ScanCodeBean;
import com.aige.lovereceiving.mydailog.MyDialog;
import com.aige.lovereceiving.mydailog.MyLoading;
import com.aige.lovereceiving.util.AnalysisUtils;
import com.aige.lovereceiving.util.BeeAndVibrateManagerUtil;
import com.aige.lovereceiving.util.ServiceUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanReceivingActivity extends AppCompatActivity {
    //导航栏
    private RelativeLayout title_bar,loading_layout;
    private TextView tv_main_title;
    private EditText scan_edit;
    private ReceivingAdapter adapter;
    private List<ReceivingBean> list;
    private Button scan_btn;
    private ListView manual_receiving_list;
    private ImageView camera_scan_img;
    private LinearLayout tv_back;
    private MyLoading myLoading;
    //限制按钮点击间隔
    private long prelongTim = 0;
    private long curTime = 0;
    //线程
    ExecutorService service;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_receiving);
        initUI();
    }
    private void initUI() {
        manual_receiving_list = findViewById(R.id.manual_receiving_list);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setBackground(getDrawable(R.color.blue));
        tv_back = findViewById(R.id.tv_back);
        tv_back.setBackground(getDrawable(R.drawable.ripple_button_blue));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanReceivingActivity.this.finish();
            }
        });
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("扫描收货");
        scan_edit = findViewById(R.id.scan_edit);
        scan_edit.requestFocus();//获得焦点
        scan_edit.setSelection(scan_edit.length());//光标置尾
        scan_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH) {
                    //这里是监听扫码枪的回车事件
                    scan_btn.performClick();
                    return true;
                }
                if(event.getKeyCode()==KeyEvent.KEYCODE_ENTER&&v.getText()!=null&& event.getAction() == KeyEvent.ACTION_DOWN){
                    //这里是监听手机的回车事件
                    scan_btn.performClick();
                }
                return true;
            }
        });
        service = Executors.newCachedThreadPool();
        scan_btn = findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收起软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(scan_edit.getWindowToken(), 0);
                //防止频繁操作
                if (prelongTim==0){//第一次单击时间
                    prelongTim=(new Date()).getTime();
                }else {
                    curTime=(new Date()).getTime();//本地单击的时间
                    if ((curTime-prelongTim)<1000){
                        prelongTim=curTime;
                        Toast.makeText(ScanReceivingActivity.this,"请勿频繁操作",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prelongTim=curTime;
                }
                String orderpre = scan_edit.getText().toString();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        scan(orderpre);
                    }
                });
            }
        });
        //清除扫描的记录
        AnalysisUtils.saveSetting(ScanReceivingActivity.this,"loginInfo","SalesOrderId","");
        camera_scan_img = findViewById(R.id.camera_scan_img);
        camera_scan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZXingLibrary.initDisplayOpinion(ScanReceivingActivity.this);
                //判断手机版本
                if (Build.VERSION.SDK_INT > 22) {
                    //判断有没有权限
                    if (ContextCompat.checkSelfPermission(ScanReceivingActivity.this,
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限
                        ActivityCompat.requestPermissions(ScanReceivingActivity.this,
                                new String[]{android.Manifest.permission.CAMERA}, 1);
                    } else {
                        //有权限直接打开摄像头
                        Intent intent = new Intent(ScanReceivingActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    //系统版本在6.0之下，不需要动态获取权限。
                    Intent intent = new Intent(ScanReceivingActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
        loading_layout = findViewById(R.id.loading_layout);
    }
    //处理权限获取结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }else {
                MyDialog.setDialog(ScanReceivingActivity.this,"请打开摄像机权限","");
            }
        }
    }
    //处理扫描结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果
        if(requestCode == 1) {
            if(data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    //获取到扫描的结果
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            scan(result);
                        }
                    });

                }
            }
        }
    }
    //通过销售单号获取所有收货列表
    private void setList(String salesOrderId,String scanType) {
        //加载动画
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manual_receiving_list.setVisibility(View.GONE);
                loading_layout.setVisibility(View.VISIBLE);
            }
        });
        list = new ArrayList<>();
        adapter = new ReceivingAdapter(this);
        List<PackageBean> packageList = ServiceUtil.getPackageList(salesOrderId, AnalysisUtils.getValue(this, "loginInfo", "userName"));
        Iterator<PackageBean> iterator = packageList.iterator();
        boolean flag = false;
        while (iterator.hasNext()) {
            PackageBean next = iterator.next();
            if("0".equals(next.getStatus())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyDialog.setDialog(ScanReceivingActivity.this,"销售单号不存在","请检查");
                        loading_layout.setVisibility(View.GONE);
                    }
                });
                break;
            }else if("1".equals(next.getStatus())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyDialog.setDialog(ScanReceivingActivity.this,"超时","请检查网络设置");
                        loading_layout.setVisibility(View.GONE);
                    }
                });
                break;
            }else if("2".equals(next.getStatus())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyDialog.setDialog(ScanReceivingActivity.this,"未知错误","请联系管理员");
                        loading_layout.setVisibility(View.GONE);
                    }
                });
                break;
            }else{
                flag = true;
                ReceivingBean bean = new ReceivingBean();
                bean.setOrderId(next.getOrderid());
                bean.setDetailName(next.getAuditname());
                bean.setPackageCode(next.getPackageCode());
                bean.setReceivingDate(next.getDeliverydate());
                bean.setScanType(scanType);
                list.add(bean);
                //扫描成功储存销售单号
                AnalysisUtils.saveSetting(ScanReceivingActivity.this,"loginInfo","SalesOrderId",salesOrderId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_layout.setVisibility(View.GONE);
                        manual_receiving_list.setVisibility(View.VISIBLE);
                        adapter.setDate((ArrayList<ReceivingBean>) list);
                        manual_receiving_list.setAnimation(AnimationUtils.loadAnimation(ScanReceivingActivity.this,R.anim.fade_alpha));
                        manual_receiving_list.setAdapter(adapter);
                    }
                });
            }
        }
        if(flag) {
            BeeAndVibrateManagerUtil.playTone(ScanReceivingActivity.this,R.raw.ok);
        }
    }
    //扫描
    private void scan(String result) {
        //判断是销售单号还是包装码
        if(result.length() == 11) {
            setList(result,"0");
        }else{
            String userName = AnalysisUtils.getValue(ScanReceivingActivity.this, "loginInfo", "userName");
            String salesOrderId = AnalysisUtils.getValue(ScanReceivingActivity.this, "loginInfo", "SalesOrderId");
            if("".equals(salesOrderId)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.no);
                        Toast.makeText(ScanReceivingActivity.this,"请扫描销售单号",Toast.LENGTH_SHORT).show();
                    }
                });
            }else if(!result.substring(0,11).equals(salesOrderId)){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.no);
                        MyDialog.setDialog(ScanReceivingActivity.this,"该包装码不在当前扫描列表","请先扫描该包装码对应的销售单号");
                    }
                });
            }else {
                List<ScanCodeBean> scanCodeBeans = ServiceUtil.scanPackage(userName, result,salesOrderId);
                Iterator<ScanCodeBean> iterator = scanCodeBeans.iterator();
                while (iterator.hasNext()) {
                    ScanCodeBean next = iterator.next();
                    String value = AnalysisUtils.getValue(ScanReceivingActivity.this, "loginInfo", "SalesOrderId");
                    if("off".equals(next.getStatuss())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.no);
                                Toast.makeText(ScanReceivingActivity.this,"此单已扫，请勿重复扫描！",Toast.LENGTH_SHORT).show();
                            }
                        });
                        setList(value,"0");
                    }else if("1".equals(next.getStatuss())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.no);
                                Toast.makeText(ScanReceivingActivity.this,"扫描超时，请检查网络连接",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if("2".equals(next.getStatuss())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.no);
                                MyDialog.setDialog(ScanReceivingActivity.this,"扫描失败","未知错误，请联系管理员");
                            }
                        });
                    }else if("3".equals(next.getStatuss())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.no);
                                Toast.makeText(ScanReceivingActivity.this,"扫描失败，包装码异常，请检查包装码",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                BeeAndVibrateManagerUtil.playShakeAndTone(ScanReceivingActivity.this,1000,R.raw.ok);
                                Toast.makeText(ScanReceivingActivity.this,"扫描成功",Toast.LENGTH_SHORT).show();

                            }
                        });
                        setList(value,"0");
                    }
                }
            }

        }
    }

}
