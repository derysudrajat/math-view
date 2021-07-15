package com.derysudrajat.mathview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.derysudrajat.mathview.Helpers.isDarkMode
import java.io.File

class MathView : WebView {
    private var text: String? = null
    private var mContext: Context? = null

    @Volatile
    private var pageLoaded = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(context: Context) {
        setBackgroundColor(Color.TRANSPARENT)
        layoutParams = LinearLayout.LayoutParams(
            280,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
        }
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setInitialScale(1)
        text = ""
        mContext = context
        pageLoaded = false

        // enable javascript
        settings.loadWithOverviewMode = true
        settings.javaScriptEnabled = true

        // caching
        val dir: File = context.cacheDir
        if (!dir.exists()) {
            Log.d(TAG, "directory does not exist")
            val mkdirsStatus: Boolean = dir.mkdirs()
            if (!mkdirsStatus) {
                Log.e(TAG, "directory creation failed")
            }
        }
        settings.setAppCachePath(dir.path)
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK

        // disable click
        isClickable = false
        isLongClickable = false
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN

        loadUrl("file:///android_asset/www/MathTemplate.html")
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                pageLoaded = true
                val data = BASE_URL + encode(if (isDarkMode()) "\\color{white}{$text}" else text)
                loadDataWithBaseURL("localhost://", getData(data), "text/html", "UTF-8", null)
                super.onPageFinished(view, url)
            }
        }
    }

    fun setFormula(text: String?) {
        this.text = text
        val data = BASE_URL + encode(if (isDarkMode()) "\\color{white}{$text}" else text)
        if (pageLoaded) loadData(getData(data), "text/html", "UTF-8")
        else Log.e(TAG, "Page is not loaded yet.")
    }

    private fun encode(url: String?): String = Helpers.encode(url)
    private fun isDarkMode(): Boolean = (mContext as Activity).isDarkMode()
    private fun getData(url: String): String {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MathPreview</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <p id=\"result\" align = \"center\">\n" +
                "        <img src=\"$url\">\n" +
                "    </p>\n" +
                "</body>\n" +
                "</html>"
    }

    companion object {
        private val TAG = MathView::class.java.simpleName
        private const val BASE_URL = "https://render.githubusercontent.com/render/math?math="
    }
}