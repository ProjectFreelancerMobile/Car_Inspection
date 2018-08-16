package com.car_inspection.library.commonview


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.car_inspection.R
import java.util.*

class CustomProgress : TextView {

    private var progressDrawable: ShapeDrawable? = null
    private var textView: TextView? = null
    private var mWidth:Int = 0
    private var mMaxWidth = 0
    private var mMaxHeight = 0
    private var progressColor: Int = 0
    private var progressBackgroundColor: Int = 0
    private var progressShape = SHAPE_RECTANGLE
    private var maximumPercentage = .0f
    private var cornerRadius = 25.0f
    /**
     * If this returns true the custom progress
     * will show progress based on getCurrentPercentage()
     * @return true for showing percentage false for not showing anything
     */
    /**
     * Set if the custom progress will show percentage or not
     * @param showingPercentage true for showing percentage false for not showing anything
     */
    var isShowingPercentage = false
    private var speed = 20
    private var resetToZero = false

    /**
     * Get current percentage based on current width
     * @return
     */
    val currentPercentage: Int
        get() = Math.ceil((mWidth / (mMaxWidth * 1.0f) * 100).toDouble()).toInt()

    //Constructor

    constructor(context: Context) : super(context) {
        setDefaultValue()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setDefaultValue()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setDefaultValue()
    }

    private fun setDefaultValue() {
        // default color
        progressColor = resources.getColor(R.color.colorGray)
        progressBackgroundColor = resources.getColor(R.color.colorWhite)
    }

    //View Lifecycle

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            initView()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        progressDrawable!!.setBounds(0, 0, mWidth, this.height)
        progressDrawable!!.draw(canvas)
        if (isShowingPercentage) {
            this.text = currentPercentage.toString() + "%"
        }
        //if the we want to reset to 0
        if (resetToZero) {
            mWidth = 0
            resetToZero = false
            invalidate()
        } else if (mWidth < mMaxWidth) {
            mWidth += this.speed
            invalidate()
        }
    }

    /**
     * Initialize the view before it will be drawn
     */
    private fun initView() {
        var progressShapeDrawable: Shape? = null
        var backgroundProgressShapeDrawable: Shape? = null
        when (progressShape) {
            SHAPE_RECTANGLE -> {
                progressShapeDrawable = RectShape()
                backgroundProgressShapeDrawable = RectShape()
            }
            SHAPE_ROUNDED_RECTANGLE -> {
                val outerRadius = FloatArray(8)
                Arrays.fill(outerRadius, cornerRadius)
                progressShapeDrawable = RoundRectShape(outerRadius, null, null)
                backgroundProgressShapeDrawable = RoundRectShape(outerRadius, null, null)
            }
        }

        //Progress
        progressDrawable = ShapeDrawable(progressShapeDrawable)
        progressDrawable!!.paint.color = progressColor
        if (this.text.length > 0 || isShowingPercentage) {
            progressDrawable!!.alpha = 100
        }

        //Background
        val backgroundDrawable = ShapeDrawable(backgroundProgressShapeDrawable)
        backgroundDrawable.paint.color = progressBackgroundColor
        backgroundDrawable.setBounds(0, 0, this.getWidth(), this.height)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.background = backgroundDrawable
        } else {
            this.setBackgroundDrawable(backgroundDrawable)
        }

        this.mMaxWidth = (this.getWidth() * maximumPercentage).toInt()

        //Percentage
        if (isShowingPercentage) {
            this.textSize = 20f
            this.setTextColor(Color.WHITE)
            this.gravity = Gravity.CENTER
        }
    }

    //Helper

    /**
     * Set the progress color
     * @param color
     */
    fun setProgressColor(color: Int) {
        this.progressColor = color
    }

    /**
     * Set the background color
     * @param color
     */
    fun setProgressBackgroundColor(color: Int) {
        this.progressBackgroundColor = color
    }

    /**
     * Reset the progress to 0
     */
    fun resetWidth() {
        mWidth = 0
    }

    /**
     * Set the maximum percentage for the progress
     * @param percentage
     */
    fun setMaximumPercentage(percentage: Float) {
        this.maximumPercentage = percentage
    }

    /**
     * Set the shape of custom progress to rectangle
     */
    fun useRectangleShape() {
        this.progressShape = SHAPE_RECTANGLE
    }

    /**
     * Set the shape of custom progress to rounded rectangle
     * @param cornerRadius radius of the corner
     */
    fun useRoundedRectangleShape(cornerRadius: Float) {
        this.progressShape = SHAPE_ROUNDED_RECTANGLE
        this.cornerRadius = cornerRadius
    }

    /**
     * Set the speed of the movement of the progress
     * @param speed as an int it should range from [1,100]
     */
    fun setSpeed(speed: Int) {
        this.speed = speed
    }

    /**
     * call the function when you want to update view
     */
    fun updateView() {
        resetToZero = true
        initView()
        invalidate()
    }

    companion object {

        private val SHAPE_RECTANGLE = 0
        private val SHAPE_ROUNDED_RECTANGLE = 1
        private val DEFAULT_TEXT_MARGIN = 10
    }
}