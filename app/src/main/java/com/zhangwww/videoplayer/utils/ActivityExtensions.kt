package com.zhangwww.videoplayer.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Looper
import android.widget.Toast

fun Activity.toast(content: String) {
    if (this.isDestroyed || this.isFinishing) return
    if (Thread.currentThread() != Looper.getMainLooper().thread) {
        runOnUiThread {
            Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}


fun Activity.copyToClipboard(content: String, label: String = "") {
    val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(label, content)
    clipboardManager.setPrimaryClip(clipData)
}