package com.hx.lib_hx.custom.grid_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.hx.lib_hx.R;

import java.util.HashMap;

public class GridLayout extends ViewGroup implements View.OnClickListener {
    /**
     * 列数
     */
    private int mSpan = 3;
    /**
     * Item 水平之间的间距
     */
    private int mHorizontalSpace = 0;
    /**
     * Item 垂直之间的间距
     */
    private int mVerticalSpace = 0;
    /**
     * 最大的Item数量
     */
    private int mMaxItem = 15;
    protected BaseAdapter mAdapter;
    protected DataSetObserver mObserver;

    private HashMap<View,Integer> mHashMap;

    public GridLayout(Context context) {
        this(context, null);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHashMap = new HashMap<>();
        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GridLayout);
        mSpan = array.getInteger(R.styleable.GridLayout_gridSpan, mSpan);
        mHorizontalSpace = (int) array.getDimension(R.styleable.GridLayout_gridHorizontalSpace, mHorizontalSpace);
        mVerticalSpace = (int) array.getDimension(R.styleable.GridLayout_gridVerticalSpace, mVerticalSpace);
        mMaxItem = array.getInteger(R.styleable.GridLayout_gridMaxItem, mMaxItem);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取控件的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 计算单个子View的宽度
        int itemWidth = (width - getPaddingLeft() - getPaddingRight() - mHorizontalSpace * (mSpan - 1)) / mSpan;
        // 测量子View的宽高
        int childCount = getChildCount();
        // 计算一下最大的条目数量
        childCount = Math.min(childCount, mMaxItem);
        if (childCount <= 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int itemSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            measureChild(child, itemSpec, itemSpec);
        }
        View child = getChildAt(0);
        int height = itemWidth/3 * (childCount % mSpan == 0 ? (childCount / mSpan) : (childCount / mSpan + 1)) + mVerticalSpace * ((childCount - 1) / mSpan);
        // 指定自己的宽高
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        // 计算一下最大的条目数量
        childCount = Math.min(childCount, mMaxItem);
        if (childCount <= 0) {
            return;
        }
        int cl = getPaddingLeft();
        int ct = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(cl, ct, cl + width, ct + height);
            // 累加宽度
            cl += width + mHorizontalSpace;
            // 如果是换行
            if ((i + 1) % mSpan == 0) {
                // 重置左边的位置
                cl = getPaddingLeft();
                // 叠加高度
                ct += height + mVerticalSpace;
            }
        }
    }

    /** * 设置Adapter */
    public void setAdapter(BaseAdapter adapter) {
        // 移除监听
        if (mAdapter != null && mObserver != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
            mAdapter = null;
            mObserver = null;
        }
        if (adapter == null) {
            throw new NullPointerException("FlowBaseAdapter is null");
        }
        mAdapter = adapter;
        resetLayout();
        mObserver = new DataSetObserver() {
            @Override public void onChanged() {
                resetLayout();
            }
        };
        mAdapter.registerDataSetObserver(mObserver);
    }
    /** * 重新添加布局 */
    protected void resetLayout() {
        removeAllViews();
        mHashMap.clear();
        int count = mAdapter.getCount();
        count = Math.min(count, mMaxItem);
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i, this);
            view.setOnClickListener(this);
            mHashMap.put(view, i);
            addView(view);
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onClick(mHashMap.get(v),v);
        }
    }

    public void setChildSelected() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).setSelected(false);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position, View view);
    }

}




