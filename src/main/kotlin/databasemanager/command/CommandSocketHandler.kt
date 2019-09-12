package databasemanager.command

import databasemanager.DatabaseManagerRunner
import databasemanager.clients.Clients
import databasemanager.command.interfaces.ICommandSocketHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import propertystorage.PortProperties
import java.net.ServerSocket
import kotlin.concurrent.thread

object CommandSocketHandler: ICommandSocketHandler {
    private lateinit var serverSocket: ServerSocket
    private lateinit var commandSocket: CommandSocket
    private val port: Int = PortProperties.getDatabasePort()
    private val logger: Logger = LoggerFactory.getLogger(CommandSocketHandler::class.java)

    init {
        thread {
            try {
                openSockets()
                acceptClients()
            } catch (ex: Exception) {
                logger.error("Error occurred while running socket handler!\n${ex.message}")
            } finally {
                closeSockets()
            }
        }
    }

    private fun acceptClients() {
        while (DatabaseManagerRunner.isRunnable()) {
            logger.info("Waiting for clients ...")
            commandSocket = CommandSocket(clientSocket = serverSocket.accept())
            commandSocket.start()
            Clients.addClient(client = commandSocket)
        }
    }

    private fun openSockets() {
        logger.info("Opening socket on port '$port' ...")
        serverSocket = ServerSocket(port)
        logger.info("Socket opened")
    }

    @Synchronized
    override fun closeSockets() {
        try {
            logger.info("Closing sockets ...")

            if (::serverSocket.isInitialized) {
                serverSocket.close()
            }

            logger.info("Sockets closed")
        } catch (ex: Exception) {
            logger.error("Error occurred while closing sockets!\n${ex.message}")
        }
    }
}