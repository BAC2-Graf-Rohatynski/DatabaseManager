package databasemanager.handler.action.get

import apibuilder.database.configuration.GetAllConfigurationsItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllConfigurations: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllConfigurations::class.java)
    private lateinit var item: GetAllConfigurationsItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllConfigurations.name}' will be executed ...")

        val jsonArray = JSONArray()

        DdfDatabaseObject.getDatabaseByKey(show = item.show).getAllRecordsInDatabase().forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllConfigurationsItem().toObject(message = message)
        return this
    }
}