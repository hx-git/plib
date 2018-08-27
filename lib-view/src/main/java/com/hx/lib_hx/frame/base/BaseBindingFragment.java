package com.hx.lib_hx.frame.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 碎片基类
 */
public abstract class BaseBindingFragment extends BaseFragment{

    /**
     * 注意，资源的ID一定要一样
     */

    protected Context mContext;
    //缓存Fragment view
    private View mRootView;
    private boolean mIsMulti = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = attachLayoutRes(inflater,container);
        initViews();
        initSwipeRefresh();
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    protected int attachLayoutRes() {
        return 0;
    }

    protected abstract View attachLayoutRes(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && isVisible() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
        } else {
            super.setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void showLoading() {
//        if (mEmptyLayout != null) {
//            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
//            SwipeRefreshHelper.enableRefresh(mSwipeRefresh, false);
//        }
    }

    @Override
    public void hideLoading() {
//        if (mEmptyLayout != null) {
//            mEmptyLayout.hide();
//            SwipeRefreshHelper.enableRefresh(mSwipeRefresh, true);
//            SwipeRefreshHelper.controlRefresh(mSwipeRefresh, false);
//        }
    }

    @Override
    public void showNetError() {
//        if (mEmptyLayout != null) {
//            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
//            mEmptyLayout.setRetryListener(this);
//        }
    }


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public void finishRefresh() {
//        if (mSwipeRefresh != null) {
//            mSwipeRefresh.setRefreshing(false);
//        }
    }

    /**
     * 初始化 Toolbar
     *
     * @param toolbar
     * @param homeAsUpEnabled
     * @param title
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        ((BaseActivity)getActivity()).initToolBar(toolbar, homeAsUpEnabled, title);
    }

    /**
     * 初始化下拉刷新
     */
    private void initSwipeRefresh() {
//        if (mSwipeRefresh != null) {
//            SwipeRefreshHelper.init(mSwipeRefresh, new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    updateViews(true);
//                }
//            });
//        }
    }

    /**
     * 绑定布局文件
     * @return  布局文件ID
     */
    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
