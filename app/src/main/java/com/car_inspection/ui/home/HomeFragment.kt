package com.car_inspection.ui.home

import com.car_inspection.R
import com.car_inspection.ui.activity.MainActivity
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.checkinfo.CheckInfoFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun initViews() {
        btnCheckCar.setOnClickListener {
            (activity as MainActivity).pushFragment(CheckInfoFragment.newInstance())
            //activity?.switchFragment(null, CheckInfoFragment.newInstance(), R.id.fragmentContainer)
        }
    }

    override fun setLayoutResourceID(): Int = R.layout.home_fragment

    override fun initData() {

    }

}