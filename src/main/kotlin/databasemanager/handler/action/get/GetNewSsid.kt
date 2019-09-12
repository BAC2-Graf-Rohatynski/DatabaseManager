package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetNewSsid: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetNewSsid::class.java)

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetNewSsid.name}' will be executed ...")
        return InfoDatabaseHandler.getNewSsid()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        return this
    }
}