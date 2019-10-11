package io.github.lumue.woelkchen.media.import


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class  ProcessFiletree(
        private val path : String,
        private val fileProcessor: suspend (file: File) -> Any ,
        private val fileFilter: (file: File) -> Boolean = { true },
        private val processorCount: Int = 1,
        private val processorContext: CoroutineContext = newFixedThreadPoolContext((processorCount/4)+1, "process-filetree-consumer"),
        private val producerContext:  CoroutineContext = Dispatchers.IO
) {

    private val logger: Logger = LoggerFactory.getLogger(ProcessFiletree::class.java)

    fun invoke(){
        execute()
    }

    fun execute() {
        val rootPath = File(path)
        if (!rootPath.exists() || !rootPath.isDirectory)
            return

        runBlocking {
            val fileListProducer = launchFileListProducer(rootPath)
            val asyncFileProcessors = launchProcessorDispatchers(fileListProducer, 10)
            asyncFileProcessors.forEach { it.join() }
        }
    }

    private fun CoroutineScope.launchProcessorDispatchers(fileListProducer: ReceiveChannel<File>, threadCount: Int): List<Deferred<Unit>> {

        return List(processorCount) {
            launchFileProcessorAsync(fileListProducer, fileProcessor,processorContext )
        }
    }

    private fun CoroutineScope.launchFileProcessorAsync(
            producer: ReceiveChannel<File>,
            handleFile: suspend (file: File) -> Any,
            processorDispatcher: CoroutineContext): Deferred<Unit> {



        return this.async(processorDispatcher) {
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

    private fun launchThreadPoolDispatcher(threadCount: Int ): ExecutorCoroutineDispatcher {
        return Executors
                .newFixedThreadPool(threadCount)
                .asCoroutineDispatcher()
    }


    private fun CoroutineScope.launchFileListProducer(rootPath: File): ReceiveChannel<File> {
        return this.produce(producerContext) {
            rootPath.walkBottomUp()
                    .filter { fileFilter(it) }
                    .forEach {
                        logger.debug("selected $it for processing ")
                        send(it)
                    }
            close()
        }
    }
}

