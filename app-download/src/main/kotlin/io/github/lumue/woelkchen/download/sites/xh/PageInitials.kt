package io.github.lumue.woelkchen.download.sites.xh

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode


data class PageInitials(
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
        @JsonProperty("gaSettings") val gaSettings: GaSettings = GaSettings(),
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





    data class NotificationsModel(
            @JsonProperty("gifts") val gifts: Int = 0,
            @JsonProperty("messages") val messages: Int = 0,
            @JsonProperty("friends") val friends: Int = 0,
            @JsonProperty("notifications") val notifications: Int = 0
    )


    data class CommentsCollection(
            @JsonProperty("modelName") val modelName: String = "",
            @JsonProperty("id") val id: Int = 0,
            @JsonProperty("userId") val userId: Int = 0,
            @JsonProperty("itemId") val itemId: Int = 0,
            @JsonProperty("text") val text: String = "",
            @JsonProperty("created") val created: Int = 0,
            @JsonProperty("metaItemprop") val metaItemprop: Boolean = false,
            @JsonProperty("author") val author: Author = Author(),
            @JsonProperty("replyTo") val replyTo:  JsonNode? = null

    ) {

        data class Author(
                @JsonProperty("lastActive") val lastActive: Any? = Any(),
                @JsonProperty("editable") val editable: Boolean = false,
                @JsonProperty("vip") val vip: Boolean = false,
                @JsonProperty("personalInfo") val personalInfo: PersonalInfo? = PersonalInfo(),
                @JsonProperty("modelName") val modelName: String = "",
                @JsonProperty("thumbURL") val thumbURL: String? = "",
                @JsonProperty("isOnline") val isOnline: Boolean = false,
                @JsonProperty("id") val id: Int = 0,
                @JsonProperty("pageURL") val pageURL: String = "",
                @JsonProperty("retired") val retired: Boolean = false,
                @JsonProperty("verified") val verified: Boolean = false,
                @JsonProperty("name") val name: String = ""
        ) {

            data class PersonalInfo(
                    @JsonProperty("gender") val gender: Gender? = Gender(),
                    @JsonProperty("orientation") val orientation: Any? = Any(),
                    @JsonProperty("geo") val geo: Geo? = Geo(),
                    @JsonProperty("birthday") val birthday: Long= 0
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




    data class MlVideoRelated(
            @JsonProperty("is_ml_related") val isMlRelated: Boolean = false,
            @JsonProperty("is_sponsor") val isSponsor: Boolean = false,
            @JsonProperty("categories") val categories: List<Category> = listOf()
    ) {

        data class Category(
                @JsonProperty("name") val name: String = "",
                @JsonProperty("url") val url: String = "",
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






    data class GaSettings(
            @JsonProperty("events") val events: List<Any> = listOf(),
            @JsonProperty("dimensions") val dimensions: Map<String,Any> = mapOf(),
            @JsonProperty("sampling") val sampling: Int = 0,
            @JsonProperty("trackerId") val trackerId: String = "",
            @JsonProperty("amp") val amp: Boolean = false,
            @JsonProperty("fields") val fields: Fields = Fields()
    ) {

        data class Fields(
                @JsonProperty("anonymizeIp") val anonymizeIp: Boolean = false
        )



    }
}