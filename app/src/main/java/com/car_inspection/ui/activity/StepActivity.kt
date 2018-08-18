package com.car_inspection.ui.activity

import androidx.fragment.app.Fragment
import com.car_inspection.R
import com.car_inspection.ui.step.StepFragment
import com.toan_itc.core.base.CoreBaseActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class StepActivity : CoreBaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun setLayoutResourceID(): Int = R.layout.step_activity

    override fun initViews(){
        pushFragment(StepFragment.newInstance())
    }

    override fun initData() {

    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.stepContainer, fragment)
                .commit()
    }

    fun popFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
