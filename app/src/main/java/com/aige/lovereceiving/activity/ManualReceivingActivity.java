package com.aige.lovereceiving.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
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

public class ManualReceivingActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_receiving);
        initUI();
    }
    private void initUI() {
        manual_receiving_list = findViewById(R.id.manual_receiving_list);
        title_bar = findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(Color.parseColor("#30B4FF"));
        tv_back = findViewById(R.id.tv_back);
        tv_back.setBackground(getResources().getDrawable(R.drawable.blue_ripple));
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManualReceivingActivity.this.finish();
            }
        });
        tv_main_title = findViewById(R.id.tv_main_title);
        tv_main_title.setText("手动收货");
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
        scan_btn = findViewById(R.id.scan_btn);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //防止频繁操作
                if (prelongTim==0){//第一次单击时间
                    prelongTim=(new Date()).getTime();
                }else {
                    curTime=(new Date()).getTime();//本地单击的时间
                    if ((curTime-prelongTim)<1000){
                        prelongTim=curTime;
                        Toast.makeText(ManualReceivingActivity.this,"请勿频繁操作",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prelongTim=curTime;
                }
                new Thread() {
                    @Override
                    public void run() {
                        String orderpre = scan_edit.getText()+"";
                        if(orderpre == "") {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ManualReceivingActivity.this,"请输入销售单号",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else if(orderpre.length() != 11) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyDialog.setDialog(ManualReceivingActivity.this,"请输入正确的销售单号","");
                                }
                            });
                        }else if(orderpre.substring(0,3).equals(AnalysisUtils.getValue(ManualReceivingActivity.this,"loginInfo","orderpre"))) {
                            setList(orderpre,"1");
                        }else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyDialog.setDialog(ManualReceivingActivity.this,"销售单号错误","");
                                }
                            });
                        }
                    }
                }.start();
            }
        });
        //清除扫描的记录
        AnalysisUtils.saveSetting(ManualReceivingActivity.this,"loginInfo","SalesOrderId","");
        camera_scan_img = findViewById(R.id.camera_scan_img);
        camera_scan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZXingLibrary.initDisplayOpinion(ManualReceivingActivity.this);
                //判断手机版本
                if (Build.VERSION.SDK_INT > 22) {
                    //判断有没有权限
                    if (ContextCompat.checkSelfPermission(ManualReceivingActivity.this,
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //申请权限
                        ActivityCompat.requestPermissions(ManualReceivingActivity.this,
                                new String[]{android.Manifest.permission.CAMERA}, 1);
                    } else {
                        //有权限直接打开摄像头
                        Intent intent = new Intent(ManualReceivingActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    //系统版本在6.0之下，不需要动态获取权限。
                    Intent intent = new Intent(ManualReceivingActivity.this, CaptureActivity.class);
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
                MyDialog.setDialog(ManualReceivingActivity.this,"请打开摄像机权限","");
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
                    new Thread() {
                        @Override
                        public void run() {
                            //判断是销售单号还是包装码
                            if(result.length() == 11) {
                                setList(result,"0");
                            }else{
                                if("".equals(AnalysisUtils.getValue(ManualReceivingActivity.this,"loginInfo","SalesOrderId"))){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BeeAndVibrateManagerUtil.playShakeAndTone(ManualReceivingActivity.this,1000,R.raw.no);
                                            MyDialog.setDialog(ManualReceivingActivity.this,"请扫描销售单号","");
                                        }
                                    });
                                }else if(!result.substring(0,11).equals(AnalysisUtils.getValue(ManualReceivingActivity.this,"loginInfo","SalesOrderId"))){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BeeAndVibrateManagerUtil.playShakeAndTone(ManualReceivingActivity.this,1000,R.raw.no);
                                            MyDialog.setDialog(ManualReceivingActivity.this,"该包装码不是当前扫描列表","");
                                        }
                                    });
                                }else {
                                    String userName = AnalysisUtils.getValue(ManualReceivingActivity.this, "loginInfo", "userName");
                                    String salesOrderId = AnalysisUtils.getValue(ManualReceivingActivity.this, "loginInfo", "salesOrderId");
                                    List<ScanCodeBean> scanCodeBeans = ServiceUtil.scanPackage(userName, result,salesOrderId);
                                    Iterator<ScanCodeBean> iterator = scanCodeBeans.iterator();
                                    while (iterator.hasNext()) {
                                        ScanCodeBean next = iterator.next();
                                        if("0".equals(next.getStatuss())) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BeeAndVibrateManagerUtil.playShakeAndTone(ManualReceivingActivity.this,1000,R.raw.no);
                                                    MyDialog.setDialog(ManualReceivingActivity.this, "此单已扫，请勿重复扫描", "");
                                                }
                                            });
                                        }else if("1".equals(next.getStatuss())) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BeeAndVibrateManagerUtil.playShakeAndTone(ManualReceivingActivity.this,1000,R.raw.no);
                                                    MyDialog.setDialog(ManualReceivingActivity.this,"扫描超时","请检查网络");
                                                }
                                            });
                                        }else if("2".equals(next.getStatuss())){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BeeAndVibrateManagerUtil.playShakeAndTone(ManualReceivingActivity.this,1000,R.raw.no);
                                                    MyDialog.setDialog(ManualReceivingActivity.this,"未知错误","请联系管理员");
                                                }
                                            });
                                        }else if("3".equals(next.getStatuss())){
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    BeeAndVibrateManagerUtil.playShakeAndTone(ManualReceivingActivity.this,1000,R.raw.no);
                                                    MyDialog.setDialog(ManualReceivingActivity.this,"包装码异常","请检查包装码");
                                                }
                                            });
                                        }else {
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    setList(next.getPackageCode().substring(0,11),"0");
                                                }
                                            }.start();
                                        }
                                    }
                                }

                            }

                        }
                    }.start();

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
                        MyDialog.setDialog(ManualReceivingActivity.this,"销售单号不存在","请检查");
                        loading_layout.setVisibility(View.GONE);
                    }
                });
                break;
            }else if("1".equals(next.getStatus())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyDialog.setDialog(ManualReceivingActivity.this,"超时","请检查网络设置");
                        loading_layout.setVisibility(View.GONE);
                    }
                });
                break;
            }else if("2".equals(next.getStatus())) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyDialog.setDialog(ManualReceivingActivity.this,"未知错误","请联系管理员");
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
                AnalysisUtils.saveSetting(ManualReceivingActivity.this,"loginInfo","SalesOrderId",salesOrderId);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading_layout.setVisibility(View.GONE);
                        manual_receiving_list.setVisibility(View.VISIBLE);
                        adapter.setDate((ArrayList<ReceivingBean>) list);
                        manual_receiving_list.setAnimation(AnimationUtils.loadAnimation(ManualReceivingActivity.this,R.anim.fade_alpha));
                        manual_receiving_list.setAdapter(adapter);
                    }
                });
            }
        }
        if(flag) {
            BeeAndVibrateManagerUtil.playTone(ManualReceivingActivity.this,R.raw.ok);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
