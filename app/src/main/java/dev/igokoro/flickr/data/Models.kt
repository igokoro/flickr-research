package dev.igokoro.flickr.data

import javax.inject.Inject

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

internal class PhotosConverter @Inject constructor(
    private val toPhoto: PhotoConverter
) : Function1<PhotoPageEnvelopeDto, List<Photo>> {
    override fun invoke(dto: PhotoPageEnvelopeDto) =
        dto.photos.photo.map { toPhoto(it) }
}

class PageConverter @Inject constructor(
    private val toPhoto: PhotoConverter
) : Function1<PhotoPageEnvelopeDto, Page> {
    override fun invoke(dto: PhotoPageEnvelopeDto): Page {
        return Page(
            dto.photos.page,
            dto.photos.pages,
            dto.photos.photo.map { toPhoto(it) }
        )

    }

}

class PhotoConverter @Inject constructor(
) : Function1<PhotoDto, Photo> {
    override fun invoke(dto: PhotoDto) = Photo(
        id = dto.id,
        farm = dto.farm,
        secret = dto.secret,
        server = dto.server,
        title = dto.title
    )

}

internal class TagConverter @Inject constructor(
    private val toPhoto: PhotoConverter
) : Function1<HotTagsDto, List<Tag>> {
    override fun invoke(dto: HotTagsDto) = dto.hottags.tag.map { tag ->
        Tag(
            tag._content,
            toPhoto(tag.thm_data.photos.photo.first())
        )
    }
}
