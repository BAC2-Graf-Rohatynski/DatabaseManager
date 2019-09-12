package databasemanager.handler.action.delete

import databasemanager.database.config.DdfDatabaseObject
import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.slave.SlaveInformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.DeleteSlaveItem
import enumstorage.database.DatabaseCommand

object DeleteSlave: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteSlave::class.java)
    private lateinit var item: DeleteSlaveItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteSlave.name}' will be executed ...")

        InfoDatabaseHandler.deleteRecordWhere(
                field = SlaveInformation.Ssid.name,
                value = item.ssid.toString())

        return DdfDatabaseObject.getAllDatabases().forEach { database ->
            database.deleteRecordWhere(
                    field = SlaveInformation.Ssid.name,
                    value = item.ssid.toString())
        }
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteSlaveItem().toObject(message = message)
        return this
    }
}