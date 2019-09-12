package databasemanager.handler.action.update

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.slave.SlaveInformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import apibuilder.database.information.UpdateSlaveAllWhereItem
import enumstorage.database.DatabaseCommand

object UpdateAllSlavesWhere: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateAllSlavesWhere::class.java)
    private lateinit var item: UpdateSlaveAllWhereItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateAllSlavesWhere.name}' will be executed ...")

        if (SlaveInformation.Ssid.name == item.setField) {
            throw Exception("It isn't allowed to change a slave's SSID with the command '${DatabaseCommand.UpdateAllSlavesWhere.name}'!")
        }

        return InfoDatabaseHandler.updateAllWhere(
                setField = item.setField,
                setValue = item.setValue.toString(),
                whereField = item.whereField,
                whereValue = item.whereValue.toString())
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = UpdateSlaveAllWhereItem().toObject(message = message)
        return this
    }
}