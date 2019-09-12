package databasemanager.handler.action.delete

import apibuilder.database.image.item.ImageItem
import databasemanager.database.image.ImageDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import enumstorage.database.DatabaseCommand
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object DeleteImage: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(DeleteImage::class.java)
    private lateinit var item: ImageItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.DeleteImage.name}' will be executed ...")
        return ImageDatabaseHandler.deleteRecordWhere(item = item)
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = ImageItem().toObject(string = message)
        return this
    }
}