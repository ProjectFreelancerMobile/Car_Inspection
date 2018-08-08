package com.car_inspection.library

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.TextureView

class FixedRatioCroppedTextureView : TextureView {
    private var mPreviewWidth: Int = 0
    private var mPreviewHeight: Int = 0
    private var mCroppedWidthWeight: Int = 0
    private var mCroppedHeightWeight: Int = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        if (measuredWidth == 0 || measuredHeight == 0) {
            return
        }

        var width: Int
        var height: Int
        if (MiscUtils.isOrientationLandscape(context)) {
            height = measuredHeight
            width = heightToWidth(measuredHeight)
            if (width > measuredWidth) {
                width = measuredWidth
                height = widthToHeight(width)
            }
        } else {
            width = measuredWidth
            height = widthToHeight(measuredWidth)
            if (height > measuredHeight) {
                height = measuredHeight
                width = heightToWidth(height)
            }
        }
        setMeasuredDimension(width, height)
    }

    private fun widthToHeight(width: Int): Int {
        return width * mCroppedHeightWeight / mCroppedWidthWeight
    }

    private fun heightToWidth(height: Int): Int {
        return height * mCroppedWidthWeight / mCroppedHeightWeight
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        val actualPreviewWidth: Int
        val actualPreviewHeight: Int
        val top: Int
        val left: Int
        if (MiscUtils.isOrientationLandscape(context)) {
            actualPreviewHeight = b - t
            actualPreviewWidth = actualPreviewHeight * mPreviewWidth / mPreviewHeight
            left = l + (r - l - actualPreviewWidth) / 2
            top = t
        } else {
            actualPreviewWidth = r - l
            actualPreviewHeight = actualPreviewWidth * mPreviewHeight / mPreviewWidth
            top = t + (b - t - actualPreviewHeight) / 2
            left = l
        }
        super.layout(left, top, left + actualPreviewWidth, top + actualPreviewHeight)
    }

    fun setPreviewSize(previewWidth: Int, previewHeight: Int) {
        mPreviewWidth = previewWidth
        mPreviewHeight = previewHeight
    }

    fun setCroppedSizeWeight(widthWeight: Int, heightWeight: Int) {
        this.mCroppedWidthWeight = widthWeight
        this.mCroppedHeightWeight = heightWeight
    }
}
