package com.aige.lovereceiving.mydailog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aige.lovereceiving.R;
import com.aige.lovereceiving.util.ScreenUtils;


public class MyDialog{
    public static void setDialog(Context context,String titleStr,String contentStr) {
        //获取layout对象
        View view = LayoutInflater.from(context).inflate(R.layout.mydialog,null,false);
        final AlertDialog dialog = new AlertDialog.Builder(context).setView(view).create();
        Button left_btn = view.findViewById(R.id.left_btn);
        Button right_btn = view.findViewById(R.id.right_btn);
        TextView title = view .findViewById(R.id.title);
        TextView title_content = view.findViewById(R.id.title_content);
        title.setText(titleStr);
        title_content.setText(contentStr);
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消事件
                dialog.dismiss();
            }
        });
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定事件
                dialog.dismiss();
            }
        });
        dialog.show();
        //此处设置位置窗体大小，我这里设置为手机屏幕宽度的3/4  注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(context)/4*3), LinearLayout.LayoutParams.WRAP_CONTENT);
    }

}