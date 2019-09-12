package databasemanager.handler.action.add

import apibuilder.database.show.AddShowItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object AddShow: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(AddShow::class.java)
    private lateinit var item: AddShowItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.AddShow.name}' will be executed ...")
        return DdfDatabaseObject.newDatabase(databaseName = item.show)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = AddShowItem().toObject(message = message)
        return this
    }
}