package dev.igokoro.flickr.data

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Definitions for Flickr API calls in `Retrofit` terms.
 */
interface FlickrApi {

    /**
     * Get recent photos. Outside of ugly JSON structure, this endpoint generates a lot of duplicate
     * photos when using paging API: with may uploads every second, entries from page #1 migrate to
     * page #2 and so on very fast. So, by the time page #2 s requested - it's full of the same
     * images that we just received in page #1.
     */
    @GET("rest/?method=flickr.photos.getRecent")
    fun recent(
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int
    ): Single<PhotoPageEnvelopeDto>

    /**
     * Get hot tags.
     */
    @GET("rest/?method=flickr.tags.getHotList&count=10")
    fun hotTags(): Single<HotTagsDto>

    /**
     * Generic photo search API, used to search for images with [tag] in this case. [before] is very
     * useful as it allows to cut off the search results newer than the date provided. This allows
     * to minimize duplicate photos as page #1 is (mostly) well defined.
     */
    @GET("rest/?method=flickr.photos.search")
    fun search(
        @Query("tags") tag: String? = null,
        @Query("per_page") pageSize: Int,
        @Query("page") page: Int,
        @Query("max_upload_date") before: Long

    ): Single<PhotoPageEnvelopeDto>
}