package com.car_inspection.ui.checkdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.widget.ArrayAdapter
import com.car_inspection.R
import com.car_inspection.ui.activity.MainActivity
import com.car_inspection.ui.activity.StepActivity
import com.car_inspection.ui.base.BaseFragment
import kotlinx.android.synthetic.main.check_detail_fragment.*

class CheckDetailFragment : BaseFragment() {

    companion object {
        fun newInstance() = CheckDetailFragment()
    }

    override fun initViews() {
        btnContinue.setOnClickListener {
            startActivity(Intent(activity,StepActivity::class.java))
            activity?.finish()
        }
        btnPrevious.setOnClickListener {
            (activity as MainActivity).popFragment()
        }
    }

    override fun setLayoutResourceID(): Int = R.layout.check_detail_fragment

    override fun initData() {
        updateProgressStep(2)
        initListOutColor()
    }

    private fun initListOutColor() {
        activity?.resources?.getStringArray(R.array.out_colors)?.let {listColor->
            val dataAdapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, listColor)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spOutColor.adapter = dataAdapter
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgressStep(step: Int) {
        val percent = step * 1f / 17
        pgStep.setMaximumPercentage(percent)
        pgStep.useRoundedRectangleShape(20.0f)
        pgStep.setProgressColor(resources.getColor(R.color.blue_500))
        pgStep.setProgressBackgroundColor(resources.getColor(R.color.blue_200))
        pgStep.text = "${step * 100 / 17}%"
        pgStep.textSize = 14f
        pgStep.setTextColor(Color.WHITE)
        pgStep.gravity = Gravity.CENTER
        pgStep.updateView()
    }
}