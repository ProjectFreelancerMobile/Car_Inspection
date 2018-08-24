package com.car_inspection.ui.step

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.car_inspection.R
import com.car_inspection.binding.FragmentDataBindingComponent
import com.car_inspection.data.model.StepModifyModel
import com.car_inspection.data.model.StepOrinalModel
import com.car_inspection.databinding.StepFragmentBinding
import com.car_inspection.event.RecordEvent
import com.car_inspection.library.youtube.YoutubeUploadRequest
import com.car_inspection.library.youtube.YoutubeUploader
import com.car_inspection.ui.adapter.StepAdapter
import com.car_inspection.ui.base.BaseDataFragment
import com.car_inspection.ui.inputtext.SuggestTextActivity
import com.car_inspection.ui.record.RecordOTGFragment
import com.car_inspection.utils.Constanst
import com.car_inspection.utils.createFolderPicture
import com.car_inspection.utils.listenToViews
import com.orhanobut.logger.Logger
import com.toan_itc.core.architecture.autoCleared
import com.toan_itc.core.utils.addFragment
import google.com.carinspection.DisposableImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.step_fragment.*
import java.io.File
import java.util.concurrent.TimeUnit

class StepFragment : BaseDataFragment<StepViewModel>(), StepAdapter.StepAdapterListener, View.OnClickListener {
    private val REQUEST_SUGGEST_TEST = 0
    private var currentSubStepName = ""
    private var items: List<StepModifyModel> = mutableListOf()
    lateinit var stepAdapter: StepAdapter
    private var isTakePicture = false
    private var currentPosition = 0
    private var currentStep = 2
    private var binding by autoCleared<StepFragmentBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    companion object {
        fun newInstance() = StepFragment()
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
        uploadYoutube(Constanst.getFolderVideoPath() + "test.mp4")
    }

    fun uploadYoutube(path: String) {
        var uri = Uri.parse(File(path).toString())
        var request = YoutubeUploadRequest()
        request.uri = uri
        request.setTitle("MPRJ Video Tite");
        request.setDescription("MPRJ Video Test");

        YoutubeUploader.upload(request, object : YoutubeUploader.ProgressListner {
            override fun onUploadProgressUpdate(progress: Int) {
                Logger.e("percent upload", progress)
            }

        }, activity)
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
                /*if (captureFragment != null) {
                    createFolderPicture(Constanst.getFolderPicturePath())
                    cameraFragment?.takePhotoOrCaptureVideo(object : CameraFragmentResultAdapter() {
                        override fun onPhotoTaken(bytes: ByteArray, filePath: String) {
                            Log.e("file images", "----------@@@@@@@@@@@@@@   $filePath")
                            stepAdapter.items?.get(currentPosition)?.imagepaths?.add(filePath)
                            showLayoutVideo()
                            overlay(activity!!, filePath, R.mipmap.ic_launcher_foreground, currentSubStepName)
                        }
                    }, Constanst.getFolderPicturePath(), currentSubStepName)

                }*/
            }
        }
    }

    private fun addFragmentRecord() {
        activity?.addFragment(RecordOTGFragment.newInstance(), R.id.fragmentRecord)
/*<<<<<<< HEAD

=======
        Observable.just(1L).delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableImpl<Long>() {
                    @SuppressLint("MissingPermission")
                    override fun onNext(t: Long) {
                        recordFragment = if (isCameraOTG())
                            RecordOTGFragment.newInstance()
                        else
                            null//RecordFragment.newInstance()
                        activity?.addFragment(recordFragment!!, R.id.fragmentRecord)
                    }
                })
>>>>>>> aaedb05d7f0db317ca6d3a610ca73e8d27f19e62*/
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
                    var stepModifyModel = StepModifyModel()
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
        rvSubStep.post(object : Runnable {
            override fun run() {
                rvSubStep.smoothScrollBy(0, stepAdapter.heightItem)
            }
        })
    }

    override fun onTextNoteClickListener(v: View, position: Int) {
        var intent = Intent(activity, SuggestTextActivity::class.java)
        intent.putExtra("position", position)
        intent.putExtra("note", items.get(position).note)
        startActivityForResult(intent, REQUEST_SUGGEST_TEST)
    }

    override fun onRadioGroupCheckChangeListner(group: RadioGroup, checkId: Int, position: Int) {
        currentPosition = position
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
        viewModel.sendEventBus(RecordEvent(true, currentSubStepName))
        isTakePicture = true
        /*activity?.apply {
            captureFragment = if (isCameraOTG())
                CaptureOTGFragment.newInstance()
            else {
                val builder = Configuration.Builder()
                builder.setCamera(Configuration.CAMERA_FACE_FRONT)
                        .setFlashMode(Configuration.FLASH_MODE_OFF)
                        .setMediaAction(Configuration.MEDIA_ACTION_PHOTO)
                CameraFragment.newInstance(builder.build())
            }
            addFragment(captureFragment!!, R.id.fragmentCapture)
        }*/
    }

    private fun showLayoutVideo() {
        viewModel.sendEventBus(RecordEvent())
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
}
