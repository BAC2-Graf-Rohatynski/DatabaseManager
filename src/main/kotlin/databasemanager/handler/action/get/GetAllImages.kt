package databasemanager.handler.action.get

import apibuilder.database.image.GetAllImagesItem
import databasemanager.database.image.ImageDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object GetAllImages: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetAllImages::class.java)
    private lateinit var item: GetAllImagesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetAllImages.name}' will be executed ...")
        return ImageDatabaseHandler.getAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetAllImagesItem().toObject(message = message)
        return this
    }
}