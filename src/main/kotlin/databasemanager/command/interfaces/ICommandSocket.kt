package databasemanager.command.interfaces

import apibuilder.database.response.ResponseItem

interface ICommandSocket {
    fun send(message: ResponseItem)
    fun getSocketId(): Int
}