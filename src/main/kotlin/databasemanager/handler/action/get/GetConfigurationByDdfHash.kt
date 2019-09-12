package databasemanager.handler.action.get

import apibuilder.database.configuration.GetConfigurationByDdfHashItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetConfigurationByDdfHash: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetConfigurationByDdfHash::class.java)
    private lateinit var item: GetConfigurationByDdfHashItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetConfigurationByDdfHash.name}' will be executed ...")
        val database = DdfDatabaseObject.getDatabaseByKey(show = item.show)
        val jsonArray = JSONArray()

        database.getConfigurationByDdfHash(ddfHash = item.ddfHash).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetConfigurationByDdfHashItem().toObject(message = message)
        return this
    }
}