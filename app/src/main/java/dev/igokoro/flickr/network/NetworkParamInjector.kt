package dev.igokoro.flickr.network

import dev.igokoro.flickr.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkParamInjector @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter(
                "format",
                "json"
            )
            .addQueryParameter(
                "nojsoncallback",
                "1"
            )
            .addQueryParameter(
                "api_key",
                BuildConfig.FLICKR_API_KEY
            )
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}