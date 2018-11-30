package io.github.lumue.woelkchen.download

data class DownloadExecution(val url: MediaLocation) {
    var expectedSize: Long=0

    var progress: Long=0

    var currentSpeed: Long=0

    fun updateProgress(progression: Long, total: Long, timeTook: Long) {
        if(expectedSize!=total)
            expectedSize=total
        progress=+progression
        if(timeTook>0)
            currentSpeed=(progression/timeTook)*1000
    }

}

enum class ExecutionState {
    WAITING, PREPARING, RUNNING, ERROR, FINISHED, CANCELLED, PREPARED
}

class ExecutionProgress {

}
