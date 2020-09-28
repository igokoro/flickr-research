package dev.igokoro.flickr.ktx

import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import dev.igokoro.flickr.ui.landing.RemoteText
import dev.igokoro.flickr.ui.landing.ResourceText
import dev.igokoro.flickr.ui.landing.Text

fun TextView.text(value: Text) {
    when (value) {
        is ResourceText -> setText(value.resId)
        is RemoteText -> text = value.text
    }
}

fun Toolbar.title(value: Text) {
    when (value) {
        is ResourceText -> setTitle(value.resId)
        is RemoteText -> title = value.text
    }
}