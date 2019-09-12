package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.GetGeoByMacAddressItem
import enumstorage.database.DatabaseCommand

object GetGeoByMacAddress: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetGeoByMacAddress::class.java)
    private lateinit var item: GetGeoByMacAddressItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetGeoByMacAddress.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveByMacAddress(macAddress = item.macAddress).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetGeoByMacAddressItem().toObject(message = message)
        return this
    }
}