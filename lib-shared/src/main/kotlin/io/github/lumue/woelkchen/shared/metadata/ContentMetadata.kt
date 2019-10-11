package io.github.lumue.woelkchen.shared.metadata

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Duration
import java.time.LocalDateTime


data class Tag(@JsonProperty("id") val id: String,
               @JsonProperty("name") val name: String
)

abstract class ContentMetadata {
    abstract val title: String
    abstract val description: String
    abstract val votes: Int?
    abstract val hoster: String?
    abstract val uploaded: LocalDateTime?
    abstract val downloaded: LocalDateTime?
    abstract val tags: Set<Tag>
}

abstract class VideoMetadata : ContentMetadata() {
    abstract val views: Int
    abstract val duration: Duration
    abstract val resolution: Short?
}

data class MovieMetadata(@JsonProperty("title") override val title: String,
                         @JsonProperty("description") override val description: String = "",
                         @JsonProperty("tags")  override val tags: Set<Tag> = setOf(),
                         @JsonProperty("actors")  val  actors: Set<Actor> = setOf(),
                         @JsonProperty("duration")  override val  duration: Duration = Duration.ZERO,
                         @JsonProperty("views")   override val  views: Int = 0,
                         @JsonProperty("downloaded")  override val  downloaded: LocalDateTime? = LocalDateTime.now(),
                         @JsonProperty("uploaded")   override val uploaded: LocalDateTime? = LocalDateTime.MIN,
                         @JsonProperty("hoster")   override val hoster: String? = "unknown",
                         @JsonProperty("resolution") override val resolution: Short?,
                         @JsonProperty("votes")   override val votes: Int? = 0

) : VideoMetadata() {


    data class Actor(@JsonProperty("id") val id: String, @JsonProperty("name") val name: String)
}