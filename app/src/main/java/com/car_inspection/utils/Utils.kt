package com.car_inspection.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.car_inspection.BuildConfig
import com.car_inspection.app.Constants
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

fun View.OnClickListener.listenToViews(vararg views: View) = views.forEach { it.setOnClickListener(this) }

fun View.OnKeyListener.listenKeyToViews(vararg views: View) = views.forEach { it.setOnKeyListener(this) }

fun View.OnFocusChangeListener.focusToViews(vararg views: View) = views.forEach { it.onFocusChangeListener = this }

fun textEmty(vararg view: TextView?) = view.forEach { it?.text = Constants.BLANK }

//fun hideView(vararg views: View) = views.forEach { it.hide() }

fun isDebug(): Boolean = BuildConfig.FLAVOR == Constants.DEV

fun disposableAll(vararg disposable: Disposable?) = disposable.forEach { it?.dispose() }

fun returnBody(value: String): RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)

fun getStringFromEditText(content: EditText): String = content.text.toString().trim()

