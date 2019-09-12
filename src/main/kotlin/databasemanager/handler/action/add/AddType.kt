package databasemanager.handler.action.add

import apibuilder.database.type.item.TypeItem
import databasemanager.database.type.TypeDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object AddType: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(AddType::class.java)
    private lateinit var item: TypeItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.AddType.name}' will be executed ...")
        return TypeDatabaseHandler.addItem(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = TypeItem().toObject(string = message)
        return this
    }
}