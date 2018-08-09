package com.car_inspection.binding

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment

/**
 * Created by Toan.IT on 08/8/17.
 * Email:Huynhvantoan.itc@gmail.com
 */

/**
 * A Data Binding Component implementation for fragments.
 */
class FragmentDataBindingComponent(fragment: Fragment) : DataBindingComponent {

    private val adapter = FragmentBindingAdapters(fragment)

    override fun getFragmentBindingAdapters() = adapter
}
