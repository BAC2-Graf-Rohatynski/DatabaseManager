package databasemanager.handler.action.add

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.slave.Slave
import enumstorage.database.DatabaseCommand

object AddSlave: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(AddSlave::class.java)
    private lateinit var slave: Slave

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.AddSlave.name}' will be executed ...")

        if (InfoDatabaseHandler.checkIfSlaveIsAlreadyAdded(ssid = slave.ssid)) {
            throw Exception("Slave with SSID '${slave.ssid}' is already existing in table '${DatabaseType.Information.name}'!")
        }

        return  InfoDatabaseHandler.addSlave(slave = slave)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        slave = Slave().build(message = message)
        return this
    }
}