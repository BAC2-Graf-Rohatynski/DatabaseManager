package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetSlaveByEnabledRotation: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetSlaveByEnabledRotation::class.java)

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetSlaveByEnabledRotation.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveByEnabledRotation().forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        return this
    }
}