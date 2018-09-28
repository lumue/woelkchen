data class AdditionalStream(
        val id: String,
        val url: String,
        val headers: List<Any>,
        val contentType: String,
        val codec: String,
        val filenameExtension: String,
        val expectedSize: Int
)