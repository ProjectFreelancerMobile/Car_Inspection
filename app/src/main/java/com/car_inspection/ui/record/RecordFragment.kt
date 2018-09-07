package com.car_inspection.ui.record

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.car_inspection.R
import com.car_inspection.data.model.FrameToRecord
import com.car_inspection.data.model.RecordModel
import com.car_inspection.library.CameraHelper
import com.car_inspection.library.MiscUtils
import com.car_inspection.listener.CameraRecordListener
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.step.StepFragment
import com.car_inspection.utils.*
import com.github.florent37.camerafragment.CameraFragment
import com.github.florent37.camerafragment.configuration.Configuration
import com.github.florent37.camerafragment.listeners.CameraFragmentResultAdapter
import com.orhanobut.logger.Logger
import com.toan_itc.core.utils.addFragment
import com.toan_itc.core.utils.removeFragment
import kotlinx.android.synthetic.main.record_fragment.*
import org.bytedeco.javacpp.avcodec
import org.bytedeco.javacpp.avutil
import org.bytedeco.javacv.FFmpegFrameFilter
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Frame
import org.bytedeco.javacv.FrameFilter
import pyxis.uzuki.live.richutilskt.utils.runDelayedOnUiThread
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ShortBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class RecordFragment : BaseFragment(), TextureView.SurfaceTextureListener, View.OnClickListener, CameraRecordListener {
    private val LOG_TAG = RecordOTGFragment::class.java.simpleName

    private val REQUEST_PERMISSIONS = 1

    private val PREFERRED_PREVIEW_WIDTH = 720
    private val PREFERRED_PREVIEW_HEIGHT = 480

    // both in milliseconds
    private val MIN_VIDEO_LENGTH = (1 * 1000).toLong()
    private val MAX_VIDEO_LENGTH = (90 * 1000).toLong()
    private var mCameraId: Int = 0
    private var mCamera: Camera? = null
    private var mFrameRecorder: FFmpegFrameRecorder? = null
    private var mVideoRecordThread: VideoRecordThread? = null
    private var mAudioRecordThread: AudioRecordThread? = null
    @Volatile
    private var mRecording = false
    private var mVideo: File? = null
    private var mFrameToRecordQueue: LinkedBlockingQueue<FrameToRecord>? = null
    private var mRecycledFrameQueue: LinkedBlockingQueue<FrameToRecord>? = null
    private var mFrameToRecordCount: Int = 0
    private var mFrameRecordedCount: Int = 0
    private var mTotalProcessFrameTime: Long = 0
    private var mRecordFragments: Stack<RecordModel>? = null
    private var currentSubStepName = ""
    private var currentStep = 2
    private val sampleAudioRateInHz = 44100
    /* The sides of width and height are based on camera orientation.
    That is, the preview size is the size before it is rotated. */
    private var mPreviewWidth = PREFERRED_PREVIEW_WIDTH
    private var mPreviewHeight = PREFERRED_PREVIEW_HEIGHT
    // Output video size
    private var videoWidth = 320
    private var videoHeight = 320
    private val frameRate = 30
    private val frameDepth = Frame.DEPTH_UBYTE
    private val frameChannels = 2
    private var takeFragment: CameraFragment? = null
    // Workaround for https://code.google.com/p/android/issues/detail?id=190966
    private var doAfterAllPermissionsGranted: Runnable? = null

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean = true

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, p1: Int, p2: Int) {
        surface?.let {
            Logger.e("onSurfaceTextureAvailable")
            startPreview(it)
        }
    }

    companion object {
        fun newInstance() = RecordFragment()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnExit1 -> recordEvent()
            R.id.mBtnReset -> {
                pauseRecording()
                object : ProgressDialogTask<Void, Int, Void>(R.string.please_wait) {
                    override fun doInBackground(vararg params: Void): Void? {
                        stopRecording()
                        stopRecorder()
                        startRecorder()
                        startRecording()
                        return null
                    }
                }.execute()
            }
            R.id.mBtnDone -> {
                pauseRecording()
                Logger.e("1111")
                mRecordFragments?.apply {
                    // check video length
                    Logger.e("recordedTime=" + calculateTotalRecordedTime(this) + "MIN_VIDEO_LENGTH=" + MIN_VIDEO_LENGTH)
                    if (calculateTotalRecordedTime(this) < MIN_VIDEO_LENGTH) {
                        Toast.makeText(context, R.string.video_too_short, Toast.LENGTH_SHORT).show()
                        return
                    }
                    FinishRecordingTask().execute()
                }
            }
            R.id.mBtnResumeOrPause -> {
                if (mRecording) {
                    pauseRecording()
                } else {
                    resumeRecording()
                }
            }
            R.id.mBtnSwitchCamera -> {
                startPreview()
            }
            R.id.mBtnTake -> {
                if (takeFragment != null) {
                    createFolderPicture(Constanst.getFolderPicturePath())
                    takeFragment?.takePhotoOrCaptureVideo(object : CameraFragmentResultAdapter() {
                        override fun onPhotoTaken(bytes: ByteArray, filePath: String) {
                            Logger.e("file images----------@@@@@@@@@@@@@@   $filePath")
                            // stepAdapter.items?.get(currentPosition)?.imagepaths?.add(filePath)
                            //showLayoutVideo()
                            showSnackBar("Save picture path：$filePath")
                            overlay(activity!!, filePath, currentSubStepName)
                            activity?.removeFragment(takeFragment!!)
                            recordEvent()
                        }
                    }, Constanst.getFolderPicturePath(), "Step$currentStep/$currentSubStepName")

                }
            }
        }
    }

    override fun initViews() {
        videoWidth = pxToDp(Constanst.heightScreen / 2).toInt()
        videoHeight = pxToDp(Constanst.widthScreen).toInt()

        var params = fragmentCapture.layoutParams
        params.height = Constanst.widthScreen
        params.width = Constanst.heightScreen/2
        fragmentCapture.setLayoutParams(params)
        initRecord()
    }

    override fun setLayoutResourceID(): Int = R.layout.record_fragment

    override fun initData() {

    }

    private fun initRecord() {
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK
        setPreviewSize(mPreviewWidth, mPreviewHeight)
        cameraPreview.setCroppedSizeWeight(videoWidth, videoHeight)
        cameraPreview.surfaceTextureListener = this
        // At most buffer 10 Frame
        mFrameToRecordQueue = LinkedBlockingQueue(10)
        // At most recycle 2 Frame
        mRecycledFrameQueue = LinkedBlockingQueue(2)
        mRecordFragments = Stack()
        listenToViews(mBtnReset, mBtnDone, mBtnResumeOrPause, mBtnSwitchCamera, mBtnTake, btnExit1)
    }

    private fun startPreview() {
        cameraPreview?.apply {
            val surfaceTexture = surfaceTexture
            object : ProgressDialogTask<Void, Int, Void>(R.string.please_wait) {
                override fun doInBackground(vararg params: Void): Void? {
                    stopRecording()
                    stopPreview()
                    releaseCamera()
                    //mCameraId = (mCameraId + 1) % 2
                    acquireCamera()
                    startPreview(surfaceTexture)
                    startRecording()
                    return null
                }
            }.execute()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecorder()
        releaseRecorder(true)
    }

    override fun onResume() {
        super.onResume()
        if (doAfterAllPermissionsGranted != null) {
            doAfterAllPermissionsGranted?.run()
            doAfterAllPermissionsGranted = null
        } else {
            val neededPermissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val deniedPermissions = ArrayList<String>()
            for (permission in neededPermissions) {
                if (ContextCompat.checkSelfPermission(context!!, permission) !== PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission)
                }
            }
            if (deniedPermissions.isEmpty()) {
                // All permissions are granted
                doAfterAllPermissionsGranted()
            } else {
                var array = arrayOfNulls<String>(deniedPermissions.size)
                array = deniedPermissions.toTypedArray()
                ActivityCompat.requestPermissions(activity!!, array, REQUEST_PERMISSIONS)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pauseRecording()
        stopRecording()
        stopPreview()
        releaseCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            var permissionsAllGranted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    permissionsAllGranted = false
                    break
                }
            }
            doAfterAllPermissionsGranted = if (permissionsAllGranted) {
                Runnable { doAfterAllPermissionsGranted }
            } else {
                Runnable {
                    Toast.makeText(context, R.string.permissions_denied_exit, Toast.LENGTH_SHORT).show()
                    activity?.finish()
                }
            }
        }
    }

    private fun doAfterAllPermissionsGranted() {
        acquireCamera()
        val surfaceTexture = cameraPreview.surfaceTexture
        if (surfaceTexture != null) {
            // SurfaceTexture already created
            Logger.e("doAfterAllPermissionsGranted")
            startPreview(surfaceTexture)
        }
        object : ProgressDialogTask<Void, Int, Void>(R.string.initiating) {

            override fun doInBackground(vararg params: Void): Void? {
                if (mFrameRecorder == null) {
                    initRecorder()
                    startRecorder()
                }
                startRecording()
                return null
            }
        }.execute()
    }

    private fun setPreviewSize(width: Int, height: Int) {
        cameraPreview?.apply {
            if (MiscUtils.isOrientationLandscape(context)) {
                setPreviewSize(width, height)
            } else {
                // Swap width and height
                setPreviewSize(height, width)
            }
        }
    }

    private fun startPreview(surfaceTexture: SurfaceTexture) {
        if (mCamera == null) {
            return
        }
        Logger.e("startPreview")
        mCamera?.apply {
            val parameters = parameters
            val previewSizes = parameters.supportedPreviewSizes
            val previewSize = CameraHelper.getOptimalSize(previewSizes,
                    PREFERRED_PREVIEW_WIDTH, PREFERRED_PREVIEW_HEIGHT)
            // if changed, reassign values and request layout
            previewSize?.apply {
                if (mPreviewWidth != width || mPreviewHeight != height) {
                    mPreviewWidth = width
                    mPreviewHeight = height
                    setPreviewSize(mPreviewWidth, mPreviewHeight)
                    cameraPreview?.requestLayout()
                }
            }
            parameters.setPreviewSize(mPreviewWidth, mPreviewHeight)
            //        parameters.setPreviewFormat(ImageFormat.NV21);
            setParameters(parameters)

            setDisplayOrientation(CameraHelper.getCameraDisplayOrientation(activity!!, mCameraId))

            // YCbCr_420_SP (NV21) format
            val bufferByte = ByteArray(mPreviewWidth * mPreviewHeight * 3 / 2)
            addCallbackBuffer(bufferByte)
            setPreviewCallbackWithBuffer(object : Camera.PreviewCallback {

                private var lastPreviewFrameTime: Long = 0

                override fun onPreviewFrame(data: ByteArray, camera: Camera) {
                    val thisPreviewFrameTime = System.currentTimeMillis()
                    /*if (lastPreviewFrameTime > 0) {
                        Log.d(LOG_TAG, "Preview frame interval: " + (thisPreviewFrameTime - lastPreviewFrameTime) + "ms")
                    }*/
                    lastPreviewFrameTime = thisPreviewFrameTime

                    // get video data
                    if (mRecording) {
                        mAudioRecordThread?.apply {
                            if (mAudioRecordThread == null || !isRunning) {
                                // wait for AudioRecord to init and start
                                mRecordFragments?.peek()?.startTimestamp = System.currentTimeMillis()
                            } else {
                                // pop the current record fragment when calculate total recorded time
                                val curFragment = mRecordFragments?.pop()
                                val recordedTime = calculateTotalRecordedTime(mRecordFragments)
                                // push it back after calculation
                                mRecordFragments?.push(curFragment)
                                val curRecordedTime = System.currentTimeMillis() - curFragment?.startTimestamp!! + recordedTime
                                // check if exceeds time limit
                                if (curRecordedTime > MAX_VIDEO_LENGTH) {
                                    pauseRecording()
                                    FinishRecordingTask().execute()
                                    return
                                }

                                val timestamp = 1000 * curRecordedTime
                                val frame: Frame?
                                var frameToRecord: FrameToRecord? = mRecycledFrameQueue?.poll()
                                if (frameToRecord != null) {
                                    frame = frameToRecord.frame
                                    frameToRecord.timestamp = timestamp
                                } else {
                                    frame = Frame(mPreviewWidth, mPreviewHeight, frameDepth, frameChannels)
                                    frameToRecord = FrameToRecord(timestamp, frame)
                                }
                                (frame?.image?.get(0)?.position(0) as? ByteBuffer)?.put(data)
                                if (mFrameToRecordQueue?.offer(frameToRecord)!!) {
                                    mFrameToRecordCount++
                                }
                            }
                        }
                    }
                    addCallbackBuffer(data)
                }
            })
            try {
                setPreviewTexture(surfaceTexture)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
            startPreview()
        }
    }

    private fun stopPreview() {
        mCamera?.apply {
            stopPreview()
            setPreviewCallbackWithBuffer(null)
        }
    }

    private fun acquireCamera() {
        try {
            mCamera = Camera.open(mCameraId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releaseCamera() {
        if (mCamera != null) {
            mCamera?.release()        // release the camera for other applications
            mCamera = null
        }
    }

    private fun initRecorder() {
        Log.i(LOG_TAG, "init mFrameRecorder")

        val recordedTime = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        mVideo = CameraHelper.getOutputMediaFile(recordedTime, CameraHelper.MEDIA_TYPE_VIDEO)
        Log.i(LOG_TAG, "Output Video: $mVideo")

        mFrameRecorder = FFmpegFrameRecorder(mVideo, videoWidth, videoHeight, 1)
        mFrameRecorder?.apply {
            format = "mp4"
            sampleRate = sampleAudioRateInHz
            frameRate = frameRate
            videoCodec = avcodec.AV_CODEC_ID_H264
            setVideoOption("crf", "28")
            setVideoOption("tune", "zerolatency")
            setVideoOption("preset", "superfast")
            // See: https://trac.ffmpeg.org/wiki/Encode/H.264#crf
            /*
             * The range of the quantizer scale is 0-51: where 0 is lossless, 23 is default, and 51 is worst possible. A lower value is a higher quality and a subjectively sane range is 18-28. Consider 18 to be visually lossless or nearly so: it should look the same or nearly the same as the input but it isn't technically lossless.
             * The range is exponential, so increasing the CRF value +6 is roughly half the bitrate while -6 is roughly twice the bitrate. General usage is to choose the highest CRF value that still provides an acceptable quality. If the output looks good, then try a higher value and if it looks bad then choose a lower value.
             */
        }
        Log.i(LOG_TAG, "mFrameRecorder initialize success")
    }

    private fun releaseRecorder(deleteFile: Boolean) {
        /*mFrameRecorder?.apply {
            try {
                release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mFrameRecorder = null
            if (deleteFile) {
                mVideo?.delete()
            }
        }*/
    }

    private fun startRecorder() {
        try {
            // mFrameRecorder?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun stopRecorder() {
        /*mFrameRecorder?.apply {
            try {
                //stop()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mRecordFragments?.clear()
        activity?.runOnUiThread { mBtnReset?.visibility = View.INVISIBLE }*/
    }

    private fun startRecording() {
        //TODO
        /* Logger.e("startRecording")
         mAudioRecordThread = AudioRecordThread()
         mAudioRecordThread?.start()
         mVideoRecordThread = VideoRecordThread()
         mVideoRecordThread?.start()*/
    }

    private fun stopRecording() {
        //TODO
        /*try {
            mAudioRecordThread?.apply {
                if (isRunning)
                    stopRunning()
                join()
                mAudioRecordThread = null
            }
            mVideoRecordThread?.apply {
                if (isRunning)
                    stopRunning()
                join()
                mVideoRecordThread = null
            }
            mFrameToRecordQueue?.clear()
            mRecycledFrameQueue?.clear()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }*/
    }

    private fun resumeRecording() {
        //TODO
        /*if (!mRecording) {
            val recordModel = RecordModel()
            recordModel.startTimestamp = System.currentTimeMillis()
            mRecordFragments?.push(recordModel)
            *//*activity?.runOnUiThread {
                mBtnReset.visibility = View.VISIBLE
                mBtnSwitchCamera.visibility = View.INVISIBLE
                mBtnResumeOrPause.setText(R.string.pause)
            }*//*
            mRecording = true
        }*/
    }

    private fun pauseRecording() {
        //TODO
        /*if (mRecording) {
            mRecordFragments?.peek()?.endTimestamp = System.currentTimeMillis()
            *//*activity?.runOnUiThread(Runnable {
                mBtnSwitchCamera.visibility = View.VISIBLE
                mBtnResumeOrPause.setText(R.string.resume)
            })*//*
            mRecording = false
        }*/
    }

    private fun calculateTotalRecordedTime(recordModel: Stack<RecordModel>?): Long {
        var recordedTime: Long = 0
        recordModel?.apply {
            for (recordFragment in this) {
                recordedTime += recordFragment.duration
                Logger.e("recordedTime=$recordedTime")
            }
        }
        return recordedTime
    }

    internal open inner class RunningThread : Thread() {
        var isRunning: Boolean = false

        open fun stopRunning() {
            this.isRunning = false
        }
    }

    internal inner class AudioRecordThread : RunningThread() {
        private var mAudioRecord: AudioRecord? = null
        private val audioData: ShortBuffer

        init {
            val bufferSize = AudioRecord.getMinBufferSize(sampleAudioRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT)
            mAudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, sampleAudioRateInHz,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize)
            audioData = ShortBuffer.allocate(bufferSize)
        }

        override fun run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO)
            mAudioRecord?.apply {
                //Logger.e(LOG_TAG+"mAudioRecord startRecording")
                startRecording()
                isRunning = true
                /* ffmpeg_audio encoding loop */
                while (isRunning) {
                    mFrameRecorder?.apply {
                        if (mRecording) {
                            val bufferReadResult = read(audioData.array(), 0, audioData.capacity())
                            audioData.limit(bufferReadResult)
                            if (bufferReadResult > 0) {
                                // Log.v(LOG_TAG, "bufferReadResult: $bufferReadResult")
                                try {
                                    recordSamples(audioData)
                                } catch (e: Exception) {
                                    Log.v(LOG_TAG, e.message)
                                    e.printStackTrace()
                                }

                            }
                        }
                    }
                }
                Log.d(LOG_TAG, "mAudioRecord stopRecording")
                stop()
                release()
                mAudioRecord = null
                Log.d(LOG_TAG, "mAudioRecord released")
            }
        }
    }

    internal inner class VideoRecordThread : RunningThread() {
        override fun run() {
            val previewWidth = mPreviewWidth
            val previewHeight = mPreviewHeight

            val filters = ArrayList<String>()
            // Transpose
            var transpose: String? = null
            var hflip: String? = null
            var vflip: String? = null
            var crop: String? = null
            var scale: String? = null
            val cropWidth: Int
            val cropHeight: Int
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(mCameraId, info)
            val rotation = activity?.windowManager?.defaultDisplay?.rotation
            when (rotation) {
                Surface.ROTATION_0 -> {
                    when (info.orientation) {
                        270 -> if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            transpose = "transpose=clock_flip" // Same as preview display
                        } else {
                            transpose = "transpose=cclock" // Mirrored horizontally as preview display
                        }
                        90 -> if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            transpose = "transpose=cclock_flip" // Same as preview display
                        } else {
                            transpose = "transpose=clock" // Mirrored horizontally as preview display
                        }
                    }
                    cropWidth = previewHeight
                    cropHeight = cropWidth * videoHeight / videoWidth
                    crop = String.format("crop=%d:%d:%d:%d",
                            cropWidth, cropHeight,
                            (previewHeight - cropWidth) / 2, (previewWidth - cropHeight) / 2)
                    // swap width and height
                    scale = String.format("scale=%d:%d", videoHeight, videoWidth)
                }
                Surface.ROTATION_90, Surface.ROTATION_270 -> {
                    when (rotation) {
                        Surface.ROTATION_90 ->
                            // landscape-left
                            when (info.orientation) {
                                270 -> if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                    hflip = "hflip"
                                }
                            }
                        Surface.ROTATION_270 ->
                            // landscape-right
                            when (info.orientation) {
                                90 -> if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                                    hflip = "hflip"
                                    vflip = "vflip"
                                }
                                270 -> if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                                    vflip = "vflip"
                                }
                            }
                    }
                    cropHeight = previewHeight
                    cropWidth = cropHeight * videoWidth / videoHeight
                    crop = String.format("crop=%d:%d:%d:%d",
                            cropWidth, cropHeight,
                            (previewWidth - cropWidth) / 2, (previewHeight - cropHeight) / 2)
                    scale = String.format("scale=%d:%d", videoWidth, videoHeight)
                }
                Surface.ROTATION_180 -> {
                }
            }
            // transpose
            if (transpose != null) {
                filters.add(transpose)
            }
            // horizontal flip
            if (hflip != null) {
                filters.add(hflip)
            }
            // vertical flip
            if (vflip != null) {
                filters.add(vflip)
            }
            // crop
            if (crop != null) {
                filters.add(crop)
            }
            // scale (to designated size)
            if (scale != null) {
                filters.add(scale)
            }

            val frameFilter = FFmpegFrameFilter(TextUtils.join(",", filters),
                    previewWidth, previewHeight)
            frameFilter.pixelFormat = avutil.AV_PIX_FMT_NV21
            frameFilter.frameRate = frameRate.toDouble()
            try {
                frameFilter.start()
            } catch (e: FrameFilter.Exception) {
                e.printStackTrace()
            }

            isRunning = true
            var recordedFrame: FrameToRecord
            while (isRunning || !mFrameToRecordQueue!!.isEmpty()) {
                try {
                    recordedFrame = mFrameToRecordQueue?.take()!!
                } catch (ie: InterruptedException) {
                    ie.printStackTrace()
                    try {
                        frameFilter.stop()
                    } catch (e: FrameFilter.Exception) {
                        e.printStackTrace()
                    }
                    break
                }

                mFrameRecorder?.apply {
                    val timestamp = recordedFrame.timestamp
                    if (timestamp > getTimestamp()) {
                        setTimestamp(timestamp)
                    }
                    val startTime = System.currentTimeMillis()
                    //                    Frame filteredFrame = recordedFrame.getFrame();
                    var filteredFrame: Frame? = null
                    try {
                        frameFilter.push(recordedFrame.frame)
                        filteredFrame = frameFilter.pull()
                    } catch (e: FrameFilter.Exception) {
                        e.printStackTrace()
                    }

                    try {
                        record(filteredFrame)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val endTime = System.currentTimeMillis()
                    val processTime = endTime - startTime
                    mTotalProcessFrameTime += processTime
                    //Log.d(LOG_TAG, "This frame process time: " + processTime + "ms")
                    val totalAvg = mTotalProcessFrameTime / ++mFrameRecordedCount
                    //Log.d(LOG_TAG, "Avg frame process time: " + totalAvg + "ms")
                }
                //Log.d(LOG_TAG, mFrameRecordedCount.toString() + " / " + mFrameToRecordCount)
                mRecycledFrameQueue?.offer(recordedFrame)
            }
        }

        override fun stopRunning() {
            super.stopRunning()
            if (state == State.WAITING) {
                interrupt()
            }
        }
    }

    internal abstract inner class ProgressDialogTask<Params, Progress, Result>(private val promptRes: Int) : AsyncTask<Params, Progress, Result>() {
        private var mProgressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressDialog = ProgressDialog.show(context,
                    null, getString(promptRes), true)
        }

        override fun onPostExecute(result: Result) {
            super.onPostExecute(result)
            mProgressDialog!!.dismiss()
        }
    }


    internal inner class FinishRecordingTask : ProgressDialogTask<Void, Int, Void>(R.string.processing) {

        override fun doInBackground(vararg params: Void): Void? {
            stopRecording()
            stopRecorder()
            releaseRecorder(false)
            RecordOTGFragment.cameraCallbackListener.uploadYoutube(mVideo?.path ?: "")
            return null
        }

    }

    override fun recordEvent(isTake: Boolean, step: Int, subStep: String) {
        tvTitleStep.text = subStep
        activity?.apply {
            if (!isFinishing) {
                when (isTake) {
                    true -> {
                        StepFragment.mRecording = false
                        Logger.e("RecordEvent")
                        currentStep = step
                        currentSubStepName = subStep
                        mBtnTake.isVisible = true
                        btnExit1.visibility = View.VISIBLE
                        tvTitleStep.isVisible = true
                        stopRecording()
                        stopPreview()
                        releaseCamera()
                        runDelayedOnUiThread({
                            val builder = Configuration.Builder()
                            builder.setCamera(mCameraId)
                                    .setFlashMode(Configuration.FLASH_MODE_OFF)
                                    .setMediaAction(Configuration.MEDIA_ACTION_PHOTO)
                            takeFragment = CameraFragment.newInstance(builder.build())
                            activity?.addFragment(takeFragment!!, R.id.fragmentCapture)
                        }, 1000)
                        Logger.e("${tvTitleStep.text} ************************************")
                    }
                    false -> {
                        StepFragment.mRecording = true
                        Logger.e("RecordEvent11111")
                        mBtnTake.isGone = true
                        btnExit1.visibility = View.GONE
                        tvTitleStep.isGone = true
                        takeFragment?.apply {
                            removeFragment(this)
                        }
                        runDelayedOnUiThread({
                            startPreview()
                        }, 1000)
                        Logger.e("${tvTitleStep.text} ************************************")
                    }
                }
            }
        }
    }
}