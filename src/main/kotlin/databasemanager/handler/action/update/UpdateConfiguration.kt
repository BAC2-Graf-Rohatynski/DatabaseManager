package databasemanager.handler.action.update

import apibuilder.database.configuration.UpdateConfigurationBySsidItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import enumstorage.database.DatabaseCommand

object UpdateConfiguration: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateConfiguration::class.java)
    private lateinit var item: UpdateConfigurationBySsidItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateConfigurationBySsid.name}' will be executed ...")
        return DdfDatabaseObject.getAllDatabases().forEach { database ->
            database.updateConfiguration(
                    ddfHash = item.ddfHash,
                    ddfFile = item.ddfFile,
                    ssid = item.ssid)
        }
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = UpdateConfigurationBySsidItem().toObject(message = message)
        return this
    }
}