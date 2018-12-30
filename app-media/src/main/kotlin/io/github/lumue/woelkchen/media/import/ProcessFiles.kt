package io.github.lumue.woelkchen.media.import


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ProcessFiles(
        private val fileFilter: (file: File) -> Boolean = { true },
        private val handleFile: suspend (file: File) -> Any = {},
        private val context: CoroutineContext = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
) {

    private val logger: Logger = LoggerFactory.getLogger(ProcessFiles::class.java)

    @ExperimentalCoroutinesApi
    operator fun invoke(path: String) {

        val rootPath = File(path)
        if (!rootPath.exists() || !rootPath.isDirectory)
            return

        runBlocking {
            val producer = this.produce(context) {
                rootPath.walkBottomUp()
                        .filter { fileFilter(it) }
                        .forEach {
                            logger.debug("selected $it for processing ")
                            send(it)
                        }
                close()
            }

            val consumer = List(20) {
                this.async(context) {
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

