package com.car_inspection.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import com.car_inspection.R
import com.car_inspection.binding.FragmentDataBindingComponent
import com.car_inspection.databinding.LoginFragmentBinding
import com.car_inspection.ui.activity.MainActivity
import com.car_inspection.ui.base.BaseDataFragment
import com.car_inspection.ui.home.HomeFragment
import com.car_inspection.utils.isDebug
import com.toan_itc.core.architecture.Status
import com.toan_itc.core.architecture.autoCleared
import com.toan_itc.core.architecture.observer
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : BaseDataFragment<LoginViewModel>() {
    private var binding by autoCleared<LoginFragmentBinding>()
    private var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun setLayoutResourceID(): Int = R.layout.login_fragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<LoginFragmentBinding>(
                inflater,
                setLayoutResourceID(),
                container,
                false,
                dataBindingComponent
        )
        binding = dataBinding
        return dataBinding.root
    }

    override fun getViewModel(): Class<LoginViewModel> = LoginViewModel::class.java

    override fun initView() {
        btnLogin.setOnClickListener {
            checkLogin()
        }
    }

    override fun initData() {
        if(isDebug()){
            edtUserName.text?.append(getString(R.string.login_username))
            edtPass.text?.append(getString(R.string.login_password))
        }
    }

    private fun checkLogin() {
        edtUserName.error = null
        edtPass.error = null
        val email = edtUserName.text.toString()
        val password = edtPass.text.toString()
        var cancel = false
        var focusView: View? = null
        if (password.isEmpty()) {
            btnLogin.isActive = false
            focusView = edtPass
            cancel = true
        } else if (password.length < 3) {
            showSnackBar(getString(R.string.error_incorrect_password))
            btnLogin.isActive = false
            focusView = edtPass
            cancel = true
        }
        if (email.isEmpty()) {
            showSnackBar(getString(R.string.error_field_required))
            btnLogin.isActive = false
            focusView = edtUserName
            cancel = true
        }
        if (cancel) {
            focusView?.requestFocus()
        } else {
            btnLogin.isActive = true
            viewModel.login(email, password).observer(this@LoginFragment) {
                when (it.status) {
                    Status.SUCCESS -> (activity as MainActivity).pushFragment(HomeFragment.newInstance())
                    Status.ERROR -> {
                        showSnackBar(it.message.toString())
                    }
                    Status.LOADING -> {
                    }
                }
            }
        }
    }
}