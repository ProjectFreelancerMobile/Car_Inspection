package com.car_inspection.ui.step

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaFormat.MIMETYPE_AUDIO_AAC
import android.media.MediaFormat.MIMETYPE_VIDEO_AVC
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.ImageLoader
import com.blankj.utilcode.util.FileUtils
import com.car_inspection.R
import com.car_inspection.app.Constants
import com.car_inspection.binding.FragmentDataBindingComponent
import com.car_inspection.data.model.StepModifyModel
import com.car_inspection.data.model.StepOrinalModel
import com.car_inspection.databinding.StepFragmentBinding
import com.car_inspection.library.record.AudioEncodeConfig
import com.car_inspection.library.record.Notifications
import com.car_inspection.library.record.ScreenRecorder
import com.car_inspection.library.record.VideoEncodeConfig
import com.car_inspection.library.youtube.UploadService
import com.car_inspection.library.youtube.util.VideoData
import com.car_inspection.listener.CameraDefaultListener
import com.car_inspection.listener.CameraRecordListener
import com.car_inspection.ui.activity.StepActivity
import com.car_inspection.ui.adapter.StepAdapter
import com.car_inspection.ui.base.BaseDataFragment
import com.car_inspection.ui.inputtext.SuggestTextActivity
import com.car_inspection.ui.record.RecordFragment
import com.car_inspection.ui.record.RecordOTGFragment
import com.car_inspection.utils.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.plus.Plus
import com.orhanobut.logger.Logger
import com.toan_itc.core.architecture.autoCleared
import com.toan_itc.core.utils.addFragment
import google.com.carinspection.DisposableImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.step_fragment.*
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class StepFragment : BaseDataFragment<StepViewModel>(), StepAdapter.StepAdapterListener, View.OnClickListener, CameraDefaultListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val TAG = StepFragment::class.java.name
    private val REQUEST_SUGGEST_TEST = 0
    private var currentSubStepName = ""
    private var items: List<StepModifyModel> = mutableListOf()
    lateinit var stepAdapter: StepAdapter
    private var isTakePicture = false
    private var currentPosition = 0
    private var currentStep = 2
    private var cameraRecordListener: CameraRecordListener? = null
    private var binding by autoCleared<StepFragmentBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    //Youtube
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mChosenAccountName: String? = null
    private var mCallbacks: Callbacks? = null

    //Screen Record
    private val REQUEST_MEDIA_PROJECTION = 1
    private val REQUEST_PERMISSIONS = 2
    private var mMediaProjectionManager: MediaProjectionManager? = null
    private var mRecorder: ScreenRecorder? = null
    private var mNotifications: Notifications? = null
    internal val VIDEO_AVC = MIMETYPE_VIDEO_AVC // H.264 Advanced Video Coding
    internal val AUDIO_AAC = MIMETYPE_AUDIO_AAC // H.264 Advanced Audio Coding
    private var timerRecord = 0
    private var mRecording = false
    private var filePath :File? =null

    companion object {
        fun newInstance() = StepFragment()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mCallbacks = activity as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        mCallbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build()
    }

    private fun loadAccount() {
        val sp = PreferenceManager
                .getDefaultSharedPreferences(activity)
        mChosenAccountName = sp.getString(StepActivity.ACCOUNT_KEY, null)
    }

    override fun onConnected(bundle: Bundle?) {
        mCallbacks?.onConnected(Plus.AccountApi.getAccountName(mGoogleApiClient))
    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult?) {
        if (connectionResult!!.hasResolution()) {
            Toast.makeText(activity,
                    R.string.connection_to_google_play_failed, Toast.LENGTH_SHORT)
                    .show()

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.errorCode,
                            connectionResult.toString()))
            try {
                connectionResult.startResolutionForResult(activity, 0)
            } catch (e: IntentSender.SendIntentException) {
                Log.e(TAG, e.toString(), e)
            }

        }
    }


    override fun onResume() {
        super.onResume()
        mGoogleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient?.disconnect()
    }

    override fun setLayoutResourceID() = R.layout.step_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<StepFragmentBinding>(
                inflater,
                setLayoutResourceID(),
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun getViewModel(): Class<StepViewModel> = StepViewModel::class.java

    override fun initView() {
        setProfileInfo()
        loadAccount()
        rvSubStep.layoutManager = LinearLayoutManager(activity)
        listenToViews(btnSave, btnContinue, btnFinish)
        addFragmentRecord()
    }

    override fun initData() {
        loadDataStep(currentStep)
        updateProgressStep(currentStep)
        mMediaProjectionManager = context?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        mNotifications = Notifications(context)
        createFolderPicture(Constanst.getFolderVideoPath())
        when {
            mRecorder != null -> stopRecorder()
            hasPermissions() -> startCaptureIntent()
            Build.VERSION.SDK_INT >= M -> requestPermissions()
        }
    }

    private fun startTimer() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableImpl<Long>() {
                    override fun onNext(t: Long) {
                        if (mRecording) {
                            timerRecord++
                            tvTimerRecord.text = formatTime(timerRecord)
                        }
                    }
                })
    }

    private fun setProfileInfo() {
        //not sure if mGoogleapiClient.isConnect is appropriate...
        if (!mGoogleApiClient!!.isConnected || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {

        } else {
            val currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)
        }
    }

    private fun uploadVideo(uri: Uri?) {
        loadAccount()
        if (mChosenAccountName == null) {
            return
        }
        // if a video is picked or recorded.
        if (uri != null) {
            val uploadIntent = Intent(activity, UploadService::class.java)
            uploadIntent.data = uri
            uploadIntent.putExtra(StepActivity.ACCOUNT_KEY, mChosenAccountName)
            activity?.startService(uploadIntent)
            activity?.runOnUiThread {
                showSnackBar(getString(R.string.youtube_upload_started))
            }
            Logger.e("uploadYoutube=$uri")
            // Go back to MainActivity after upload
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSave -> {
                saveDataStep(currentStep)
                btnContinue.isActive = true
            }
            R.id.btnContinue -> {
                Observable.just(1L).delay(100, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableImpl<Long>() {
                            override fun onNext(t: Long) {
                                currentStep++
                                loadDataStep(currentStep)
                                btnContinue.isActive = false
                            }
                        })
            }
            R.id.btnFinish -> saveDataStep(currentStep)
            R.id.btnTakePicture -> {

            }
        }
    }

    private fun addFragmentRecord() {
        val fragment = RecordOTGFragment.newInstance(this)
        cameraRecordListener = fragment
        activity?.addFragment(fragment, R.id.fragmentRecord)
    }

    private fun saveDataStep(step: Int) {

    }

    private fun updateProgressStep(step: Int) {
        val percent = (step + 1) * 1f / 17
        pgStep.setMaximumPercentage(percent)
        pgStep.useRoundedRectangleShape(20.0f)
        pgStep.setProgressColor(resources.getColor(R.color.blue_500))
        pgStep.setProgressBackgroundColor(resources.getColor(R.color.blue_200))
        pgStep.text = "${(step + 1) * 100 / 17}%"
        pgStep.textSize = 14f
        pgStep.setTextColor(Color.WHITE)
        pgStep.gravity = Gravity.CENTER
        pgStep.updateView()
    }

    private fun loadDataStep(step: Int) {
        if (layoutContinue.visibility == View.VISIBLE)
            layoutContinue.visibility = View.GONE
        if (layoutFinish.visibility == View.VISIBLE)
            layoutFinish.visibility = View.GONE
        items = convertStepOrinalModelsToStepModifyModels(viewModel.initStepData(step))
        stepAdapter = StepAdapter(this.activity!!)
        stepAdapter.stepAdapterListener = this
        stepAdapter.items = items
        rvSubStep.adapter = stepAdapter

        updateProgressStep(step)

        val stepTitle = "Bước ${step}: ${items[0].stepTitle}"
        val content = SpannableString(stepTitle)
        content.setSpan(UnderlineSpan(), 0, stepTitle.length, 0)
        tvStep.text = content
    }

    private fun convertStepOrinalModelsToStepModifyModels(stepOrinalModels: List<StepOrinalModel>): List<StepModifyModel> {
        val stepModifyModels = ArrayList<StepModifyModel>()
        if (stepOrinalModels != null && stepOrinalModels.size > 0) {
            for (stepOrinalModel in stepOrinalModels)
                run {
                    val stepModifyModel = StepModifyModel()
                    stepModifyModel.step = stepOrinalModel.step
                    stepModifyModel.subStep = stepOrinalModel.subStep
                    stepModifyModel.stepTitle = stepOrinalModel.stepTitle
                    stepModifyModel.subStepTitle1 = stepOrinalModel.subStepTitle1
                    stepModifyModel.subStepTitle2 = stepOrinalModel.subStepTitle2
                    stepModifyModel.subStepTitle3 = stepOrinalModel.subStepTitle3
                    stepModifyModel.canIgnore = stepOrinalModel.canIgnore
                    stepModifyModels.add(stepModifyModel)
                }
        }
        return stepModifyModels
    }


    fun autoScrollAfterCheckComplete() {
        rvSubStep.post { rvSubStep.smoothScrollBy(0, stepAdapter.heightItem) }
    }

    override fun onTextNoteClickListener(v: View, position: Int) {
        val intent = Intent(activity, SuggestTextActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("note", items.get(position).note)
        startActivityForResult(intent, REQUEST_SUGGEST_TEST)
    }

    override fun onRadioGroupCheckChangeListner(group: RadioGroup, checkId: Int, position: Int) {
        currentPosition = position
        screenShot(layoutStep)
        currentSubStepName = stepAdapter.items?.get(position)?.subStepTitle3!!
        when (checkId) {
            R.id.cbG -> {
                showLayoutVideo()
                items[position].rating = "G"
            }
            R.id.cbP -> {
                showLayoutTakepicture()
                items[position].rating = "P"
            }
            R.id.cbF -> {
                showLayoutTakepicture()
                items[position].rating = "F"
            }
        }
        if (stepAdapter.isFinishCheckItem()) {
            if (currentStep < 15) {
                if (layoutContinue.visibility == View.GONE)
                    layoutContinue.visibility = View.VISIBLE
            } else {
                layoutFinish.bringToFront()
                layoutFinish.visibility = View.VISIBLE
            }
        }

        //else layoutFinishCheck.visibility = View.GONE

//        if (position < stepAdapter.itemCount - 3)
//            autoScrollAfterCheckComplete()
    }


    private fun showLayoutTakepicture() {
        cameraRecordListener?.recordEvent(true, currentStep, currentSubStepName)
        isTakePicture = true
    }

    private fun showLayoutVideo() {
        cameraRecordListener?.recordEvent()
        isTakePicture = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SUGGEST_TEST) {
            if (resultCode == Activity.RESULT_OK) {
                val position = data.getIntExtra("position", 0)
                items[position].note = data.getStringExtra("note")
            }
        }else if (requestCode == REQUEST_MEDIA_PROJECTION) {
            // NOTE: Should pass this result data into a Service to run ScreenRecorder.
            // The following codes are merely exemplary.

            val mediaProjection = mMediaProjectionManager?.getMediaProjection(resultCode, data)
            if (mediaProjection == null) {
                Log.e("@@", "media projection is null")
                return
            }

            val video = createVideoConfig()
            val audio = createAudioConfig() // audio can be null
            if (video == null) {
                showSnackBar("Create ScreenRecorder failure")
                mediaProjection.stop()
                return
            }
            createFolderPicture(Constanst.getFolderVideoPath())
            filePath = FileUtils.getFileByPath(Constanst.getFolderVideoPath() + System.currentTimeMillis()+".mp4")
            audio?.apply {
                Logger.e( "Create recorder with :$video \n $this\n $filePath")
                mRecorder = newRecorder(mediaProjection, video, this, filePath!!)
                if (hasPermissions()) {
                    startRecorder()
                } else {
                    cancelRecorder()
                }
            }
        }
    }

    override fun showCameraDefault() {
        activity?.apply {
            if (!isFinishing) {
                fragmentRecord.isGone = true
                fragmentRecordDefault.isVisible = true
                Logger.e("onCameraDefaultEventonCameraDefaultEvent")
                val fragment = RecordFragment.newInstance()
                cameraRecordListener = fragment
                activity?.addFragment(fragment, R.id.fragmentRecordDefault)
            }
        }
    }

    override fun uploadYoutube(path: String) {
        filePath?.path?.apply {
            uploadVideo(getImageContentUri(context, this))
        }
    }

    interface Callbacks {
        fun onGetImageLoader(): ImageLoader

        fun onVideoSelected(video: VideoData)

        fun onConnected(connectedAccountName: String)
    }


    //Screen Record

    private fun newRecorder(mediaProjection: MediaProjection, video: VideoEncodeConfig,
                            audio: AudioEncodeConfig, output: File): ScreenRecorder {
        val r = ScreenRecorder(video, audio, 1, mediaProjection, output.absolutePath)
        r.setCallback(object : ScreenRecorder.Callback {
            var startTime: Long = 0

            override fun onStop(error: Throwable?) {
                runOnUiThread { stopRecorder() }
                if (error != null) {
                    showSnackBar("Recorder error ! See logcat for more details")
                    error.printStackTrace()
                    output.delete()
                } else {
                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                            .addCategory(Intent.CATEGORY_DEFAULT)
                            .setData(Uri.fromFile(output))
                    context?.sendBroadcast(intent)
                }
            }

            override fun onStart() {
                mNotifications?.recording(0)
            }

            override fun onRecording(presentationTimeUs: Long) {
                if (startTime <= 0) {
                    startTime = presentationTimeUs
                }
                val time = (presentationTimeUs - startTime) / 1000
                mNotifications?.recording(time)
            }
        })
        return r
    }

    private fun createAudioConfig(): AudioEncodeConfig? {
        return AudioEncodeConfig("OMX.google.aac.encoder", AUDIO_AAC, 80000, 44100, 1, 1)
    }

    private fun createVideoConfig(): VideoEncodeConfig? {
        //val codec = getSelectedVideoCodec() ?: // no selected codec ?? return null
        return VideoEncodeConfig(1080, 1920, 800000, 15, 1,"OMX.Exynos.AVC.Encoder", VIDEO_AVC,null)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopRecorder()
    }

    private fun startCaptureIntent() {
        val captureIntent = mMediaProjectionManager?.createScreenCaptureIntent()
        startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION)
    }

    private fun startRecorder() {
        if (mRecorder == null) return
        mRecorder?.start()
        context?.apply {
            mRecording = true
            registerReceiver(mStopActionReceiver, IntentFilter(Constants.ACTION_STOP))
            startTimer()
            //activity?.moveTaskToBack(true)
        }
    }

    private fun stopRecorder() {
        mNotifications?.clear()
        mRecorder?.quit()
        mRecorder = null
        mRecording = false
        try {
            context?.unregisterReceiver(mStopActionReceiver)
        } catch (e: Exception) {
            //ignored
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {
            var granted = PackageManager.PERMISSION_GRANTED
            for (r in grantResults) {
                granted = granted or r
            }
            if (granted == PackageManager.PERMISSION_GRANTED) {
                startCaptureIntent()
            } else {
                showSnackBar("No Permission!")
            }
        }
    }

    private fun cancelRecorder() {
        if (mRecorder == null) return
        showSnackBar("Permission denied! Screen recorder is cancel")
        stopRecorder()
    }

    private val mStopActionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val file = File(mRecorder?.savedPath)
            if (Constants.ACTION_STOP == intent.action) {
                stopRecorder()
            }
            showSnackBar("Recorder stopped!\n Saved file $file")
        }
    }

    @TargetApi(M)
    private fun requestPermissions() {
        val permissions = if (true)// audio enable
            arrayOf(WRITE_EXTERNAL_STORAGE, RECORD_AUDIO)
        else
            arrayOf<String>(WRITE_EXTERNAL_STORAGE)
        var showRationale = false
        for (perm in permissions) {
            showRationale = showRationale or shouldShowRequestPermissionRationale(perm)
        }
        if (!showRationale) {
            requestPermissions(permissions, REQUEST_PERMISSIONS)
            return
        }
        AlertDialog.Builder(context)
                .setMessage("Using your mic to record audio and your sd card to save video file")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { dialog, which -> requestPermissions(permissions, REQUEST_PERMISSIONS) }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show()
    }

    private fun hasPermissions(): Boolean {
        var granted = 0
        context?.apply {
            val pm = packageManager
            val packageName = packageName
            granted = (if (true) pm.checkPermission(RECORD_AUDIO, packageName) else PackageManager.PERMISSION_GRANTED) or pm.checkPermission(WRITE_EXTERNAL_STORAGE, packageName)
        }

        return granted == PackageManager.PERMISSION_GRANTED
    }

}
