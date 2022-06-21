package com.zhangwww.videoplayer.ui.video

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.blankj.utilcode.util.BrightnessUtils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.slider.Slider
import com.gyf.immersionbar.ktx.immersionBar
import com.zhangwww.videoplayer.databinding.ActivityVideoBinding
import com.zhangwww.videoplayer.ui.dialog.VideoSpeed
import com.zhangwww.videoplayer.utils.showSpeedDialog
import com.zhangwww.videoplayer.utils.toast

class VideoActivity : AppCompatActivity() {

    private val viewModel: VideoViewModel by viewModels()
    private var videoUrl: String = ""

    private val viewBinding: ActivityVideoBinding by lazy {
        ActivityVideoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionBar(isOnly = true) {
            fitsSystemWindows(false)
        }
        // setContentView(R.layout.activity_video)
        setContentView(viewBinding.root)
        val url = intent.getStringExtra(PARAM_URL)
        if (url.isNullOrBlank()) {
            toast("Url 无效, url = $url")
            return
        }
        videoUrl = url
        initView()
        initAction()
    }

    override fun onDestroy() {
        viewBinding.playerView.player?.release()
        super.onDestroy()
    }

    private fun initView() {
        prepareVideo(videoUrl)
        viewBinding.ivMenu.setOnClickListener {
            setupControllerView()
            viewBinding.llControllerContainer.isVisible = true
        }
        viewBinding.playerView.setControllerVisibilityListener {
            val isVisible = it == View.VISIBLE
            viewBinding.ivMenu.isVisible = isVisible
        }
        viewBinding.llControllerContainer.setOnClickListener {
            viewBinding.llControllerContainer.isVisible = false
            viewBinding.playerView.hideController()
        }
        viewBinding.speedSlider.addOnChangeListener { _, value, fromUser ->
            if (!fromUser) return@addOnChangeListener
            updateSpeed(value)
        }
        viewBinding.voiceSlider.addOnChangeListener { _, value, fromUser ->
            if (!fromUser) return@addOnChangeListener
            updateVoice(value)
        }
        viewBinding.brightnessSlider.addOnChangeListener { _, value, fromUser ->
            if (!fromUser) return@addOnChangeListener
            updateBrightness(value)
        }
        viewBinding.playerView.setOnClickListener {
            viewBinding.llControllerContainer.isVisible = false
        }
    }

    private fun initAction() {

    }

    private fun prepareVideo(url: String) {
        if (viewBinding.playerView.player != null) return
        viewBinding.playerView.isVisible = true
        val player = ExoPlayer.Builder(this).build()
        player.playWhenReady = true
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.addListener(PlayerListenerImpl())
        viewBinding.playerView.player = player
    }


    private fun updateSpeed(value: Float) {
        val player = viewBinding.playerView.player ?: return
        player.playbackParameters = player.playbackParameters.withSpeed(value)
    }

    private fun updateVoice(value: Float) {
        val player = viewBinding.playerView.player ?: return
        player.deviceVolume = value.toInt()
    }

    private fun updateBrightness(value: Float) {
        window.attributes = window.attributes.apply {
            screenBrightness = value / 100f
        }
    }

    private inner class PlayerListenerImpl : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            viewBinding.lottieLoading.isVisible = playbackState == Player.STATE_BUFFERING
            when (playbackState) {
                Player.STATE_IDLE -> {

                }
                Player.STATE_BUFFERING -> {

                }
                Player.STATE_READY -> {

                }
                Player.STATE_ENDED -> {

                }
            }
        }
    }

    private fun setupControllerView() {
        val player = viewBinding.playerView.player ?: return
        viewBinding.speedSlider.value = player.playbackParameters.speed
        viewBinding.voiceSlider.value = player.volume * 100f

        viewBinding.voiceSlider.valueFrom = player.deviceInfo.minVolume.toFloat()
        viewBinding.voiceSlider.valueTo = player.deviceInfo.maxVolume.toFloat()
        viewBinding.voiceSlider.value = player.deviceVolume.toFloat()

        val brightness = window.attributes.screenBrightness
        if (brightness < 0) {
            viewBinding.brightnessSlider.value = 50f
        } else {
            viewBinding.brightnessSlider.value = brightness * 100f
        }
    }

    companion object {
        private const val TAG = "VideoActivity"
        private const val PARAM_URL = "param_url"

        fun launch(context: Context, url: String) {
            context.startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(PARAM_URL, url)
            })
        }
    }
}