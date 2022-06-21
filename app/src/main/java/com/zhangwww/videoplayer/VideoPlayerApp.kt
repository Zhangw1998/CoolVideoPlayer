package com.zhangwww.videoplayer

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.Utils
import com.zhangwww.videoplayer.utils.LogTree
import timber.log.Timber

class VideoPlayerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        Timber.plant(LogTree())
        Utils.init(this)
    }

    companion object {
        lateinit var appContext: Context
            private set
    }

}