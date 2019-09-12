package databasemanager.handler.action.get

import apibuilder.database.color.GetAllColorsItem
import databasemanager.database.color.ColorDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllColors: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllColors::class.java)
    private lateinit var item: GetAllColorsItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllColors.name}' will be executed ...")
        return ColorDatabaseHandler.getAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllColorsItem().toObject(message = message)
        return this
    }
}