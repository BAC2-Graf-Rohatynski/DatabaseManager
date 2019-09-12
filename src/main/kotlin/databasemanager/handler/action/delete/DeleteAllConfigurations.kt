package databasemanager.handler.action.delete

import apibuilder.database.configuration.DeleteAllConfigurationsItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import enumstorage.database.DatabaseCommand

object DeleteAllConfigurations: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllConfigurations::class.java)
    private lateinit var item: DeleteAllConfigurationsItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllConfigurations.name}' will be executed ...")
        return DdfDatabaseObject.getDatabaseByKey(show = item.show).deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllConfigurationsItem().toObject(message = message)
        return this
    }
}