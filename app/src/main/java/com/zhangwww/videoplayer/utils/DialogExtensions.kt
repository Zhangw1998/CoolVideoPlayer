package com.zhangwww.videoplayer.utils

import android.app.Activity
import com.lxj.xpopup.XPopup
import com.zhangwww.videoplayer.config.OriginSource
import com.zhangwww.videoplayer.data.ParseInfo
import com.zhangwww.videoplayer.ui.dialog.VideoOriginDialog
import com.zhangwww.videoplayer.ui.dialog.VideoSpeed
import com.zhangwww.videoplayer.ui.dialog.VideoSpeedDialog
import com.zhangwww.videoplayer.ui.main.ParseSourceDialog

fun Activity.showChooseOriginSourceDialog(onOriginSourceSelected: (source: OriginSource) -> Unit) {
    val dialog = VideoOriginDialog(this, onOriginSourceSelected)
    XPopup.Builder(this)
        .isDestroyOnDismiss(true)
        .asCustom(dialog)
        .show()
}

fun Activity.showChooseParseSourceDialog(onParseSelected: (ParseInfo) -> Unit) {
    val dialog = ParseSourceDialog(this, onParseSelected)
    XPopup.Builder(this)
        .isDestroyOnDismiss(true)
        .asCustom(dialog)
        .show()
}

fun Activity.showSpeedDialog(onSpeedSelect: (VideoSpeed) -> Unit) {
    val dialog = VideoSpeedDialog(this, onSpeedSelect)
    XPopup.Builder(this)
        .isDestroyOnDismiss(true)
        .asCustom(dialog)
        .show()
}