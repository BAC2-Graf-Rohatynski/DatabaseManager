package databasemanager.handler.action.delete

import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.type.DeleteAllTypesItem
import databasemanager.database.type.TypeDatabaseHandler
import enumstorage.database.DatabaseCommand

object DeleteAllTypes: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllTypes::class.java)
    private lateinit var item: DeleteAllTypesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllTypes.name}' will be executed ...")
        return TypeDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllTypesItem().toObject(message = message)
        return this
    }
}