package com.toan_itc.core.utils

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * Created by ToanDev on 11/1/17.
 * Email:Huynhvantoan.itc@gmail.com
 */

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction()
            .func()
            .addToBackStack(javaClass.simpleName)
            .commitAllowingStateLoss()
}

fun AppCompatActivity.popFragment() {
    supportFragmentManager.popBackStack()
}

fun FragmentActivity.popFragment() {
    supportFragmentManager.popBackStack()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int){
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction{replace(frameId, fragment)}
}

fun FragmentActivity.switchFragment(from: Fragment?, to: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        if (from == null) {
            replace(frameId, to)
        } else {
            if (!to.isAdded) {
                hide(from).add(frameId, to)
            } else {
                hide(from).show(to)
            }
        }
    }
}

fun FragmentActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun FragmentActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.inTransaction { remove(fragment) }
}

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addFragmentToActivity(fragment: Fragment, tag: String) {
    supportFragmentManager.transact {
        add(fragment, tag)
    }
}
/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}


fun setRequestedOrientationPortrait(activity: FragmentActivity) {
    if (activity.resources.configuration.orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun setRequestedOrientationLandscape(activity: FragmentActivity) {
    if (activity.resources.configuration.orientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}