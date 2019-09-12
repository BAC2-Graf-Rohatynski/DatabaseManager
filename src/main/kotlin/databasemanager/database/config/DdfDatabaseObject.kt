package databasemanager.database.config

import databasemanager.database.config.interfaces.IDdfDatabaseObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object DdfDatabaseObject: IDdfDatabaseObject {
    private val databases = hashMapOf<String, DdfDatabaseHandler>()
    private val logger: Logger = LoggerFactory.getLogger(DdfDatabaseObject::class.java)
    private const val columnName = "DdfTable"

    init {
        try {
            logger.info("Loading all saved DDF databases from storage ...")
            val resultSet = DdfDatabaseStorage.getAllDdfDatabases()

            while (resultSet.next()) {
                val databaseName = resultSet.getString(columnName)
                logger.info("Loading DDF database '$databaseName' ...")
                addDatabase(databaseName = databaseName)
            }

            logger.info("DDF databases loaded from storage")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    private fun addDatabase(databaseName: String) {
        databases[databaseName] = DdfDatabaseHandler(tableName = databaseName)
        logger.info("DDF database $databaseName added")
    }

    @Synchronized
    override fun getDatabaseByKey(show: String): DdfDatabaseHandler {
        return if (databases.containsKey(show)) {
            databases[show]!!
        } else {
            throw Exception("Database of show '$show' not existing!")
        }
    }

    @Synchronized
    override fun isShowAvailable(databaseName: String): Boolean = databases.containsKey(databaseName)

    @Synchronized
    override fun getAllDatabasesKeys(): List<String> {
        val ddfDatabases = mutableListOf<String>()

        try {
            databases.forEach{ ddfConfig ->
                ddfDatabases.add(ddfConfig.key)
            }
        } catch (ex: Exception) {
            logger.error("Error while requesting all DDF databases!\n${ex.message}")
        }

        return ddfDatabases
    }

    @Synchronized
    override fun getAllDatabases(): List<DdfDatabaseHandler> {
        val ddfDatabases = mutableListOf<DdfDatabaseHandler>()

        try {
            databases.forEach{ ddfConfig ->
                ddfDatabases.add(ddfConfig.value)
            }
        } catch (ex: Exception) {
            logger.error("Error while requesting all DDF databases!\n${ex.message}")
        }

        return ddfDatabases
    }

    @Synchronized
    override fun newDatabase(databaseName: String): Boolean {
        return try {
            if (databases.containsKey(databaseName)) {
                logger.error("DDF database $databaseName already exists!")
                return false
            }
            create(databaseName = databaseName)
            logger.info("Database '$databaseName' added to database storage")
            true
        } catch (ex: Exception) {
            logger.error("Error while adding DDF database $databaseName!\n${ex.message}")
            false
        }
    }

    @Synchronized
    override fun renameDatabase(databaseName: String, newDatabaseName: String): Boolean {
        return try {
            if (databases.containsKey(databaseName)) {
                rename(databaseName = databaseName, newDatabaseName = newDatabaseName)
                true
            } else {
                logger.error("DDF database $databaseName not existing!")
                false
            }
        } catch (ex: Exception) {
            logger.error("Error while renaming DDF database $databaseName!\n${ex.message}")
            false
        }
    }

    @Synchronized
    override fun clearDatabase(databaseName: String): Boolean {
        return try {
            if (databases.containsKey(databaseName)) {
                clear(databaseName = databaseName)
                logger.info("DDF database $databaseName cleared")
                true
            } else {
                logger.error("DDF database $databaseName not existing!")
                false
            }
        } catch (ex: Exception) {
            logger.error("Error while clearing DDF database $databaseName!\n${ex.message}")
            false
        }
    }

    @Synchronized
    override fun deleteDatabase(databaseName: String): Boolean {
        return try {
            if (databases.containsKey(databaseName)) {
                if (databases.size <= 1) {
                    logger.error("Last DDF config cannot be deleted!")
                    return false
                }
                delete(databaseName = databaseName)
                logger.info("DDF database $databaseName deleted")
                true
            } else {
                logger.error("DDF database $databaseName not existing!")
                false
            }
        } catch (ex: Exception) {
            logger.error("Error while deleting DDF database $databaseName!\n${ex.message}")
            false
        }
    }

    private fun clear(databaseName: String) {
        databases[databaseName]!!.deleteAllRecordsInDatabase()
        logger.info("Table $databaseName cleared in DDF object handler")
    }

    private fun create(databaseName: String) {
        databases[databaseName] = DdfDatabaseHandler(tableName = databaseName)
        if (DdfDatabaseStorage.addDatabase(databaseName = databaseName)) {
            logger.info("Table $databaseName created in DDF object handler")
        } else {
            throw Exception("Database $databaseName cannot be created!")
        }
    }

    private fun delete(databaseName: String) {
        databases[databaseName]!!.dropTable()
        databases.remove(databaseName)
        DdfDatabaseStorage.deleteDdfDatabase(databaseName = databaseName)
        logger.info("Table $databaseName deleted in DDF object handler")
    }

    private fun rename(databaseName: String, newDatabaseName: String) {
        databases[databaseName]!!.renameTable(newDatabaseName = newDatabaseName)
        val table = databases.getValue(databaseName)
        databases.remove(databaseName)
        databases[newDatabaseName] = table
        DdfDatabaseStorage.renameDdfDatabase(databaseName = databaseName, newDatabaseName = newDatabaseName)
        logger.info("Table $databaseName in DDF object handler renamed to $newDatabaseName")
    }
}