package com.car_inspection.ui.main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.car_inspection.R
import com.car_inspection.data.local.database.model.StepModifyModel
import com.car_inspection.data.local.database.model.StepOrinalModel
import com.car_inspection.ui.adapter.StepAdapter
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.inputtext.SuggestTextActivity
import com.car_inspection.ui.record.RecordFragment
import com.toan_itc.core.utils.addFragment
import google.com.carinspection.DisposableImpl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.concurrent.TimeUnit
import android.app.Activity



class MainFragment : BaseFragment(), StepAdapter.StepAdapterListener {
    private val REQUEST_SUGGEST_TEST = 0;
    private val SAVE_PATH: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

    var items: ArrayList<StepModifyModel> = ArrayList()
    lateinit var stepAdapter: StepAdapter
    var isTakePicture = false
    var currentPosition = 0

    var currentStep = 2

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun initViews() {
        activity?.addFragment(RecordFragment.newInstance(), R.id.fragmentRecord)
    }

    override fun setLayoutResourceID() = R.layout.main_fragment

    override fun initData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSubStep.layoutManager = LinearLayoutManager(activity)

        loadDataStep(currentStep)
        updateProgressStep(currentStep)

        btnSave.setOnClickListener { btnContinue.isActive = true }
        btnContinue.setOnClickListener {
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

        // disable scroll up android
//        rvSubStep.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
//            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//            }
//
//            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
//                val action = event.getAction()
//                if (action == MotionEvent.ACTION_DOWN) {
//                    lastY = event.getY()
//                }
//                 if (action == MotionEvent.ACTION_MOVE && event.getY() < lastY) {
//                     return true
//                }
//                 else return false
//            }
//
//            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//            }
//
//        })
    }

    fun updateProgressStep(step: Int) {
        val percent = step * 1f / 7
        pgStep.setMaximumPercentage(percent)
        pgStep.useRoundedRectangleShape(20.0f)
        pgStep.setProgressColor(resources.getColor(R.color.blue_500))
        pgStep.setProgressBackgroundColor(resources.getColor(R.color.blue_200))
        pgStep.text = "${step * 100 / 7}%"
        pgStep.textSize = 14f
        pgStep.setTextColor(Color.WHITE)
        pgStep.gravity = Gravity.CENTER
        pgStep.updateView()
    }

    fun loadDataStep(step: Int) {
        if (layoutFinishCheck.visibility == View.VISIBLE)
            layoutFinishCheck.visibility = View.GONE
        items.clear()
        items = initStepTestData(step)
        stepAdapter = StepAdapter(this.activity!!)
        stepAdapter.stepAdapterListener = this
        stepAdapter.items = items
        rvSubStep.adapter = stepAdapter

        updateProgressStep(step)
        tvStep.text = "Bước ${items.get(0).subStep}: ${items.get(0).subStepTitle1}"
    }

    fun convertStepOrinalModelsToStepModifyModels(stepOrinalModels: ArrayList<StepOrinalModel>): ArrayList<StepModifyModel> {
        var stepModifyModels = ArrayList<StepModifyModel>()
        if (stepOrinalModels != null && stepOrinalModels.size > 0) {
            for (stepOrinalModel in stepOrinalModels)
                run {
                    var stepModifyModel = StepModifyModel()
                    stepModifyModel.subStep = stepOrinalModel.subStep
                    stepModifyModel.subStepTitle1 = stepOrinalModel.subStepTitle1
                    stepModifyModel.subStepTitle2 = stepOrinalModel.subStepTitle2
                    stepModifyModel.subStepTitle3 = stepOrinalModel.subStepTitle3
                    stepModifyModels.add(stepModifyModel)
                }
        }
        return stepModifyModels
    }

    fun initStepTestData(step: Int): ArrayList<StepModifyModel> {
        var stepOrinalModels = ArrayList<StepOrinalModel>()
        var size = 5
        if (step % 2 == 0)
            size = 4

        for (i in 1..size) {
            var stepmodify = StepOrinalModel()
            stepmodify.step = "$step"
            stepmodify.subStep = "$step." + i
            stepmodify.subStepTitle1 = "bên ngoài xe"
            stepmodify.subStepTitle2 = "bên trái trước"
            stepmodify.subStepTitle3 = "bên ngoài cửa xe"
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
        intent.putExtra("note",items.get(position).note)
        startActivityForResult(intent, REQUEST_SUGGEST_TEST)
    }

    override fun onRadioGroupCheckChangeListner(group: RadioGroup, checkId: Int, position: Int) {
        currentPosition = position
        when (checkId) {
            R.id.cbG -> {
                showLayoutVideo()
                items.get(position).rating = "G"
            }
            R.id.cbP -> {
                showLayoutTakepicture()
                items.get(position).rating = "P"
            }
            R.id.cbF -> {
                showLayoutTakepicture()
                items.get(position).rating = "F"
            }
        }
        if (stepAdapter.isFinishCheckItem() && currentStep < 7) {
            if (layoutFinishCheck.visibility == View.GONE)
                layoutFinishCheck.visibility = View.VISIBLE
        }
        //else layoutFinishCheck.visibility = View.GONE

//        if (position < stepAdapter.itemCount - 3)
//            autoScrollAfterCheckComplete()
    }


    fun showLayoutTakepicture() {
        isTakePicture = true
        layoutTakePicture.visibility = View.VISIBLE
        layoutVideo.visibility = View.GONE
    }

    fun showLayoutVideo() {
        isTakePicture = false
        layoutTakePicture.visibility = View.GONE
        layoutVideo.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_SUGGEST_TEST) {
            if (resultCode === Activity.RESULT_OK) {
                val position = data.getIntExtra("position",0)
                items.get(position).note = data.getStringExtra("note")
            }
        }
    }
}
