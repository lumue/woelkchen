package io.github.lumue.mc.download.resolve

interface LocationMetadataResolver {

     suspend fun resolveMetadata(l: MediaLocation) : LocationMetadata

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

