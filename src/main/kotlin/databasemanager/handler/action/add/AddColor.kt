package databasemanager.handler.action.add

import apibuilder.database.color.item.ColorItem
import databasemanager.database.color.ColorDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object AddColor: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(AddColor::class.java)
    private lateinit var item: ColorItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.AddColor.name}' will be executed ...")
        return ColorDatabaseHandler.addItem(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = ColorItem().toObject(string = message)
        return this
    }
}