package model.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ProcessRepository {
    private val processList: MutableMap<String, Process> = mutableMapOf()
    private val scope: CoroutineScope = MainScope()

    fun any(key: String): Boolean {
        return processList[key] != null
    }

    fun insert(key: String, process: Process, onDestroy: (suspend () -> Unit)? = null) {
        processList[key] = process
        scope.launch(Dispatchers.IO) {
            process.waitForRunning(MONITORING_DELAY)
            process.monitor(MONITORING_INTERVAL) { onDestroy?.invoke() }
        }
    }

    fun delete(key: String) {
        processList[key]?.destroy()
        processList.remove(key)
    }

    private suspend fun Process.waitForRunning(interval: Long) {
        withTimeout(TIMEOUT) {
            while (!this@waitForRunning.isAlive) {
                delay(interval)
            }
        }
    }

    private suspend fun Process.monitor(interval: Long, onDestroy: suspend () -> Unit) {
        while (this.isAlive) {
            delay(interval)
        }
        onDestroy.invoke()
    }

    companion object {
        private const val TIMEOUT = 10000L
        private const val MONITORING_DELAY = 1000L
        private const val MONITORING_INTERVAL = 1000L
    }
}
