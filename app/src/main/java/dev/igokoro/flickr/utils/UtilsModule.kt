package dev.igokoro.flickr.utils

import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers

@InstallIn(ApplicationComponent::class)
@Module
class UtilsModule {
    @Provides
    fun provideRxSchedulers(): RxSchedulers {
        return RxSchedulersImpl(AndroidSchedulers.from(Looper.getMainLooper()))
    }
}