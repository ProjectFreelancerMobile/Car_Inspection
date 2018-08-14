package com.car_inspection.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.car_inspection.BuildConfig
import com.car_inspection.app.Constants
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix


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

fun isCameraOTG(): Boolean = false

fun disposableAll(vararg disposable: Disposable?) = disposable.forEach { it?.dispose() }

fun returnBody(value: String): RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)

fun getStringFromEditText(content: EditText): String = content.text.toString().trim()

fun getTimeStamFromDate(dateString: String): Long {
    var result: Long = 0
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    var date: Date? = null
    try {
        date = formatter.parse(dateString) as Date
        result = date!!.getTime()
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return result
}

fun getDate(timstamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.setTime(Date(timstamp))
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH)
    val year = calendar.get(Calendar.YEAR)
    return formatTimeNumber(day) + "/" + formatTimeNumber(month + 1) + "/" + formatTimeNumber(year)
}

fun getTime(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.setTime(Date(timestamp))
    val h = calendar.get(Calendar.HOUR_OF_DAY)
    val m = calendar.get(Calendar.MINUTE)
    return formatTimeNumber(h) + ":" + formatTimeNumber(m)
}

fun formatTimeNumber(number: Int): String {
    return if (number < 10)
        "0$number"
    else
        number.toString() + ""
}

fun createFolderPicture(path: String) {
    var folder = File(path)
    if (!folder.exists())
        folder.mkdirs()
}

fun overlay(bmp1: Bitmap, bmp2: Bitmap): Bitmap {
    val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
    val canvas = Canvas(bmOverlay)
    canvas.drawBitmap(bmp1, Matrix(), null)
    canvas.drawBitmap(bmp2, Matrix(), null)
    return bmOverlay
}

