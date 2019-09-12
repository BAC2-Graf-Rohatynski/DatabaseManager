package databasemanager.clients

import apibuilder.database.response.ResponseItem
import databasemanager.clients.interfaces.IClients
import databasemanager.command.CommandSocket
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Clients: IClients {
    private val clients: MutableList<CommandSocket> = mutableListOf()
    private val logger: Logger = LoggerFactory.getLogger(Clients::class.java)

    @Synchronized
    override fun sendToAllClients(response: ResponseItem) {
        clients.forEach { client ->
            try {
                client.send(message = response)
            } catch (ex: Exception) {
                logger.error("Error occurred while sending response to all clients!\n${ex.message}")
                removeClient(client = client)
            }
        }
    }

    @Synchronized
    override fun sendToSingleClient(response: ResponseItem) {
        clients.forEach { client ->
            if (client.getSocketId() == response.socketId) {
                try {
                    client.send(message = response)
                } catch (ex: Exception) {
                    logger.error("Error occurred while sending response to a single client!\n${ex.message}")
                    removeClient(client = client)
                }
            }
        }
    }

    @Synchronized
    override fun addClient(client: CommandSocket) {
        try {
            if (!clients.contains(client)) {
                clients.add(client)
                logger.info("Client added")
            }
        } catch (ex: Exception) {
            logger.error("Error occurred while adding client!\n${ex.message}")
        }
    }

    @Synchronized
    override fun removeClient(client: CommandSocket) {
        try {
            if (clients.contains(client)) {
                clients.remove(client)
                logger.info("Client removed")
            }
        } catch (ex: Exception) {
            logger.error("Error occurred while removing client!\n${ex.message}")
        }
    }
}