package io.github.lumue.mc.dlservice.sites.xh
import com.fasterxml.jackson.databind.ObjectMapper

class VideoModelParser{

    val objectMapper : ObjectMapper= ObjectMapper()

    fun fromString(s: String):VideoModel{
        return objectMapper.readValue(s,VideoModel::class.java)
    }

}
