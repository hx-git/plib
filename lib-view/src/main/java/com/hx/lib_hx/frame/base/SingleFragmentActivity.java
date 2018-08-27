package com.hx.lib_hx.frame.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.hx.lib_hx.R;


public abstract class SingleFragmentActivity extends BaseActivity {

    public abstract Fragment createFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_fragment);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.activity_fragment, fragment)
                    .commit();
        }
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_framelayout;
    }
    @Override
    protected void initViews() {

    }

}
