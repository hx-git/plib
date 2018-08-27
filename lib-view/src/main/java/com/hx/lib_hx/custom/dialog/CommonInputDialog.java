package com.hx.lib_hx.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hx.lib_hx.R;


public class CommonInputDialog extends Dialog implements View.OnClickListener{
    private EditText contentTxt;
    private TextView titleTxt;
    private TextView submitTxt;
    private TextView cancelTxt;

    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public CommonInputDialog(Context context, String content, OnCloseListener listener) {
        super(context, R.style.dialog);
        this.content = content;
        this.listener = listener;

    }

    public CommonInputDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CommonInputDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CommonInputDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        //位置
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.gravity = Gravity.CENTER;
//        attributes.verticalMargin = 0.5f;// dialog上边框距 顶部50%
        getWindow().setAttributes(attributes);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        contentTxt = findViewById(R.id.content);
        titleTxt = findViewById(R.id.title);
        submitTxt = findViewById(R.id.submit);
        submitTxt.setOnClickListener(this);
        cancelTxt = findViewById(R.id.cancel);
        cancelTxt.setOnClickListener(this);

        contentTxt.setHint(content);
        if(!TextUtils.isEmpty(positiveName)){
            submitTxt.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancelTxt.setText(negativeName);
        }

        if(!TextUtils.isEmpty(title)){
            titleTxt.setText(title);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            if(listener != null){
                listener.onClick(this, false,content);
            }
        } else if (v.getId() == R.id.submit) {
            if(listener != null){
                listener.onClick(this, true,contentTxt.getText().toString().trim());
            }
        }
        this.dismiss();
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm, String content);
    }
}
