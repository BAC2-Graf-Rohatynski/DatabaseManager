package databasemanager.handler.action.get

import apibuilder.database.show.IsShowAvailableItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object IsShowAvailable: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(IsShowAvailable::class.java)
    private lateinit var item: IsShowAvailableItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.IsShowAvailable.name}' will be executed ...")
        return DdfDatabaseObject.isShowAvailable(databaseName = item.show)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = IsShowAvailableItem().toObject(message = message)
        return this
    }
}