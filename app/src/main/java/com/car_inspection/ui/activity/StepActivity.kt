package com.car_inspection.ui.activity

import android.accounts.AccountManager
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.toolbox.ImageLoader
import com.blankj.utilcode.util.CrashUtils
import com.car_inspection.R
import com.car_inspection.library.youtube.Auth
import com.car_inspection.library.youtube.Constants
import com.car_inspection.library.youtube.util.Utils
import com.car_inspection.library.youtube.util.VideoData
import com.car_inspection.ui.step.StepFragment
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.Scopes
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeScopes
import com.toan_itc.core.base.CoreBaseActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import java.io.IOException
import java.util.*
import javax.inject.Inject

class StepActivity : CoreBaseActivity(), HasSupportFragmentInjector,StepFragment.Callbacks {
    override fun onGetImageLoader(): ImageLoader {
        return mImageLoader!!
    }

    override fun onVideoSelected(video: VideoData) {
    }

    override fun onConnected(connectedAccountName: String) {
        loadData()
    }

    private fun loadData() {
        if (mChosenAccountName == null) {
            chooseAccount()
            return
        }

        loadUploadedVideos()
    }

    private fun loadUploadedVideos() {
        if (mChosenAccountName == null) {
            return
        }

        setProgressBarIndeterminateVisibility(true)
        object : AsyncTask<Void, Void, List<VideoData>>() {
            override fun doInBackground(vararg voids: Void): List<VideoData>? {

                val youtube = YouTube.Builder(transport, jsonFactory,
                        credential).setApplicationName(Constants.APP_NAME)
                        .build()

                try {
                    /*
                     * Now that the user is authenticated, the app makes a
                     * channels list request to get the authenticated user's
                     * channel. Returned with that data is the playlist id for
                     * the uploaded videos.
                     * https://developers.google.com/youtube
                     * /v3/docs/channels/list
                     */
                    val clr = youtube.channels()
                            .list("contentDetails").setMine(true).execute()

                    // Get the user's uploads playlist's id from channel list
                    // response
                    val uploadsPlaylistId = clr.items[0]
                            .contentDetails.relatedPlaylists
                            .uploads

                    val videos = ArrayList<VideoData>()

                    // Get videos from user's upload playlist with a playlist
                    // items list request
                    val pilr = youtube.playlistItems()
                            .list("id,contentDetails")
                            .setPlaylistId(uploadsPlaylistId)
                            .setMaxResults(20L).execute()
                    val videoIds = ArrayList<String>()

                    // Iterate over playlist item list response to get uploaded
                    // videos' ids.
                    for (item in pilr.items) {
                        videoIds.add(item.contentDetails.videoId)
                    }

                    // Get details of uploaded videos with a videos list
                    // request.
                    val vlr = youtube.videos()
                            .list("id,snippet,status")
                            .setId(TextUtils.join(",", videoIds)).execute()

                    // Add only the public videos to the local videos list.
                    for (video in vlr.items) {
                        if ("public" == video.status
                                        .privacyStatus) {
                            val videoData = VideoData()
                            videoData.video = video
                            videos.add(videoData)
                        }
                    }

                    // Sort videos by title
                    Collections.sort(videos) { videoData, videoData2 ->
                        videoData.title.compareTo(
                                videoData2.title)
                    }

                    return videos

                } catch (availabilityException: GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(availabilityException
                            .connectionStatusCode)
                } catch (userRecoverableException: UserRecoverableAuthIOException) {
                    startActivityForResult(
                            userRecoverableException.intent,
                            REQUEST_AUTHORIZATION)
                } catch (e: IOException) {
                    Utils.logAndShow(this@StepActivity, Constants.APP_NAME, e)
                }

                return null
            }

            override fun onPostExecute(videos: List<VideoData>?) {
                setProgressBarIndeterminateVisibility(false)

                if (videos == null) {
                    return
                }
            }

        }.execute(null as Void?)
    }
    companion object {
        val ACCOUNT_KEY = "accountName"
        val MESSAGE_KEY = "message"
        val YOUTUBE_ID = "youtubeId"
        val YOUTUBE_WATCH_URL_PREFIX = "http://www.youtube.com/watch?v="
        val REQUEST_AUTHORIZATION_INTENT = "com.google.example.yt.RequestAuth"
        val REQUEST_AUTHORIZATION_INTENT_PARAM = "com.google.example.yt.RequestAuth.param"
        val REQUEST_GOOGLE_PLAY_SERVICES = 0
        val REQUEST_GMS_ERROR_DIALOG = 1
        val REQUEST_ACCOUNT_PICKER = 2
        val REQUEST_AUTHORIZATION = 3
        val RESULT_PICK_IMAGE_CROP = 4
        val RESULT_VIDEO_CAP = 5
        val REQUEST_DIRECT_TAG = 6
    }

    private var broadcastReceiver: UploadBroadcastReceiver? = null
    val TAG = "MainActivity"

    var transport = AndroidHttp.newCompatibleTransport()
    var jsonFactory: JsonFactory = GsonFactory()
    lateinit var credential: GoogleAccountCredential
    private var mImageLoader: ImageLoader? = null
    private var mChosenAccountName: String? = null
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun setLayoutResourceID(): Int = R.layout.step_activity

    override fun initViews() {
        pushFragment(StepFragment.newInstance())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        credential = GoogleAccountCredential.usingOAuth2(
                applicationContext, listOf(Scopes.PROFILE, YouTubeScopes.YOUTUBE))
        // set exponential backoff policy
        credential.backOff = ExponentialBackOff()

        if (savedInstanceState != null) {
            mChosenAccountName = savedInstanceState.getString(ACCOUNT_KEY)
        } else {
            loadAccount()
        }

        credential.selectedAccountName = mChosenAccountName
    }

    private fun isCorrectlyConfigured(): Boolean {
        // This isn't going to internationalize well, but we only really need
        // this for the sample app.
        // Real applications will remove this section of code and ensure that
        // all of these values are configured.
        if (Auth.KEY.startsWith("Replace")) {
            return false
        }
        return if (Constants.UPLOAD_PLAYLIST.startsWith("Replace")) {
            false
        } else true
    }

    override fun onResume() {
        super.onResume()
        if (broadcastReceiver == null)
            broadcastReceiver = UploadBroadcastReceiver()
        val intentFilter = IntentFilter(
                REQUEST_AUTHORIZATION_INTENT)
        LocalBroadcastManager.getInstance(this).registerReceiver(
                broadcastReceiver!!, intentFilter)
    }

    private fun loadAccount() {
        val sp = PreferenceManager
                .getDefaultSharedPreferences(this)
        mChosenAccountName = sp.getString(ACCOUNT_KEY, null)
        invalidateOptionsMenu()
    }

    private fun saveAccount() {
        val sp = PreferenceManager
                .getDefaultSharedPreferences(this)
        sp.edit().putString(ACCOUNT_KEY, mChosenAccountName).commit()
    }

    override fun onPause() {
        super.onPause()
        if (broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(
                    broadcastReceiver!!)
        }
        if (isFinishing) {
            // mHandler.removeCallbacksAndMessages(null);
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES ->
                if (resultCode == Activity.RESULT_OK) {
                    haveGooglePlayServices()
                } else {
                    checkGooglePlayServicesAvailable()
                }
            REQUEST_AUTHORIZATION ->
                if (resultCode != Activity.RESULT_OK) {
                    chooseAccount()
                }

            REQUEST_ACCOUNT_PICKER ->
                if (resultCode == Activity.RESULT_OK && data != null
                        && data.extras != null) {
                    val accountName = data.extras!!.getString(
                            AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName != null) {
                        mChosenAccountName = accountName
                        credential.selectedAccountName = accountName
                        saveAccount()
                    }
                }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ACCOUNT_KEY, mChosenAccountName)
    }

    fun showGooglePlayServicesAvailabilityErrorDialog(
            connectionStatusCode: Int) {
        runOnUiThread {
            val dialog = GooglePlayServicesUtil.getErrorDialog(
                    connectionStatusCode, this@StepActivity,
                    REQUEST_GOOGLE_PLAY_SERVICES)
            dialog.show()
        }
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     */
    private fun checkGooglePlayServicesAvailable(): Boolean {
        val connectionStatusCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this)
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
            return false
        }
        return true
    }

    private fun haveGooglePlayServices() {
        // check if there is already an account selected
        if (credential.selectedAccountName == null) {
            // ask user to choose account
            chooseAccount()
        }
    }

    private fun chooseAccount() {
        startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER)
    }

    private inner class UploadBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == REQUEST_AUTHORIZATION_INTENT) {
                Log.d(TAG, "Request auth received - executing the intent")
                val toRun = intent
                        .getParcelableExtra<Intent>(REQUEST_AUTHORIZATION_INTENT_PARAM)
                startActivityForResult(toRun, REQUEST_AUTHORIZATION)
            }
        }
    }

    private inner class MissingConfig(val title: String, val body: String)


    override fun initData() {

    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.stepContainer, fragment)
                .commit()
    }

    fun popFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
