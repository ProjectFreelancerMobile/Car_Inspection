package com.car_inspection.ui.base

import androidx.core.content.ContextCompat
import com.car_inspection.R
import com.toan_itc.core.base.CoreBaseFragment
import com.yuvraj.livesmashbar.anim.AnimIconBuilder
import com.yuvraj.livesmashbar.enums.GravityView
import com.yuvraj.livesmashbar.view.LiveSmashBar
import pyxis.uzuki.live.richutilskt.utils.runOnUiThread


/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

abstract class BaseFragment : CoreBaseFragment() {

   /* override fun onStart() {
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
    }*/

    fun showSnackBar(message: String) {
        context?.apply {
            runOnUiThread {
                LiveSmashBar.Builder(activity!!)
                        .icon(R.mipmap.ic_launcher)
                        .iconAnimation(AnimIconBuilder(this).pulse())
                        .title(message)
                        .titleColor(ContextCompat.getColor(this, R.color.white))
                        .gravity(GravityView.TOP)
                        .duration(2000)
                        .dismissOnTapOutside()
                        .show()
            }
        }
    }

}
