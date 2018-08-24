package com.car_inspection.ui.activity

import android.Manifest
import android.content.pm.ActivityInfo
import androidx.fragment.app.Fragment
import com.car_inspection.R
import com.car_inspection.ui.login.LoginFragment
import com.toan_itc.core.base.CoreBaseActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

@RuntimePermissions
class MainActivity : CoreBaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun setLayoutResourceID(): Int = R.layout.main_activity

    @NeedsPermission(Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.GET_ACCOUNTS)
    fun startFragment() {}

    override fun initViews(){
        startFragmentWithPermissionCheck()
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
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