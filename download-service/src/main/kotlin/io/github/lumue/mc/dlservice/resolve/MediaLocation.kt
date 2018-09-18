package io.github.lumue.mc.dlservice.resolve

import java.time.LocalDateTime

data class MediaLocation(val url: String,
                         val added: LocalDateTime) {

}

data class LocationMetadata(val url: String,
                            val contentMetadata: ContentMetadata,
                            val downloadMetadata : DownloadMetadata) {

    data class DownloadMetadata(val selectedStreams: List<MediaStreamMetadata>,
                                val additionalStreams: List<MediaStreamMetadata> )


    data class MediaStreamMetadata (val id:String,
                                    val url:String,
                                    val headers: Map<String,String>,
                                    val contentType: ContentType,
                                    val codec: String,
                                    val filenameExtension : String,
                                    val expectedSize: Long)


    enum class ContentType {AUDIO,VIDEO,CONTAINER}

    data class ContentMetadata(val title: String, val desription:String)

}