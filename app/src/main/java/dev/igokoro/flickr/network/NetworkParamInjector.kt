package dev.igokoro.flickr.network

import dev.igokoro.flickr.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * Takes care of injecting common Flickr API query params:
 * * `format=json` to get response in `JSON` format
 * * `nojsoncallback=1` to get a raw `JSON` instead of `JSONP`
 * * `api_key`
 */
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