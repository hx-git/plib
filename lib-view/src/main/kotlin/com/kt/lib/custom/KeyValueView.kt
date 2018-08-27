package com.kt.lib.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hx.lib_hx.R
import com.hx.lib_hx.utils.DisplayUtils

class KeyValueView(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
    : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var mIv:ImageView
    private lateinit var mKey:TextView
    private lateinit var mValue:TextView
    private lateinit var mColon:TextView

    constructor(context: Context):this(context,null,0)
    constructor(context: Context,attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context,defStyleAttr: Int,attrs: AttributeSet?):this(context,attrs,defStyleAttr){
        val view = LayoutInflater.from(context).inflate(R.layout.key_value,this,true)
        mIv = view.findViewById(R.id.key_icon)
        mKey = view.findViewById(R.id.key)
        mValue = view.findViewById(R.id.value)
        mColon = view.findViewById(R.id.colon)
        initAttrs(context,attrs)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val tapeArray = context.obtainStyledAttributes(attrs,R.styleable.KeyValueView)
        if (tapeArray != null) {
            //TextView
            keyColor(tapeArray.getColor(R.styleable.KeyValueView_key_text_color, 0xFF424242.toInt()))
            keyText(tapeArray.getString(R.styleable.KeyValueView_key_text))
            valueColor(tapeArray.getColor(R.styleable.KeyValueView_value_text_color,0xFF000000.toInt()))
            valueText(tapeArray.getString(R.styleable.KeyValueView_value_text))
            //size
            val dimension = tapeArray.getDimension(R.styleable.KeyValueView_text_size, 12.toFloat())
            val size = DisplayUtils.sp2px(context, dimension)
            textSize(size.toFloat())
            //icon
            iconRes(tapeArray.getResourceId(R.styleable.KeyValueView_icon,0))
            //colon
            colonVisible(tapeArray.getBoolean(R.styleable.KeyValueView_colon,true))
        }
        tapeArray.recycle()
    }

    fun colonVisible(visible: Boolean) {
        mColon.visibility = if(visible){ View.VISIBLE}else{View.GONE}
    }

    fun iconSize(size: Int) {
        val layoutParams = mIv.layoutParams
        layoutParams.height=size
        layoutParams.width=size
        mIv.layoutParams=layoutParams
    }

    fun iconRes(res: Int) {
        mIv.setImageResource(res)
    }

    fun textSize(size: Float) {
        mKey.textSize = size
        mValue.textSize = size
        mColon.textSize = size
    }

    fun keyText(text: String) {
        mKey.text=text
    }

    fun valueText(text: String) {
        mValue.text=text
    }

    fun keyColor(color: Int) {
        mKey.setTextColor(color)
    }

    fun valueColor(color: Int) {
        mValue.setTextColor(color)
    }
}