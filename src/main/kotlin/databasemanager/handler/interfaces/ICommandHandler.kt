package databasemanager.handler.interfaces

import apibuilder.database.header.Header

interface ICommandHandler {
    fun parseMessage(header: Header, message: String)
}