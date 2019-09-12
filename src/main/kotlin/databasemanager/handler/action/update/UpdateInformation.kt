package databasemanager.handler.action.update

import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.slave.Slave
import databasemanager.database.info.InfoDatabaseHandler
import enumstorage.database.DatabaseCommand

object UpdateInformation: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateInformation::class.java)
    private lateinit var slave: Slave

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateInformationBySsid.name}' will be executed ...")
        return InfoDatabaseHandler.updateInformation(slave = slave)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        slave = Slave().build(message = message)
        return this
    }
}