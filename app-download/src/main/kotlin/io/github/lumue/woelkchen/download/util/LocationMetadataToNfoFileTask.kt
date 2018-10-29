package io.github.lumue.woelkchen.download.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.lumue.nfotools.Movie
import io.github.lumue.woelkchen.download.LocationMetadataReader
import io.github.lumue.woelkchen.download.isMetadataJson
import io.github.lumue.woelkchen.download.locationMetadataFileSuffix
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.xml.bind.JAXBContext
import kotlin.coroutines.experimental.CoroutineContext


private val objectMapper: ObjectMapper = ObjectMapper().registerModule(JavaTimeModule())


private val locationMetadataReader: LocationMetadataReader = LocationMetadataReader(objectMapper)


private val threadpoolContext: CoroutineContext = newFixedThreadPoolContext(30, "load-meta-json-worker")


private val nfoSerializer=io.github.lumue.nfotools.NfoMovieSerializer(
        JAXBContext.newInstance(Movie::class.java, Movie.Actor::class.java)
)

fun main(args: Array<String>) {


    val fileProcessor = ProcessFiles(
            context = threadpoolContext,
            fileFilter = { it.isMetadataJson },
            handleFile = { convertLocationMetadataFileToNfoFile(it) }
    )

    fileProcessor(path = "/mnt/nasbox/media/adult/incoming")


}

fun convertLocationMetadataFileToNfoFile(metadatafile: File) {
    val instream = FileInputStream(metadatafile)
    val locationMetadata = locationMetadataReader.read(instream)
    instream.close()
    val movieBuilder = Movie.MovieBuilder()
    locationMetadata.configureMovieBuilderWithLocationMetadata(movieBuilder)
    val out=FileOutputStream(metadatafile.absolutePath.replace(locationMetadataFileSuffix,".nfo"))
    nfoSerializer.serialize(movieBuilder.build(),out)
    out.close()
}



