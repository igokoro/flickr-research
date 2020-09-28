package dev.igokoro.flickr.ui.landing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.autoDispose
import dev.igokoro.flickr.utils.RxSchedulers
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LiveDataFactory @Inject constructor(
    private val rxSchedulers: RxSchedulers
) {
    fun <T> liveDataFor(
        stream: Observable<T>,
        inside: ViewModel
    ): LiveData<T> {
        return MutableLiveData<T>().apply {
            stream
                .autoDispose(inside)
                .observeOn(rxSchedulers.ui)
                .subscribe { data -> value = data }
        }
    }

    fun <T> mutable() = MutableLiveData<T>()
}