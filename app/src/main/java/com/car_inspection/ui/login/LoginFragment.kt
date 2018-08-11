package com.car_inspection.ui.login

import android.os.Bundle
import android.view.View
import com.car_inspection.MainActivity
import com.car_inspection.R
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.home.HomeFragment
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment() {
    override fun initViews() {

    }

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun setLayoutResourceID(): Int = R.layout.login_fragment


    override fun initData() {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener { (activity as MainActivity).pushFragment(HomeFragment.newInstance()) }
    }
}