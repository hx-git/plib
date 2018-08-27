package com.hx.lib_hx.custom.image_preview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hx.lib_hx.R
import com.hx.lib_hx.custom.image_preview.ImagePagerFragment.ARG_CURRENT_ITEM
import com.hx.lib_hx.custom.image_preview.ImagePagerFragment.ARG_PATH

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        supportFragmentManager.beginTransaction()
                .add(R.id.container,ImagePagerFragment.newInstance(
                        intent.getStringArrayListExtra(ARG_PATH),
                        intent.getIntExtra(ARG_CURRENT_ITEM,0)
                )).commit()
    }
}
