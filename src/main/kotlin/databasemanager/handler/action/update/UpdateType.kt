package databasemanager.handler.action.update

import apibuilder.database.type.item.TypeItem
import databasemanager.database.type.TypeDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object UpdateType: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateType::class.java)
    private lateinit var item: TypeItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateTypeById.name}' will be executed ...")
        return TypeDatabaseHandler.updateById(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = TypeItem().toObject(string = message)
        return this
    }
}