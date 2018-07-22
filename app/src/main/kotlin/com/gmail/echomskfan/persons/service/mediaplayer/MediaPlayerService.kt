package com.gmail.echomskfan.persons.service.mediaplayer

import android.app.Notification
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
import android.widget.Toast
import com.gmail.echomskfan.persons.MainActivity
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.CastVM
import java.io.IOException


//http://blog.nkdroidsolutions.com/android-foreground-service-example-tutorial/
//https://medium.com/@britt.barak/notifications-part-3-going-custom-31c31609f314

class MediaPlayerService : Service() {

    private val NOTIFICATION_ID = 1

    var playingItem: CastVM? = null
    var parentActivity: MainActivity? = null

    private var mediaPlayer: MediaPlayer? = null
    private var trackTread: Thread? = null

    private var actionsBroadCastReseiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()

        val filter = IntentFilter()
        filter.addAction(PLAY_ACTION)
        filter.addAction(PAUSE_ACTION)

        actionsBroadCastReseiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("SSS", "actionsBroadCastReseiver  onReceive ${intent!!.action}")
            }
        }
        registerReceiver(actionsBroadCastReseiver, filter)
    }

    override fun onDestroy() {
        actionsBroadCastReseiver?.let { unregisterReceiver(it) }
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
        if (trackTread != null) {
            trackTread?.interrupt()
            trackTread = null
        }
    }

    private fun startForeground() {
        if (playingItem != null) {

            val appName = applicationContext.getString(R.string.app_name)

            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

            val notificationView = RemoteViews(this.packageName, R.layout.media_player_notification)

            bindButton(R.id.notificationPlayButton, Intent(this, NotificationPlayButtonHandler::class.java), notificationView)
            bindButton(R.id.notificationPauseButton, Intent(this, NotificationPauseButtonHandler::class.java), notificationView)

            val notificationBuilder = NotificationCompat.Builder(applicationContext)
                    .setSmallIcon(R.drawable.ic_baseline_play_arrow_24px)
                    .setContentTitle(appName)
                    .setContentText(playingItem?.formattedDate + " : " + playingItem?.getTypeSubtype()) // message preview
                    .setContent(notificationView)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)

            val notification = notificationBuilder.build()
            notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
            notification.tickerText = appName + " " +
                    applicationContext.getString(R.string.notification_ticket)
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun bindButton(@IdRes resId: Int, intent: Intent, notificationView: RemoteViews) {
        val buttonPlayPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        notificationView.setOnClickPendingIntent(resId, buttonPlayPendingIntent)
    }

    private fun startTracking() {
        trackTread = Thread(Runnable {
            try {
                while (!Thread.currentThread().isInterrupted) {
                    if (parentActivity == null || mediaPlayer == null) {
                        break
                    }
                    parentActivity?.trackMediaPlayer()

                    val isPlaying: Boolean = try {
                        mediaPlayer?.isPlaying ?: false
                    } catch (e: java.lang.IllegalStateException) {
                        false
                    }

                    if (!isPlaying) {
                        stopForeground(true)
                        stopSelf()
                        break
                    }

                    Thread.sleep(1000)
                }

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        })
        trackTread?.start()
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
            Toast.makeText(context, "Play Clicked", Toast.LENGTH_SHORT).show()
            MediaPlayerService.sendActionBroadCast(context, PLAY_ACTION)
//            val serviceIntent = Intent()
//            serviceIntent.action = PLAY_ACTION
//            context.sendBroadcast(serviceIntent)
        }
    }

    class NotificationPauseButtonHandler : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(context, "Pause Clicked", Toast.LENGTH_SHORT).show()
            MediaPlayerService.sendActionBroadCast(context, PAUSE_ACTION)

//            val serviceIntent = Intent()
//            serviceIntent.action = PLAY_ACTION
//            context.sendBroadcast(serviceIntent)


            //      context.sendBroadcast(MediaPlayerService.createActionBroadCast(PLAY_ACTION))
//            val extra = intent.getIntExtra(MediaPlayerService.BUTTON_EXTRA, 0)
//            Log.d("SSS", "PAUSE! $extra")
        }
    }

    companion object {

        fun sendActionBroadCast(context: Context, action: String) {
            val serviceIntent = Intent()
            serviceIntent.action = action
            context.sendBroadcast(serviceIntent)
        }

        const val PLAY_ACTION = "PLAY_ACTION"
        const val PAUSE_ACTION = "PAUSE_ACTION"


    }
}