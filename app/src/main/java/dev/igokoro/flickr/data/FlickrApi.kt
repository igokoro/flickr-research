package dev.igokoro.flickr.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("rest/?method=flickr.photos.getRecent")
    fun recent(
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int
    ): Single<PhotoPageEnvelopeDto>

    @GET("rest/?method=flickr.tags.getHotList&count=10")
    fun hotTags(): Single<HotTagsDto>

    @GET("rest/?method=flickr.photos.search")
    fun search(
        @Query("tags") tag: String? = null,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
        @Query("max_upload_date") before: Long

    ): Single<PhotoPageEnvelopeDto>
}