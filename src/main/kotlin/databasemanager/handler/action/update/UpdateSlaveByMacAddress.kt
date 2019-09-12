package databasemanager.handler.action.update

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.UpdateSlaveByMacAddressItem
import enumstorage.database.DatabaseCommand

object UpdateSlaveByMacAddress: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateSlaveByMacAddress::class.java)
    private lateinit var item: UpdateSlaveByMacAddressItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateSlaveByMacAddress.name}' will be executed ...")

        return InfoDatabaseHandler.updateByMacAddress(
                field = item.field,
                value = item.value.toString(),
                macAddress = item.macAddress)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = UpdateSlaveByMacAddressItem().toObject(message = message)
        return this
    }
}