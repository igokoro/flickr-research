package dev.igokoro.flickr.ui.landing

import dev.igokoro.flickr.R
import dev.igokoro.flickr.data_layer.FlickrRepo
import dev.igokoro.flickr.data_layer.Page
import dev.igokoro.flickr.data_layer.Photo
import dev.igokoro.flickr.data_layer.RecentPhotosRequest
import dev.igokoro.flickr.ktx.first
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * This will prepare a stream that drives presentation for the landing screen.
 */
class MainPageLoadingUseCase @Inject constructor(
    private val recentPhotosUseCase: RecentPhotosClusterUseCase,
    private val hotTagsUseCase: HotTagsUseCase
) {

    /**
     * Combines a grid of recent photos at the top with a list of hot tags. Initial load includes
     * an empty recent photos grid to improve perception of UI loading.
     */
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

/**
 * This takes hot tags from repository and prepares data for display in UI.
 */
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

/**
 * This takes recent photos from repository and prepares them to be displayed.
 */
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
        // Updating recent photos is very simple!
        // but then we also need to think about the appropriate lifecycle/scoping for the stream
        // as updating when fragment is not displayed or is in back stack is not wanted.
        // Yet using simple viewFragmentLifecycle is not sufficient as the stream would be
        // interrupted on configuration changes...
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