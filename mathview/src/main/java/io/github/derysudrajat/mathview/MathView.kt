package io.github.derysudrajat.mathview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.WebView
import io.github.derysudrajat.mathview.Helpers.isDarkMode


class MathView : WebView {

    private var mContext: Context? = null

    @Volatile
    private var pageLoaded = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    var formula: String = ""
        set(value) {
            loadFormula(value)
        }

    var loadFromMathJax: Boolean = false
        set(value) {
            field = value
            loadFormula(formula)
        }

    private var textColor: String = ""

    private fun loadFormula(value: String) {
        val data = BASE_URL + encode(if (isDarkMode()) "\\color{white}{$value}" else value)
        Log.d(TAG, "setFormula: $data")

        loadData(
            if (loadFromMathJax) getDataFromMatJax(value) else getData(data),
            "text/html; charset=utf-8",
            "UTF-8"
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(context: Context) {
        setBackgroundColor(Color.TRANSPARENT)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setInitialScale(1)
        formula = ""
        mContext = context
        pageLoaded = false
        loadFromMathJax = false
        textColor = if (isDarkMode()) "white" else "black"

        this.settings.javaScriptEnabled = true
        this.settings.useWideViewPort = true
        this.settings.loadWithOverviewMode = true
        this.settings.domStorageEnabled = true
    }

    private fun encode(url: String?): String = Helpers.encode(url)
    private fun isDarkMode(): Boolean = mContext?.isDarkMode() ?: false
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

    /**
     * @param value formula to be rendered
     * @return html data to be rendered
     */
    private fun getDataFromMatJax(value: String) = """
    <!DOCTYPE html>
    <html>
    <head>
    <script src="https://polyfill.io/v3/polyfill.min.js?features=es6"></script>
    <script type="text/javascript" id="MathJax-script" async
      src="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-chtml.js">
    </script>
    </head>
    <body>
    <h2
        style= "color: $textColor;">
        $$${"\\huge $value"}$$
    </h2>
    </body>
    </html>""".trimIndent()


    companion object {
        private val TAG = MathView::class.java.simpleName
        private const val BASE_URL = "https://render.githubusercontent.com/render/math?math="
    }
}
