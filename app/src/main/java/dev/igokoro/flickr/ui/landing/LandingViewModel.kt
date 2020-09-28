package dev.igokoro.flickr.ui.landing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import dev.igokoro.flickr.ui.grid.PhotoGridParam
import dev.igokoro.flickr.ui.grid.RollType
import dev.igokoro.flickr.utils.RxSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject

interface LandingViewModel {
    val content: LiveData<List<PhotoCluster>>
    val progress: LiveData<Boolean>
    val navigation: Observable<NavDirections>
    fun onRecentClick()
    fun onTagClick(tag: String)
}

internal class LandingViewModelImpl @ViewModelInject constructor(
    rxSchedulers: RxSchedulers,
    useCase: MainPageLoadingUseCase,
    liveDataFactory: LiveDataFactory
) : ViewModel(), LandingViewModel {

    override val progress: MutableLiveData<Boolean> = liveDataFactory.mutable()

    override val content: LiveData<List<PhotoCluster>> = liveDataFactory.liveDataFor(
        stream = useCase.prepareStream()
            .observeOn(rxSchedulers.ui)
            .doOnSubscribe { progress.value = true }
            .doFinally { progress.value = false },
        inside = this
    )
    private val navigationSink: Subject<NavDirections> = PublishSubject.create()
    override val navigation: Observable<NavDirections> by lazy { navigationSink.hide() }

    override fun onRecentClick() {
        navigationSink.onNext(
            LandingFragmentDirections.goToPhotoGridFragment(
                PhotoGridParam(
                    rollType = RollType.RECENT
                )
            )
        )
    }

    override fun onTagClick(tag: String) {
        navigationSink.onNext(
            LandingFragmentDirections.goToPhotoGridFragment(
                PhotoGridParam(
                    rollType = RollType.TAG,
                    tag = tag
                )
            )
        )
    }
}