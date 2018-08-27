package com.hx.lib_hx.custom.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.hx.lib_hx.R;

/**
 * 调用该类需要注意内存泄漏
 * 选择页面
 */
public class BottomSelectDialog extends Dialog{


    private Activity mContext;
    private FrameLayout mContainer;
    private OnItemClickListener mListener;
    private static volatile BottomSelectDialog sBottomSelectDialog;
    public static BottomSelectDialog newInstance(Activity context) {
        if (sBottomSelectDialog == null) {
            synchronized (BottomSelectDialog.class) {
                if (sBottomSelectDialog == null) {
                    sBottomSelectDialog = new BottomSelectDialog(context);
                }
            }
        }
        return sBottomSelectDialog;
    }

    private BottomSelectDialog(Activity context) {
        super(context, R.style.dialog);
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_select);
        initView();
        setCanceledOnTouchOutside(false);
        getWindow().setWindowAnimations(R.style.dialogWindowAnimBottom); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm =mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);

    }

    private void initView() {
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
                    mListener.onClick(BottomSelectDialog.this,true);
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
        mContainer.addView(view);
    }

    public static void onDestroy() {
        sBottomSelectDialog = null;
    }

    public interface OnItemClickListener {
        void onClick(Dialog dialog, boolean isConfirm);
    }
}
