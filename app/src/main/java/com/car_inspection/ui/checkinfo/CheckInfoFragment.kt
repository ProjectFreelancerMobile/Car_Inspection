package com.car_inspection.ui.checkinfo

import android.app.DatePickerDialog
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.car_inspection.R
import com.car_inspection.library.commonview.DatePickerFragment
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.checkdetail.CheckDetailFragment
import com.car_inspection.utils.Constanst
import com.car_inspection.utils.formatTimeNumber
import com.toan_itc.core.utils.popFragment
import com.toan_itc.core.utils.switchFragment
import kotlinx.android.synthetic.main.check_info_fragment.*

class CheckInfoFragment : BaseFragment() {
    private var CAR_PRODUCTS = arrayListOf<String>("Toyota", "Honda", "BMW", "Hyundai", "Kia")
    private var dayCheck: String = ""

    companion object {
        fun newInstance() = CheckInfoFragment()
    }

    override fun initViews() {
        btnContinue.setOnClickListener { activity?.switchFragment(null, CheckDetailFragment.newInstance(), R.id.fragmentContainer) }
        btnPrevious.setOnClickListener { activity?.popFragment() }
        edtLicensePlate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Constanst.CAR_CODE = if (TextUtils.isEmpty(p0)) "" else p0.toString()
            }

        })
    }

    override fun setLayoutResourceID(): Int = R.layout.check_info_fragment

    override fun initData() {
        updateProgressStep(1)
        initListCarProducts()
        initListCarSeries()
        initListYearProduct()
        initCalenda()
    }

    private fun initCalenda() {
        tvCalendar.setOnClickListener {
            val timePicker = DatePickerFragment.newInstance(System.currentTimeMillis())
            timePicker.setOnDateSetListener(object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
                    val dateResult = formatTimeNumber(day) + "/" + formatTimeNumber(month + 1) + "/" + year
                    tvDate.text = dateResult
                }
            })
            timePicker.show(fragmentManager, "datePicker")
        }
    }

    private fun initListCarProducts() {
        val listCarProductors = activity?.resources?.getStringArray(R.array.car_productor)
        val dataAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, listCarProductors)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCarProductors.adapter = dataAdapter
    }

    private fun initListCarSeries() {
        val listCarSeries = activity?.resources?.getStringArray(R.array.car_series)
        val dataAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, listCarSeries)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCarSeries.adapter = dataAdapter
    }

    private fun initListYearProduct() {
        val listYears = activity?.resources?.getStringArray(R.array.year_product)
        val dataAdapter = ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, listYears)
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spYearProduct.adapter = dataAdapter
    }

    private fun updateProgressStep(step: Int) {
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