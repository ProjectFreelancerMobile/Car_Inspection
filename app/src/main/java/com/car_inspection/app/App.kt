package com.car_inspection.app

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.Utils
import com.car_inspection.R
import com.car_inspection.data.local.prefs.PreferenceManager
import com.car_inspection.di.AppInjector
import com.car_inspection.utils.isDebug
import com.crashlytics.android.Crashlytics
import com.github.moduth.blockcanary.BlockCanary
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.fabric.sdk.android.Fabric
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.realm.Realm
import java.io.IOException
import java.net.SocketException
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Created by ToanDev on 28/2/18.
 * Email:Huynhvantoan.itc@gmail.com
 */

class App : Application(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector() = dispatchingAndroidInjector
    private lateinit var appObserver: ForegroundBackgroundListener
    private var mRefWatcher: RefWatcher by Delegates.notNull()

    companion object {
        lateinit var instance: App
            private set
    }

    fun getRefWatcher(context: Context?): RefWatcher? {
        val app = context?.applicationContext as? App
        return app?.mRefWatcher
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        if (isDebug()) {
            //strictMode()
            setupLogger()
            //setupTest()
        }
        initCrash()
        rxJava()
        setupData()
    }

    private fun strictMode() {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .build())
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                .detectAll()
                .build())
    }

    private fun setupLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(getString(R.string.app_name))
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return isDebug()
            }
        })
        ProcessLifecycleOwner.get()
                .lifecycle
                .addObserver(ForegroundBackgroundListener()
                .also { appObserver = it })
    }

    private fun initCrash(){
        CrashUtils.init{crashInfo, _->
            Logger.e(crashInfo)
            //AppUtils.relaunchApp()
        }
    }

    private fun setupData() {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        Utils.init(this)
        PreferenceManager.initialize(this, Constants.APP_NAME)
        Realm.init(this)
        Fabric.with(this, Crashlytics())
    }

    private fun setupTest() {
        (getSystemService(ACTIVITY_SERVICE) as? ActivityManager)?.apply {
            Logger.e("memorySize=$memoryClass")
        }
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)
        BlockCanary.install(this, AppBlockCanaryContext()).start()
    }

    private fun rxJava(){
        RxJavaPlugins.setErrorHandler { e ->
            Logger.e("Undeliverable exception received, not sure what to do"+ e.message)
            if (e is OnErrorNotImplementedException || e is IOException || e is SocketException || e is InterruptedException
                    || e is NullPointerException || e is IllegalArgumentException || e is IllegalStateException) {
                e.printStackTrace()
                return@setErrorHandler
            }
        }
    }
}
