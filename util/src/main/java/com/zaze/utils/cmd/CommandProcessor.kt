package com.zaze.utils.cmd

import com.zaze.utils.ByteBuf
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import java.io.DataOutputStream
import java.io.InputStream
import java.lang.Exception

/**
 * Description :
 * @author : zaze
 * @version : 2021-09-29 - 10:34
 */
internal object CommandProcessor {
    private const val TAG = "${ZTag.TAG}cmd"
    private const val COMMAND_EXIT = "exit\n"
    private const val COMMAND_LINE_END = "\n"

    // --------------------------------------------------
    fun execute(command: Command): CommandReturn {
        var returnCode = -1
        val commands = command.cmdList
        if (commands.isEmpty()) {
            return CommandReturn(returnCode)
        }
        // --------------------------------------------------
        var commandReturn: CommandReturn? = null
        var outputStream: DataOutputStream? = null
        try {
            val process = when (command) {
                is Command.RootCommand -> {
                    Runtime.getRuntime().exec("su")
                }
                is Command.NormalCommand -> {
                    Runtime.getRuntime().exec("sh")
                }
            }
            outputStream = DataOutputStream(process.outputStream)
            commands.forEach {
                if (command.needLog) {
                    ZLog.d(TAG, "commands: $it")
                }
                // donnot use os.writeBytes(commmand), avoid chinese charset error
                outputStream.write(it.toByteArray())
                outputStream.writeBytes(COMMAND_LINE_END)
            }
            outputStream.writeBytes(COMMAND_EXIT)
            outputStream.flush()
            // --------------------------------------------------
            returnCode = process.waitFor()
            // --------------------------------------------------
            commandReturn = CommandReturn(
                code = returnCode,
                success = read(process.inputStream),
                error = read(process.errorStream)
            )
            process.destroy()
        } catch (e: Exception) {
            if (command.needLog) {
                ZLog.w(TAG, "execCommand error", e)
            }
        } finally {
            try {
                outputStream?.close()
            } catch (e: Exception) {
                // ignore
            }
        }
        return (commandReturn ?: CommandReturn(returnCode)).apply {
            if (command.needLog) {
                ZLog.d(TAG, "CommandReturn: $this")
            }
        }
    }

    fun read(inputStream: InputStream): String {
        val onceSize = 1048
        val byteBuf = ByteBuf(onceSize)
        var byteLength = 0
        while (byteLength != -1) {
            byteLength = byteBuf.readToBuffer(inputStream, onceSize)
        }
        inputStream.close()
        return String(byteBuf.get())
    }
}