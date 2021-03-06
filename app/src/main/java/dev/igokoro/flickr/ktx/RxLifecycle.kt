package dev.igokoro.flickr.ktx

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.rxjava3.core.Observable

/**
 * Extension that allows automatically disposing [Observable] when [lifecycle] enters
 * [Lifecycle.State.DESTROYED].
 * This is useful when [Observable] must be exposed to `View` directly.
 */
fun <T> Observable<T>.autoDispose(lifecycle: Lifecycle): Observable<T> {
    return doOnSubscribe {
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                it.dispose()
                lifecycle.removeObserver(this)
            }
        })
    }
}