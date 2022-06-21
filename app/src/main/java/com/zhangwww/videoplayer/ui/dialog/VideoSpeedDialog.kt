package com.zhangwww.videoplayer.ui.dialog

import android.annotation.SuppressLint
import android.content.Context
import com.blankj.utilcode.util.ScreenUtils
import com.lxj.xpopup.core.CenterPopupView
import com.zhangwww.videoplayer.R
import com.zhangwww.videoplayer.databinding.DialogVideoSpeedBinding

@SuppressLint("ViewConstructor")
class VideoSpeedDialog(context: Context, private val onSpeedSelect: (speed: VideoSpeed) -> Unit) :
    CenterPopupView(context) {

    override fun getImplLayoutId(): Int = R.layout.dialog_video_speed

    override fun onCreate() {
        super.onCreate()
        val binding = DialogVideoSpeedBinding.bind(popupImplView)
        binding.btnSpeed05.setOnClickListener {
            onClick(VideoSpeed.SpeedX05)
        }
        binding.btnSpeed10.setOnClickListener {
            onClick(VideoSpeed.SpeedX10)
        }
        binding.btnSpeed15.setOnClickListener {
            onClick(VideoSpeed.SpeedX15)
        }
        binding.btnSpeed20.setOnClickListener {
            onClick(VideoSpeed.SpeedX20)
        }
        binding.btnSpeed25.setOnClickListener {
            onClick(VideoSpeed.SpeedX25)
        }
    }


    private fun onClick(speed: VideoSpeed) {
        dismiss()
        onSpeedSelect.invoke(speed)
    }

}

enum class VideoSpeed(val value: Float) {
    SpeedX05(0.5f),
    SpeedX10(1f),
    SpeedX15(1.5f),
    SpeedX20(2f),
    SpeedX25(2.5f),
}