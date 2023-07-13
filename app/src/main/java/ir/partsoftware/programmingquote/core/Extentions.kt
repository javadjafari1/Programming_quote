package ir.partsoftware.programmingquote.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import ir.partsoftware.programmingquote.R

fun Context.shareText(text: String) {
    try {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        startActivity(sendIntent)
    } catch (e: Exception) {
        Toast.makeText(
            this,
            getString(R.string.suitable_app_not_found_message),
            Toast.LENGTH_LONG
        ).show()
    }
}

fun Context.openUrl(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(
            this,
            getString(R.string.suitable_app_not_found_message),
            Toast.LENGTH_LONG
        ).show()
    }
}
