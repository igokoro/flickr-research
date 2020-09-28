package dev.igokoro.flickr.ui.landing

import dev.igokoro.flickr.R
import dev.igokoro.flickr.data.FlickrRepo
import dev.igokoro.flickr.data.Page
import dev.igokoro.flickr.data.Photo
import dev.igokoro.flickr.data.RecentPhotosRequest
import dev.igokoro.flickr.ktx.first
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class MainPageLoadingUseCase @Inject constructor(
    private val recentPhotosUseCase: RecentPhotosClusterUseCase,
    private val hotTagsUseCase: HotTagsUseCase
) {

    fun prepareStream(): Observable<List<PhotoCluster>> {
        return Observable.combineLatest(
            recentPhotosUseCase.execute()
                .map { listOf(it) }
                .toObservable()
                .startWithItem(
                    listOf(
                        PhotoCluster(
                            0,
                            0,
                            ResourceText(R.string.recent),
                            listOf()
                        )
                    )
                ),
            hotTagsUseCase.execute()
                .map { clusters ->
                    clusters.map { cluster ->
                        cluster.copy(order = cluster.order + 1)
                    }
                }
                .toObservable()
                .startWithItem(listOf()),
            { recent, tags -> (recent + tags).sortedBy { it.order } }
        )
    }
}

class HotTagsUseCase @Inject constructor(
    private val repo: FlickrRepo,
    private val toUrl: PhotoUrlConverter
) {
    fun execute(): Single<List<PhotoCluster>> {
        return repo.hotTags().map {
            it.mapIndexed { index, tag ->
                PhotoCluster(
                    type = 1,
                    order = index,
                    label = RemoteText("#${tag.label}"),
                    photos = listOf(toUrl(tag.cover))
                )
            }
        }
    }
}

class RecentPhotosClusterUseCase @Inject constructor(
    private val repo: FlickrRepo,
    private val toCluster: PageToClusterConverter
) {
    fun execute(): Flowable<PhotoCluster> {
        return repo.recent(
            RecentPhotosRequest(
                pageSize = 8,
                page = 1,
                before = System.currentTimeMillis() / 1000
            )
        )
            .map { toCluster(it) }.toFlowable()
//            .repeatWhen { Flowable.interval(30L, TimeUnit.SECONDS) }
    }
}

class PageToClusterConverter @Inject constructor(
    private val toUrl: PhotoUrlConverter
) : Function1<Page, PhotoCluster> {
    override fun invoke(page: Page): PhotoCluster {
        return PhotoCluster(
            type = 0,
            order = 0,
            label = ResourceText(R.string.recent),
            photos = page.photos.first(8).map { toUrl(it) }
        )
    }
}

class PhotoUrlConverter @Inject constructor(
) : Function1<Photo, String> {
    override fun invoke(photo: Photo) =
        "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg"
}