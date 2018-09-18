package com.hx.lib_hx.custom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hx.lib_hx.R;


/**
 * 调用该类需要注意内存泄漏
 * 选择页面
 */
public class BottomDialog extends Dialog{
    private AppCompatActivity mContext;
    private FrameLayout mContainer;
    private OnItemClickListener mListener;
    private LinearLayout mBtnLayout;
    private static volatile BottomDialog sBottomDialog;
    private boolean mIsInput;
    public static BottomDialog newInstance(AppCompatActivity context, boolean isInput) {
        if (sBottomDialog == null) {
            synchronized (BottomDialog.class) {
                if (sBottomDialog == null) {
                    sBottomDialog = new BottomDialog(context,isInput);
                }
            }
        }
        return sBottomDialog;
    }

    private BottomDialog(AppCompatActivity context, boolean isInput) {
        super(context, R.style.dialog);
        this.mContext = context;
        mIsInput = isInput;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_bottom_select);
        initView();
        setCanceledOnTouchOutside(false);
        //设置窗口弹出动画
        getWindow().setWindowAnimations(R.style.dialogWindowAnimBottom);
        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm =mContext.getWindowManager();

        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        getWindow().setAttributes(lp);

    }

    /**
     * 确定 取消
     */
    private void initView() {
        findViewById(R.id.tv_cancel).setOnClickListener(v -> dismiss());
        mContainer = findViewById(R.id.container);
        View lineView = findViewById(R.id.line);
        findViewById(R.id.tv_upload).setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(BottomDialog.this,true);
            }
        });
        mBtnLayout = findViewById(R.id.btn_layout);
        if (mIsInput) {
            lineView.setVisibility(View.GONE);
            mBtnLayout.setVisibility(View.GONE);
        }else {
            lineView.setVisibility(View.VISIBLE);
            mBtnLayout.setVisibility(View.VISIBLE);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setView(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }else {
            mContainer.removeAllViews();
        }
        mContainer.addView(view);
//        Log.i("hx-dialog数量：", mContainer.getChildCount()+"");
    }

    public static void onDestroy() {
        sBottomDialog = null;
    }

    public interface OnItemClickListener {
        void onClick(Dialog dialog, boolean isConfirm);
    }
}
