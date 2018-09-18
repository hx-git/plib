package com.kt.lib.custom.item_view

import android.app.Dialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.res.TypedArray
import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.graphics.Canvas
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.hx.lib_hx.R
import com.hx.lib_hx.R.style.dialog
import com.hx.lib_hx.SimpleItemAdapter
import com.hx.lib_hx.custom.dialog.BottomDialog
import com.hx.lib_hx.custom.dialog.TopDialog
import com.hx.lib_hx.utils.DisplayUtils
import com.hx.lib_hx.utils.SoftKeyBoardListener
import com.kt.lib.custom.MyTextChangedListener

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class PlatformItemView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(mContext, attrs, defStyleAttr), View.OnClickListener {
    private val mRightIv: ImageView
    private val mIv: ImageView
    private val mKey: TextView
    private val mValue: TextView
    private val mColon: TextView
    private var mCanClick: Boolean = false
    private var mType: Int = 0
    private val TYPE_DATE = 1
    private val TYPE_PICK = 2
    private val TYPE_EDIT = 3
    private val TYPE_PICK_EDIT = 4
    private val TYPE_FUZZY = 5
    private val TYPE_EDIT_NUM = 6
    private val mOptionsPickList = ArrayList<Any>()
    private var mAdapter: SimpleItemAdapter? = null
    private var mValueHint: String? = null
    var isFlag = true
    private var mValueText: String? = null
    private var mListener: InverseBindingListener? = null
    private var mValueTextChangeListener: OnValueTextChangeListener? = null
    var animOpen: RotateAnimation = RotateAnimation(0f, 90f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
    var animClose: RotateAnimation = RotateAnimation(90f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)

    var keyText: String?
        get() = mKey.text.toString().trim { it <= ' ' }
        set(text) {
            mKey.text = text
        }


    //左对齐
    //上对齐
    var valueText: String?
        get() = mValueText
        set(text) {
            mValue.text = text
            mValue.post {
                if (mValue.lineCount > 1) {
                    val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                    lp.setMargins(8, 0, 0, 0)
                    mValue.layoutParams = lp
                    mValue.gravity = Gravity.LEFT
                    showSnackBar(mValue.text.toString())
                    this@PlatformItemView.gravity = Gravity.TOP
                }
            }

        }

    private var mItemClickListener: OnItemClickListener? = null

    init {
        setWillNotDraw(false)
        LayoutInflater.from(mContext).inflate(R.layout.platform_item_layout, this, true)
        mRightIv = findViewById(R.id.key_icon_right)
        mIv = findViewById(R.id.key_icon)
        mKey = findViewById(R.id.key)
        mValue = findViewById(R.id.value)
        mColon = findViewById(R.id.colon)
        this.setOnClickListener(this)
        mValue.addTextChangedListener(object : MyTextChangedListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (TextUtils.isEmpty(s) && mValueHint != null) {
                    mValue.hint = mValueHint
                }
                mValueText = s!!.toString().trim { it <= ' ' }
                if (mListener != null) {
                    mListener!!.onChange()
                }
                if (mValueTextChangeListener != null) {
                    mValueTextChangeListener!!.onChange(mValueText)
                }
            }
        })
        initAttrs(mContext, attrs)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        Paint paint = new Paint();
        //        paint.setStrokeWidth(5);
        //        paint.setColor(mContext.getResources().getColor(R.color.line));
        //        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
    }


    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PlatformItemView)
        if (typedArray != null) {
            val keyColor = typedArray.getColor(R.styleable.PlatformItemView_key_text_color, -0xbdbdbe)
            setKeyColor(keyColor)
            val keyString = typedArray.getString(R.styleable.PlatformItemView_key_text)
            keyText = keyString
            //0xb1b1b1b1
            val valueColor = typedArray.getColor(R.styleable.PlatformItemView_value_text_color, -0x313233)
            setValueColor(valueColor)
            val valueString = typedArray.getString(R.styleable.PlatformItemView_value_text)
            valueText = valueString
            //valueText默认值
            mValueHint = typedArray.getString(R.styleable.PlatformItemView_value_hint)
            //设置icon
            val resourceId = typedArray.getResourceId(R.styleable.PlatformItemView_icon, 0)
            setIconRes(resourceId)
            val iconSize = typedArray.getLayoutDimension(R.styleable.PlatformItemView_icon_size, 56)
            setIconSize(iconSize, mIv)
            setIconSize(iconSize, mRightIv)
            //icon padding
            iconPadding(typedArray.getDimension(R.styleable.PlatformItemView_icon_padding, 8f))
            //设置权重
            val isAlign_parent = typedArray.getBoolean(R.styleable.PlatformItemView_touch_parent_right, false)
            if (isAlign_parent) {
                val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)
                lp.setMargins(4, 0, 0, 0)
                mValue.layoutParams = lp
                mValue.gravity = Gravity.RIGHT
            }
            // 设置冒号
            val colonVisible = typedArray.getBoolean(R.styleable.PlatformItemView_colon, false)
            setColonVisible(colonVisible)
            val textSize = typedArray.getDimension(R.styleable.PlatformItemView_text_size, 12f)
            setTextSize(DisplayUtils.sp2px(context, textSize).toFloat())
            //右边icon
            setRightIconRes(typedArray.getResourceId(R.styleable.PlatformItemView_icon_right, 0))
            //是否可点击
            setCanClick(typedArray.getBoolean(R.styleable.PlatformItemView_item_onclick, true))
            //type
            mType = typedArray.getInt(R.styleable.PlatformItemView_type, 0)
            typedArray.recycle()
        }
    }

    private fun iconPadding(icPadding: Float) {
        val padding = icPadding.toInt()
        mRightIv.setPadding(padding, padding, padding, padding)
    }

    private fun setColonVisible(colonVisible: Boolean) {
        mColon.visibility = if (colonVisible) View.VISIBLE else View.GONE
    }

    private fun setIconSize(iconSize: Int, iv: ImageView) {
        val layoutParams = iv.layoutParams
        layoutParams.width = iconSize
        layoutParams.height = iconSize
        iv.layoutParams = layoutParams
    }

    private fun setIconRes(resourceId: Int) {
        mIv.visibility = if (resourceId == 0) View.GONE else View.VISIBLE
        mIv.setImageResource(resourceId)
    }

    fun setText(text: String) {
        mValue.text = text
    }


    fun setVisibilityChangeListener(listener: InverseBindingListener) {
        mListener = listener
    }

    fun setValueTextChangeListener(listener: OnValueTextChangeListener) {
        mValueTextChangeListener = listener
    }

    interface OnValueTextChangeListener {
        fun onChange(value: String?)
    }

    interface OnVisibilityChangeListener {
        fun onChange()
    }

    fun setTextSize(size: Float) {
        mKey.textSize = size
        mValue.textSize = size
        mColon.textSize = size
    }

    fun setKeyColor(color: Int) {
        mKey.setTextColor(color)
    }

    fun setValueColor(color: Int) {
        mValue.setTextColor(color)
    }

    fun setRightIconRes(rightIconRes: Int) {
        if (rightIconRes == 0) {
            mRightIv.visibility = View.GONE
        } else {
            mRightIv.setImageResource(rightIconRes)
        }
    }

    fun setCanClick(canClick: Boolean) {
        this.isEnabled = canClick
        mCanClick = canClick
        if (!canClick) {
            mRightIv.clearAnimation()
            mRightIv.invalidate()
            mRightIv.visibility = View.GONE
        }
    }

    override fun onClick(view: View) {
        if (!mCanClick) {
            return
        }
        when (mType) {
            TYPE_DATE -> showBottomDateDialog()
            TYPE_PICK -> showBottomDialog()
            TYPE_EDIT -> {
                showDialogSoftKey()
                if (mItemClickListener != null) {
                    mOptionsPickList.clear()
                    mItemClickListener!!.onClick(null)
                }
            }
            TYPE_EDIT_NUM -> {
                showDialogSoftKey()
                if (mItemClickListener != null) {
                    mOptionsPickList.clear()
                    mItemClickListener!!.onClick(null)
                }
            }
            TYPE_PICK_EDIT -> {
                mValue.setOnClickListener(this)
                if (view == mValue) {
                    showDialogSoftKey()
                } else {
                    if (mItemClickListener != null) {
                        mOptionsPickList.clear()
                        mItemClickListener!!.onClick(null)
                    }
                }
            }
            TYPE_FUZZY -> showFuzzyDialog()
            else -> if (mItemClickListener != null) {
                mOptionsPickList.clear()
                mItemClickListener!!.onClick(null)
            }
        }
    }

    private fun showSnackBar(content: String) {
        //        SnackBarKt.snackbar(mContext,content,Snackbar.LENGTH_SHORT);
    }

    /**
     * 展示模糊查询
     */
    private fun showFuzzyDialog() {
        val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_top_input, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        mOptionsPickList.clear()
        mAdapter = SimpleItemAdapter(mContext, mOptionsPickList)
        recyclerView.adapter = mAdapter

        val inputComment = view.findViewById<EditText>(R.id.et_content)
        //获取焦点 弹出软键盘
        inputComment.isFocusable = true
        inputComment.isFocusableInTouchMode = true
        inputComment.requestFocus()

        val dialog = TopDialog.newInstance(mContext as AppCompatActivity, true)
        dialog.show()
        dialog.setView(view)
        //全屏
        dialog.setContainerLayout()
        mAdapter!!.setOnItemClickListener { text ->
            mValue.text = text
            dialog.dismiss()
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val inputManager = inputComment.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(inputComment, 0)
            }
        }, 1)

        inputComment.addTextChangedListener(object : MyTextChangedListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (mItemClickListener != null) {
                    mOptionsPickList.clear()
                    mOptionsPickList.add(s!!.toString())
                    mItemClickListener!!.onClick(mOptionsPickList)
                }
            }
        })
        //设置监听软键盘关闭
        bindSoftBoard(dialog)
    }

    fun updateTopList() {
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     * 输入框dialog
     */
    private fun showDialogSoftKey() {
        val view = View.inflate(mContext, R.layout.dialog_bottom_input, null)
        val mViewConfirm = view.findViewById<TextView>(R.id.btn_confirm)
        val inputComment = view.findViewById<EditText>(R.id.et_content)
        //获取焦点 弹出软键盘
        inputComment.isFocusable = true
        inputComment.isFocusableInTouchMode = true
        inputComment.requestFocus()
        // 提示信息
        inputComment.hint = keyText
        val dialog = com.kt.lib.custom.dialog.BottomDialog.Companion.Builder(mContext as AppCompatActivity, true)
                .show()
                .setView(view)
                .setCanceledOnTouchOutside(true)
                .build()

        if (mType == TYPE_EDIT_NUM) {
            inputComment.inputType = InputType.TYPE_CLASS_NUMBER
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                val inputManager = inputComment.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(inputComment, 0)
            }
        }, 100)

        inputComment.addTextChangedListener(object : MyTextChangedListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mViewConfirm.isEnabled = !TextUtils.isEmpty(s)
            }
        })

        //确定
        mViewConfirm.setOnClickListener { view1 ->
            valueText = inputComment.text.toString().trim { it <= ' ' }
            dialog.dismiss()
        }
        //设置监听软键盘关闭
        bindSoftBoard(dialog)
    }

    private fun bindSoftBoard(dialog: Dialog) {
        SoftKeyBoardListener.setListener(mContext as AppCompatActivity, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {

            }

            override fun keyBoardHide(height: Int) {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
        })
    }


    /**
     * 弹出日期选择器
     */
    private fun showBottomDateDialog() {
        TimePickerBuilder(mContext) { date, v ->
            val text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                    .format(date)
            mValue.text = text
        }.setType(booleanArrayOf(true, true, true, true, true, false))
                .build()
                .show()
    }

    /**
     * list选择器
     */
    private fun showBottomDialog() {
        if (mItemClickListener != null) {
            mOptionsPickList.clear()
            mItemClickListener!!.onClick(mOptionsPickList)
        }
        val build = OptionsPickerBuilder(mContext) { options1, options2, options3, v -> mValue.text = mOptionsPickList[options1] as CharSequence }.build<Any>()
        build.setPicker(mOptionsPickList)
        build.show()
    }

    fun setVisible(visible: Boolean) {
        if (visible) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }

    /**
     * 旋转右侧图标
     */
    fun setRightIvRotation() {
        if (isFlag) {
            animClose.duration = 400
            animClose.fillAfter = true
            mRightIv.startAnimation(animClose)
            //            mRightIv.setRotation(0);
        } else {
            animOpen.duration = 400
            animOpen.fillAfter = true
            mRightIv.startAnimation(animOpen)
            //            mRightIv.setRotation(90);
        }
    }

    /**
     * 通过外部获取数据
     */
    interface OnItemClickListener {
        fun onClick(list: List<Any>?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mItemClickListener = listener
    }

    companion object {

        @BindingAdapter(value = ["value_text"])
        fun setValueText(itemView: PlatformItemView, text: String) {
            itemView.valueText = text
        }

        @InverseBindingAdapter(attribute = "value_text", event = "value_textAttrChanged")
        fun getValueText(itemView: PlatformItemView): String? {
            return itemView.valueText
        }


        @BindingAdapter(value = ["value_textAttrChanged"])
        fun setChangeListener(view: PlatformItemView, listener: InverseBindingListener) {
            view.setVisibilityChangeListener(listener)
        }
    }


}
