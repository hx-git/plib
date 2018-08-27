package com.hx.plib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hx.lib_hx.custom.item_view.PlatformItemView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PlatformItemView view = new PlatformItemView(this);
        view.setKeyText("hello");
        setContentView(view);
    }
}
