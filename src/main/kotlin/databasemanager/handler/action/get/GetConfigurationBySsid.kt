package databasemanager.handler.action.get

import apibuilder.database.configuration.GetConfigurationBySsidItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetConfigurationBySsid: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetConfigurationBySsid::class.java)
    private lateinit var item: GetConfigurationBySsidItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetConfigurationBySsid.name}' will be executed ...")
        val database = DdfDatabaseObject.getDatabaseByKey(show = item.show)
        val jsonArray = JSONArray()

        database.getConfigurationBySsid(ssid = item.ssid).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetConfigurationBySsidItem().toObject(message = message)
        return this
    }
}