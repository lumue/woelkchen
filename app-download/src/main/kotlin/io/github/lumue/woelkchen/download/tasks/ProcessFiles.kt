package io.github.lumue.woelkchen.download.tasks


import kotlinx.coroutines.async
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.coroutines.CoroutineContext

class ProcessFiles(
        val fileFilter: (file: File) -> Boolean = { true },
        val context: CoroutineContext = newFixedThreadPoolContext(30, "process-file-worker"),
    val handleFile: suspend (file: File) -> Any = {}){

    private val logger: Logger = LoggerFactory.getLogger(ProcessFiles::class.java)

    operator fun invoke(path: String) {

        val rootPath = File(path)
        if (!rootPath.exists() || !rootPath.isDirectory)
            return

        runBlocking {
            val producer = produce {
                rootPath.walkBottomUp()
                        .filter { fileFilter(it) }
                        .forEach {
                            logger.debug("selected $it for processing ")
                            send(it)
                        }
                close()
            }

            val consumer = List(20) {
                async(context) {
                    for (file in producer) {
                        try {
                            logger.debug("processing $file")
                            handleFile(file)
                            logger.debug("$file processed")
                        } catch (t: Throwable) {
                            logger.error("error processing $file: ${t.message}", t)
                        }
                    }
                }
            }
            consumer.forEach { it.join() }
        }
    }
}

