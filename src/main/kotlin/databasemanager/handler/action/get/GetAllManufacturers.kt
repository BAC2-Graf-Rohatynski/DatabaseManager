package databasemanager.handler.action.get

import apibuilder.database.manufacturer.GetAllManufacturersItem
import databasemanager.database.manufacturer.ManufacturerDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllManufacturers: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllManufacturers::class.java)
    private lateinit var item: GetAllManufacturersItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllManufacturers.name}' will be executed ...")
        return ManufacturerDatabaseHandler.getAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllManufacturersItem().toObject(message = message)
        return this
    }
}