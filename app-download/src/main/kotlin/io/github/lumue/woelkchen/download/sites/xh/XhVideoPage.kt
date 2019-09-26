package io.github.lumue.woelkchen.download.sites.xh

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode


data class XhVideoPage(
        val ldJson : LdJson=LdJson(),
        val initialsJson: InitialsJson= InitialsJson()
){

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class LdJson(
            @JsonProperty("video") val video: Video? = Video()
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        data class Video (
            @JsonProperty("keywords") val keywords: List<String>? = listOf()
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class InitialsJson(
            @JsonProperty("visitFavorite") val visitFavorite: Boolean = false,
            @JsonProperty("videoModel") val videoModel: VideoModel=VideoModel(),
            @JsonProperty("relatedVideosPagination") val relatedVideosPagination: RelatedVideosPagination = RelatedVideosPagination(),
            @JsonProperty("commentsCollection") val commentsCollection: List<CommentsCollection> = listOf(),
            @JsonProperty("xplayerSettings") val xplayerSettings: JsonNode? = null,
            @JsonProperty("xplayerPluginSettings") val xplayerPluginSettings: JsonNode? = null,
            @JsonProperty("isVideoViewDurationNeeded") val isVideoViewDurationNeeded: Boolean = false,
            @JsonProperty("partnersCounter") val partnersCounter: JsonNode? = null,
            @JsonProperty("downloadNoDialog") val downloadNoDialog: Boolean = false,
            @JsonProperty("downloadBanner") val downloadBanner: JsonNode? = null,
            @JsonProperty("plAds") val plAds:  JsonNode? = null,
            @JsonProperty("preroll") val preroll: Boolean = false,
            @JsonProperty("vr") val vr:  JsonNode? = null,
            @JsonProperty("vrStats") val vrStats: Boolean = false,
            @JsonProperty("urls") val urls:  JsonNode? = null,
            @JsonProperty("camsData") val camsData: JsonNode? = null,
            @JsonProperty("camsDataTagName") val camsDataTagName: String? = "",
            @JsonProperty("stripchatWidgetSimilarModelsExperiment") val stripchatWidgetSimilarModelsExperiment:  JsonNode? = null,
            @JsonProperty("downloadVRApp") val downloadVRApp: Boolean = false,
            @JsonProperty("favoritesVideoCollectionsCollection") val favoritesVideoCollectionsCollection:  JsonNode? = null,
            @JsonProperty("favoriteEntity") val favoriteEntity: Any? = Any(),
            @JsonProperty("defaultVideoCollectionId") val defaultVideoCollectionId: String? = "",
            @JsonProperty("options") val options: JsonNode? = null,
            @JsonProperty("mlVideoRelated") val mlVideoRelated: MlVideoRelated? = MlVideoRelated(),
            @JsonProperty("userComment") val userComment: UserComment? = UserComment(),
            @JsonProperty("gaSettings") val gaSettings: GaSettings? = GaSettings(),
            @JsonProperty("staticURL") val staticURL: String = "",
            @JsonProperty("trustURLs") val trustURLs: List<String> = listOf(),
            @JsonProperty("recaptchaKey") val recaptchaKey: String = "",
            @JsonProperty("isComeFromAmp") val isComeFromAmp: Boolean = false,
            @JsonProperty("userGender") val userGender: String? = "",
            @JsonProperty("isDesktopSite") val isDesktopSite: Boolean = false,
            @JsonProperty("bannerPrefix") val bannerPrefix: String = "",
            @JsonProperty("orientation") val orientation: String = "",
            @JsonProperty("isDesktop") val isDesktop: Boolean = false,
            @JsonProperty("promo") val promo: String = "",
            @JsonProperty("frontStats") val frontStats: Boolean = false,
            @JsonProperty("notificationsModel") val notificationsModel: JsonNode? = null,
            @JsonProperty("userId") val userId: Int? = 0,
            @JsonProperty("webpushEnabled") val webpushEnabled:  JsonNode? = null,
            @JsonProperty("stripchatWidgetSimilarModels") val stripchatWidgetSimilarModels:  JsonNode? = null
    ) {


        @JsonIgnoreProperties(ignoreUnknown = true)
        data class NotificationsModel(
                @JsonProperty("gifts") val gifts: Int = 0,
                @JsonProperty("messages") val messages: Int = 0,
                @JsonProperty("friends") val friends: Int = 0,
                @JsonProperty("notifications") val notifications: Int = 0
        )

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class CommentsCollection(
                @JsonProperty("modelName") val modelName: String = "",
                @JsonProperty("id") val id: Int = 0,
                @JsonProperty("userId") val userId: Int = 0,
                @JsonProperty("itemId") val itemId: Int = 0,
                @JsonProperty("text") val text: String = "",
                @JsonProperty("created") val created: Int = 0,
                @JsonProperty("metaItemprop") val metaItemprop: Boolean = false,
                @JsonProperty("author") val author: Author? = Author(),
                @JsonProperty("replyTo") val replyTo: JsonNode? = null

        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Author(
                    @JsonProperty("lastActive") val lastActive: Any? = Any(),
                    @JsonProperty("editable") val editable: Boolean = false,
                    @JsonProperty("vip") val vip: Boolean = false,
                    @JsonProperty("personalInfo") val personalInfo: PersonalInfo? = PersonalInfo(),
                    @JsonProperty("modelName") val modelName: String = "",
                    @JsonProperty("thumbURL") val thumbURL: String? = "",
                    @JsonProperty("isOnline") val isOnline: Boolean = false,
                    @JsonProperty("id") val id: Int = 0,
                    @JsonProperty("pageURL") val pageURL: String? = "",
                    @JsonProperty("retired") val retired: Boolean = false,
                    @JsonProperty("verified") val verified: Boolean = false,
                    @JsonProperty("name") val name: String = ""
            ) {
                @JsonIgnoreProperties(ignoreUnknown = true)
                data class PersonalInfo(
                        @JsonProperty("gender") val gender: Gender? = Gender(),
                        @JsonProperty("orientation") val orientation: Any? = Any(),
                        @JsonProperty("geo") val geo: Geo? = Geo(),
                        @JsonProperty("birthday") val birthday: Long = 0
                ) {

                    data class Gender(
                            @JsonProperty("name") val name: String = "",
                            @JsonProperty("icon") val icon: String = "",
                            @JsonProperty("label") val label: String = ""
                    )


                    data class Geo(
                            @JsonProperty("countryCode") val countryCode: String? = "",
                            @JsonProperty("countryName") val countryName: String? = ""
                    )
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class RelatedVideosPagination(
                @JsonProperty("active") val active: Int = 0,
                @JsonProperty("next") val next: Int = 0,
                @JsonProperty("prev") val prev: Int = 0,
                @JsonProperty("buttonClass") val buttonClass: String = "",
                @JsonProperty("minPage") val minPage: Int = 0,
                @JsonProperty("maxPage") val maxPage: Int = 0,
                @JsonProperty("maxPages") val maxPages: Int = 0,
                @JsonProperty("pageLinkTemplate") val pageLinkTemplate: String = "",
                @JsonProperty("pageLinkFirst") val pageLinkFirst: String = ""
        )


        @JsonIgnoreProperties(ignoreUnknown = true)
        data class MlVideoRelated(
                @JsonProperty("is_ml_related") val isMlRelated: Boolean = false,
                @JsonProperty("is_sponsor") val isSponsor: Boolean = false,
                @JsonProperty("categories") val categories: List<Category> = listOf()
        ) {
            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Category(
                    @JsonProperty("name") val name: String? = "",
                    @JsonProperty("url") val url: String? = "",
                    @JsonProperty("sponsor-tag") val sponsorTag: Boolean = false,
                    @JsonProperty("pornstar") val pornstar: Boolean = false,
                    @JsonProperty("id") val id: String? = "",
                    @JsonProperty("description") val description: Any? = Any()
            )
        }


        data class UserComment(
                @JsonProperty("lastActive") val lastActive: Int = 0,
                @JsonProperty("editable") val editable: Boolean = false,
                @JsonProperty("vip") val vip: Boolean = false,
                @JsonProperty("personalInfo") val personalInfo: PersonalInfo = PersonalInfo(),
                @JsonProperty("modelName") val modelName: String = "",
                @JsonProperty("thumbURL") val thumbURL: String = "",
                @JsonProperty("isOnline") val isOnline: Boolean = false,
                @JsonProperty("id") val id: Int = 0,
                @JsonProperty("pageURL") val pageURL: String = "",
                @JsonProperty("retired") val retired: Boolean = false,
                @JsonProperty("verified") val verified: Boolean = false,
                @JsonProperty("name") val name: String = ""
        ) {

            data class PersonalInfo(
                    @JsonProperty("gender") val gender: Gender = Gender(),
                    @JsonProperty("orientation") val orientation: Orientation = Orientation(),
                    @JsonProperty("geo") val geo: Geo = Geo(),
                    @JsonProperty("birthday") val birthday: Long = 0
            ) {

                data class Gender(
                        @JsonProperty("name") val name: String = "",
                        @JsonProperty("icon") val icon: String = "",
                        @JsonProperty("label") val label: String = ""
                )


                data class Geo(
                        @JsonProperty("countryCode") val countryCode: String = "",
                        @JsonProperty("countryName") val countryName: String = ""
                )


                data class Orientation(
                        @JsonProperty("name") val name: String = "",
                        @JsonProperty("icon") val icon: String = "",
                        @JsonProperty("label") val label: String = ""
                )
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        data class GaSettings(
                @JsonProperty("events") val events: List<Any> = listOf(),
                @JsonProperty("dimensions") val dimensions: Map<String, Any> = mapOf(),
                @JsonProperty("sampling") val sampling: Int = 0,
                @JsonProperty("trackerId") val trackerId: String = "",
                @JsonProperty("amp") val amp: Boolean = false,
                @JsonProperty("fields") val fields: Fields? = Fields()
        ) {

            data class Fields(
                    @JsonProperty("anonymizeIp") val anonymizeIp: Boolean = false
            )


        }


        @JsonIgnoreProperties(ignoreUnknown = true)
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
                @JsonProperty("created") val created: Long = 0,
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
                @JsonProperty("sources") val sources: Sources = Sources(),
                @JsonProperty("dimensions") val dimensions: JsonNode? = null,
                @JsonProperty("categories") val categories: List<Category> = listOf(),
                @JsonProperty("sponsor") val sponsor: JsonNode? = null,
                @JsonProperty("reported") val reported: Boolean = false,
                @JsonProperty("editable") val editable: Boolean = false,
                @JsonProperty("subscriptionModel") val subscriptionModel: SubscriptionModel = SubscriptionModel(),
                @JsonProperty("author") val author: Author = Author()
        ) {


            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Category(
                    @JsonProperty("name") val name: String? = "",
                    @JsonProperty("url") val url: String? = "",
                    @JsonProperty("pornstar") val pornstar: Boolean = false,
                    @JsonProperty("id") val id: String? = "",
                    @JsonProperty("description") val description: Any? = Any()
            )


            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Sources(
                    @JsonProperty("download") val download: Map<String, Download> = mapOf(),
                    @JsonProperty("mp4") val mp4: Map<String, String> = mapOf(),
                    @JsonProperty("flv") val flv: Map<String, String>? = mapOf()
            ) {


                @JsonIgnoreProperties(ignoreUnknown = true)
                data class Download(
                        @JsonProperty("link") val link: String = "",
                        @JsonProperty("size") val size: Double = 0.0
                )

            }


            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Rating(
                    @JsonProperty("modelName") val modelName: String = "",
                    @JsonProperty("value") val value: Int = 0,
                    @JsonProperty("entityModel") val entityModel: String = "",
                    @JsonProperty("entityID") val entityID: Int = 0,
                    @JsonProperty("likes") val likes: Int = 0,
                    @JsonProperty("dislikes") val dislikes: Int = 0,
                    @JsonProperty("state") val state: Int = 0
            )


            @JsonIgnoreProperties(ignoreUnknown = true)
            data class SubscriptionModel(
                    @JsonProperty("modelName") val modelName: String = "",
                    @JsonProperty("id") val id: Int = 0,
                    @JsonProperty("subscribed") val subscribed: Boolean = false,
                    @JsonProperty("subscribers") val subscribers: Int = 0
            )


            @JsonIgnoreProperties(ignoreUnknown = true)
            data class Author(
                    @JsonProperty("modelName") val modelName: String = "",
                    @JsonProperty("id") val id: Int = 0,
                    @JsonProperty("pageURL") val pageURL: String = "",
                    @JsonProperty("retired") val retired: Boolean = false,
                    @JsonProperty("verified") val verified: Boolean = false,
                    @JsonProperty("name") val name: String = ""
            )


        }
    }




}
