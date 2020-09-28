package dev.igokoro.flickr.utils

import io.reactivex.rxjava3.core.Scheduler

interface RxSchedulers {
    val ui: Scheduler
}

internal class RxSchedulersImpl(override val ui: Scheduler) : RxSchedulers

