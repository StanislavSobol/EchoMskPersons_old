package com.gmail.echomskfan.persons.service.mediaplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.support.v4.app.NotificationCompat
import com.gmail.echomskfan.persons.MainActivity
import com.gmail.echomskfan.persons.R
import com.gmail.echomskfan.persons.data.CastVM
import java.io.IOException

class MediaPlayerService : Service() {

    private val NOTIFICATION_ID = 1

    var playingItem: CastVM? = null

    private var mediaPlayer: MediaPlayer? = null
    private var trackTread: Thread? = null

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
            val pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0)

            val notificationBuilder = NotificationCompat.Builder(applicationContext)
                    .setSmallIcon(R.drawable.ic_baseline_play_arrow_24px)
                    .setContentTitle(appName)
                    .setContentText(playingItem?.formattedDate + " : " + playingItem?.getTypeSubtype()) // message preview
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)

            val notification = notificationBuilder.build()
            notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
            notification.tickerText = appName + " " +
                    applicationContext.getString(R.string.notification_ticket)
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    var parentActivity: MainActivity? = null

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
}