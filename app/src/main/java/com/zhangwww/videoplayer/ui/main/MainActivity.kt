package com.zhangwww.videoplayer.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.zhangwww.videoplayer.R
import com.zhangwww.videoplayer.config.IParseRule
import com.zhangwww.videoplayer.config.OriginSource
import com.zhangwww.videoplayer.databinding.ActivityMainBinding
import com.zhangwww.videoplayer.ui.video.VideoActivity
import com.zhangwww.videoplayer.utils.*

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersionBar(isOnly = true) { fitsSystemWindows(true) }
        // setContentView(R.layout.activity_main)
        setContentView(viewBinding.root)
        WebViewHelper.init(this)
        initView()
        initAction()
        initData()

    }

    override fun onDestroy() {
        WebViewHelper.destroy()
        ParseHelper.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (WebViewHelper.browserWebView.canGoBack()) {
            WebViewHelper.browserWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initData() {
        ParseHelper.setParseRule(IParseRule.MU)
        ParseHelper.setOriginSource(OriginSource.IQIYI)
        WebViewHelper.browserWebView.loadUrl(OriginSource.IQIYI.url)
    }

    private fun initView() {
        viewBinding.flContainer.addView(WebViewHelper.browserWebView)
        WebViewHelper.browserListener = object : WebViewHelper.OnWebViewEventListener {
            override fun onPageStart() {
                viewBinding.progressBar.progress = 0
                viewBinding.progressBar.isVisible = true
                viewBinding.lottiePurse.isVisible = false
            }

            override fun onPageFinish(url: String?) {
                viewBinding.progressBar.isVisible = false
                viewBinding.lottiePurse.isVisible = true
                viewBinding.lottiePurse.progress = 0f
                viewBinding.lottiePurse.playAnimation()
                if (url == null) return
            }

            override fun onProgressChanged(newProgress: Int) {
                viewBinding.progressBar.progress = newProgress
            }
        }
        viewBinding.lottiePurse.setOnClickListener {
            val currentUrl = WebViewHelper.browserWebView.url
            if (currentUrl.isNullOrBlank()) {
                toast("Url 无效， Url=$currentUrl")
                return@setOnClickListener
            }
            viewBinding.lottieLoading.isVisible = true
            ParseHelper.parse(currentUrl)
        }
        // todo 长按输入链接解析
        viewBinding.lottiePurse.setOnLongClickListener {
            XPopup.Builder(this)
                .asCustom(InputDialog(this) {
                    VideoActivity.launch(this, it)
                }).show()
            return@setOnLongClickListener true
        }
        viewBinding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.item_menu_choose_parse -> {
                    showChooseParseSourceDialog { parseInfo ->
                        ParseHelper.setParseRule(parseInfo.parseRule)
                    }
                }
                R.id.item_menu_choose_source -> {
                    showChooseOriginSourceDialog { source ->
                        ParseHelper.setOriginSource(source)
                        WebViewHelper.browserWebView.loadUrl(source.url)
                    }
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun initAction() {
        ParseHelper.parseListener = ParseHelper.OnParseListener { isSuccess, url ->
            runOnUiThread {
                viewBinding.lottieLoading.isVisible = false
            }
            if (isSuccess) {
                toast("解析成功！")
                VideoActivity.launch(this, url!!)
            }
        }
    }
}