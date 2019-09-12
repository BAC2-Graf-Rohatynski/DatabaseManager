package databasemanager.handler.action.delete

import apibuilder.database.gobo.DeleteAllGobosItem
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import databasemanager.database.gobo.GoboDatabaseHandler
import enumstorage.database.DatabaseCommand

object DeleteAllGobos: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllGobos::class.java)
    private lateinit var item: DeleteAllGobosItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllGobos.name}' will be executed ...")
        return GoboDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllGobosItem().toObject(message = message)
        return this
    }
}