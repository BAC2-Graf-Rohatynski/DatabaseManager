package databasemanager.handler.action.update

import apibuilder.database.gobo.item.GoboItem
import databasemanager.database.gobo.GoboDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object UpdateGobo: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateGobo::class.java)
    private lateinit var item: GoboItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateGoboById.name}' will be executed ...")
        return GoboDatabaseHandler.updateById(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GoboItem().toObject(string = message)
        return this
    }
}