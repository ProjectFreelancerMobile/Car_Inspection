package com.car_inspection.ui.step

import android.app.Activity
import android.content.*
import android.graphics.Color
import android.media.projection.MediaProjectionManager
import android.net.Uri
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
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.SizeUtils
import com.car_inspection.R
import com.car_inspection.binding.FragmentDataBindingComponent
import com.car_inspection.data.model.StepModifyModel
import com.car_inspection.data.model.StepOrinalModel
import com.car_inspection.databinding.StepFragmentBinding
import com.car_inspection.library.youtube.UploadService
import com.car_inspection.library.youtube.util.VideoData
import com.car_inspection.listener.CameraDefaultListener
import com.car_inspection.listener.CameraRecordListener
import com.car_inspection.service.ScreenRecorderService
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
import com.toan_itc.core.utils.removeFragment
import google.com.carinspection.DisposableImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.step_fragment.*
import pyxis.uzuki.live.richutilskt.utils.runDelayedOnUiThread
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
    private var recordFragment : RecordFragment? = null
    private var recordOTGFragment : RecordOTGFragment? = null
    private var cameraRecordListener: CameraRecordListener? = null
    private var binding by autoCleared<StepFragmentBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    //Youtube
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mChosenAccountName: String? = null
    private var mCallbacks: Callbacks? = null

    //Screen Record
    private var mReceiver: MyBroadcastReceiver? = null
    private var timerRecord = 0

    companion object {
        var mRecording = true
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
        mGoogleApiClient?.connect()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ScreenRecorderService.ACTION_QUERY_STATUS_RESULT)
        context?.registerReceiver(mReceiver, intentFilter)
        queryRecordingStatus()
        super.onResume()
    }

    override fun onPause() {
        mGoogleApiClient?.disconnect()
        mReceiver?.apply {
            context?.unregisterReceiver(mReceiver)
        }
        super.onPause()
    }

    override fun onDestroy() {
        stopScreenRecord()
        super.onDestroy()
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
        listenToViews(btnSave, btnContinue, btnFinish, btnRecordPause, btnExit, btnRecordType)
        addFragmentRecord()
    }

    override fun initData() {
        loadDataStep(currentStep)
        updateProgressStep(currentStep)
        createFolderPicture(Constanst.getFolderVideoPath())
        if (mReceiver == null) {
            mReceiver = MyBroadcastReceiver()
        }
        startScreenRecord()
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
        runDelayedOnUiThread({
            loadAccount()
            if (mChosenAccountName == null) {
                return@runDelayedOnUiThread
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
        },1000)
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
            R.id.btnFinish -> {
                try {
                    saveDataStep(currentStep)
                    if(ScreenRecorderService().outputPath.isNotEmpty()) {
                        val file = File(ScreenRecorderService().outputPath)
                        showSnackBar("Recorder stopped!\n Saved file $file")
                        stopScreenRecord()
                        uploadYoutube(file.path)
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
            R.id.btnRecordPause -> {
                if(mRecording){
                    pauseScreenRecord()
                    mRecording = false
                    btnRecordPause.text = getString(R.string.continues)
                }else{
                    resumeScreenRecord()
                    mRecording = true
                    btnRecordPause.text = getString(R.string.stop)
                }
            }
            R.id.btnRecordType ->{
                if(recordFragment !=null && fragmentRecord.isGone){
                    recordFragment?.apply {
                        activity?.removeFragment(this)
                    }
                    recordFragment = null
                    addFragmentRecord()
                }else if(recordOTGFragment !=null && fragmentRecordDefault.isGone){
                    recordOTGFragment?.apply {
                        Logger.e("removeFragment:recordOTGFragment")
                        activity?.removeFragment(this)
                    }
                    recordOTGFragment = null
                    showCameraDefault()
                }
            }
            R.id.btnExit -> showLayoutVideo()
        }
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

        val stepTitle = "Bước $step: ${items[0].stepTitle}"
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


    private fun autoScrollAfterCheckComplete() {
        rvSubStep.post { rvSubStep.smoothScrollBy(0, stepAdapter.heightItem + SizeUtils.dp2px(5f)) }
    }

    override fun onTextNoteClickListener(v: View, position: Int) {
        pauseScreenRecord()
        val intent = Intent(activity, SuggestTextActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("note", items.get(position).note)
        startActivityForResult(intent, REQUEST_SUGGEST_TEST)
    }

    override fun onRadioGroupCheckChangeListner(group: RadioGroup, checkId: Int, position: Int) {
        currentPosition = position
//        screenShot(layoutStep)
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
                if (layoutContinue.isGone)
                    layoutContinue.isVisible = true
            } else {
                layoutFinish.bringToFront()
                layoutFinish.isVisible = true
            }
        }

        //else layoutFinishCheck.visibility = View.GONE

        if (stepAdapter.isAllItemBeforeChecked(position) && position < stepAdapter.itemCount - 3)
            autoScrollAfterCheckComplete()
    }


    private fun showLayoutTakepicture() {
        pauseScreenRecord()
        btnExit.isVisible = true
        btnRecordPause.isGone = true
        btnRecordType.isGone = true
        cameraRecordListener?.recordEvent(true, currentStep, currentSubStepName)
        isTakePicture = true
    }

    private fun showLayoutVideo() {
        resumeScreenRecord()
        btnExit.isGone = true
        btnRecordPause.isVisible = true
        btnRecordType.isVisible = true
        cameraRecordListener?.recordEvent()
        isTakePicture = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SUGGEST_TEST) {
            resumeScreenRecord()
            if (resultCode == Activity.RESULT_OK) {
                val position = data.getIntExtra("position", 0)
                items[position].note = data.getStringExtra("note")
            }
        }else if (ScreenRecorderService.REQUEST_CODE_SCREEN_RECORD == requestCode) {
            if (resultCode != Activity.RESULT_OK) {
                // when no permission
                showSnackBar("permission denied")
                return
            }
            startScreenRecorder(resultCode, data)
        }
    }

    private fun addFragmentRecord() {
        activity?.apply {
            if (!isFinishing) {
                btnRecordType?.text = getString(R.string.camera_otg)
                fragmentRecord?.isVisible = true
                fragmentRecordDefault?.isGone = true
                recordOTGFragment = RecordOTGFragment.newInstance(this@StepFragment)
                recordOTGFragment?.let {fragment->
                    Logger.e("addFragmentRecord")
                    cameraRecordListener = fragment
                    addFragment(fragment, R.id.fragmentRecord)
                }
            }
        }
    }

    override fun showCameraDefault() {
        activity?.apply {
            if (!isFinishing) {
                btnRecordType?.text = getString(R.string.camera_default)
                fragmentRecord?.isGone = true
                fragmentRecordDefault?.isVisible = true
                recordFragment = RecordFragment.newInstance()
                recordFragment?.let {fragment->
                    Logger.e("showCameraDefault")
                    cameraRecordListener = fragment
                    addFragment(fragment, R.id.fragmentRecordDefault)
                }
            }
        }
    }

    override fun uploadYoutube(path: String) {
        uploadVideo(getImageContentUri(context, path))
    }

    interface Callbacks {
        fun onGetImageLoader(): ImageLoader

        fun onVideoSelected(video: VideoData)

        fun onConnected(connectedAccountName: String)
    }

    //Screen Record

    private fun startScreenRecord(){
        val manager = context?.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?
        val permissionIntent = manager!!.createScreenCaptureIntent()
        startActivityForResult(permissionIntent, ScreenRecorderService.REQUEST_CODE_SCREEN_RECORD)
        mRecording = true
        startTimer()
    }

    private fun stopScreenRecord(){
        mRecording = false
        val intent = Intent(context, ScreenRecorderService::class.java)
        intent.action = ScreenRecorderService.ACTION_STOP
        context?.startService(intent)
    }

    private fun queryRecordingStatus() {
        val intent = Intent(context, ScreenRecorderService::class.java)
        intent.action = ScreenRecorderService.ACTION_QUERY_STATUS
        context?.startService(intent)
    }

    private fun pauseScreenRecord(){
        /*if(mRecording) {
            runDelayedOnUiThread({
                val intent = Intent(context, ScreenRecorderService::class.java)
                intent.action = ScreenRecorderService.ACTION_PAUSE
                context?.startService(intent)
            }, 1000)
        }*/
    }

    private fun resumeScreenRecord(){
        /*if(!mRecording) {
            val intent = Intent(context, ScreenRecorderService::class.java)
            intent.action = ScreenRecorderService.ACTION_RESUME
            context?.startService(intent)
        }*/
    }

    private fun startScreenRecorder(resultCode: Int, data: Intent) {
        val intent = Intent(context, ScreenRecorderService::class.java)
        intent.action = ScreenRecorderService.ACTION_START
        intent.putExtra(ScreenRecorderService.EXTRA_RESULT_CODE, resultCode)
        intent.putExtras(data)
        context?.startService(intent)
    }

    private inner class MyBroadcastReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ScreenRecorderService.ACTION_QUERY_STATUS_RESULT.equals(action)) {
                mRecording = when(intent.getBooleanExtra(ScreenRecorderService.EXTRA_QUERY_RESULT_PAUSING, false)){
                    true -> false
                    else -> true
                }
            }
        }
    }

}
