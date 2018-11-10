package io.github.lumue.woelkchen.download

import org.springframework.context.annotation.AdviceMode
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.task.TaskExecutor
import org.springframework.kotlin.experimental.coroutine.EnableCoroutine
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.experimental.CoroutineContext

@Configuration
@EnableCoroutine(
        proxyTargetClass = false, mode = AdviceMode.PROXY,
        order = Ordered.LOWEST_PRECEDENCE, schedulerDispatcher = "")
class DownloadserviceAppConfig {

    @Bean("downloadDispatcher")
    fun download_dispatcher() : TaskExecutor{
        val executor = executor("download",10)
        return executor
    }
    private fun executor(name: String, threads: Int?): ThreadPoolTaskExecutor {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.setThreadNamePrefix(name)
        threadPoolTaskExecutor.setThreadGroupName(name)
        threadPoolTaskExecutor.corePoolSize = threads!!
        threadPoolTaskExecutor.maxPoolSize = threads
        threadPoolTaskExecutor.initialize()
        return threadPoolTaskExecutor
    }
}