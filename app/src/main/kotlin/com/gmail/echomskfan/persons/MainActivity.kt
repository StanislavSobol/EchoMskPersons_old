package com.gmail.echomskfan.persons

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gmail.echomskfan.persons.data.CastVM
import com.gmail.echomskfan.persons.service.mediaplayer.MediaPlayerService
import com.gmail.echomskfan.persons.ui.BaseActivity
import com.gmail.echomskfan.persons.ui.casts.CastsFragment
import com.gmail.echomskfan.persons.ui.vips.VipsFragment
import com.gmail.echomskfan.persons.utils.StringUtils
import com.gmail.echomskfan.persons.utils.makeVisible
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.media_player.*

class MainActivity : BaseActivity<VipsFragment>(), IMainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter() = MainPresenter()

    private var mediaPlayerServiceIntent: Intent? = null
    private var mediaPlayerServiceConnection: ServiceConnection? = null
    private var mediaPlayerService: MediaPlayerService? = null
    private var playedItem: CastVM? = null
    private var playBackPosition: Int = 0

    override fun createFragment() = VipsFragment.newInstance()

    fun showCast(url: String) {
        val fragment = CastsFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragmentHolder, fragment, CastsFragment.TAG)
                .addToBackStack(CastsFragment.TAG)
                .commit()
    }

    override fun initViews() {
        initMediaPlayer()
        initPlayerViews()
    }

    private fun initMediaPlayer() {
        mediaPlayerServiceIntent = Intent(this, MediaPlayerService::class.java)
        mediaPlayerServiceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                mediaPlayerService = (service as MediaPlayerService.MediaServiceBinder).service
                mediaPlayerService?.parentActivity = this@MainActivity
                if (playedItem != null) {
                    mediaPlayerService?.play(playedItem!!)
                } else {
                    playedItem = mediaPlayerService?.playingItem
                    if (playedItem != null) {
                        initAudioPanel()
                    }
                }
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mediaPlayerService = null
            }
        }

        if (intent.action == null) {
            bindService(mediaPlayerServiceIntent, mediaPlayerServiceConnection, BIND_AUTO_CREATE)
        }
    }

    fun trackMediaPlayer() {
        if (mediaPlayerService != null) {
            this.playedItem = mediaPlayerService?.playingItem
            val mCurrentPosition = mediaPlayerService?.getCurrentPosition()?.div(1000)
            runOnUiThread {
                if (mediaPlayerService != null) {
                    if (mediaPlayerService?.isPlaying() == true) {
                        audioDurationTextView.text = StringUtils.getAudioDuration(mCurrentPosition
                                ?: 0)
                        audioSeekBar.progress = mCurrentPosition ?: 0
                        audioPlayImageButton.setImageDrawable(pauseDrawable())
                        audioSeekBar.isEnabled = true

                    } else {
                        audioDurationTextView.text = StringUtils.getAudioDuration(playedItem?.mp3Duration
                                ?: 0)
                        audioSeekBar.progress = 0
                        audioPlayImageButton.setImageDrawable(playDrawable())
                        audioSeekBar.isEnabled = false
                    }
                }
            }
        }
    }

    private fun initAudioPanel() {
        mediaPlayerLayout.makeVisible()

        audioPlayImageButton.setImageDrawable(pauseDrawable())
        audioSeekBar.max = playedItem?.mp3Duration ?: 0

        audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar) {}

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayerService?.seekTo(progress * 1000)
                    playBackPosition = mediaPlayerService?.getCurrentPosition() ?: 0
                }
            }
        })

    }

    private fun initPlayerViews() {

        mediaPlayerLayout.visibility = View.GONE
        audioPlayImageButton.setImageDrawable(pauseDrawable())
        audioPlayImageButton.setOnClickListener { processMediaPlayer() }

        val cancelImageButton = findViewById<ImageButton>(R.id.cancelImageButton)
        cancelImageButton.setImageDrawable(clearDrawable())
        cancelImageButton.setOnClickListener {
            mediaPlayerService?.pause()
            mediaPlayerService?.stop()
            unbindService(mediaPlayerServiceConnection)
            stopService(mediaPlayerServiceIntent)
            mediaPlayerService = null
            mediaPlayerLayout.visibility = View.GONE
        }
    }

    private fun processMediaPlayer() {
        if (mediaPlayerService?.isPlaying() == true) {
            mediaPlayerService?.pause()
            playBackPosition = mediaPlayerService?.getCurrentPosition() ?: 0
            audioPlayImageButton.setImageDrawable(playDrawable())
        } else {
            mediaPlayerService?.seekTo(playBackPosition)
            mediaPlayerService?.resume()
            audioPlayImageButton.setImageDrawable(pauseDrawable())
        }
    }

    fun play(item: CastVM) {
        if (!MApplication.isOnlineWithToast(true)) {
            return
        }

        this.playedItem = item

        if (mediaPlayerService == null) {
            startService(mediaPlayerServiceIntent)
            bindService(mediaPlayerServiceIntent, mediaPlayerServiceConnection, BIND_AUTO_CREATE)
        } else {
            mediaPlayerService?.play(item)
        }
        initAudioPanel()
    }

    private fun pauseDrawable() = ContextCompat.getDrawable(this, R.drawable.ic_baseline_pause_24px)

    private fun playDrawable() = ContextCompat.getDrawable(this, R.drawable.ic_baseline_play_arrow_24px)

    private fun clearDrawable() = ContextCompat.getDrawable(this, R.drawable.ic_clear_white_48dp)

}
