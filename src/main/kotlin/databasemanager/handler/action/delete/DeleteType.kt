package databasemanager.handler.action.delete

import apibuilder.database.type.item.TypeItem
import databasemanager.database.type.TypeDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object DeleteType: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteType::class.java)
    private lateinit var item: TypeItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteType.name}' will be executed ...")
        return TypeDatabaseHandler.deleteRecordWhere(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = TypeItem().toObject(string = message)
        return this
    }
}