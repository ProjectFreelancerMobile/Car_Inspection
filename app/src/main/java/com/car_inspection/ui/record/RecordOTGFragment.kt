package com.car_inspection.ui.record

import android.annotation.SuppressLint
import android.hardware.usb.UsbDevice
import android.view.Surface
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.car_inspection.R
import com.car_inspection.listener.CameraDefaultListener
import com.car_inspection.listener.CameraRecordListener
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.step.StepFragment
import com.car_inspection.utils.*
import com.jiangdg.usbcamera.UVCCameraHelper
import com.jiangdg.usbcamera.utils.FileUtils
import com.orhanobut.logger.Logger
import com.serenegiant.usb.CameraDialog
import com.serenegiant.usb.USBMonitor
import com.serenegiant.usb.common.AbstractUVCCameraHandler
import com.serenegiant.usb.encoder.RecordParams
import com.serenegiant.usb.widget.CameraViewInterface
import google.com.carinspection.DisposableImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.record_otg_fragment.*
import pyxis.uzuki.live.richutilskt.utils.runDelayedOnUiThread
import java.util.*
import java.util.concurrent.TimeUnit

class RecordOTGFragment : BaseFragment(), CameraDialog.CameraDialogParent, CameraViewInterface.Callback, View.OnClickListener, CameraRecordListener {

    private var mCameraHelper: UVCCameraHelper? = null
    private var mUVCCameraView: CameraViewInterface? = null
    private var isRequest: Boolean = false
    private var isPreview: Boolean = false
    private var currentSubStepName: String = ""
    private var currentStep = 2

    companion object {
        lateinit var cameraCallbackListener: CameraDefaultListener
        fun newInstance(cameraRecord: CameraDefaultListener): RecordOTGFragment {
            cameraCallbackListener = cameraRecord
            return RecordOTGFragment()
        }
    }

    private val listener = object : UVCCameraHelper.OnMyDevConnectListener {

        override fun onAttachDev(device: UsbDevice) {
            if (mCameraHelper == null || mCameraHelper?.usbDeviceCount == 0) {
                showSnackBar("check no usb camera")
                cameraCallbackListener.showCameraDefault()
                return
            }
            // request open permission
            if (!isRequest) {
                isRequest = true
                mCameraHelper?.requestPermission(0)
            }
        }

        override fun onDettachDev(device: UsbDevice) {
            // close camera
            if (isRequest) {
                isRequest = false
                mCameraHelper?.closeCamera()
            }
        }

        override fun onConnectDev(device: UsbDevice, isConnected: Boolean) {
            if (!isConnected) {
                showSnackBar("fail to connect,please check resolution params")
                isPreview = false
            } else {
                isPreview = true
                showSnackBar("connecting")
            }
        }

        override fun onDisConnectDev(device: UsbDevice) {
            activity?.apply {
                if (!isFinishing)
                    showSnackBar("disconnecting")
            }
        }
    }

    override fun initViews() {
        initUVCCameraHelper()
    }

    override fun setLayoutResourceID(): Int = R.layout.record_otg_fragment

    override fun initData() {
        listenToViews(mBtnRecord, btnTakePicture)
    }

    private fun initUVCCameraHelper() {
        try {
            Logger.e("initUVCCameraHelper")
            // step.1 initialize UVCCameraHelper
            mUVCCameraView = cameraView
            mUVCCameraView?.apply {
                setCallback(this@RecordOTGFragment)
                mCameraHelper = UVCCameraHelper.getInstance()
                mCameraHelper?.apply {
                    setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_YUYV)
                    initUSBMonitor(activity, mUVCCameraView, listener)
                    setOnPreviewFrameListener(AbstractUVCCameraHandler.OnPreViewResultListener { })
                    if (mCameraHelper == null || mCameraHelper?.usbDeviceCount == 0) {
                        showSnackBar("check no usb camera")
                        cameraCallbackListener.showCameraDefault()
                        return
                    }
                    setModelValue(UVCCameraHelper.MODE_BRIGHTNESS, 80)
                    setModelValue(UVCCameraHelper.MODE_CONTRAST, 60)
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnTakePicture -> {
                mCameraHelper?.apply {
                    if (isCameraOpened) {
                        com.blankj.utilcode.util.FileUtils.createOrExistsDir(Constanst.getFolderPicturePath() + "Step$currentStep")
                        val picPath = Constanst.getFolderPicturePath() + "Step$currentStep/$currentSubStepName" + UVCCameraHelper.SUFFIX_JPEG
                        capturePicture(picPath) { path ->
                            showSnackBar("Save picture path：$path")
                            overlay(activity!!, path, currentSubStepName)
                        }
                    }
                }
            }
            R.id.mBtnRecord -> {
                mCameraHelper?.apply {
                    if (isCameraOpened) {
                        if (!isPushing) {
                            // val videoPath = UVCCameraHelper.ROOT_PATH + System.currentTimeMillis()
                            // FileUtils.createfile(FileUtils.ROOT_PATH + "test666.h264")
                            createFolderPicture(Constanst.getFolderVideoPath())
                            // if you want to record,please create RecordParams like this
                            val params = RecordParams()
                            params.recordPath = Constanst.getFolderVideoPath() + System.currentTimeMillis()
                            params.recordDuration = 0                        // 设置为0，不分割保存
                            params.isVoiceClose = false    // is close voice
                            startPusher(params, object : AbstractUVCCameraHandler.OnEncodeResultListener {
                                override fun onEncodeResult(data: ByteArray, offset: Int, length: Int, timestamp: Long, type: Int) {
                                    // type = 1,h264 video stream
                                    if (type == 1) {
                                        FileUtils.putFileStream(data, offset, length)
                                    }
                                    // type = 0,aac audio stream
                                    if (type == 0) {

                                    }
                                }

                                override fun onRecordResult(videoPath: String) {
                                    showSnackBar("Save video path: $videoPath")
                                    cameraCallbackListener.uploadYoutube(videoPath)
                                }
                            })
                            // if you only want to push stream,please call like this
                            // mCameraHelper.startPusher(listener);
                            mBtnRecord.text = "Stop Record"
                            showSnackBar("start record...")
                        } else {
                            mBtnRecord.text = "Start Record"
                            showSnackBar("stop record...")
                            FileUtils.releaseFile()
                            stopPusher()
                        }
                    }
                }
            }
            /* R.id.mBtnRes -> {
                 Toast.makeText(context, getResolutionList().toString(), Toast.LENGTH_LONG).show()
                 Logger.e("getResolutionList="+getResolutionList().toString())
             }
             R.id.mBtnFocus -> mCameraHelper?.startCameraFoucs()*/
        }
    }

    private fun getResolutionList(): List<String>? {
        var resolutions: MutableList<String>? = null
        mCameraHelper?.apply {
            val list = supportedPreviewSizes
            if (list != null && list.size != 0) {
                resolutions = ArrayList()
                for (size in list) {
                    if (size != null) {
                        resolutions?.add(size.width.toString() + "x" + size.height)
                    }
                }
            }
        }
        return resolutions
    }

    override fun getUSBMonitor(): USBMonitor = mCameraHelper?.usbMonitor!!

    override fun onDialogResult(canceled: Boolean) {

    }

    override fun onSurfaceCreated(view: CameraViewInterface?, surface: Surface?) {
        mCameraHelper?.apply {
            if (!isPreview && isCameraOpened) {
                startPreview(mUVCCameraView)
                isPreview = true
            }
        }
    }

    override fun onSurfaceChanged(view: CameraViewInterface?, surface: Surface?, width: Int, height: Int) {

    }

    override fun onSurfaceDestroy(view: CameraViewInterface?, surface: Surface?) {
        mCameraHelper?.apply {
            if (!isPreview && isCameraOpened) {
                stopPreview()
                isPreview = false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // step.2 register USB event broadcast
        mCameraHelper?.registerUSB()
    }

    override fun onDestroyView() {
        mCameraHelper?.unregisterUSB()
        FileUtils.releaseFile()
        // step.4 release uvc camera resources
        Logger.e("onDestroy")
        mCameraHelper?.release()
        super.onDestroyView()
    }

    override fun recordEvent(isTake: Boolean, step: Int, subStep: String) {
        tvTitleStep.text = subStep
        activity?.apply {
            if (!isFinishing) {
                when (isTake) {
                    true -> {
                        StepFragment.mRecording = false
                        this@RecordOTGFragment.currentStep = step
                        this@RecordOTGFragment.currentSubStepName = subStep
                        btnTakePicture?.isVisible = true
                        tvTitleStep?.isVisible = true
                    }
                    false -> {
                        StepFragment.mRecording = true
                        btnTakePicture?.isGone = true
                        tvTitleStep?.isGone = true
                    }
                }
            }
        }
    }

}