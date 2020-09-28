package dev.igokoro.flickr.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
internal abstract class RepoModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun FlickrRepoImpl.bindRepo(): FlickrRepo

}