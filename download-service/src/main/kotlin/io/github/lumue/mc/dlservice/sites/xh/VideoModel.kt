package io.github.lumue.mc.dlservice.sites.xh

import com.fasterxml.jackson.annotation.JsonProperty


data class VideoModel(
        @JsonProperty("duration") val duration: Int = 0,
        @JsonProperty("title") val title: String = "",
        @JsonProperty("pageURL") val pageURL: String = "",
        @JsonProperty("icon") val icon: Any? = Any(),
        @JsonProperty("spriteURL") val spriteURL: String = "",
        @JsonProperty("trailerURL") val trailerURL: String = "",
        @JsonProperty("rating") val rating: Rating = Rating(),
        @JsonProperty("isVR") val isVR: Boolean = false,
        @JsonProperty("isHD") val isHD: Boolean = false,
        @JsonProperty("isUHD") val isUHD: Boolean = false,
        @JsonProperty("created") val created: Int = 0,
        @JsonProperty("modelName") val modelName: String = "",
        @JsonProperty("thumbURL") val thumbURL: String = "",
        @JsonProperty("id") val id: Int = 0,
        @JsonProperty("views") val views: Int = 0,
        @JsonProperty("comments") val comments: Int = 0,
        @JsonProperty("modified") val modified: Int = 0,
        @JsonProperty("orientation") val orientation: Int = 0,
        @JsonProperty("secured") val secured: Int = 0,
        @JsonProperty("status") val status: Int = 0,
        @JsonProperty("description") val description: String = "",
        @JsonProperty("mp4File") val mp4File: String = "",
        @JsonProperty("spriteCount") val spriteCount: Int = 0,
        @JsonProperty("playerThumbURL") val playerThumbURL: String = "",
        @JsonProperty("sources") val sources: Sources = Sources(),
        @JsonProperty("dimensions") val dimensions: Dimensions = Dimensions(),
        @JsonProperty("categories") val categories: List<Category> = listOf(),
        @JsonProperty("sponsor") val sponsor: Sponsor = Sponsor(),
        @JsonProperty("reported") val reported: Boolean = false,
        @JsonProperty("editable") val editable: Boolean = false,
        @JsonProperty("subscriptionModel") val subscriptionModel: SubscriptionModel = SubscriptionModel(),
        @JsonProperty("author") val author: Author = Author()
) {

    data class Dimensions(
            @JsonProperty("ext") val ext: String = "",
            @JsonProperty("v") val v: Int = 0,
            @JsonProperty("cnt320p") val cnt320p: Int = 0,
            @JsonProperty("valid320p") val valid320p: Int = 0,
            @JsonProperty("mediaInfo") val mediaInfo: MediaInfo = MediaInfo(),
            @JsonProperty("watermark") val watermark: Boolean = false,
            @JsonProperty("trailer") val trailer: Map<String,Long> = mapOf(),
            @JsonProperty("cdn") val cdn: Int = 0,
            @JsonProperty("video") val video: Map<String,Map<String,String>> = mapOf(),
            @JsonProperty("ml-thumb") val mlThumb: Int = 0,
            @JsonProperty("ceph-cleaned") val cephCleaned: Int = 0
    ) {

        data class MediaInfo(
                @JsonProperty("keyframe") val keyframe: Int = 0
        )






    }


    data class Category(
            @JsonProperty("name") val name: String = "",
            @JsonProperty("url") val url: String = "",
            @JsonProperty("sponsor-tag") val sponsorTag: Boolean = false,
            @JsonProperty("pornstar") val pornstar: Boolean = false,
            @JsonProperty("id") val id: String? = "",
            @JsonProperty("description") val description: Any? = Any()
    )


    data class Sources(
            @JsonProperty("download") val download: Map<String,Download> = mapOf(),
            @JsonProperty("mp4") val mp4: Map<String,String> = mapOf()
    ) {


            data class Download(
                    @JsonProperty("link") val link: String = "",
                    @JsonProperty("size") val size: Double = 0.0
            )

    }


    data class Rating(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("value") val value: Int = 0,
            @JsonProperty("entityModel") val entityModel: String = "",
            @JsonProperty("entityID") val entityID: Int = 0,
            @JsonProperty("likes") val likes: Int = 0,
            @JsonProperty("dislikes") val dislikes: Int = 0,
            @JsonProperty("state") val state: Int = 0
    )


    data class Sponsor(
            @JsonProperty("id") val id: Int = 0,
            @JsonProperty("site") val site: String = "",
            @JsonProperty("banner") val banner: Banner = Banner(),
            @JsonProperty("name") val name: String = "",
            @JsonProperty("message") val message: String = "",
            @JsonProperty("link") val link: String = "",
            @JsonProperty("landing") val landing: Int = 0,
            @JsonProperty("description") val description: String = ""
    ) {

        data class Banner(
                @JsonProperty("width") val width: Int = 0,
                @JsonProperty("height") val height: Int = 0,
                @JsonProperty("src") val src: String = ""
        )
    }


    data class SubscriptionModel(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("id") val id: Int = 0,
            @JsonProperty("subscribed") val subscribed: Boolean = false,
            @JsonProperty("subscribers") val subscribers: Int = 0
    )


    data class Author(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("id") val id: Int = 0,
            @JsonProperty("pageURL") val pageURL: String = "",
            @JsonProperty("retired") val retired: Boolean = false,
            @JsonProperty("verified") val verified: Boolean = false,
            @JsonProperty("name") val name: String = ""
    )
}