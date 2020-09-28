package dev.igokoro.flickr.utils

import io.reactivex.rxjava3.core.Scheduler

/**
 * Interface to simplify hoping between Rx schedulers. This is especially useful in unit tests where
 * we no longer need to worry about overriding schedules via RxJava API before any test. Instead,
 * we can just mock whatever scheduler to be [io.reactivex.rxjava3.schedulers.Schedulers.trampoline]
 * to get a blocking execution.
 */
interface RxSchedulers {
    val ui: Scheduler
}

internal class RxSchedulersImpl(override val ui: Scheduler) : RxSchedulers

