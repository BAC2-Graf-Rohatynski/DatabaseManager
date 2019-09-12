package databasemanager.handler.action.get

import apibuilder.database.device.GetAllDevicesItem
import databasemanager.database.device.DeviceDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllDevices: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllDevices::class.java)
    private lateinit var item: GetAllDevicesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllDevices.name}' will be executed ...")
        return DeviceDatabaseHandler.getAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllDevicesItem().toObject(message = message)
        return this
    }
}