package com.car_inspection.ui.record

import android.hardware.usb.UsbDevice
import android.view.Surface
import android.view.View
import android.widget.Toast
import com.car_inspection.R
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.utils.listenToViews
import com.jiangdg.usbcamera.UVCCameraHelper
import com.jiangdg.usbcamera.utils.FileUtils
import com.orhanobut.logger.Logger
import com.serenegiant.usb.CameraDialog
import com.serenegiant.usb.USBMonitor
import com.serenegiant.usb.common.AbstractUVCCameraHandler
import com.serenegiant.usb.encoder.RecordParams
import com.serenegiant.usb.widget.CameraViewInterface
import kotlinx.android.synthetic.main.record_otg_fragment.*
import java.util.*

class RecordOTGFragment: BaseFragment() , CameraDialog.CameraDialogParent, CameraViewInterface.Callback, View.OnClickListener{

    private val LOG_TAG = RecordOTGFragment::class.java.simpleName
    private var mCameraHelper: UVCCameraHelper? = null
    private var mUVCCameraView: CameraViewInterface? = null
    private var isRequest: Boolean = false
    private var isPreview: Boolean = false

    companion object {
        fun newInstance() = RecordOTGFragment()
    }

    private val listener = object : UVCCameraHelper.OnMyDevConnectListener {

        override fun onAttachDev(device: UsbDevice) {
            if (mCameraHelper == null || mCameraHelper?.usbDeviceCount == 0) {
                Toast.makeText(context, "check no usb camera", Toast.LENGTH_LONG).show()
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
                Toast.makeText(context, "fail to connect,please check resolution params", Toast.LENGTH_LONG).show()
                isPreview = false
            } else {
                isPreview = true
                Toast.makeText(context, "connecting", Toast.LENGTH_LONG).show()
            }
        }

        override fun onDisConnectDev(device: UsbDevice) {
            Toast.makeText(context, "disconnecting", Toast.LENGTH_LONG).show()
        }
    }
    override fun initViews() {
        initUVCCameraHelper()
    }

    override fun setLayoutResourceID(): Int = R.layout.record_otg_fragment


    override fun initData() {
        listenToViews(mBtnTake,mBtnRecord,mBtnRes,mBtnFocus)
    }

    private fun initUVCCameraHelper(){
        try {
            // step.1 initialize UVCCameraHelper
            mUVCCameraView = cameraView
            mUVCCameraView?.apply {
                setCallback(this@RecordOTGFragment)
                mCameraHelper = UVCCameraHelper.getInstance()
                mCameraHelper?.apply {
                    setDefaultFrameFormat(UVCCameraHelper.FRAME_FORMAT_YUYV)
                    initUSBMonitor(activity, mUVCCameraView, listener)
                    setOnPreviewFrameListener(AbstractUVCCameraHandler.OnPreViewResultListener { })
                    setModelValue(UVCCameraHelper.MODE_BRIGHTNESS, 80)
                    setModelValue(UVCCameraHelper.MODE_CONTRAST, 60)
                }
            }
        }catch (e:IllegalStateException){
            e.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.mBtnTake -> {
                mCameraHelper?.apply {
                    if(isCameraOpened){
                        val picPath = (UVCCameraHelper.ROOT_PATH + System.currentTimeMillis() + UVCCameraHelper.SUFFIX_JPEG)
                        capturePicture(picPath) { path -> Logger.e( "save path：$path") }
                    }
                }

            }
            R.id.mBtnRecord -> {
                mCameraHelper?.apply {
                    if(isCameraOpened){
                        if (!isPushing) {
                            val videoPath = UVCCameraHelper.ROOT_PATH + System.currentTimeMillis()
                            FileUtils.createfile(FileUtils.ROOT_PATH + "test666.h264")
                            // if you want to record,please create RecordParams like this
                            val params = RecordParams()
                            params.recordPath = videoPath
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
                                    Logger.e( "videoPath = $videoPath")
                                    Toast.makeText(context, "videoPath = $videoPath", Toast.LENGTH_LONG).show()
                                }
                            })
                            // if you only want to push stream,please call like this
                            // mCameraHelper.startPusher(listener);
                            Toast.makeText(context, "start record...", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "stop record...", Toast.LENGTH_LONG).show()
                            FileUtils.releaseFile()
                            stopPusher()
                        }
                    }
                }
            }
            R.id.mBtnRes -> {
                Toast.makeText(context, getResolutionList().toString(), Toast.LENGTH_LONG).show()
                Logger.e("getResolutionList="+getResolutionList().toString())
            }
            R.id.mBtnFocus -> mCameraHelper?.startCameraFoucs()
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

    override fun onStop() {
        super.onStop()
        // step.3 unregister USB event broadcast
        mCameraHelper?.unregisterUSB()
    }

    override fun onDestroy() {
        super.onDestroy()
        FileUtils.releaseFile()
        // step.4 release uvc camera resources
        mCameraHelper?.release()
    }
}