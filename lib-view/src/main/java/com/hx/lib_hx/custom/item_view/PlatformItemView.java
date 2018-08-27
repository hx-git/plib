package com.hx.lib_hx.custom.item_view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hx.lib_hx.R;
import com.hx.lib_hx.SimpleItemAdapter;
import com.hx.lib_hx.custom.dialog.BottomDialog;
import com.hx.lib_hx.custom.dialog.TopDialog;
import com.hx.lib_hx.utils.DisplayUtils;
import com.hx.lib_hx.utils.SoftKeyBoardListener;
import com.kt.lib.custom.MyTextChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PlatformItemView extends LinearLayout implements View.OnClickListener {
    private ImageView mRightIv;
    private ImageView mIv;
    private TextView mKey;
    private TextView mValue;
    private TextView mColon;
    private boolean mCanClick;
    private int mType;
    private Context mContext;
    private final int TYPE_DATE = 1;
    private final int TYPE_PICK = 2;
    private final int TYPE_EDIT = 3;
    private final int TYPE_PICK_EDIT = 4;
    private final int TYPE_FUZZY = 5;
    private final int TYPE_EDIT_NUM = 6;
    private List<Object> mOptionsPickList = new ArrayList<>();
    private SimpleItemAdapter mAdapter;
    private String mValueHint;
    private boolean mFlag = true;
    private String mValueText;
    private OnVisibilityChangeListener mListener;
    private OnValueTextChangeListener mValueTextChangeListener;
    RotateAnimation animOpen;
    RotateAnimation animClose;

    public PlatformItemView(Context context) {
        this(context, null);
    }

    public PlatformItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlatformItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        LayoutInflater.from(context).inflate(R.layout.platform_item_layout, this, true);
        mRightIv = findViewById(R.id.key_icon_right);
        mIv = findViewById(R.id.key_icon);
        mKey = findViewById(R.id.key);
        mValue = findViewById(R.id.value);
        mColon = findViewById(R.id.colon);
        mContext = context;
        this.setOnClickListener(this);
        mValue.addTextChangedListener(new MyTextChangedListener(){
            @Override
            public void onTextChanged(@org.jetbrains.annotations.Nullable CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s) && mValueHint != null) {
                    mValue.setHint(mValueHint);
                }
                mValueText = s.toString().trim();
                if (mListener != null) {
                    mListener.onChange();
                }
                if (mValueTextChangeListener != null) {
                    mValueTextChangeListener.onChange(mValueText);
                }
            }
        });
        initAttrs(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint = new Paint();
//        paint.setStrokeWidth(5);
//        paint.setColor(mContext.getResources().getColor(R.color.line));
//        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlatformItemView);
        if (typedArray != null) {
            int keyColor = typedArray.getColor(R.styleable.PlatformItemView_key_text_color, 0xFF424242);
            setKeyColor(keyColor);
            String keyString = typedArray.getString(R.styleable.PlatformItemView_key_text);
            setKeyText(keyString);
            //0xb1b1b1b1
            int valueColor = typedArray.getColor(R.styleable.PlatformItemView_value_text_color, 0xFFCECDCD);
            setValueColor(valueColor);
            String valueString = typedArray.getString(R.styleable.PlatformItemView_value_text);
            setValueText(valueString);
            //valueText默认值
            mValueHint = typedArray.getString(R.styleable.PlatformItemView_value_hint);
            //设置icon
            int resourceId = typedArray.getResourceId(R.styleable.PlatformItemView_icon, 0);
            setIconRes(resourceId);
            int iconSize = typedArray.getLayoutDimension(R.styleable.PlatformItemView_icon_size, 56);
            setIconSize(iconSize, mIv);
            setIconSize(iconSize, mRightIv);
            //icon padding
            iconPadding(typedArray.getDimension(R.styleable.PlatformItemView_icon_padding, 8));
            //设置权重
            boolean isAlign_parent = typedArray.getBoolean(R.styleable.PlatformItemView_touch_parent_right, false);
            if (isAlign_parent) {
                LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
                lp.setMargins(4,0,0,0);
                mValue.setLayoutParams(lp);
                mValue.setGravity(Gravity.RIGHT);
            }
            // 设置冒号
            boolean colonVisible = typedArray.getBoolean(R.styleable.PlatformItemView_colon, false);
            setColonVisible(colonVisible);
            float textSize = typedArray.getDimension(R.styleable.PlatformItemView_text_size, 12);
            setTextSize(DisplayUtils.sp2px(context, textSize));
            //右边icon
            setRightIconRes(typedArray.getResourceId(R.styleable.PlatformItemView_icon_right, 0));
            //是否可点击
            setCanClick(typedArray.getBoolean(R.styleable.PlatformItemView_item_onclick, true));
            //type
            mType = typedArray.getInt(R.styleable.PlatformItemView_type, 0);
            typedArray.recycle();
        }
    }

    private void iconPadding(float icPadding) {
        int padding = (int) icPadding;
        mRightIv.setPadding(padding, padding, padding, padding);
    }

    private void setColonVisible(boolean colonVisible) {
        mColon.setVisibility(colonVisible ? VISIBLE : GONE);
    }

    private void setIconSize(int iconSize, ImageView iv) {
        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
        layoutParams.width = iconSize;
        layoutParams.height = iconSize;
        iv.setLayoutParams(layoutParams);
    }

    private void setIconRes(int resourceId) {
        mIv.setVisibility(resourceId == 0 ? GONE : VISIBLE);
        mIv.setImageResource(resourceId);
    }


    public void setKeyText(String text) {
        mKey.setText(text);
    }

    public String getKeyText() {
        return mKey.getText().toString().trim();
    }


    public void setValueText(String text) {
        mValue.setText(text);
        mValue.post(() -> {
            if (mValue.getLineCount() > 1) {
                LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1.0f);
                lp.setMargins(8,0,0,0);
                mValue.setLayoutParams(lp);
                mValue.setGravity(Gravity.LEFT); //左对齐
                showSnackBar(mValue.getText().toString());
                //上对齐
                PlatformItemView.this.setGravity(Gravity.TOP);
            }
        });

    }

    public void setText(String text) {
        mValue.setText(text);
    }

    @BindingAdapter(value = {"value_text"})
    public static void setValueText(PlatformItemView itemView, String text) {
        itemView.setValueText(text);
    }

    @InverseBindingAdapter(attribute = "value_text", event = "value_textAttrChanged")
    public static String getValueText(PlatformItemView itemView) {
        return itemView.getValueText();
    }


    @BindingAdapter(value = "value_textAttrChanged")
    public static void setChangeListener(PlatformItemView view, InverseBindingListener listener) {
        view.setVisibilityChangeListener(listener::onChange);
    }


    public void setVisibilityChangeListener(OnVisibilityChangeListener listener) {
        mListener = listener;
    }

    public void setValueTextChangeListener(OnValueTextChangeListener listener) {
        mValueTextChangeListener = listener;
    }

    public interface OnValueTextChangeListener {
        void onChange(String value);
    }

    public interface OnVisibilityChangeListener {
        void onChange();
    }


    public String getValueText() {
        return mValueText;
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

    public void setRightIconRes(int rightIconRes) {
        if (rightIconRes == 0) {
            mRightIv.setVisibility(GONE);
        }else {
            mRightIv.setImageResource(rightIconRes);
        }
    }

    public void setCanClick(boolean canClick) {
        this.setEnabled(canClick);
        mCanClick = canClick;
        if (!canClick) {
            mRightIv.clearAnimation();
            mRightIv.invalidate();
            mRightIv.setVisibility(GONE);
        }
    }

    public boolean isFlag() {
        return mFlag;
    }

    public void setFlag(boolean flag) {
        mFlag = flag;
    }

    @Override
    public void onClick(View view) {
        if (!mCanClick) {
            return;
        }
        switch (mType) {
            case TYPE_DATE:
                showBottomDateDialog();
                break;
            case TYPE_PICK:
                showBottomDialog();
                break;
            case TYPE_EDIT:
                showDialogSoftKey();
                if (mItemClickListener != null) {
                    mOptionsPickList.clear();
                    mItemClickListener.onClick(null);
                }
                break;
            case TYPE_EDIT_NUM:
                showDialogSoftKey();
                if (mItemClickListener != null) {
                    mOptionsPickList.clear();
                    mItemClickListener.onClick(null);
                }
                break;
            case TYPE_PICK_EDIT:
                mValue.setOnClickListener(this);
                if (view.equals(mValue)) {
                    showDialogSoftKey();
                } else {
                    if (mItemClickListener != null) {
                        mOptionsPickList.clear();
                        mItemClickListener.onClick(null);
                    }
                }
                break;
            case TYPE_FUZZY:
                showFuzzyDialog();
                break;
            default:
                if (mItemClickListener != null) {
                    mOptionsPickList.clear();
                    mItemClickListener.onClick(null);
                }
        }
    }

    private void showSnackBar(String content) {
//        SnackBarKt.snackbar(mContext,content,Snackbar.LENGTH_SHORT);
    }

    /**
     * 展示模糊查询
     */
    private void showFuzzyDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_top_input, null);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mOptionsPickList.clear();
        mAdapter = new SimpleItemAdapter(mContext, mOptionsPickList);
        recyclerView.setAdapter(mAdapter);

        final EditText inputComment = view.findViewById(R.id.et_content);
        //获取焦点 弹出软键盘
        inputComment.setFocusable(true);
        inputComment.setFocusableInTouchMode(true);
        inputComment.requestFocus();

        final TopDialog dialog = TopDialog.newInstance((AppCompatActivity) mContext, true);
        dialog.show();
        dialog.setView(view);
        //全屏
        dialog.setContainerLayout();
        mAdapter.setOnItemClickListener(text -> {
            mValue.setText(text);
            dialog.dismiss();
        });
        new Timer().schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) inputComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(inputComment, 0);
            }
        }, 1);

        inputComment.addTextChangedListener(new MyTextChangedListener(){
            @Override
            public void onTextChanged(@org.jetbrains.annotations.Nullable CharSequence s, int start, int before, int count) {
                if (mItemClickListener != null) {
                    mOptionsPickList.clear();
                    mOptionsPickList.add(s.toString());
                    mItemClickListener.onClick(mOptionsPickList);
                }
            }
        });
        //设置监听软键盘关闭
        bindSoftBoard(dialog);
    }

    public void updateTopList() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 输入框dialog
     */
    private void showDialogSoftKey() {
        View view = View.inflate(mContext, R.layout.dialog_bottom_input, null);
        final TextView mViewConfirm = view.findViewById(R.id.btn_confirm);
        final EditText inputComment = view.findViewById(R.id.et_content);
        //获取焦点 弹出软键盘
        inputComment.setFocusable(true);
        inputComment.setFocusableInTouchMode(true);
        inputComment.requestFocus();
        // 提示信息
        inputComment.setHint(getKeyText());
        final BottomDialog dialog = BottomDialog.newInstance((AppCompatActivity) mContext, true);
        dialog.show();
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(true);
        if (mType == TYPE_EDIT_NUM) {
            inputComment.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        new Timer().schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) inputComment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(inputComment, 0);
            }
        }, 100);

        inputComment.addTextChangedListener(new MyTextChangedListener(){
            @Override
            public void onTextChanged(@org.jetbrains.annotations.Nullable CharSequence s, int start, int before, int count) {
                mViewConfirm.setEnabled(!TextUtils.isEmpty(s));
            }
        });

        //确定
        mViewConfirm.setOnClickListener(view1 -> {
            setValueText(inputComment.getText().toString().trim());
            dialog.dismiss();
        });
        //设置监听软键盘关闭
        bindSoftBoard(dialog);
    }

    private void bindSoftBoard(final Dialog dialog) {
        SoftKeyBoardListener.setListener((AppCompatActivity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    /**
     * 弹出日期选择器
     */
    private void showBottomDateDialog() {
        new TimePickerBuilder(mContext, (date, v) -> {
            String text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA)
                    .format(date);
            mValue.setText(text);
        }).setType(new boolean[]{true, true, true, true, true, false})
                .build()
                .show();
    }

    /**
     * list选择器
     */
    private void showBottomDialog() {
        if (mItemClickListener != null) {
            mOptionsPickList.clear();
            mItemClickListener.onClick(mOptionsPickList);
        }
        OptionsPickerView<Object> build = new OptionsPickerBuilder(mContext, (options1, options2, options3, v) -> mValue.setText((CharSequence) mOptionsPickList.get(options1))).build();
        build.setPicker(mOptionsPickList);
        build.show();
    }

    private OnItemClickListener mItemClickListener;

    public void setVisible(boolean visible) {
        if (visible) {
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(GONE);
        }
    }

    /**
     * 旋转右侧图标
     */
    public void setRightIvRotation() {
        if (mFlag) {
            animClose = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animClose.setDuration(400);
            animClose.setFillAfter(true);
            mRightIv.startAnimation(animClose);
//            mRightIv.setRotation(0);
        } else {
            animOpen = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animOpen.setDuration(400);
            animOpen.setFillAfter(true);
            mRightIv.startAnimation(animOpen);
//            mRightIv.setRotation(90);
        }
    }

    /**
     * 通过外部获取数据
     */
    public interface OnItemClickListener {
        void onClick(List<Object> list);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }


}
