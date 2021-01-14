package com.linf.toolbartitleview

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import kotlin.math.max

/**
 * Copyright (C)：linf
 *
 * Author       ：linf
 *
 * Date         ：2021/01/15
 *
 * Description  : 统一标题样式
 */
class ToolBarTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    //右边图标
    private var mToolBarView: ConstraintLayout? = null

    //右边图标
    private var mToolBarImgLeft: ImageView? = null

    //中间标题
    private var mToolBarTitle: TextView? = null

    //右边文本
    private var mToolBarTvRight: TextView? = null

    //右边图标
    private var mToolBarImgRight: ImageView? = null

    //标题内容
    private var mToolBarTitleStr: String? = null

    //控件高度
    private var mToolBarHeight = 47

    //标题文本颜色
    private var mToolBarTitleColor: Int = Color.parseColor("#333333")

    //标题的字体大小
    private var mToolBarTitleTestSize = 22

    //右边内容
    private var mToolBarRightStr: String? = null

    //右边的文本颜色
    private var mToolBarTitleRightColor = Color.parseColor("#333333")

    //右边的文本字体大小
    private var mToolBarTitleRightTestSize = 16

    //左边的图标
    private var mToolBarLeftDrawable: Drawable? = null

    //右边的图标
    private var mToolBarRightDrawable: Drawable? = null

    init {
        val rootView = View.inflate(context, R.layout.toolbar_title_view_def, null)
        mToolBarView = rootView.findViewById(R.id.toolbar_title_view)
        mToolBarImgLeft = rootView.findViewById(R.id.toolbar_img_left)
        mToolBarTitle = rootView.findViewById(R.id.toolbar_center_title)
        mToolBarTvRight = rootView.findViewById(R.id.toolbar_tv_right)
        mToolBarImgRight = rootView.findViewById(R.id.toolbar_img_right)
        //监听事件
        mToolBarImgLeft?.setOnClickListener(this)
        mToolBarTvRight?.setOnClickListener(this)
        mToolBarImgRight?.setOnClickListener(this)

        //加载 attributes
        val a = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.ToolBarTitleView,
            defStyleAttr,
            0
        )

        //rootView 的高度
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_height)) {
            mToolBarHeight =
                a.getDimensionPixelSize(R.styleable.ToolBarTitleView_Toolbar_height, mToolBarHeight)
            mToolBarView?.minHeight = mToolBarHeight
        }
        //标题内容
        mToolBarTitleStr = a.getString(R.styleable.ToolBarTitleView_Toolbar_title)
        if (!TextUtils.isEmpty(mToolBarTitleStr)) {
            mToolBarTitle?.let { it.text = mToolBarTitleStr }
        }
        //判断是否设置了颜色值
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_title_test_color)) {
            //标题颜色值
            mToolBarTitleColor = a.getColor(
                R.styleable.ToolBarTitleView_Toolbar_title_test_color,
                mToolBarTitleColor
            )
            //设置颜色值
            mToolBarTitle?.setTextColor(mToolBarTitleColor)
        }
        //标题文字大小
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_title_size)) {
            mToolBarTitleTestSize = a.getDimensionPixelSize(
                R.styleable.ToolBarTitleView_Toolbar_title_size,
                mToolBarTitleTestSize
            )
            mToolBarTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, mToolBarTitleTestSize.toFloat())
        }

        //右边的标题内容
        mToolBarRightStr = a.getString(R.styleable.ToolBarTitleView_Toolbar_right_test)
        if (!TextUtils.isEmpty(mToolBarRightStr)) {
            mToolBarTvRight?.visibility = View.VISIBLE
            mToolBarTvRight?.text = mToolBarRightStr
        }
        //判断是否设置了颜色值
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_right_test_color)) {
            //标题颜色值
            mToolBarTitleRightColor = a.getColor(
                R.styleable.ToolBarTitleView_Toolbar_right_test_color,
                mToolBarTitleRightColor
            )
            //设置颜色值
            mToolBarTvRight?.setTextColor(mToolBarTitleRightColor)
        }
        //标题文字大小
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_right_size)) {
            mToolBarTitleRightTestSize = a.getDimensionPixelSize(
                R.styleable.ToolBarTitleView_Toolbar_right_size,
                mToolBarTitleRightTestSize
            )
            if (mToolBarImgRight?.visibility == View.VISIBLE) {
                mToolBarImgRight?.visibility = View.GONE
            }
            mToolBarTvRight?.visibility = View.VISIBLE
            mToolBarTvRight?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                mToolBarTitleRightTestSize.toFloat()
            )
        }

        //获取左边的图标
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_left_img_Drawable)) {
            mToolBarLeftDrawable =
                a.getDrawable(R.styleable.ToolBarTitleView_Toolbar_left_img_Drawable)
            if (mToolBarLeftDrawable != null) {
                mToolBarRightDrawable?.let { it.callback = this@ToolBarTitleView }
                mToolBarImgLeft?.setImageDrawable(mToolBarLeftDrawable)
            }
        }
        //是否隐藏左边的图标
        val isVisibleLeftImage =
            a.getBoolean(R.styleable.ToolBarTitleView_Toolbar_left_img_Drawable_visible, true)
        mToolBarImgLeft?.visibility = if (isVisibleLeftImage) View.VISIBLE else GONE
        //获取右边的图标
        if (a.hasValue(R.styleable.ToolBarTitleView_Toolbar_right_img_Drawable)) {
            mToolBarRightDrawable =
                a.getDrawable(R.styleable.ToolBarTitleView_Toolbar_right_img_Drawable)
            if (mToolBarRightDrawable != null) {
                if (mToolBarTvRight?.visibility == View.VISIBLE) {
                    mToolBarTvRight?.visibility = View.GONE
                }
                mToolBarImgRight?.visibility = View.VISIBLE
                mToolBarRightDrawable?.let { it.callback = this@ToolBarTitleView }
                mToolBarImgRight?.setImageDrawable(mToolBarRightDrawable)
            }
        }
        resetLayoutParams()
        a.recycle()
        addView(rootView)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbar_img_left -> {
                //左边图标
                mListener?.onLeftClick()
            }
            R.id.toolbar_tv_right -> {
                //右边文本
                mListener?.onRightTvClick()
            }
            R.id.toolbar_img_right -> {
                //右边图标
                mListener?.onRightImgClick()
            }
        }
    }

    /**
     * 对接提供的接口
     */
    interface OnToolBarListener {
        fun onLeftClick() {}
        fun onRightTvClick() {}
        fun onRightImgClick() {}
    }

    private var mListener: OnToolBarListener? = null
    fun setOnToolBarListener(listener: OnToolBarListener) {
        mListener = listener
    }

    /**
     * dp转px
     */
    fun dp2px(context: Context, dpVal: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.resources.displayMetrics
        ).toInt()
    }

    /**
     * 计算左右两边需要margin多少出来
     */
    private fun calculateCenterTitleViewMargin(): Int {
        //左边按钮宽度
        var width = 0
        var leftWidth = 0
        var rightTvWidth = 0
        var rightImgWidth = 0
        //计算左边按钮占用宽度
        mToolBarImgLeft?.let {
            if (it.visibility == VISIBLE) {
                leftWidth = it.paddingStart + it.paddingEnd + it.marginStart + it.marginEnd
            }
        }
        //计算右边tv 的宽度
        mToolBarTvRight?.let {
            if (it.visibility == VISIBLE) {
                rightTvWidth = it.paddingStart + it.paddingEnd + it.marginStart + it.marginEnd
            }
        }
        //计算右边图标宽度
        mToolBarImgRight?.let {
            if (it.visibility == VISIBLE) {
                rightImgWidth = it.paddingStart + it.paddingEnd + it.marginStart + it.marginEnd
            }
        }
        width = max(max(leftWidth, rightTvWidth), rightImgWidth)
        return dp2px(context, width.toFloat())
    }

    /**
     * 重新设置LayoutParams
     */
    private fun resetLayoutParams() {
        val margin = calculateCenterTitleViewMargin()
        if (margin != 0) {
            val layoutParams = mToolBarTitle?.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.marginStart = margin
            layoutParams.marginEnd = margin
            mToolBarTitle?.let { it.layoutParams = layoutParams }
        }
    }

    //————————————————————————————————————————————————————对外提供的方法

    /**
     * 设置标题
     * @hide
     */
    fun setTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            //计算中间title可以获取多少空间
            resetLayoutParams()
            mToolBarTitle?.text = title
        }
    }

    /**
     * 设置右边标题
     */
    fun setRightTitle(title: String) {
        if (!TextUtils.isEmpty(title)) {
            mToolBarTvRight?.text = title
            resetLayoutParams()
        }
    }

    /**
     * 设置左边图标
     */
    fun setToolBarImgLeft(@DrawableRes resId: Int) {
        if (mToolBarImgLeft != null) {
            mToolBarImgLeft?.setImageResource(resId)
            resetLayoutParams()
        }
    }

    /**
     * 设置右边边图标
     */
    fun setToolBarImgRight(@DrawableRes resId: Int) {
        if (mToolBarImgRight != null) {
            mToolBarImgRight?.setImageResource(resId)
            resetLayoutParams()
        }
    }

    fun getTitleTextView(): TextView? {
        return mToolBarTitle
    }
}


