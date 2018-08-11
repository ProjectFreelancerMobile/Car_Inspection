package com.car_inspection.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.car_inspection.MainActivity
import com.car_inspection.R
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.checkinfo.CheckInfoFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun initViews() {
    }

    override fun setLayoutResourceID(): Int = R.layout.home_fragment

    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCheckCar.setOnClickListener { (activity as MainActivity).pushFragment(CheckInfoFragment.newInstance()) }
    }
}