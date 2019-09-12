package databasemanager.handler.interfaces

import org.json.JSONArray

interface ICommandHandlerAction {
    fun run(): Any
    fun build(message: String): ICommandHandlerAction
}