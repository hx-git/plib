package com.kt.lib.custom.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.hx.lib_hx.R


/**
 * 调用该类需要注意内存泄漏
 * 选择页面
 */
class BottomDialog private constructor(private val mContext: AppCompatActivity, private val mIsInput: Boolean) : Dialog(mContext, R.style.dialog) {
    private var mContainer: FrameLayout? = null
    private var mListener: OnItemClickListener? = null
    private var mBtnLayout: LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dialog_bottom_select)
        initView()
        setCanceledOnTouchOutside(false)
        //设置窗口弹出动画
        window!!.setWindowAnimations(R.style.dialogWindowAnimBottom)
        //全屏处理
        val lp = window!!.attributes
        val wm = mContext.windowManager

        lp.width = wm.defaultDisplay.width //设置宽度
        window!!.attributes = lp

    }

    /**
     * 确定 取消
     */
    private fun initView() {
        findViewById<View>(R.id.tv_cancel).setOnClickListener { v -> dismiss() }
        mContainer = findViewById(R.id.container)
        val lineView = findViewById<View>(R.id.line)
        findViewById<View>(R.id.tv_upload).setOnClickListener { v ->
            if (mListener != null) {
                mListener!!.onClick(this@BottomDialog, true)
            }
        }
        mBtnLayout = findViewById(R.id.btn_layout)
        if (mIsInput) {
            lineView.visibility = View.GONE
            mBtnLayout!!.visibility = View.GONE
        } else {
            lineView.visibility = View.VISIBLE
            mBtnLayout!!.visibility = View.VISIBLE
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    fun setView(view: View) {
        if (view.parent != null) {
            val parent = view.parent as ViewGroup
            parent.removeAllViews()
        }else{
            mContainer!!.removeAllViews()
        }
        mContainer!!.addView(view)
        //        Log.i("hx-dialog数量：", mContainer.getChildCount()+"");
    }

    interface OnItemClickListener {
        fun onClick(dialog: Dialog, isConfirm: Boolean)
    }

    companion object {
            class Builder(val context: AppCompatActivity, val isInput: Boolean){
            private val dialog = BottomDialog(context,isInput)
            fun show(): Builder {
                dialog.show()
                return this
            }

            fun setView(view:View):Builder{
                dialog.setView(view)
                return this
            }

            fun setCanceledOnTouchOutside(isCancel:Boolean):Builder{
                dialog.setCanceledOnTouchOutside(isCancel)
                return this
            }

            fun build(): BottomDialog {
                return dialog
            }
        }
    }


}
