package dev.igokoro.flickr.data_layer

import androidx.paging.PagingSource.LoadResult
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PhotosRxPagingSourceFactory @Inject constructor(
    private val flickrRepo: FlickrRepo
) {
    // can't use constructor injection to avoid internal -> public visibility propagation
    @Inject internal lateinit var converter: PageToLoadResultConverter

    fun build(config: SearchConfig? = null): RxPagingSource<Int, Photo> {
        val ceiling = System.currentTimeMillis() / 1000
        return if (config == null) {
            PhotosRxPagingSource(converter) { page, size ->
                flickrRepo.recent(
                    RecentPhotosRequest(
                        page = page,
                        pageSize = size,
                        before = ceiling
                    )
                )
            }
        } else {
            PhotosRxPagingSource(converter) { page, size ->
                flickrRepo.photosForTag(
                    SearchRequestParams(
                        tag = config.tag,
                        page = page,
                        pageSize = size,
                        before = ceiling
                    )
                )
            }
        }
    }
}

internal class PhotosRxPagingSource constructor(
    private val toLoadResult: Function1<Page, LoadResult<Int, Photo>>,
    private val source: (Int, Int) -> Single<Page>
) : RxPagingSource<Int, Photo>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Photo>> {
        val page = params.key ?: 1
        return source(
            page,
            params.loadSize
        )
            .map { toLoadResult(it) }
            .onErrorReturn { LoadResult.Error(it) }
    }
}

internal class PageToLoadResultConverter @Inject constructor(
) : Function1<Page, LoadResult<Int, Photo>> {
    override fun invoke(page: Page): LoadResult<Int, Photo> {
        return LoadResult.Page(
            data = page.photos,
            prevKey = if (page.number == 1) null else (page.number - 1),
            nextKey = page.number + 1
        )
    }
}