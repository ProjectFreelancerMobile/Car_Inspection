package com.car_inspection.ui.step

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.Color
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
import com.blankj.utilcode.util.UriUtils
import com.car_inspection.R
import com.car_inspection.binding.FragmentDataBindingComponent
import com.car_inspection.data.model.StepModifyModel
import com.car_inspection.data.model.StepOrinalModel
import com.car_inspection.databinding.StepFragmentBinding
import com.car_inspection.library.youtube.UploadService
import com.car_inspection.library.youtube.util.Utils
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
import java.io.File
import java.io.FileOutputStream
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

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mChosenAccountName: String? = null
    private var mCallbacks: Callbacks? = null

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
        rvSubStep.layoutManager = LinearLayoutManager(activity)
        listenToViews(btnSave, btnContinue, btnFinish)
        addFragmentRecord()
    }

    override fun initData() {
        loadDataStep(currentStep)
        updateProgressStep(currentStep)

        createFolderPicture(Constanst.getFolderVideoPath())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfileInfo()
        loadAccount()
        // getSaveImageFilePath("test",layoutStep)
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
        val percent = (step + 1) * 1f / 8
        pgStep.setMaximumPercentage(percent)
        pgStep.useRoundedRectangleShape(20.0f)
        pgStep.setProgressColor(resources.getColor(R.color.blue_500))
        pgStep.setProgressBackgroundColor(resources.getColor(R.color.blue_200))
        pgStep.text = "${(step + 1) * 100 / 8}%"
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

        val stepTitle = "Bước ${items[0].step}: ${items[0].stepTitle}"
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
                    stepModifyModels.add(stepModifyModel)
                }
        }
        return stepModifyModels
    }

    private fun initStepTestData(step: Int): List<StepModifyModel> {
        val stepOrinalModels = ArrayList<StepOrinalModel>()
        var size = 5
        if (step % 2 == 0)
            size = 4
        if (step == 4) {
            for (i in 1..size) {
                var stepmodify = StepOrinalModel()
                stepmodify.step = "$step"
                stepmodify.stepTitle = "kiểm tra khoang động cơ"
                stepmodify.subStepTitle3 = "bên ngoài cửa xe $step-$i"
                stepOrinalModels.add(stepmodify)
            }
        } else
            for (i in 1..size) {
                val stepmodify = StepOrinalModel()
                stepmodify.step = "$step"
                stepmodify.stepTitle = "kiểm tra chung"
                stepmodify.subStep = "$step." + i
                stepmodify.subStepTitle1 = "bên ngoài xe"
                stepmodify.subStepTitle2 = "bên trái trước"
                stepmodify.subStepTitle3 = "bên ngoài cửa xe $step-$i"
                stepOrinalModels.add(stepmodify)
            }

        return convertStepOrinalModelsToStepModifyModels(stepOrinalModels)
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
            if (currentStep < 7) {
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
        uploadVideo(getImageContentUri(context, path))
    }

    interface Callbacks {
        fun onGetImageLoader(): ImageLoader

        fun onVideoSelected(video: VideoData)

        fun onConnected(connectedAccountName: String)
    }
}
