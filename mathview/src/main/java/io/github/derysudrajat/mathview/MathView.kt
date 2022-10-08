package io.github.derysudrajat.mathview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.WebView
import androidx.annotation.ColorRes
import com.derysudrajat.mathview.R
import io.github.derysudrajat.mathview.Helpers.isDarkMode


class MathView : WebView {

    private var mContext: Context? = null

    @Volatile
    private var pageLoaded = false

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    var formula: String = ""
        set(value) {
            field = value
            loadFormula()
        }

    private var engine: String = DEFAULT_ENGINE
    private var textColor: RGB? = null
    private var textAlignment: String = DEFAULT_TEXT_ALIGNMENT

    private fun loadFormula() {
        val data = when (Helpers.safeValueOf(engine, MathViewEngine.MATH_JAX)) {
            MathViewEngine.MATH_JAX -> getDataFromMatJax(formula)
            MathViewEngine.MATH_RENDER -> {
                val data = BASE_URL + encode(if (isDarkMode()) "\\color{white}{$formula}" else formula)
                getData(data)
            }
        }
        Log.d(TAG, data)
        loadData(
            data,
            "text/html; charset=utf-8",
            "UTF-8"
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun init(context: Context, attrs: AttributeSet?) {
        setBackgroundColor(Color.TRANSPARENT)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setInitialScale(1)
        mContext = context

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MathView)
        formula = if (typedArray.hasValue(R.styleable.MathView_formula)) {
            typedArray.getString(R.styleable.MathView_formula) ?: ""
        } else {
            ""
        }

        textColor = if (typedArray.hasValue(R.styleable.MathView_textColor)) {
            val hexColor = String.format("#%06X",
                0xFFFFFF and typedArray.getColor(R.styleable.MathView_textColor, Color.BLACK))
            val color = hexColor.removePrefix("#")
            val r = color.substring(0, 2).toInt(16) // 16 for hex
            val g = color.substring(2, 4).toInt(16) // 16 for hex
            val b = color.substring(4, 6).toInt(16) // 16 for hex
            RGB(r, g, b)
        } else {
            if (isDarkMode()) RGB.WHITE else RGB.BLACK
        }

        textAlignment = if (typedArray.hasValue(R.styleable.MathView_textAlignment)) {
            when (typedArray.getString(R.styleable.MathView_textAlignment).orEmpty()) {
                "0" -> TextAlignment.RIGHT.value
                "1" -> TextAlignment.LEFT.value
                else -> TextAlignment.CENTER.value
            }
        } else DEFAULT_TEXT_ALIGNMENT

        engine = if (typedArray.hasValue(R.styleable.MathView_engine)) {
            when (typedArray.getString(R.styleable.MathView_engine).orEmpty()) {
                "0" -> MathViewEngine.MATH_JAX.value
                "1" -> MathViewEngine.MATH_RENDER.value
                else -> MathViewEngine.MATH_JAX.value
            }
        } else DEFAULT_ENGINE
        typedArray.recycle()

        loadFormula()

        pageLoaded = false
        this.settings.apply {
            javaScriptEnabled = true
            builtInZoomControls = false
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            domStorageEnabled = true
        }
        this.setOnLongClickListener { true }
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
    <script>
     MathJax = {
        chtml: { displayAlign: '${textAlignment}' }
    };
    </script>
    <script src="https://polyfill.io/v3/polyfill.min.js?features=es6"></script>
    <script type="text/javascript" id="MathJax-script" async
      src="https://cdn.jsdelivr.net/npm/mathjax@3/es5/tex-chtml.js">
    </script>
    </head>
    <body>
        <h2
            style= "color: ${textColor.toString()}">
            $$${"\\huge $value"}$$
        </h2>
    </body>
    </html>""".trimIndent()


    fun setTextColor(@ColorRes colorInt: Int) {
        val nothing: Int? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mContext?.getColor(colorInt)
            } else {
                mContext?.resources?.getColor(colorInt)
            }
        if (nothing == null) return
        val hexColor = String.format("#%06X", (0xFFFFFF and nothing))
        setTextColor(hexColor)
    }

    fun setTextColor(hexColor: String) {
        val color = hexColor.removePrefix("#")
        val r = color.substring(0, 2).toInt(16) // 16 for hex
        val g = color.substring(2, 4).toInt(16) // 16 for hex
        val b = color.substring(4, 6).toInt(16) // 16 for hex
        setTextColor(RGB(r,
            g,
            b)) // Webview doesn't handle colors in hex format well. it is necessary to convert to RGB
    }

    fun setTextColor(rgb: RGB) {
        textColor = rgb
    }

    /**
     * Set text alignment
     * @param textAlignment text alignment
     */
    fun setTextAlignment(textAlignment: TextAlignment) {
        this.textAlignment = textAlignment.value
    }

    /**
     * Set engine to be used
     * @param engine engine to be used
     */
    fun setMathViewEngine(engine: MathViewEngine) {
        this.engine = engine.value
    }

    data class RGB(val r: Int, val g: Int, val b: Int) {
        companion object {
            val WHITE = RGB(255, 255, 255)
            val BLACK = RGB(0, 0, 0)
        }

        override fun toString(): String = "rgb($r, $g, $b)"
    }


    enum class TextAlignment(val value: String) {
        LEFT("left"),
        CENTER("center"),
        RIGHT("right"),
    }

    enum class MathViewEngine(val value: String) {
        MATH_JAX("math_jax"),
        MATH_RENDER("math_render"),
    }

    companion object {
        private val TAG = MathView::class.java.simpleName
        private const val BASE_URL = "https://render.githubusercontent.com/render/math?math="
        private val DEFAULT_ENGINE = MathViewEngine.MATH_JAX.value
        private val DEFAULT_TEXT_ALIGNMENT = TextAlignment.CENTER.value
    }
}
