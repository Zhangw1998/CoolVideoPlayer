package com.zhangwww.videoplayer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.*
import timber.log.Timber

@SuppressLint("StaticFieldLeak")
object WebViewHelper {

    private const val UserAgentPC =
        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Mobile Safari/537.36"

    lateinit var browserWebView: WebView
        private set

    var browserListener: OnWebViewEventListener? = null

    fun init(context: Context) {
        browserWebView = WebView(context)
        setupWebView(browserWebView)
    }

    fun destroy() {
        browserWebView.parent?.let {
            (it as? ViewGroup)?.removeView(browserWebView)
        }
        browserWebView.destroy()
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
        webView.settings.apply {
            setSupportZoom(true)
            domStorageEnabled = true
            allowFileAccess = true
            loadsImagesAutomatically = true
            loadWithOverviewMode = false
            javaScriptEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            useWideViewPort = true
            defaultTextEncodingName = "UTF-8"
            userAgentString = UserAgentPC
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            loadWithOverviewMode = true
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
            Timber.d("onProgressChanged: newProgress = $newProgress")
            browserListener?.onProgressChanged(newProgress)
        }
    }

    private class MyWebViewClient : WebViewClient() {

        override fun onPageFinished(view: WebView?, url: String?) {
            Timber.d("onPageFinished: url = $url")
            browserListener?.onPageFinish(url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            Timber.d("onPageStarted: url = $url")
            browserListener?.onPageStart()
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            Timber.d(
                """
                shouldInterceptRequest:
                url    = ${request?.url},
                method = ${request?.method},
                header  = ${request?.requestHeaders},
            """
            )
            return super.shouldInterceptRequest(view, request)
        }


        override fun onLoadResource(view: WebView?, url: String?) {
            // Timber.d("onLoadResource: url = $url")
            super.onLoadResource(view, url)
        }
    }

    interface OnWebViewEventListener {
        fun onPageStart()
        fun onPageFinish(url: String?)
        fun onProgressChanged(newProgress: Int)
    }
}