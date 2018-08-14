package com.car_inspection

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.car_inspection.ui.login.LoginFragment
import com.car_inspection.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            pushFragment(LoginFragment.newInstance())
        }
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
