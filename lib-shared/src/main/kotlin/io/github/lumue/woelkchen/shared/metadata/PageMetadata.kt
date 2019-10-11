package io.github.lumue.woelkchen.shared.metadata

import com.fasterxml.jackson.annotation.JsonProperty

data class MoviepageMetadata(@JsonProperty("url") val url: String,
                             @JsonProperty("contentMetadata") val contentMetadata: MovieMetadata,
                             @JsonProperty("downloadMetadata") val downloadMetadata : DownloadMetadata) {

    data class DownloadMetadata(@JsonProperty("selectedStreams") val selectedStreams: List<MediaStreamMetadata>,
                                @JsonProperty("additionalStreams") val additionalStreams: List<MediaStreamMetadata> )


    data class MediaStreamMetadata (@JsonProperty("id") val id:String,
                                    @JsonProperty("url") val url:String,
                                    @JsonProperty("headers") val headers: Map<String,String>,
                                    @JsonProperty("contentType") val contentType: ContentType,
                                    @JsonProperty("codec") val codec: String,
                                    @JsonProperty("filenameExtension") val filenameExtension : String,
                                    @JsonProperty("expectedSize") val expectedSize: Long)


    enum class ContentType {AUDIO,VIDEO,CONTAINER}





}