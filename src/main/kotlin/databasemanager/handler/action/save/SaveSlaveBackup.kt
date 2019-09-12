package databasemanager.handler.action.save

import databasemanager.database.info.InfoBackupDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object SaveSlaveBackup: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(SaveSlaveBackup::class.java)

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.SaveSlaveBackup.name}' will be executed ...")
        return InfoBackupDatabaseHandler.saveBackup()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        return this
    }
}