package com.car_inspection.ui.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.toan_itc.core.base.BaseViewModel
import com.toan_itc.core.base.CoreBaseDataActivity
import io.reactivex.disposables.Disposable

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

abstract class BaseDataActivity<VM : BaseViewModel, DB : ViewDataBinding> : CoreBaseDataActivity<VM, DB>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}
