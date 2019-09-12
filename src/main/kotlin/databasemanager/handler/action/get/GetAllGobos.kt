package databasemanager.handler.action.get

import apibuilder.database.gobo.GetAllGobosItem
import databasemanager.database.gobo.GoboDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllGobos: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllGobos::class.java)
    private lateinit var item: GetAllGobosItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllGobos.name}' will be executed ...")
        return GoboDatabaseHandler.getAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllGobosItem().toObject(message = message)
        return this
    }
}