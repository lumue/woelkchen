package io.github.lumue.woelkchen.download

class DownloadExecution {

    private val currentProgress: ExecutionProgress = ExecutionProgress()



}

enum class ExecutionState {
    WAITING, PREPARING, RUNNING, ERROR, FINISHED, CANCELLED, PREPARED
}

class ExecutionProgress {

}
