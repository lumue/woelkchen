package io.github.lumue.mc.dlservice

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.charset.Charset

class LocationMetadataWriter(val mapper:ObjectMapper=ObjectMapper()) {

    fun write(metadata: LocationMetadata,out: OutputStream){
        mapper.writerWithDefaultPrettyPrinter().forType(LocationMetadata::class.java).writeValue(out,metadata)
    }

    fun writeString(metadata: LocationMetadata):String{
        val out = ByteArrayOutputStream()
        write(metadata,out)
        return String(out.toByteArray(), Charset.defaultCharset())
    }
}

fun LocationMetadata.write(out: OutputStream){
    LocationMetadataWriter().write(this,out)
}


fun LocationMetadata.jsonString():String{
   return  LocationMetadataWriter().writeString(this)
}