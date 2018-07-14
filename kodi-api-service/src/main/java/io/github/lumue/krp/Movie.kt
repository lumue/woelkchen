package io.github.lumue.krp

import com.fasterxml.jackson.annotation.JsonCreator

import java.net.URLEncoder

data class Movie

@JsonCreator
constructor(val movieid: String,
            val title: String,
            val thumbnail: String,
            val tagline: String,
            val dateadded: String,
            val userrating: Long?,
            val tag: Set<String>,
            val genre: Set<String>,
            val lastplayed: String,
            val runtime: Int?,
            val streamdetails: StreamDetails) {

    val thumbnailHttpUrl: String
        get() = "http://osmc-wozi:9000/image/" + URLEncoder.encode(thumbnail)

    val duration: Int
        get() = if(runtime!=null && runtime!=0)
                    runtime
                else
                    streamdetails.video.first().duration
}



data class StreamDetails
@JsonCreator
constructor(
    val audio: List<Audio>,
    val subtitle: List<Any>,
    val video: List<Video>
)

data class Audio
@JsonCreator
constructor(
    val channels: Int,
    val codec: String,
    val language: String
)

data class Video
@JsonCreator
constructor(
    val aspect: Double,
    val codec: String,
    val duration: Int,
    val height: Int,
    val language: String,
    val stereomode: String,
    val width: Int
)