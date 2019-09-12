package databasemanager.handler.action.delete

import apibuilder.database.configuration.DeleteConfigurationItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import enumstorage.slave.SlaveInformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DeleteConfiguration: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteConfiguration::class.java)
    private lateinit var item: DeleteConfigurationItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteConfiguration.name}' will be executed ...")

        return DdfDatabaseObject
                .getDatabaseByKey(show = item.show)
                .deleteRecordWhere(
                        field = SlaveInformation.Ssid.name,
                        value = item.ssid.toString())
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteConfigurationItem().toObject(message = message)
        return this
    }
}