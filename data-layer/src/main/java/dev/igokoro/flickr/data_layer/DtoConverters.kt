package dev.igokoro.flickr.data_layer

import javax.inject.Inject

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

internal class PhotoConverter @Inject constructor(
) : Function1<PhotoDto, Photo> {
    override fun invoke(dto: PhotoDto) = Photo(
        id = dto.id,
        farm = dto.farm,
        secret = dto.secret,
        server = dto.server,
        title = dto.title
    )
}

internal class PageConverter @Inject constructor(
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