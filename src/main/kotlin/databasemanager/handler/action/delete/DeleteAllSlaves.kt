package databasemanager.handler.action.delete

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.DeleteAllSlavesItem
import enumstorage.database.DatabaseCommand

object DeleteAllSlaves: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllSlaves::class.java)
    private lateinit var item: DeleteAllSlavesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllSlaves.name}' will be executed ...")
        return InfoDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllSlavesItem().toObject(message = message)
        return this
    }
}