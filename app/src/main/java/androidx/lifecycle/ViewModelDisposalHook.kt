package androidx.lifecycle

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import java.io.Closeable

private const val TAG = "disposable"
fun <T> Single<T>.autoDispose(viewModel: ViewModel) {
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
        .subscribe()
}

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