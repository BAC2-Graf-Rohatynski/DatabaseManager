package databasemanager.handler.action.delete

import apibuilder.database.device.item.DeviceItem
import databasemanager.database.device.DeviceDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object DeleteDevice: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteDevice::class.java)
    private lateinit var item: DeviceItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteDevice.name}' will be executed ...")
        return DeviceDatabaseHandler.deleteRecordWhere(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeviceItem().toObject(string = message)
        return this
    }
}