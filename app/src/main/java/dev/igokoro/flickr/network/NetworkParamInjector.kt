package dev.igokoro.flickr.network

import dev.igokoro.flickr.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

private const val PARAM_FLICKR_API_KEY = "api_key"
private const val PARAM_FORMAT = "format"
private const val VALUE_FORMAT_JSON = "json"
private const val PARAM_DO_NOT_USE_JSONP = "nojsoncallback"
private const val VALUE_NO_JSONP = "1"

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
                PARAM_FORMAT,
                VALUE_FORMAT_JSON
            )
            .addQueryParameter(
                PARAM_DO_NOT_USE_JSONP,
                VALUE_NO_JSONP
            )
            .addQueryParameter(
                PARAM_FLICKR_API_KEY,
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