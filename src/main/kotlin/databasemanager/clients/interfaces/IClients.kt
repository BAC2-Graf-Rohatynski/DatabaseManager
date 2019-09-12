package databasemanager.clients.interfaces

import apibuilder.database.response.ResponseItem
import databasemanager.command.CommandSocket
import org.json.JSONArray

interface IClients {
    fun sendToAllClients(response: ResponseItem)
    fun sendToSingleClient(response: ResponseItem)
    fun addClient(client: CommandSocket)
    fun removeClient(client: CommandSocket)
}