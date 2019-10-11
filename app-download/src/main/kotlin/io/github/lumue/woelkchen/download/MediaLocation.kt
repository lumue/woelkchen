package io.github.lumue.woelkchen.download

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.lumue.woelkchen.shared.metadata.ContentMetadata
import io.github.lumue.woelkchen.shared.metadata.MovieMetadata
import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.time.Duration
import java.time.LocalDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class MediaLocation( val url: String,
                          val added: LocalDateTime? = LocalDateTime.now()){
    @JsonCreator
    constructor(@JsonProperty("url") url: String) :
            this(url,LocalDateTime.now())

}




