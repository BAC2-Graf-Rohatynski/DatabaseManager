package databasemanager.handler.action.update

import databasemanager.database.config.DdfDatabaseObject
import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.UpdateSlaveBySsidItem
import enumstorage.database.DatabaseCommand
import enumstorage.slave.SlaveInformation

object UpdateSlaveBySsid: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateSlaveBySsid::class.java)
    private lateinit var item: UpdateSlaveBySsidItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateSlaveBySsid.name}' will be executed ...")

        if (item.field == SlaveInformation.Ssid.name) {
            DdfDatabaseObject.getAllDatabases().forEach { ddfDatabase ->
                ddfDatabase.updateConfigurationSsid(ssid = item.ssid)
            }
        }

        return InfoDatabaseHandler.updateBySsid(
                field = item.field,
                value = item.value.toString(),
                ssid = item.ssid)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = UpdateSlaveBySsidItem().toObject(message = message)
        return this
    }
}