package databasemanager.handler.action.delete

import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.manufacturer.DeleteAllManufacturersItem
import databasemanager.database.manufacturer.ManufacturerDatabaseHandler
import enumstorage.database.DatabaseCommand

object DeleteAllManufacturers: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllManufacturers::class.java)
    private lateinit var item: DeleteAllManufacturersItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllManufacturers.name}' will be executed ...")
        return ManufacturerDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllManufacturersItem().toObject(message = message)
        return this
    }
}