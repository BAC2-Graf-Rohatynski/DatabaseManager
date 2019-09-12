package databasemanager

import error.ErrorClientRunner
import databasemanager.command.CommandSocketHandler
import databasemanager.database.color.ColorDatabaseHandler
import databasemanager.database.config.DdfDatabaseObject
import databasemanager.database.config.DdfDatabaseStorage
import databasemanager.database.device.DeviceDatabaseHandler
import databasemanager.database.gobo.GoboDatabaseHandler
import databasemanager.database.image.ImageDatabaseHandler
import databasemanager.database.info.InfoBackupDatabaseHandler
import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.database.manufacturer.ManufacturerDatabaseHandler
import databasemanager.database.type.TypeDatabaseHandler
import enumstorage.update.ApplicationName
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DatabaseManagerRunner {
    private val logger: Logger = LoggerFactory.getLogger(DatabaseManagerRunner::class.java)

    @Volatile
    private var runApplication = true

    fun start() {
        logger.info("Starting application")
        ErrorClientRunner
        InfoDatabaseHandler
        InfoBackupDatabaseHandler
        DdfDatabaseStorage
        DdfDatabaseObject
        ManufacturerDatabaseHandler
        TypeDatabaseHandler
        DeviceDatabaseHandler
        ImageDatabaseHandler
        GoboDatabaseHandler
        ColorDatabaseHandler
        CommandSocketHandler
    }

    @Synchronized
    fun isRunnable(): Boolean = runApplication

    fun stop() {
        logger.info("Stopping application")
        runApplication = false

        CommandSocketHandler.closeSockets()
        ErrorClientRunner.stop()
    }

    fun getUpdateInformation(): JSONObject = UpdateInformation.getAsJson(applicationName = ApplicationName.DatabaseManager.name)
}