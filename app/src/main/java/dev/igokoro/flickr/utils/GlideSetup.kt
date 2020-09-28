package dev.igokoro.flickr.utils

import android.content.Context
import androidx.fragment.app.Fragment
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped

// 200 MB
private const val diskCacheSizeBytes = 1024 * 1024 * 200L

@InstallIn(FragmentComponent::class)
@Module
class GlideDaggerModule {

    @FragmentScoped
    @Provides
    fun Fragment.provideGlide(): GlideRequests = GlideApp.with(this)
}

@GlideModule
class AppGlideModule : AppGlideModule() {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {

        builder.setDiskCache(
            InternalCacheDiskCacheFactory(
                context,
                "images",
                diskCacheSizeBytes
            )
        )
    }
}