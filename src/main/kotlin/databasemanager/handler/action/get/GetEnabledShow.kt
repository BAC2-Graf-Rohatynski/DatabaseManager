package databasemanager.handler.action.get

import databasemanager.database.config.DdfDatabaseStorage
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetEnabledShow: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetEnabledShow::class.java)

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetEnabledShow.name}' will be executed ...")
        return DdfDatabaseStorage.getEnabledDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        return this
    }
}