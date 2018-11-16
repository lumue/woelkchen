package io.github.lumue.woelkchen.media.data

import java.net.URL
import java.time.LocalDateTime
import java.util.*

import org.neo4j.ogm.annotation.*


interface MediaContent{
    val id: String
    val name: String
    val locations: List<ContentLocation>
    val tags: List<Tag>
}

@NodeEntity
data class Tag(@Id  val id:String=UUID.randomUUID().toString(),
               val name:String,
               val names: Set<String> = mutableSetOf())

@NodeEntity
data class ContentLocation(
        @Id @GeneratedValue val id:String=UUID.randomUUID().toString(),
        @Index(unique = true) val url: URL,
        val added: LocalDateTime= LocalDateTime.now(),
        var lastSeen: LocalDateTime=added)

@NodeEntity
data class Movie(
        val originaltitle: String?,
        val sorttitle: String?,
        val set: String?,
        val rating: String?,
        val year: String?,
        val top250: String?,
        val votes: String?,
        val outline: String?,
        val plot: String?,
        val tagline: String?,
        val runtime: String?,
        val thumb: String?,
        val mpaa: String?,
        val trailer: String?,
        val genre: String?,
        val credits: String?,
        val director: Person?,
        @Relationship(type = "ACTED_IN", direction = Relationship.INCOMING)
        val actors: List<Role> = listOf(),
        val title: String,
        val videoFormat: VideoFormat?,
        val audioFormat: AudioFormat?,
        @Id override val id: String,
        override val name: String,
        override val locations: List<ContentLocation>,
        override val tags: List<Tag>
) : MediaContent

@RelationshipEntity(type = "ACTED_IN")
data class Role(
        val name:String="",
        @StartNode val person: Person,
        @EndNode val movie: Movie)

data class AudioFormat(
        val channels: Int,
        val codec: String,
        val language: String
)

data class VideoFormat(
        val aspect: Double,
        val codec: String,
        val duration: Int,
        val height: Int,
        val language: String,
        val stereomode: String,
        val width: Int
)


