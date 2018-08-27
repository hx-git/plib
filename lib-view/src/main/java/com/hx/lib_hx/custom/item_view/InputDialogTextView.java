package com.hx.lib_hx.custom.item_view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import com.hx.lib_hx.custom.dialog.CommonInputDialog;

public class InputDialogTextView extends AppCompatTextView implements View.OnClickListener{
    private Context mContext;

    private OnDialogCloseListener mListener;
    public InputDialogTextView(Context context) {
        this(context,null);
    }

    public InputDialogTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InputDialogTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setOnClickListener(this);
//        this.setAutoLinkMask(Linkify.ALL);
    }

    @Override
    public void onClick(View v) {
        CommonInputDialog dialog=new CommonInputDialog(mContext,InputDialogTextView.this.getText().toString(), new CommonInputDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm, String content) {
                if (mListener != null) {
                    mListener.onClick(content,confirm);
                }else {
                    InputDialogTextView.this.setText(content);
                }
            }
        });
        dialog.show();
    }

    public interface OnDialogCloseListener{
        void onClick(String content, boolean confirm);
    }

    public void setOnDialogCloaseListener(OnDialogCloseListener listener) {
        this.mListener = listener;
    }

    public void setUnderLine() {
        //设置下划线
        getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

}
