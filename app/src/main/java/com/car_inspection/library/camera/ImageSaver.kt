package com.car_inspection.library.camera

import android.media.Image
import android.util.Log
import com.blankj.utilcode.util.ImageUtils
import java.io.File
import java.io.IOException
import android.graphics.BitmapFactory
import android.graphics.Bitmap

/**
 * Saves a JPEG [Image] into the specified [File].
 */
internal class ImageSaver(
        /**
         * The JPEG image
         */
        private val image: Image,

        /**
         * The file we save the image into.
         */
        private val file: File
) : Runnable {

    override fun run() {
        try {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        val myBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        ImageUtils.save(myBitmap,file, Bitmap.CompressFormat.JPEG,true)
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } finally {
            image.close()
        }
    }

    companion object {
        /**
         * Tag for the [Log].
         */
        private val TAG = "ImageSaver"
    }
}
