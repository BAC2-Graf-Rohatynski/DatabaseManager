package databasemanager.handler.action.add

import apibuilder.database.device.item.DeviceItem
import databasemanager.database.device.DeviceDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import enumstorage.database.DatabaseCommand

object AddDevice: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(AddDevice::class.java)
    private lateinit var item: DeviceItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.AddDevice.name}' will be executed ...")
        return DeviceDatabaseHandler.addItem(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeviceItem().toObject(string = message)
        return this
    }
}