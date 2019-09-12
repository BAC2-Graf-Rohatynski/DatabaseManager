package databasemanager.handler.action.delete

import apibuilder.database.device.DeleteAllDevicesItem
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import databasemanager.database.device.DeviceDatabaseHandler
import enumstorage.database.DatabaseCommand

object DeleteAllDevices: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllDevices::class.java)
    private lateinit var item: DeleteAllDevicesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllDevices.name}' will be executed ...")
        return DeviceDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllDevicesItem().toObject(message = message)
        return this
    }
}