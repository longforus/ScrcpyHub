package model.command.factory

import model.entity.Device
import java.io.File

class ScrcpyCommandFactory(
    override val path: String? = null
) : CommandFactory<Device> {
    @OptIn(kotlin.ExperimentalStdlibApi::class)
    override fun create(data: Device): List<String> {
        return buildList {
            if (path != null) {
                if (path.endsWith(File.separator)) {
                    add("$path$COMMAND_NAME")
                } else {
                    add("$path${File.separator}$COMMAND_NAME")
                }
            } else {
                add(COMMAND_NAME)
            }

            add(DEVICE_OPTION_NAME)
            add(data.id)

            val maxSize = data.maxSize
            if (maxSize != null) {
                add(MAX_SIZE_OPTION_NAME)
                add(maxSize.toString())
            }
        }
    }

    @OptIn(kotlin.ExperimentalStdlibApi::class)
    override fun createHelp(): List<String> {
        return buildList {
            if (path != null) {
                if (path.endsWith(File.separator)) {
                    add("$path$COMMAND_NAME")
                } else {
                    add("$path${File.separator}$COMMAND_NAME")
                }
            } else {
                add(COMMAND_NAME)
            }
            add(HELP_OPTION_NAME)
        }
    }

    companion object {
        private const val COMMAND_NAME = "scrcpy"
        private const val DEVICE_OPTION_NAME = "-s"
        private const val MAX_SIZE_OPTION_NAME = "-m"
        private const val HELP_OPTION_NAME = "-h"
    }
}
