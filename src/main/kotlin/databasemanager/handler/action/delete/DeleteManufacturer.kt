package databasemanager.handler.action.delete

import apibuilder.database.manufacturer.item.ManufacturerItem
import databasemanager.database.manufacturer.ManufacturerDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object DeleteManufacturer: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteManufacturer::class.java)
    private lateinit var item: ManufacturerItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteManufacturer.name}' will be executed ...")
        return ManufacturerDatabaseHandler.deleteRecordWhere(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = ManufacturerItem().toObject(string = message)
        return this
    }
}