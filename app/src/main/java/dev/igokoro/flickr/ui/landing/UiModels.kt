package dev.igokoro.flickr.ui.landing

import androidx.annotation.StringRes

data class PhotoCluster(
    val type: Int,
    val order: Int,
    val label: Text,
    val photos: List<String>
)

/**
 * Model to support both resource and "remote" strings
 */
sealed class Text

data class RemoteText(
    val text: String
) : Text()

data class ResourceText(
    @StringRes val resId: Int
) : Text()