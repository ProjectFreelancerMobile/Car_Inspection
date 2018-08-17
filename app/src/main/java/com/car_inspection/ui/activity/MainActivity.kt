package com.car_inspection.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.car_inspection.R
import com.car_inspection.ui.login.LoginFragment
import com.car_inspection.ui.main.MainFragment
import com.toan_itc.core.base.CoreBaseActivity
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
        pushFragment(LoginFragment.newInstance())
    }

    override fun initData() {

    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
    }

    fun popFragment() {
        supportFragmentManager.popBackStack()
    }

    fun setRequestedOrientationPortrait() {
        if (resources.configuration.orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun setRequestedOrientationLandscape() {
        if (resources.configuration.orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}