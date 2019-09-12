package databasemanager.handler.action.delete

import apibuilder.database.color.DeleteAllColorsItem
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import databasemanager.database.color.ColorDatabaseHandler
import enumstorage.database.DatabaseCommand

object DeleteAllColors: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllColors::class.java)
    private lateinit var item: DeleteAllColorsItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllColors.name}' will be executed ...")
        return ColorDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllColorsItem().toObject(message = message)
        return this
    }
}