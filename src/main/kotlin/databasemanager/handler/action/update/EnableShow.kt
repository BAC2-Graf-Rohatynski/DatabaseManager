package databasemanager.handler.action.update

import apibuilder.database.show.EnableShowItem
import databasemanager.database.config.DdfDatabaseStorage
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object EnableShow: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(EnableShow::class.java)
    private lateinit var item: EnableShowItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.EnableShow.name}' will be executed ...")
        return DdfDatabaseStorage.enableDatabase(databaseName = item.show)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = EnableShowItem().toObject(message = message)
        return this
    }
}