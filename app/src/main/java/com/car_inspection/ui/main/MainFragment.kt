package com.car_inspection.ui.main

import com.car_inspection.R
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.login.LoginFragment
import com.toan_itc.core.utils.addFragment

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun initViews() {
        activity?.addFragment(LoginFragment.newInstance(), R.id.fragmentContainer)
    }

    override fun setLayoutResourceID(): Int = R.layout.main_fragment

    override fun initData() {

    }

}