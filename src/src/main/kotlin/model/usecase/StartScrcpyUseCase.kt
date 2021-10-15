package model.usecase

import model.command.ScrcpyCommand
import model.entity.Device
import model.entity.Resolution
import model.repository.ProcessRepository

class StartScrcpyUseCase(
    private val scrcpyCommand: ScrcpyCommand,
    private val processRepository: ProcessRepository
) {
    fun execute(device: Device, resolution: Resolution?, onDestroy: suspend () -> Unit): Boolean {
        val exists = processRepository.any(device.id)
        if (exists) {
            return false
        }

        return try {
            val process = scrcpyCommand.run(device, resolution)
            processRepository.insert(device.id, process) {
                processRepository.delete(device.id)
                onDestroy.invoke()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}