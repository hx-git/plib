package com.hx.lib_hx.custom.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hx.lib_hx.R;

/**
 * 调用该类需要注意内存泄漏
 * 选择页面
 */
public class NormalDialog extends Dialog{


    private Activity mContext;
    private FrameLayout mContainer;
    private OnItemClickListener mListener;
    private TextView mTitle;
    private static volatile NormalDialog sBottomSelectDialog;
    public static NormalDialog newInstance(Activity context) {
        if (sBottomSelectDialog == null) {
            synchronized (NormalDialog.class) {
                if (sBottomSelectDialog == null) {
                    sBottomSelectDialog = new NormalDialog(context);
                }
            }
        }
        return sBottomSelectDialog;
    }

    private NormalDialog(Activity context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_normal);
        initView();
        setCanceledOnTouchOutside(false);
        getWindow().setWindowAnimations(R.style.dialogWindowAnimBottom); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm =mContext.getWindowManager();

        lp.width = (int) (wm.getDefaultDisplay().getWidth()*0.7); //设置宽度
        getWindow().setAttributes(lp);

    }

    private void initView() {
        mTitle = findViewById(R.id.title);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mContainer = findViewById(R.id.container);

        findViewById(R.id.tv_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(NormalDialog.this,true);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        mContainer.removeAllViews();
        mContainer.addView(view);
    }

    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    public static void onDestroy() {
        sBottomSelectDialog = null;
    }

    public interface OnItemClickListener {
        void onClick(Dialog dialog, boolean isConfirm);
    }
}
