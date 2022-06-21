package com.zhangwww.videoplayer.utils

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import com.zhangwww.videoplayer.VideoPlayerApp
import com.zhangwww.videoplayer.config.IParseRule
import com.zhangwww.videoplayer.config.OriginSource
import timber.log.Timber

object ParseHelper {

    private const val UserAgentPC =
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Mobile Safari/537.36"

    private val parseWebView: WebView by lazy {
        val webView = WebView(VideoPlayerApp.appContext)
        setupWebView(webView)
        return@lazy webView
    }

    private var isParseSuccess = false
    private lateinit var parseRule: IParseRule
    private lateinit var originSource: OriginSource

    var parseListener: OnParseListener? = null

    // 需要先设置解析规则，才能开始解析
    fun setParseRule(parseRule: IParseRule) {
        this.parseRule = parseRule
    }

    fun setOriginSource(originSource: OriginSource) {
        this.originSource = originSource
    }

    fun parse(url: String) {
        val parseUrl = parseRule.url + url
        parseWebView.loadUrl(parseUrl)
        Timber.d("parse: parseUrl = $parseUrl")
    }

    fun destroy() {
        parseWebView.parent?.let {
            (it as? ViewGroup)?.removeView(parseWebView)
        }
        parseWebView.destroy()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(webView: WebView) {
        try {
            CookieManager.getInstance().removeAllCookies(null)
            CookieManager.getInstance().flush()
        } catch (e: Exception) {
            Timber.e("setupWebView: $webView", e)
        }
        CookieManager.getInstance().setAcceptCookie(true)
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
        webView.settings.domStorageEnabled = true
        webView.settings.apply {
            setSupportZoom(true)
            domStorageEnabled = true
            allowFileAccess = true
            loadsImagesAutomatically = true
            loadWithOverviewMode = false
            javaScriptEnabled = true
            builtInZoomControls = true
            useWideViewPort = true
            defaultTextEncodingName = "UTF-8"
            userAgentString = UserAgentPC
        }
        webView.webChromeClient = MyWebChromeClient()
        webView.webViewClient = MyWebViewClient()
    }

    private class MyWebChromeClient : WebChromeClient() {
        override fun onJsAlert(
            view: WebView,
            url: String,
            message: String,
            result: JsResult
        ): Boolean {
            Timber.d("onJsAlert: url = $url, message = $message")
            return false
        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            // Timber.d("onProgressChanged: newProgress = $newProgress")
        }
    }

    private class MyWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            Timber.d("onPageFinished: url = $url")
            if (!isParseSuccess) {
                Timber.d("onParseSuccess: false, $url")
                parseListener?.onParseSuccess(false, url)
            }
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Timber.d("onPageStarted: url = $url")
            isParseSuccess = false
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
//            Timber.d(
//                """ shouldInterceptRequest:
//                url    = ${request?.url},
//                method = ${request?.method},
//                header  = ${request?.requestHeaders},
//            """
//            )
            val requestUrl = request?.url?.toString()

//            if (requestUrl?.contains("m3u8") == true) {
//                Timber.d("shouldInterceptRequest: $requestUrl")
//            }

            Timber.d("shouldInterceptRequest: $requestUrl")

            if (parseRule.isSuccess(requestUrl, originSource)) {
                isParseSuccess = true
                Timber.d("onParseSuccess: success, $requestUrl")
                parseListener?.onParseSuccess(true, requestUrl!!)
                return null
            }
            return super.shouldInterceptRequest(view, request)
        }


        override fun onLoadResource(view: WebView?, url: String?) {
            // Timber.d("onLoadResource: url = $url")
            super.onLoadResource(view, url)
        }
    }


    fun interface OnParseListener {
        fun onParseSuccess(isSuccess: Boolean, url: String?)
    }

}