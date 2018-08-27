package com.hx.lib_hx.custom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
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
public class TopDialog extends Dialog{


    private AppCompatActivity mContext;
    private FrameLayout mContainer;
    private OnItemClickListener mListener;
    private LinearLayout mBtnLayout;
    private boolean mIsInput;
    private static volatile TopDialog sBottomSelectDialog;
    public static TopDialog newInstance(AppCompatActivity context, boolean isInput) {
        if (sBottomSelectDialog == null) {
            synchronized (TopDialog.class) {
                if (sBottomSelectDialog == null) {
                    sBottomSelectDialog = new TopDialog(context,isInput);
                }
            }
        }
        return sBottomSelectDialog;
    }

    private TopDialog(AppCompatActivity context, boolean isInput) {
        super(context, R.style.dialog);
        this.mContext = context;
        this.mIsInput = isInput;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_top_select);
        initView();
        setCanceledOnTouchOutside(false);
        getWindow().setWindowAnimations(R.style.dialogWindowAnimTop); //设置窗口弹出动画

        //全屏处理
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        WindowManager wm =mContext.getWindowManager();
        lp.width = wm.getDefaultDisplay().getWidth(); //设置宽度
        //顶部位置
        lp.gravity = Gravity.TOP;
        getWindow().setAttributes(lp);

    }

    private void initView() {
        mBtnLayout = findViewById(R.id.btn_layout);
        if (mIsInput) {
            mBtnLayout.setVisibility(View.GONE);
        }
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
                    mListener.onClick(TopDialog.this,true);
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setView(View view){
        isShow();
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        mContainer.addView(view);
    }

    public void setContainerLayout() {
        ViewGroup.LayoutParams layoutParams = mContainer.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mContainer.setLayoutParams(layoutParams);
    }

    public static void onDestroy() {
        sBottomSelectDialog = null;
    }

    public interface OnItemClickListener {
        void onClick(Dialog dialog, boolean isConfirm);
    }

    private void isShow() {
        if (!this.isShowing()) {
            try {
                throw new Exception("dialog is not showing");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
