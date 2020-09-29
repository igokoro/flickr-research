package dev.igokoro.flickr.data_layer

data class Tag(
    val label: String,
    val cover: Photo
)

data class Page(
    val number: Int,
    val total: Int,
    val photos: List<Photo>
)

data class Photo(
    val id: String,
    val farm: String,
    val secret: String,
    val server: String,
    val title: String
)

data class SearchConfig(
    val tag: String
)

sealed class PagedPhotoRequestParams

data class RecentPhotosRequest(
    val page: Int,
    val pageSize: Int,
    val before: Long
) : PagedPhotoRequestParams()

data class SearchRequestParams(
    val tag: String,
    val page: Int,
    val pageSize: Int,
    val before: Long
) : PagedPhotoRequestParams()
