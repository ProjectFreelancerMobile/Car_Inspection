package com.car_inspection.ui.checkdetail

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import com.car_inspection.MainActivity
import com.car_inspection.R
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.main.MainFragment
import kotlinx.android.synthetic.main.check_detail_fragment.*

class CheckDetailFragment : BaseFragment() {
    companion object {
        fun newInstance() = CheckDetailFragment()
    }

    override fun initViews() {

    }

    override fun setLayoutResourceID(): Int = R.layout.check_detail_fragment

    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateProgressStep(2)
        initListOutColor()

        btnContinue.setOnClickListener { (activity as MainActivity).pushFragment(MainFragment.newInstance()) }
        btnPrevious.setOnClickListener { (activity as MainActivity).popFragment() }
    }

    fun initListOutColor() {
        var listColor = activity?.resources?.getStringArray(R.array.out_colors)
        var dataAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, listColor)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spOutColor.adapter = dataAdapter
    }

    fun updateProgressStep(step: Int) {
        val percent = step * 1f / 8
        pgStep.setMaximumPercentage(percent)
        pgStep.useRoundedRectangleShape(20.0f)
        pgStep.setProgressColor(resources.getColor(R.color.blue_500))
        pgStep.setProgressBackgroundColor(resources.getColor(R.color.blue_200))
        pgStep.text = "${step * 100 / 8}%"
        pgStep.textSize = 14f
        pgStep.setTextColor(Color.WHITE)
        pgStep.gravity = Gravity.CENTER
        pgStep.updateView()
    }
}