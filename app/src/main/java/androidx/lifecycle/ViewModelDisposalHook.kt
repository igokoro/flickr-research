package androidx.lifecycle

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import java.io.Closeable

private const val TAG = "disposable"

/**
 * Associate [Observable] with [ViewModel] lifecycle. This allows to dispose the stream, when
 * [ViewModel.clear] is called. Internally, this taps into [ViewModel.setTagIfAbsent] API that
 * coroutines use to expose `viewModelScope`. Just to level the playing field...
 */
fun <T> Observable<T>.autoDispose(viewModel: ViewModel): Observable<T> =
    doOnSubscribe { disposable ->
        var disposables: CompositeCloseable? = viewModel.getTag(TAG)
        if (disposables == null) {
            disposables = CompositeCloseable()
            viewModel.setTagIfAbsent(
                TAG,
                disposables
            )
        }
        disposables += disposable
    }

private class CompositeCloseable : Closeable {
    private val disposables: MutableList<Disposable> = mutableListOf()

    operator fun plusAssign(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun close() {
        disposables.forEach { it.dispose() }
    }

}