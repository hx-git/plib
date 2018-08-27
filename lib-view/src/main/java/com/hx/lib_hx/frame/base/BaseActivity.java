package com.hx.lib_hx.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hx.lib_hx.custom.dialog.BottomSelectDialog;
import com.hx.lib_hx.custom.dialog.NormalDialog;
import com.hx.lib_hx.custom.dialog.TopSelectDialog;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

import static android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE;

public abstract class BaseActivity extends RxAppCompatActivity implements IBaseView{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        initViews();
        initSwipeRefresh();
    }

    protected abstract int attachLayoutRes();

    protected abstract void initViews();

    protected void initSwipeRefresh() {
//        if (mSwipeRefreshLayout != null) {
//            SwipeRefreshHelper.init(mSwipeRefreshLayout, new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    updateViews(true);
//                }
//            });
//        }
    }


    @Override
    public void showLoading() {
//        if (mEmptyLayout != null) {
//            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_LOADING);
//        }
    }

    @Override
    public void hideLoading() {
//        if (mEmptyLayout != null) {
//            mEmptyLayout.hide();
//        }
    }


    @Override
    public void showNetError() {
//        if (mEmptyLayout != null) {
//            mEmptyLayout.setEmptyStatus(EmptyLayout.STATUS_NO_NET);
//        }
    }


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.bindToLifecycle();
    }

    @Override
    public void finishRefresh() {
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setRefreshing(false);
//        }
    }

    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnable, int resId) {
        initToolBar(toolbar,homeAsUpEnable,getString(resId));
    }

    /**
     * 初始化toolbar
     **/
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(homeAsUpEnabled);
        }
    }

    /**
     * 添加
     * @param containerViewId
     * @param fragment
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        handlerFragment(true,containerViewId,fragment,null);
    }

    /**
     * 添加
     * @param containerViewId
     * @param fragment
     * @param tag
     */
    protected void addFragment(int containerViewId, Fragment fragment,String tag) {
        handlerFragment(true,containerViewId,fragment,tag);
    }

    /**
     * 替换
     * @param containerViewId
     * @param fragment
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        handlerFragment(false,containerViewId,fragment,null);
    }

    /**
     * 替换
     * @param containerViewId
     * @param fragment
     * @param tag
     */
    protected void replaceFragment(int containerViewId, Fragment fragment,String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            handlerFragment(false,containerViewId,fragment,tag);
        }else {
            //弹出在上面的fragment
            getSupportFragmentManager().popBackStack(tag, 0);
        }
    }

    /**
     *  替换或添加
     * @param isAdd
     * @param containerViewId
     * @param fragment
     * @param tag
     */
    private void handlerFragment(boolean isAdd,int containerViewId, Fragment fragment,String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isAdd) {
            transaction.add(containerViewId, fragment, tag);
        } else {
            transaction.replace(containerViewId, fragment, tag);
        }
        transaction.setTransition(TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        TopSelectDialog.onDestroy();
        BottomSelectDialog.onDestroy();
        NormalDialog.onDestroy();
    }
}
