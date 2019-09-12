package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.GetSlaveByTypeItem
import enumstorage.database.DatabaseCommand

object GetSlaveByType: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetSlaveByType::class.java)
    private lateinit var item: GetSlaveByTypeItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetSlaveByType.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveByType(type = item.type).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetSlaveByTypeItem().toObject(message = message)
        return this
    }
}