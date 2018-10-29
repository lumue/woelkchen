package io.github.lumue.woelkchen.download

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.io.*
import java.nio.charset.Charset

const val locationMetadataFileSuffix = ".meta.json"

class LocationMetadataWriter(val mapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())) {

    fun write(metadata: LocationMetadata, file:File) {
        val outStream=FileOutputStream(file)
        mapper.writerWithDefaultPrettyPrinter().forType(LocationMetadata::class.java).writeValue(outStream, metadata)
        outStream.close()
    }

    fun write(metadata: LocationMetadata, out: OutputStream) {
        mapper.writerWithDefaultPrettyPrinter().forType(LocationMetadata::class.java).writeValue(out, metadata)
    }

    fun writeString(metadata: LocationMetadata): String {
        val out = ByteArrayOutputStream()
        write(metadata, out)
        return String(out.toByteArray(), Charset.defaultCharset())
    }
}

class LocationMetadataReader(val mapper: ObjectMapper = ObjectMapper()) {

    fun read(instream: InputStream): LocationMetadata {
        try {
            return mapper.readValue<LocationMetadata>(instream, LocationMetadata::class.java)
        }
        catch(t:Throwable){
            throw RuntimeException(t)
        }
    }
}



fun LocationMetadata.write(out: OutputStream) {
    LocationMetadataWriter().write(this, out)
}



fun LocationMetadata.jsonString(): String {
    return LocationMetadataWriter().writeString(this)
}

val File.isMetadataJson: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith(locationMetadataFileSuffix)
    }

