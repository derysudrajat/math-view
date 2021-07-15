package com.derysudrajat.mathview

import android.app.Activity
import android.content.res.Configuration
import java.net.URLEncoder

object Helpers {

    fun encode(url: String?): String = URLEncoder.encode("\\Huge $url", "utf-8")

    fun Activity.isDarkMode(): Boolean =
        this.resources?.configuration?.uiMode
            ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}