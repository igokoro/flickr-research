package dev.igokoro.flickr.ui.grid

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.RxPagingSource
import androidx.paging.rxjava3.cachedIn
import androidx.paging.rxjava3.observable
import dev.igokoro.flickr.R
import dev.igokoro.flickr.data_layer.Photo
import dev.igokoro.flickr.data_layer.PhotosRxPagingSourceFactory
import dev.igokoro.flickr.data_layer.SearchConfig
import dev.igokoro.flickr.ui.landing.RemoteText
import dev.igokoro.flickr.ui.landing.ResourceText
import dev.igokoro.flickr.ui.landing.Text
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

interface PhotoGridViewModel {

    val photos: Observable<PagingData<Photo>>
    val title: Text

}

private const val PARAM_GRID = "grid"

class PhotoGridViewModelImpl @ViewModelInject constructor(
    private val preparePagingSource: PreparePagingSource,
    buildTitle: ScreenTitle,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel(), PhotoGridViewModel {


    override val photos: Observable<PagingData<Photo>> =
        Pager(
            PagingConfig(
                pageSize = 60,
                maxSize = 4000,
                initialLoadSize = 60
            )
        ) {
            preparePagingSource(savedStateHandle.get(PARAM_GRID)!!)
        }
            .observable
            .cachedIn(viewModelScope)

    override val title = buildTitle(savedStateHandle.get(PARAM_GRID)!!)
}

class PreparePagingSource @Inject constructor(
    private val photosRxPagingSourceFactory: PhotosRxPagingSourceFactory
) : Function1<PhotoGridParam, RxPagingSource<Int, Photo>> {
    override fun invoke(param: PhotoGridParam) = when (param.rollType) {
        RollType.RECENT -> photosRxPagingSourceFactory.build()
        RollType.TAG -> photosRxPagingSourceFactory.build(
            SearchConfig(
                param.tag!!
            )
        )
    }
}

class ScreenTitle @Inject constructor(
) : Function1<PhotoGridParam, Text> {
    override fun invoke(param: PhotoGridParam) = when (param.rollType) {
        RollType.RECENT -> ResourceText(R.string.recent)
        RollType.TAG -> RemoteText(param.tag!!)
    }
}