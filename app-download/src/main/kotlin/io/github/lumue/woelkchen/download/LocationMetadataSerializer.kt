package io.github.lumue.woelkchen.download

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.nio.charset.Charset

const val locationMetadataFileSuffix = ".meta.json"

class LocationMetadataWriter(private val mapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())) {

    fun write(metadata: MoviepageMetadata, file:File) {
        val outStream=FileOutputStream(file)
        mapper.writerWithDefaultPrettyPrinter().forType(MoviepageMetadata::class.java).writeValue(outStream, metadata)
        outStream.close()
    }

    fun write(metadata: MoviepageMetadata, out: OutputStream) {
        mapper.writerWithDefaultPrettyPrinter().forType(MoviepageMetadata::class.java).writeValue(out, metadata)
    }

    fun writeString(metadata: MoviepageMetadata): String {
        val out = ByteArrayOutputStream()
        write(metadata, out)
        return String(out.toByteArray(), Charset.defaultCharset())
    }
}

class LocationMetadataReader(val mapper: ObjectMapper = ObjectMapper()) {

    fun read(instream: InputStream): MoviepageMetadata {
        try {
            return mapper.readValue<MoviepageMetadata>(instream, MoviepageMetadata::class.java)
        }
        catch(t:Throwable){
            throw RuntimeException(t)
        }
    }
}



fun MoviepageMetadata.write(out: OutputStream) {
    LocationMetadataWriter().write(this, out)
}

suspend fun MoviepageMetadata.writeToFile(filename:String) {
    val out= withContext (Dispatchers.IO){
        FileOutputStream(filename)
    }
    LocationMetadataWriter().write(this, out)
    withContext(Dispatchers.IO) {
        out.close()
    }
}

fun MoviepageMetadata.jsonString(): String {
    return LocationMetadataWriter().writeString(this)
}

val File.isMetadataJson: Boolean
    get() {
        return this.exists()
                && !this.isDirectory
                && this.name.endsWith(locationMetadataFileSuffix)
    }

