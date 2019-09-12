package databasemanager.handler.action.delete

import databasemanager.handler.interfaces.ICommandHandlerAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.image.DeleteAllImagesItem
import databasemanager.database.image.ImageDatabaseHandler
import enumstorage.database.DatabaseCommand

object DeleteAllImages: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteAllImages::class.java)
    private lateinit var item: DeleteAllImagesItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteAllImages.name}' will be executed ...")
        return ImageDatabaseHandler.deleteAllRecordsInDatabase()
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = DeleteAllImagesItem().toObject(message = message)
        return this
    }
}