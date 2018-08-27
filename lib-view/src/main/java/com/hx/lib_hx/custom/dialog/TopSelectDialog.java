package com.hx.lib_hx.custom.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
public class TopSelectDialog extends Dialog{


    private Activity mContext;
    private FrameLayout mContainer;
    private OnItemClickListener mListener;
    private LinearLayout mBtnLayout;
    private View mLine;
    private static int mHeightPosition = 0;
    private static volatile TopSelectDialog sBottomSelectDialog;
    public static TopSelectDialog newInstance(Activity context,View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null) {
            mHeightPosition = view.getHeight();
        }
        if (sBottomSelectDialog == null) {
            synchronized (TopSelectDialog.class) {
                if (sBottomSelectDialog == null) {
                    sBottomSelectDialog = new TopSelectDialog(context);
                }
            }
        }
        return sBottomSelectDialog;
    }

    private TopSelectDialog(Activity context) {
        super(context, R.style.dialog);
        this.mContext = context;
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
        lp.y = mHeightPosition*2/3;
        //顶部位置
        lp.gravity = Gravity.TOP;
        getWindow().setAttributes(lp);

    }

    private void initView() {
        mBtnLayout = findViewById(R.id.btn_layout);
        mLine = findViewById(R.id.line);
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
                    mListener.onClick(TopSelectDialog.this,true);
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

    public void setBtnLayoutVisible(boolean visible) {
        isShow();
        if (mBtnLayout != null) {
            mBtnLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
            mLine.setVisibility(visible ? View.VISIBLE : View.GONE);
            TopSelectDialog.this.setCanceledOnTouchOutside(!visible);
        }
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
