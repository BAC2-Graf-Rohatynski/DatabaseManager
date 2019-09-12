package databasemanager.handler.action.update

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.slave.Slave
import enumstorage.database.DatabaseCommand

object UpdateRotation: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(UpdateRotation::class.java)
    private lateinit var slave: Slave

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.UpdateRotation.name}' will be executed ...")
        return InfoDatabaseHandler.updateRotating(
                isRotating = slave.isRotating,
                ssid = slave.ssid)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        slave = Slave().build(message = message)
        return this
    }
}