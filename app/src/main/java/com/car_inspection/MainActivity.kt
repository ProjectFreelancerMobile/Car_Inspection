package com.car_inspection

import androidx.fragment.app.Fragment
import com.car_inspection.ui.login.LoginFragment
import com.toan_itc.core.base.CoreBaseActivity
import com.toan_itc.core.utils.addFragment
import com.toan_itc.core.utils.removeFragment
import com.toan_itc.core.utils.switchFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : CoreBaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun setLayoutResourceID(): Int = R.layout.main_activity

    override fun initViews(){
        addFragment(LoginFragment.newInstance(), R.id.container)
    }

    override fun initData() {

    }

}