package com.car_inspection.ui.login

import com.car_inspection.R
import com.car_inspection.ui.activity.MainActivity
import com.car_inspection.ui.base.BaseFragment
import com.car_inspection.ui.home.HomeFragment
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun setLayoutResourceID(): Int = R.layout.login_fragment

    override fun initViews() {
        btnLogin.setOnClickListener {
            /*activity?.apply {
                switchFragment(null, HomeFragment.newInstance() , R.id.fragmentContainer)
            }*/
            (activity as MainActivity).pushFragment(HomeFragment.newInstance())
        }
    }

    override fun initData() {
    }

}