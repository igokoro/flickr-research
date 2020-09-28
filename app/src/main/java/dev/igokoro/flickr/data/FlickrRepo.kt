package dev.igokoro.flickr.data

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface FlickrRepo {
    fun recent(params: RecentPhotosRequest): Single<Page>

    fun hotTags(): Single<List<Tag>>
    fun photosForTag(params: SearchRequestParams): Single<Page>
}

internal class FlickrRepoImpl @Inject constructor(
    private val api: FlickrApi,
    private val toTags: TagConverter,
    private val toPage: PageConverter
) : FlickrRepo {
    override fun recent(params: RecentPhotosRequest): Single<Page> {
        return api.search(
            pageSize = params.pageSize,
            page = params.page,
            before = System.currentTimeMillis()
        )
            .map { toPage(it) }
    }

    override fun hotTags(): Single<List<Tag>> {
        return api.hotTags()
            .map { toTags(it) }
    }

    override fun photosForTag(params: SearchRequestParams): Single<Page> {
        return api.search(
            tag = params.tag,
            pageSize = params.pageSize,
            page = params.page,
            before = params.before
        )
            .map { toPage(it) }
    }
}