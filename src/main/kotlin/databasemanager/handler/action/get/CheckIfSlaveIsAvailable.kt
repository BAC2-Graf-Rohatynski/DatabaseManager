package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.CheckIfSlaveIsAvailableItem
import enumstorage.database.DatabaseCommand

object CheckIfSlaveIsAvailable: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(CheckIfSlaveIsAvailable::class.java)
    private lateinit var item: CheckIfSlaveIsAvailableItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.CheckIfSlaveIsAvailable.name}' will be executed ...")
        return InfoDatabaseHandler.checkIfSlaveIsAlreadyAdded(ssid = item.ssid)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = CheckIfSlaveIsAvailableItem().toObject(message = message)
        return this
    }
}