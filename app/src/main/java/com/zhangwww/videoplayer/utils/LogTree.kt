package com.zhangwww.videoplayer.utils

import android.util.Log
import com.blankj.utilcode.util.AppUtils
import timber.log.Timber

class LogTree : Timber.DebugTree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return AppUtils.isAppDebug() || Log.isLoggable("VideoPlayerTag", Log.DEBUG)
    }

}