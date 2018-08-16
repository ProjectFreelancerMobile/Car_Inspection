package com.car_inspection.ui.home

import com.car_inspection.R
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.checkinfo.CheckInfoFragment
import com.toan_itc.core.utils.addFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun initViews() {
        btnCheckCar.setOnClickListener { activity?.addFragment(CheckInfoFragment.newInstance(), R.id.container) }
    }

    override fun setLayoutResourceID(): Int = R.layout.home_fragment

    override fun initData() {

    }

}