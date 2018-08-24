package com.car_inspection.ui.base

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.car_inspection.R
import com.google.android.material.snackbar.Snackbar
import com.toan_itc.core.base.CoreBaseFragment
import org.greenrobot.eventbus.EventBus

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

abstract class BaseFragment : CoreBaseFragment() {

    override fun onStart() {
        try {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }catch (e:Exception){}
        super.onStart()
    }

    override fun onDestroy() {
        try {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this)
            }
        }catch (e:Exception){}
        super.onDestroy()
    }

    fun showSnackBar(message: String) {
        view?.let {
            val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val view = snackBar.view
            val tv = view.findViewById(R.id.snackbar_text) as TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            } else {
                tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
                tv.gravity = Gravity.CENTER_HORIZONTAL
            }
            snackBar.show()
        }
    }

}
