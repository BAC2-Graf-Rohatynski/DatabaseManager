package databasemanager.handler.action.delete

import apibuilder.database.show.DeleteShowItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.database.config.DdfDatabaseStorage
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object DeleteShow: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteShow::class.java)
    private lateinit var item: DeleteShowItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteShow.name}' will be executed ...")

        if (DdfDatabaseStorage.getEnabledDatabase() == item.show) {
            throw Exception("Database ${item.show} is still enabled!")
        }

        return DdfDatabaseObject.deleteDatabase(databaseName = item.show)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteShowItem().toObject(message = message)
        return this
    }
}