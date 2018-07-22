package com.gmail.echomskfan.persons.service.mediaplayer

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.support.annotation.IdRes
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.RemoteViews
import com.gmail.echomskfan.persons.MainActivity
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.utils.StringUtils
import com.gmail.echomskfan.persons.utils.fromComputationToMain
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.io.IOException
import java.util.concurrent.TimeUnit


//http://blog.nkdroidsolutions.com/android-foreground-service-example-tutorial/
//https://medium.com/@britt.barak/notifications-part-3-going-custom-31c31609f314

class MediaPlayerService : Service() {

    private val NOTIFICATION_ID = 1

    var playingItem: CastVM? = null
    var parentActivity: MainActivity? = null

    private var mediaPlayer: MediaPlayer? = null
    //    private var trackTread: Thread? = null
    private var intervalDisposable: Disposable? = null
    private var actionsBroadCastReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter()
        filter.addAction(PLAY_ACTION)
        filter.addAction(PAUSE_ACTION)

        actionsBroadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.action.let {
                    when (it) {
                        PLAY_ACTION -> resume() //playingItem?.let { play(it) }
                        PAUSE_ACTION -> pause()
                        else -> {

                        }
                    }
                }
                Log.d("SSS", "actionsBroadCastReseiver  onReceive ${intent!!.action}")
            }
        }
        registerReceiver(actionsBroadCastReceiver, filter)
    }

    override fun onDestroy() {
        actionsBroadCastReceiver?.let { unregisterReceiver(it) }
        intervalDisposable?.run { dispose() }
        super.onDestroy()
    }

    override fun onBind(p0: Intent?) = MediaServiceBinder()

    fun play(playingItem: CastVM) {

        this.playingItem = playingItem

        stop()

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }

            try {
                mediaPlayer?.reset()
            } catch (illegalStateException: java.lang.IllegalStateException) {
                illegalStateException.printStackTrace()
            }

            mediaPlayer?.setDataSource(playingItem.mp3Url)

            mediaPlayer?.setOnPreparedListener({
                mediaPlayer?.start()
                startForeground()
                startTracking()
            })

            mediaPlayer?.prepareAsync()

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stop() {
        intervalDisposable?.run { dispose() }
    }

    private var notificationView: RemoteViews? = null


    private var notificationBuilder: NotificationCompat.Builder? = null

    private fun startForeground() {
        if (playingItem != null) {

            val appName = applicationContext.getString(R.string.app_name)

            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            notificationView = RemoteViews(this.packageName, R.layout.media_player_notification)

            notificationView?.setTextViewText(R.id.notificationDurationTextView, "SSSSSSSSS")


            bindButton(R.id.notificationPlayButton, Intent(this, NotificationPlayButtonHandler::class.java), notificationView!!)
            bindButton(R.id.notificationPauseButton, Intent(this, NotificationPauseButtonHandler::class.java), notificationView!!)

            notificationBuilder = NotificationCompat.Builder(applicationContext)
                    .setSmallIcon(R.drawable.ic_baseline_play_arrow_24px)
                    .setContentTitle(appName)
                    .setContentText(playingItem?.formattedDate + " : " + playingItem?.getTypeSubtype()) // message preview
                    .setContent(notificationView)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)

            notificationBuilder?.build()?.let {
                it.flags = it.flags or Notification.FLAG_ONGOING_EVENT
                it.tickerText = appName + " " +
                        applicationContext.getString(R.string.notification_ticket)
                startForeground(NOTIFICATION_ID, it)
            }
        }
    }

    private fun bindButton(@IdRes resId: Int, intent: Intent, notificationView: RemoteViews) {
        val buttonPlayPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        notificationView.setOnClickPendingIntent(resId, buttonPlayPendingIntent)
    }

    private fun updateNotification() {
        val mNotificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder?.build())
    }

//    private fun updateNotification() {
//        val mNotificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
//    }

    private fun startTracking() {
        intervalDisposable?.run { dispose() }

        intervalDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .fromComputationToMain()
                .subscribe({
                    if (parentActivity == null || mediaPlayer == null) {
                        intervalDisposable?.dispose()
                        return@subscribe
                    }

                    parentActivity?.trackMediaPlayer()

                    val mCurrentPosition = getCurrentPosition().div(1000)
                    notificationView?.setTextViewText(R.id.notificationDurationTextView, StringUtils.getAudioDuration(mCurrentPosition))
                    Log.d("SSS", StringUtils.getAudioDuration(mCurrentPosition))
//                    notificationView?.setTextViewText(R.id.notificationDurationTextView, it.toString())
                    updateNotification()

                    val isPlaying: Boolean = try {
                        mediaPlayer?.isPlaying ?: false
                    } catch (e: java.lang.IllegalStateException) {
                        false
                    }

                    if (!isPlaying) {
                        stopForeground(true)
                        stopSelf()
                        intervalDisposable?.dispose()
                    }
                })
    }

    fun getCurrentPosition() = mediaPlayer?.currentPosition ?: 0

    fun isPlaying() = mediaPlayer?.isPlaying ?: false

    fun seekTo(playBackPosition: Int) {
        mediaPlayer?.seekTo(playBackPosition)
    }

    fun pause() {
        mediaPlayer?.pause()
        stop()
    }

    fun resume() {
        mediaPlayer?.start()
        startTracking()
    }

    inner class MediaServiceBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }

    class NotificationPlayButtonHandler : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            MediaPlayerService.sendActionBroadCast(context, PLAY_ACTION)
        }
    }

    class NotificationPauseButtonHandler : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            MediaPlayerService.sendActionBroadCast(context, PAUSE_ACTION)
        }
    }

    companion object {

        const val PLAY_ACTION = "PLAY_ACTION"
        const val PAUSE_ACTION = "PAUSE_ACTION"

        fun sendActionBroadCast(context: Context, action: String) {
            val serviceIntent = Intent()
            serviceIntent.action = action
            context.sendBroadcast(serviceIntent)
        }
    }
}


