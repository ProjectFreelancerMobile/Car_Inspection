package com.car_inspection.common

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.car_inspection.ui.activity.MainActivity
import com.car_inspection.app.App

/**
 * Created by ToanDev on 07/08/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

class StartAppAfterCrashHandler(private val activity: Activity) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        //FirebaseCrash.report(ex);

        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra("crash", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                or Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(App.instance, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val mgr = App.instance.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)
        activity.finish()
        System.exit(2)
    }
}
