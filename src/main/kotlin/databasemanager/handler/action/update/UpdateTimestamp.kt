package databasemanager.handler.action.update

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.slave.Slave
import enumstorage.database.DatabaseCommand

object UpdateTimestamp: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateTimestamp::class.java)
    private lateinit var slave: Slave

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateTimestamp.name}' will be executed ...")
        return InfoDatabaseHandler.updateTimestamp(macAddress = slave.macAddress)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        slave = Slave().build(message = message)
        return this
    }
}