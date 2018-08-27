package com.hx.lib_hx.custom.item_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hx.lib_hx.R;
import com.hx.lib_hx.utils.DisplayUtils;


public class KeyValueView extends LinearLayout {
    private ImageView mIv;
    private TextView mKey;
    private TextView mValue;
    private TextView mColon;
    public KeyValueView(Context context) {
        this(context,null);
    }

    public KeyValueView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public KeyValueView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.key_value, this, true);
        mIv = findViewById(R.id.key_icon);
        mKey = findViewById(R.id.key);
        mValue = findViewById(R.id.value);
        mColon = findViewById(R.id.colon);
        initAttrs(context,attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KeyValueView);
        if (typedArray != null) {
            int keyColor = typedArray.getColor(R.styleable.KeyValueView_key_text_color, 0xFF424242);
            setKeyColor(keyColor);
            String keyString = typedArray.getString(R.styleable.KeyValueView_key_text);
            setKeyText(keyString);

            int valueColor = typedArray.getColor(R.styleable.KeyValueView_value_text_color, 0xFF000000);
            setValueColor(valueColor);
            String valueString = typedArray.getString(R.styleable.KeyValueView_value_text);
            setValueText(valueString);
            //设置icon
            int resourceId = typedArray.getResourceId(R.styleable.KeyValueView_icon, 0);
            setIconRes(resourceId);
            int iconSize = typedArray.getLayoutDimension(R.styleable.KeyValueView_icon_size, 56);
            setIconSize(iconSize);
            //设置权重
            boolean isAlign_parent = typedArray.getBoolean(R.styleable.KeyValueView_touch_parent_right, false);
            if (isAlign_parent) {
                LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
                mValue.setLayoutParams(lp);
                mValue.setGravity(Gravity.RIGHT);
            }
            // 设置冒号
            boolean colonVisi = typedArray.getBoolean(R.styleable.KeyValueView_colon, true);
            setColonVisible(colonVisi);
            float textSize = typedArray.getDimension(R.styleable.KeyValueView_text_size, 12);
            setTextSize(DisplayUtils.sp2px(context,textSize));
            typedArray.recycle();
        }
    }

    private void setColonVisible(boolean colonVisi) {
        mColon.setVisibility(colonVisi?VISIBLE:GONE);
    }

    private void setIconHeight(int iconHeight) {
        ViewGroup.LayoutParams layoutParams = mIv.getLayoutParams();
        layoutParams.height = iconHeight;
        mIv.setLayoutParams(layoutParams);
    }

    private void setIconSize(int iconSize) {
        ViewGroup.LayoutParams layoutParams = mIv.getLayoutParams();
        layoutParams.width = iconSize;
        layoutParams.height = iconSize;
        mIv.setLayoutParams(layoutParams);
    }

    private void setIconRes(int resourceId) {
        mIv.setVisibility(resourceId == 0 ? GONE : VISIBLE);
        mIv.setImageResource(resourceId);
    }


    public void setKeyText(String text) {
        mKey.setText(text);
    }

    public void setValueText(String text) {
        mValue.setText(text);
    }

    public void setTextSize(float size) {
        mKey.setTextSize(size);
        mValue.setTextSize(size);
        mColon.setTextSize(size);
    }

    public void setKeyColor(int color) {
        mKey.setTextColor(color);
    }

    public void setValueColor(int color) {
        mValue.setTextColor(color);
    }
}
