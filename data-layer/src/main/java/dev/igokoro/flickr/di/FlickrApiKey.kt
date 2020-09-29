package dev.igokoro.flickr.di

import javax.inject.Qualifier

/**
 * This is a "public api" for dagger to allow injecting api key from the app level to this module.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FlickrApiKey