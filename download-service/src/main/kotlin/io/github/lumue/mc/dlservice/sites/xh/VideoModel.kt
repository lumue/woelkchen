package io.github.lumue.mc.dlservice.sites.xh

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode


data class VideoModel(
        @JsonProperty("duration") val duration: Long = 0,
        @JsonProperty("title") val title: String? = "",
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
        @JsonProperty("orientation") val orientation: String = "",
        @JsonProperty("secured") val secured: Int = 0,
        @JsonProperty("status") val status: Int = 0,
        @JsonProperty("description") val description: String = "",
        @JsonProperty("mp4File") val mp4File: String = "",
        @JsonProperty("spriteCount") val spriteCount: Int = 0,
        @JsonProperty("playerThumbURL") val playerThumbURL: String = "",
        @JsonProperty("sources") val sources: Sources = Sources(),
        @JsonProperty("dimensions") val dimensions: JsonNode?=null,
        @JsonProperty("categories") val categories: List<Category> = listOf(),
        @JsonProperty("sponsor") val sponsor: JsonNode?=null,
        @JsonProperty("reported") val reported: Boolean = false,
        @JsonProperty("editable") val editable: Boolean = false,
        @JsonProperty("subscriptionModel") val subscriptionModel: SubscriptionModel = SubscriptionModel(),
        @JsonProperty("author") val author: Author = Author()
) {




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
            @JsonProperty("mp4") val mp4: Map<String,String> = mapOf(),
            @JsonProperty("flv") val flv: Map<String,String>? = mapOf()
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

