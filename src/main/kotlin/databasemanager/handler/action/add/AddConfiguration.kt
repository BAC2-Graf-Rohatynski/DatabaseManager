package databasemanager.handler.action.add

import apibuilder.database.configuration.AddConfigurationItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import enumstorage.database.DatabaseCommand

object AddConfiguration: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(AddConfiguration::class.java)
    private lateinit var item: AddConfigurationItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.AddConfiguration.name}' will be executed ...")

        if (!InfoDatabaseHandler.checkIfSlaveIsAlreadyAdded(ssid = item.ssid)) {
            throw Exception("Slave with SSID '${item.ssid}' isn't existing in table '${DatabaseType.Information.name}'!")
        }

        return DdfDatabaseObject
                .getDatabaseByKey(show = item.show)
                .addConfig(ddfFile = item.ddfFile, ddfHash = item.ddfHash, ssid = item.ssid)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = AddConfigurationItem().toObject(message = message)
        return this
    }
}