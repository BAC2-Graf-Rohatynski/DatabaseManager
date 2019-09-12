package databasemanager.handler.action.update

import apibuilder.database.manufacturer.item.ManufacturerItem
import databasemanager.database.manufacturer.ManufacturerDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object UpdateManufacturer: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateManufacturer::class.java)
    private lateinit var item: ManufacturerItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateManufacturerById.name}' will be executed ...")
        return ManufacturerDatabaseHandler.updateById(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = ManufacturerItem().toObject(string = message)
        return this
    }
}