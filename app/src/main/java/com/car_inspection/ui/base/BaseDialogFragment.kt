package com.car_inspection.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.car_inspection.R

abstract class BaseDialogFragment : DialogFragment() {
    private var mContentView: View? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AppTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        if (null == mContentView) {
            mContentView = inflater.inflate(setLayoutResourceID(), container, false)
        }
        return mContentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
    }

    override fun show(manager: FragmentManager?, tag: String?) {
        manager?.let {
            val transaction = it.beginTransaction()
            try {
                transaction.add(this, tag)
                transaction.commit()
            } catch (ignored: IllegalStateException) {
                transaction.commitAllowingStateLoss()
                ignored.printStackTrace()
            }
        }
    }

    protected abstract fun initViews()

    @LayoutRes
    protected abstract fun setLayoutResourceID(): Int

    protected abstract fun initData()

}