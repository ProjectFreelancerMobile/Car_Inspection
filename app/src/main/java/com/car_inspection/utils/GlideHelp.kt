package com.car_inspection.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.request.target.Target
import com.car_inspection.R

fun glideHelp(context: Context, url: String, imageView: ImageView) {
    GlideApp.with(context)
            .load("$url?img.contain=${imageView.width}x${imageView.height}")
            .dontAnimate()
            .into(imageView)
}

fun glideHelpVertival(context: Context, url: String, imageView: ImageView) {
    GlideApp.with(context)
            .load("$url?img.contain=180x270")
            .dontAnimate()
            .into(imageView)
}

fun glideHelpHorizone(context: Context, url: String, imageView: ImageView) {
    GlideApp.with(context)
            .load("$url?img.contain=500x430")
            .dontAnimate()
            .into(imageView)
}

fun glideHelpHorizoneCrop(context: Context, url: String, imageView: ImageView) {
    GlideApp.with(context)
            .load("$url?img.crop=500x360")
            .centerCrop()
            .dontAnimate()
            .into(imageView)
}

fun glideHelp(context: Context, url: String, size: Int, target: Target<Bitmap>) {
    GlideApp.with(context)
            .asBitmap()
            .load("$url?img.contain=${size}x$size")
            .override(size,size)
            .centerInside()
            .dontAnimate()
            .error(R.mipmap.ic_launcher)
            .into(target)
}

fun glideHelpDrawable(context: Context, drawable: Int, imageView: ImageView) {
    GlideApp.with(context)
            .load(drawable)
            .dontAnimate()
            .error(R.mipmap.ic_launcher)
            .into(imageView)
}

