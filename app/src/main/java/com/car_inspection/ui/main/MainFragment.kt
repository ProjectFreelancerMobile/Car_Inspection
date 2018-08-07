package com.car_inspection.ui.main

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
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment(), StepAdapter.StepAdapterListener {

    private val SAVE_PATH: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

    var items: ArrayList<StepModifyModel> = ArrayList()
    lateinit var stepAdapter: StepAdapter
    var isTakePicture = false
    var currentPosition = 0
    companion object {
        fun newInstance() = MainFragment()
    }

    override fun initViews() {

    }

    override fun setLayoutResourceID() = R.layout.main_fragment

    override fun initData() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSubStep.layoutManager = LinearLayoutManager(activity)

        initTestData()

        stepAdapter = StepAdapter (this!!.activity!!)
        stepAdapter.stepAdapterListener = this
        stepAdapter.items = this.items
        rvSubStep.adapter = stepAdapter
        stepAdapter.notifyDataSetChanged()

        pgStep.useRectangleShape()
        pgStep.setMaximumPercentage(0.3f)
        pgStep.gravity = Gravity.CENTER
        pgStep.isShowingPercentage = true
        pgStep.setTextColor(Color.BLACK)
        pgStep.setText("30%")
        pgStep.updateView()
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

    fun initTestData() {
        var stepOrinalModels = ArrayList<StepOrinalModel>()

        for (i in 1..20) {
            var stepmodify = StepOrinalModel()
            stepmodify.step = "2"
            stepmodify.subStep = "2." + i
            stepmodify.subStepTitle1 = "bên ngoài xe"
            stepmodify.subStepTitle2 = "bên trái trước"
            stepmodify.subStepTitle3 = "bên ngoài cửa xe"
            stepOrinalModels.add(stepmodify)
        }

        items = convertStepOrinalModelsToStepModifyModels(stepOrinalModels)
    }

    fun autoScrollAfterCheckComplete() {
        rvSubStep.post(object : Runnable {
            override fun run() {
                rvSubStep.smoothScrollBy(0,  stepAdapter.heightItem)
            }
        })
    }

    override fun onRadioGroupCheckChangeListner(group: RadioGroup, checkId: Int, position: Int) {
        currentPosition = position
        when (checkId) {
            R.id.cbG -> {
                showLayoutVideo()
                items?.get(position)?.rating = "G"
            }
            R.id.cbP -> {
                showLayoutTakepicture()
                items?.get(position)?.rating = "P"
            }
            R.id.cbF -> {
                showLayoutTakepicture()
                items?.get(position)?.rating = "F"
            }
        }
        if (stepAdapter.isFinishCheckItem()) {
            layoutFinishCheck.visibility = View.VISIBLE
        } else layoutFinishCheck.visibility = View.GONE

        if (position < stepAdapter.itemCount - 3)
            autoScrollAfterCheckComplete()
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
}
