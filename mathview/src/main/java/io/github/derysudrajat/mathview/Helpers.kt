package io.github.derysudrajat.mathview

import android.content.Context
import android.content.res.Configuration
import java.net.URLEncoder

object Helpers {

    fun encode(url: String?): String = URLEncoder.encode("\\Huge $url", "utf-8")

    fun Context.isDarkMode(): Boolean =
        this.resources?.configuration?.uiMode
            ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    inline fun <reified T : Enum<T>> safeValueOf(type: String, default: T): T {
        return try {
            val name = type.map { if (it.isLetterOrDigit()) it.uppercase() else "_" }
                .joinToString("")
            java.lang.Enum.valueOf(T::class.java, name)
        } catch (e: IllegalArgumentException) {
            default
        }
    }
}
