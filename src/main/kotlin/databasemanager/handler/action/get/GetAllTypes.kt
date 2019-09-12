package databasemanager.handler.action.get

import apibuilder.database.type.GetAllTypesItem
import databasemanager.database.type.TypeDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllTypes: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllTypes::class.java)
    private lateinit var item: GetAllTypesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllTypes.name}' will be executed ...")
        return TypeDatabaseHandler.getAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllTypesItem().toObject(message = message)
        return this
    }
}