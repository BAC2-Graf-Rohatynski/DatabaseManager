package databasemanager.handler.action.get

import apibuilder.database.information.GetAllSlavesItem
import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllSlaves: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllSlaves::class.java)
    private lateinit var item: GetAllSlavesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllSlaves.name}' will be executed ...")

        val jsonArray = JSONArray()

        InfoDatabaseHandler.getAllRecordsInDatabase().forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllSlavesItem().toObject(message = message)
        return this
    }
}