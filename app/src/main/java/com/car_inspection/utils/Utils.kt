package com.car_inspection.utils

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.car_inspection.BuildConfig
import com.car_inspection.R
import com.car_inspection.app.Constants
import com.car_inspection.ui.record.RecordFragment
import io.reactivex.disposables.Disposable
import okhttp3.MediaType
import okhttp3.RequestBody
import pyxis.uzuki.live.richutilskt.utils.runAsync
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
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

fun isDebug(): Boolean = BuildConfig.FLAVOR == Constants.DEBUG

fun disposableAll(vararg disposable: Disposable?) = disposable.forEach { it?.dispose() }

fun returnBody(value: String): RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), value)

fun getStringFromEditText(content: EditText): String = content.text.toString().trim()

fun formatTimeNumber(number: Int): String {
    return if (number < 10)
        "0$number"
    else
        number.toString() + ""
}

fun formatTime(timeInSeconds: Int): String {
    var result = ""
    var seconds = timeInSeconds
    var hours = 0
    var minutes = 0
    if (seconds > 3600) {
        hours = seconds / 3600
        seconds = seconds - hours * 3600
    }
    if (seconds > 60) {
        minutes = seconds / 60
        seconds = seconds - minutes * 60
    }
    if (hours > 0)
        result = "${formatTimeNumber(hours)}:"
    result = result + formatTimeNumber(minutes) + ":" + formatTimeNumber(seconds)

    return result
}

fun createFolderPicture(path: String) {
    var folder = File(path)
    if (!folder.exists())
        folder.mkdirs()
}

fun getImageContentUri(context: Context?, absPath: String): Uri? {
    val cursor = context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID), MediaStore.Images.Media.DATA + "=? ", arrayOf(absPath), null)
    return if (cursor != null && cursor.moveToFirst()) {
        val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
        Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id))

    } else if (!absPath.isEmpty()) {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DATA, absPath)
        context?.contentResolver?.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    } else {
        null
    }
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
    runAsync{
        var icon = decodeSampledBitmapFromResource(context.resources, R.drawable.logo, 150, 60)
        icon = Bitmap.createScaledBitmap(icon, 150, 60, true)
        val bmp1 = BitmapFactory.decodeFile(filePath)
        if (icon == null || bmp1 == null) {
            return@runAsync
        }
        val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
        val canvas = Canvas(bmOverlay)
        val paint = Paint()
        val r = Rect()
        canvas.getClipBounds(r)
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
}

fun getSaveImageFilePath(fileName: String, view: View): String {

    createFolderPicture(Constanst.getFolderPictureInsertVideoPath())

    var imageFile = File(Constanst.getFolderPictureInsertVideoPath() + "$fileName")

    val selectedOutputPath = imageFile.absolutePath

    view.setDrawingCacheEnabled(true)
    view.buildDrawingCache()
    var bitmap = Bitmap.createBitmap(view.getDrawingCache())

    val maxSize = 1080

    val bWidth = bitmap.getWidth()
    val bHeight = bitmap.getHeight()

    if (bWidth > bHeight) {
        val imageHeight = Math.abs(maxSize * (bitmap.getWidth() as Float / bitmap.getHeight() as Float)).toInt()
        bitmap = Bitmap.createScaledBitmap(bitmap, maxSize, imageHeight, true)
    } else {
        val imageWidth = Math.abs(maxSize * (bitmap.getWidth() as Float / bitmap.getHeight() as Float)).toInt()
        bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, maxSize, true)
    }
    view.setDrawingCacheEnabled(false)
    view.destroyDrawingCache()

    var fOut: OutputStream? = null
    try {
        fOut = FileOutputStream(imageFile)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        fOut!!.flush()
        fOut!!.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return selectedOutputPath
}
fun chooseOptimalSize(choices: Array<Size>, textureViewWidth: Int, textureViewHeight: Int, maxWidth: Int, maxHeight: Int, aspectRatio: Size): Size {

    // Collect the supported resolutions that are at least as big as the preview Surface
    val bigEnough = ArrayList<Size>()
    // Collect the supported resolutions that are smaller than the preview Surface
    val notBigEnough = ArrayList<Size>()
    val w = aspectRatio.width
    val h = aspectRatio.height
    for (option in choices) {
        if (option.width <= maxWidth && option.height <= maxHeight &&
                option.height == option.width * h / w) {
            if (option.width >= textureViewWidth && option.height >= textureViewHeight) {
                bigEnough.add(option)
            } else {
                notBigEnough.add(option)
            }
        }
    }

    // Pick the smallest of those big enough. If there is no one big enough, pick the
    // largest of those not big enough.
    if (bigEnough.size > 0) {
        return Collections.min(bigEnough, RecordFragment.CompareSizesByArea())
    } else if (notBigEnough.size > 0) {
        return Collections.max(notBigEnough, RecordFragment.CompareSizesByArea())
    } else {
        Log.e("chooseOptimalSize=", "Couldn't find any suitable preview size")
        return choices[0]
    }
}

private fun store(bm: Bitmap, imageFile: File) {
    try {
        val fOut = FileOutputStream(imageFile)
        bm.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}
