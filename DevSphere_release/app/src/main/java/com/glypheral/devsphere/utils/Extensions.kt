package com.glypheral.devsphere.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment

fun View.show() { visibility = View.VISIBLE }
fun View.hide() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Context.toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
fun Fragment.toast(msg: String) = requireContext().toast(msg)

fun String.isValidUrl(): Boolean {
    return try {
        val url = if (!startsWith("http://") && !startsWith("https://")) "https://$this" else this
        android.util.Patterns.WEB_URL.matcher(url).matches()
    } catch (e: Exception) { false }
}

fun String.toHttpsUrl(): String {
    return when {
        startsWith("http://") || startsWith("https://") -> this
        contains(".") -> "https://$this"
        else -> "https://www.google.com/search?q=${android.net.Uri.encode(this)}"
    }
}
