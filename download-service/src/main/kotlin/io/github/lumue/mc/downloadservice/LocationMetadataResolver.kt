package io.github.lumue.mc.downloadservice

interface LocationMetadataResolver {

     suspend fun resolveMetadata(l:MediaLocation) : LocationMetadata

}



data class LocationMetadata(val contentMetadata: ContentMetadata, val downloadMetadata : DownloadMetadata) {

    data class DownloadMetadata(val selectedStreams: List<MediaStreamMetadata>,
                                val additionalStreams: List<MediaStreamMetadata> )

    data class MediaStreamMetadata (val url:String,
                               val headers: Map<String,String>,
                               val contentType: ContentType,
                               val codec: String,
                               val filenameExtension : String,
                               val expectedSize: Long)


    enum class ContentType {AUDIO,VIDEO,CONTAINER}

    data class ContentMetadata(val title: String)

}
