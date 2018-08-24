package com.car_inspection.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.car_inspection.BuildConfig
import com.car_inspection.R
import com.car_inspection.app.Constants
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

fun View.OnClickListener.listenToViews(vararg views: View) = views.forEach { it.setOnClickListener(this) }

fun View.OnKeyListener.listenKeyToViews(vararg views: View) = views.forEach { it.setOnKeyListener(this) }

fun View.OnFocusChangeListener.focusToViews(vararg views: View) = views.forEach { it.onFocusChangeListener = this }

fun textEmty(vararg view: TextView?) = view.forEach { it?.text = Constants.BLANK }

//fun hideView(vararg views: View) = views.forEach { it.hide() }

fun isDebug(): Boolean = BuildConfig.BUILD_TYPE == Constants.DEBUG

fun disposableAll(vararg disposable: Disposable?) = disposable.forEach { it?.dispose() }

fun returnBody(value: String): RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)

fun getStringFromEditText(content: EditText): String = content.text.toString().trim()

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

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    return inSampleSize
}
private fun decodeSampledBitmapFromResource(res: Resources, resId: Int,
                                            reqWidth: Int, reqHeight: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}
fun overlay(context: Context, filePath: String, text: String) {
    var icon = decodeSampledBitmapFromResource(context.resources, R.drawable.logo, 150,60)
    icon = Bitmap.createScaledBitmap(icon, 150, 60, true)
    val bmp1 = BitmapFactory.decodeFile(filePath)
    if(icon == null || bmp1 == null){
        return
    }
    val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
    val canvas = Canvas(bmOverlay)
    val paint = Paint()
    val r = Rect()
    canvas.getClipBounds(r)
    val cHeight = r.height()
    val cWidth = r.width()
    paint.textAlign = Paint.Align.LEFT
    paint.color = Color.WHITE
    paint.textSize = 16f
    paint.getTextBounds(text, 0, text.length, r)
    val x = bmp1.width - 135f
    val y = bmp1.height - 70f

    canvas.drawBitmap(bmp1, Matrix(), null)
    canvas.drawText(text, x, y, paint)
    canvas.drawBitmap(icon, bmp1.width - 150f, bmp1.height - 60f, null)
    var out: FileOutputStream? = null
    try {
        out = FileOutputStream(filePath)
        bmOverlay.compress(Bitmap.CompressFormat.PNG, 100, out) // bmp is your Bitmap instance
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            out?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

