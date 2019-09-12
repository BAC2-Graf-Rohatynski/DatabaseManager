package databasemanager.database.config

import apibuilder.slave.Slave
import databasemanager.database.Database
import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.database.config.interfaces.IDdfDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.slave.SlaveInformation
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.ResultSet

class DdfDatabaseHandler(private val tableName: String): IDdfDatabaseHandler {
    private var database = Database()
    private val logger: Logger = LoggerFactory.getLogger(DdfDatabaseHandler::class.java)

    init {
        logger.info("Starting '${DatabaseType.Configuration.name}' database handler ...")
        database.setTableName(tableName = tableName)
        database.connectToDatabase()

        if (database.checkWhetherTableExists()) {
            logger.info("Table $tableName not created yet")
            dropTable()
            createTable()
        }

        logger.info("'${DatabaseType.Configuration.name}' database started")
        deleteUnusedEntriesOnStartup()
        showEntries()
    }

    @Synchronized
    override fun addConfig(ssid: Int, ddfHash: String, ddfFile: String): Boolean {
        logger.info("Adding item to table $tableName ...")

        val query = "INSERT INTO $tableName VALUES (" +
                "'$ssid', " +
                "'$ddfHash', " +
                "'$ddfFile')"

        return database.addToDatabase(query = query)
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")

        val query = "CREATE TABLE $tableName" +
                "(`${SlaveInformation.Ssid.name}` Integer, " +
                "`${SlaveInformation.DdfHash.name}` VARCHAR(255), " +
                "`${SlaveInformation.DdfFile.name}` VARCHAR(255), " +
                "PRIMARY KEY (${SlaveInformation.Ssid.name}))"

        database.createTableInDatabase(query = query)
        logger.info("Table $tableName created")
    }

    override fun showEntries() {
        logger.info("\n-------------------------------------------------------------\n")
        logger.info("Database entries of table $tableName")

        getAllRecordsInDatabase().forEach { slave ->
            logger.info("${SlaveInformation.Ssid.name}: ${slave.ssid} || " +
                    "${SlaveInformation.DdfHash.name}: ${slave.ddfHash} ||" +
                    "${SlaveInformation.DdfFile.name}: ${slave.ddfFile}")
        }

        logger.info("\n\n-------------------------------------------------------------")
    }

    private fun deleteUnusedEntriesOnStartup() {
        logger.info("Searching for unused configuration in table '$tableName'")
        val ssidsInInformationTable = mutableListOf<Int>()
        val ssidsToRemoveInConfigTable = mutableListOf<Int>()

        InfoDatabaseHandler.getAllRecordsInDatabase().forEach { slave ->
            ssidsInInformationTable.add(slave.ssid)
        }

        getAllRecordsInDatabase().forEach { slave ->
            ssidsToRemoveInConfigTable.add(slave.ssid)
        }

        ssidsToRemoveInConfigTable.forEach { ssid ->
            logger.warn("SSID $ssid will be removed from '$tableName' due to it isn't used anymore in Information table!")
            deleteRecordWhere(field = SlaveInformation.Ssid.name, value = ssid.toString())
        }
    }

    @Synchronized
    override fun renameTable(newDatabaseName: String) = database.renameTable(newTableName = newDatabaseName)

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }

    @Synchronized
    override fun deleteRecordWhere(field: String, value: String) = database.deleteRecordWhere(field = field, value = value)

    @Synchronized
    override fun getAllRecordsInDatabase(): List<Slave> {
        val slaves = database.getAllRecords()
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() = database.deleteAllRecords()

    @Synchronized
    override fun getConfigurationBySsid(ssid: Int): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.Ssid.name,
                value = ssid.toString())
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun getConfigurationByDdfHash(ddfHash: String): List<Slave> {
        val slaves = database.selectEntryWhere(field = SlaveInformation.DdfHash.name, value = ddfHash)
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun updateConfiguration(ssid: Int, ddfHash: String, ddfFile: String) = database.updateConfiguration(
            ssid = ssid,
            ddfhHash = ddfHash,
            ddfFile = ddfFile)

    @Synchronized
    override fun updateConfigurationSsid(ssid: Int) = database.updateConfigurationSsid(ssid = ssid)

    private fun createSlaveList(resultSet: ResultSet): MutableList<Slave> {
        val slaves = mutableListOf<Slave>()

        while (resultSet.next()) {
            val slave = JSONObject()
                    .put(SlaveInformation.Ssid.name, resultSet.getInt(SlaveInformation.Ssid.name))
                    .put(SlaveInformation.DdfHash.name, resultSet.getString(SlaveInformation.DdfHash.name))
                    .put(SlaveInformation.DdfFile.name, resultSet.getString(SlaveInformation.DdfFile.name))

            slaves.add(Slave().build(message = slave))
        }

        return slaves
    }
}