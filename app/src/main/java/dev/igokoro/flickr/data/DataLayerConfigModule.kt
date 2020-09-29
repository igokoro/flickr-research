package dev.igokoro.flickr.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.igokoro.flickr.BuildConfig
import dev.igokoro.flickr.di.FlickrApiKey
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DataLayerConfigModule {

    @FlickrApiKey
    @Singleton
    @Provides
    fun provideFlickrApiKey(): String = BuildConfig.FLICKR_API_KEY
}