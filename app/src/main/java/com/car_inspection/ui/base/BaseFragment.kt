package com.car_inspection.ui.base

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

}
