package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.GetSlaveByDeviceItem
import enumstorage.database.DatabaseCommand

object GetSlaveByDevice: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetSlaveByDevice::class.java)
    private lateinit var item: GetSlaveByDeviceItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetSlaveByDevice.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveByDevice(device = item.device).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetSlaveByDeviceItem().toObject(message = message)
        return this
    }
}