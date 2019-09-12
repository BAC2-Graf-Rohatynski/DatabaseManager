package databasemanager.command

import apibuilder.database.header.HeaderBuilder
import apibuilder.database.response.ResponseItem
import databasemanager.DatabaseManagerRunner
import databasemanager.clients.Clients
import databasemanager.command.interfaces.ICommandSocket
import databasemanager.handler.CommandHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.lang.Exception

class CommandSocket(private val clientSocket: Socket): Thread(), ICommandSocket {
    private lateinit var printWriter: PrintWriter
    private lateinit var bufferedReader: BufferedReader
    private val logger: Logger = LoggerFactory.getLogger(CommandSocket::class.java)
    private val socketId = (10000..19999).shuffled().first()

    override fun run() {
        try {
            openSockets()
            receive()
        } catch (ex: Exception) {
            logger.error("Error occurred while running command socket!\n${ex.message}")
        } finally {
            closeSockets()
        }
    }

    @Synchronized
    override fun getSocketId(): Int = socketId

    @Synchronized
    override fun send(message: ResponseItem) {
        try {
            logger.info("Message to send: ${message.toJson()}")
            printWriter.println(message.toJson())
        } catch (ex: Exception) {
            logger.error("Error while sending message!\n${ex.message}")
        }
    }

    private fun receive() {
        bufferedReader.use {
            while (DatabaseManagerRunner.isRunnable()) {
                try {
                    val inputLine = bufferedReader.readLine()

                    if (inputLine != null) {
                        logger.info("Message '$inputLine' received")
                        val header = HeaderBuilder().build(message = inputLine, socketId = socketId)
                        CommandHandler.parseMessage(message = inputLine, header = header)
                    }
                } catch (ex: Exception) {
                    if (ex.message!!.contains("Connection reset")) {
                        return logger.warn(ex.message)
                    } else {
                        logger.error("Error occurred while parsing message!\n${ex.message}")
                    }
                }
            }
        }
    }

    private fun openSockets() {
        try {
            logger.info("Opening sockets ...")
            printWriter = PrintWriter(clientSocket.getOutputStream(), true)
            bufferedReader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            logger.info("Sockets opened")
        } catch (ex: Exception) {
            logger.error("Error occurred while opening sockets!\n${ex.message}")
        }
    }

    private fun closeSockets() {
        try {
            logger.info("Closing sockets ...")
            if (::printWriter.isInitialized) {
                printWriter.close()
            }

            if (::bufferedReader.isInitialized) {
                bufferedReader.close()
            }

            Clients.removeClient(client = this)
            clientSocket.close()
            logger.info("Sockets closed")
        } catch (ex: Exception) {
            logger.error("Error occurred while closing sockets!\n${ex.message}")
        }
    }
}