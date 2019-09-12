package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.GetSlaveByMacAddressItem
import enumstorage.database.DatabaseCommand

object GetSlaveByMacAddress: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetSlaveByMacAddress::class.java)
    private lateinit var item: GetSlaveByMacAddressItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetSlaveByMacAddress.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveByMacAddress(macAddress = item.macAddress).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetSlaveByMacAddressItem().toObject(message = message)
        return this
    }
}