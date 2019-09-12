package databasemanager.handler.action.get

import apibuilder.database.show.GetAllShowsItem
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllShows: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllShows::class.java)
    private lateinit var item: GetAllShowsItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllShows.name}' will be executed ...")
        return DdfDatabaseObject.getAllDatabasesKeys().toString()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllShowsItem().toObject(message = message)
        return this
    }
}