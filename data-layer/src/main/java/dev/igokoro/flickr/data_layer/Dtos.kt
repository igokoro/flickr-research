package dev.igokoro.flickr.data_layer

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class PhotoPageEnvelopeDto(
    val photos: PhotoPageDto
)

@JsonClass(generateAdapter = true)
internal data class PhotoPageDto(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val total: Int,
    val photo: List<PhotoDto>
)

@JsonClass(generateAdapter = true)
internal data class PhotoDto(
    val id: String,
    val farm: String,
    val secret: String,
    val server: String,
    val title: String
)

@JsonClass(generateAdapter = true)
internal data class PhotosDto(
    val photo: List<PhotoDto>
)

@JsonClass(generateAdapter = true)
internal data class ThmDataDto(
    val photos: PhotosDto
)

@JsonClass(generateAdapter = true)
internal data class TagDto(
    val _content: String,
    val thm_data: ThmDataDto
)

@JsonClass(generateAdapter = true)
internal data class TagsDto(
    val tag: List<TagDto>
)

@JsonClass(generateAdapter = true)
internal data class HotTagsDto(
    val hottags: TagsDto
)