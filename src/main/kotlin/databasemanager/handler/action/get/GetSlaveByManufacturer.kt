package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.GetSlaveByManufacturerItem
import enumstorage.database.DatabaseCommand

object GetSlaveByManufacturer: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetSlaveByManufacturer::class.java)
    private lateinit var item: GetSlaveByManufacturerItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetSlaveByManufacturer.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveByManufacturer(manufacturer = item.manufacturer).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetSlaveByManufacturerItem().toObject(message = message)
        return this
    }
}