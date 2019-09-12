package databasemanager.handler.action.update

import apibuilder.database.show.RenameShowItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.database.config.DdfDatabaseStorage
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object RenameShow: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(RenameShow::class.java)
    private lateinit var item: RenameShowItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.RenameShow.name}' will be executed ...")

        if (DdfDatabaseStorage.getEnabledDatabase() == item.show) {
            throw Exception("Database ${item.show} is still enabled!")
        }

        return DdfDatabaseObject.renameDatabase(
                databaseName = item.show,
                newDatabaseName = item.value)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = RenameShowItem().toObject(message = message)
        return this
    }
}