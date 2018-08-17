package com.car_inspection.ui.base

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.car_inspection.R
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.toan_itc.core.base.BaseViewModel
import com.toan_itc.core.base.CoreBaseDataFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.EventBusException

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

abstract class BaseDataFragment<VM : BaseViewModel> : CoreBaseDataFragment<VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.v("onCreate:" + this.javaClass.simpleName)
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        Logger.v("onDestroyView:" + this.javaClass.simpleName)
        super.onDestroyView()
    }

    override fun onDestroy() {
        Logger.v("onDestroy:" + this.javaClass.simpleName)
        /*if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }*/
        super.onDestroy()
    }

    override fun onStart() {
       /* try {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }catch (e: EventBusException){
            e.printStackTrace()
        }*/
        super.onStart()
    }

    fun showSnackBar(message: String) {
        view?.let {
            val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_LONG)
            val view = snackBar.view
            val tv = view.findViewById(R.id.snackbar_text) as TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            } else {
                tv.gravity = Gravity.CENTER_HORIZONTAL
            }
            snackBar.show()
        }
    }
}
