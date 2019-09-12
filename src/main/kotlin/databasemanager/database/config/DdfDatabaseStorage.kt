package databasemanager.database.config

import databasemanager.database.Database
import databasemanager.database.config.interfaces.IDdfDatabaseStorage
import enumstorage.database.DatabaseType
import enumstorage.slave.SlaveInformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import propertystorage.DatabaseProperties
import java.lang.Exception
import java.sql.ResultSet

object DdfDatabaseStorage: IDdfDatabaseStorage {
    private var database = Database()
    private val tableName = DatabaseType.DdfDatabaseStorage.name
    private val logger: Logger = LoggerFactory.getLogger(DdfDatabaseStorage::class.java)
    private const val columnName = "DdfTable"

    init {
        logger.info("Starting '${DatabaseType.DdfDatabaseStorage.name}' database handler ...")
        database.setTableName(tableName = tableName)
        database.connectToDatabase()

        if (database.checkWhetherTableExists()) {
            logger.info("Table $tableName not created yet")
            dropTable()
            createTable()
        }

        logger.info("'${DatabaseType.DdfDatabaseStorage.name}' database started")

        if (!checkIfConfigurationTableExist()) {
            createDefaultDatabase()
        }
    }

    private fun checkIfConfigurationTableExist(): Boolean = getAllDdfDatabases().next()

    private fun createDefaultDatabase() {
        val databaseName = DatabaseProperties.getDefaultDdfDatabaseName()
        logger.warn("Create default DDF database '$databaseName' in '${DatabaseType.DdfDatabaseStorage.name}'")
        DdfDatabaseObject.newDatabase(databaseName = databaseName)
        addDatabase(databaseName = databaseName)
        enableDatabase(databaseName = databaseName)
    }

    @Synchronized
    override fun addDatabase(databaseName: String): Boolean {
        logger.info("Adding DDF database $databaseName ...")
        val query = "INSERT INTO $tableName VALUES (" +
                "'$databaseName', " +
                "'false')"
        return database.addToDatabase(query = query)
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName (" +
                "`$columnName` VARCHAR(255), " +
                "`${SlaveInformation.Enabled.name}` VARCHAR(255), " +
                "PRIMARY KEY ($columnName))"
        database.createTableInDatabase(query = query)
    }

    @Synchronized
    override fun dropTable() = database.dropTable()

    @Synchronized
    override fun deleteDdfDatabase(databaseName: String) {
        database.deleteRecordWhere(field = columnName, value = databaseName)
        logger.info("Database $databaseName removed from storage")
    }

    @Synchronized
    override fun renameDdfDatabase(databaseName: String, newDatabaseName: String) {
        database.updateAllWhere(
                whereField = columnName,
                whereValue = databaseName,
                setField = columnName,
                setValue = newDatabaseName)

        logger.info("Database $databaseName updated in storage to $newDatabaseName")
    }

    @Synchronized
    override fun enableDatabase(databaseName: String) {
        if (checkIfDatabaseExists(resultSet = getAllDdfDatabases(), databaseName = databaseName)) {
            val databasesToDisable = mutableListOf<String>()
            val databases = getAllDdfDatabases()

            while (databases.next()) {
                if (databases.getString(columnName) != databaseName) {
                    databasesToDisable.add(databases.getString(columnName))
                }
            }

            logger.info("Enabling database $databaseName ...")
            updateAllWhere(databaseName = databaseName, isEnabled = true)

            databasesToDisable.forEach { databaseNameToDisable ->
                logger.info("Disabling database $databaseNameToDisable ...")
                updateAllWhere(databaseName = databaseNameToDisable, isEnabled = false)
            }
        } else {
            logger.error("Cannot enable DDF database! DDF database '$databaseName' doesn't exist!")
        }
    }

    @Synchronized
    override fun getEnabledDatabase(): String {
        val resultSet = getAllDdfDatabases()

        while (resultSet.next()) {
            if (resultSet.getBoolean(SlaveInformation.Enabled.name)) {
                return resultSet.getString(columnName)
            }
        }

        throw Exception("No active database in storage found!")
    }

    private fun checkIfDatabaseExists(resultSet: ResultSet, databaseName: String): Boolean {
        while (resultSet.next()) {
            if (resultSet.getString(columnName) == databaseName) {
                return true
            }
        }

        return false
    }

    private fun updateAllWhere(databaseName: String, isEnabled: Boolean) = database.updateAllWhere(
            whereField = columnName,
            whereValue = databaseName,
            setField = SlaveInformation.Enabled.name,
            setValue = isEnabled.toString())

    @Synchronized
    override fun getAllDdfDatabases(): ResultSet = database.getAllRecords()
}