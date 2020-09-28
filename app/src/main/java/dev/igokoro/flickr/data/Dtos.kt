package dev.igokoro.flickr.data

import com.squareup.moshi.JsonClass

// TODO move data layer code to its own module
@JsonClass(generateAdapter = true)
data class PhotoPageEnvelopeDto(
    val photos: PhotoPageDto
)

@JsonClass(generateAdapter = true)
data class PhotoPageDto(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoDto>
)

@JsonClass(generateAdapter = true)
data class PhotoDto(
    val id: String,
    val farm: String,
    val secret: String,
    val server: String,
    val title: String
)

@JsonClass(generateAdapter = true)
data class PhotosDto(
    val photo: List<PhotoDto>
)

@JsonClass(generateAdapter = true)
data class ThmDataDto(
    val photos: PhotosDto
)

@JsonClass(generateAdapter = true)
data class TagDto(
    val _content: String,
    val thm_data: ThmDataDto
)

@JsonClass(generateAdapter = true)
data class TagsDto(
    val tag: List<TagDto>
)

@JsonClass(generateAdapter = true)
data class HotTagsDto(
    val hottags: TagsDto
)