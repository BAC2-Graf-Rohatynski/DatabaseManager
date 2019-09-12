package databasemanager.database.info

import apibuilder.slave.Slave
import databasemanager.database.Database
import databasemanager.database.info.interfaces.IInfoBackupDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.slave.SlaveInformation
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.sql.ResultSet

object InfoBackupDatabaseHandler: IInfoBackupDatabaseHandler {
    private var tableName = DatabaseType.InformationBackup.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(InfoBackupDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.InformationBackup.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            logger.info("'${DatabaseType.InformationBackup.name}' database started")
            loadBackup()
        } catch (ex: Exception) {
            logger.error("Error occurred\n${ex.message}")
        }
    }

    private fun loadBackup() {
        logger.info("Loading '${DatabaseType.InformationBackup.name}' backup ...")
        InfoDatabaseHandler.deleteAllRecordsInDatabase()

        createSlaveList(resultSet = getAllRecords()).forEach { slave ->
            InfoDatabaseHandler.addSlave(slave = slave)
        }

        InfoDatabaseHandler.showEnries()
        logger.info("'${DatabaseType.InformationBackup.name}' backup loaded into slave database")
    }

    @Synchronized
    fun saveBackup() {
        logger.info("Saving '${DatabaseType.InformationBackup.name}' backup ...")
        deleteAllRecordsInDatabase()

        InfoDatabaseHandler.getAllRecordsInDatabase().forEach { slave ->
            addSlave(slave = slave)
        }

        logger.info("'${DatabaseType.InformationBackup.name}' backup saved")
    }

    private fun createSlaveList(resultSet: ResultSet): MutableList<Slave> {
        val slaves = mutableListOf<Slave>()

        while (resultSet.next()) {
            val slave = JSONObject()
                    .put(SlaveInformation.Ssid.name, resultSet.getInt(SlaveInformation.Ssid.name))
                    .put(SlaveInformation.MacAddress.name, resultSet.getString(SlaveInformation.MacAddress.name))
                    .put(SlaveInformation.MacAddressTemporary.name, resultSet.getString(SlaveInformation.MacAddressTemporary.name))
                    .put(SlaveInformation.IpAddress.name, resultSet.getString(SlaveInformation.IpAddress.name))
                    .put(SlaveInformation.DeviceImageHash.name, resultSet.getString(SlaveInformation.DeviceImageHash.name))
                    .put(SlaveInformation.GoboImageHash.name, resultSet.getString(SlaveInformation.GoboImageHash.name))
                    .put(SlaveInformation.Status.name, resultSet.getString(SlaveInformation.Status.name))
                    .put(SlaveInformation.Type.name, resultSet.getString(SlaveInformation.Type.name))
                    .put(SlaveInformation.Device.name, resultSet.getString(SlaveInformation.Device.name))
                    .put(SlaveInformation.Manufacturer.name, resultSet.getString(SlaveInformation.Manufacturer.name))
                    .put(SlaveInformation.PositionX.name, resultSet.getInt(SlaveInformation.PositionX.name))
                    .put(SlaveInformation.PositionY.name, resultSet.getInt(SlaveInformation.PositionY.name))
                    .put(SlaveInformation.PositionZ.name, resultSet.getInt(SlaveInformation.PositionZ.name))
                    .put(SlaveInformation.RotationX.name, resultSet.getInt(SlaveInformation.RotationX.name))
                    .put(SlaveInformation.RotationY.name, resultSet.getInt(SlaveInformation.RotationY.name))
                    .put(SlaveInformation.RotationZ.name, resultSet.getInt(SlaveInformation.RotationZ.name))
                    .put(SlaveInformation.AccelerationX.name, resultSet.getInt(SlaveInformation.AccelerationX.name))
                    .put(SlaveInformation.AccelerationY.name, resultSet.getInt(SlaveInformation.AccelerationY.name))
                    .put(SlaveInformation.AccelerationZ.name, resultSet.getInt(SlaveInformation.AccelerationZ.name))
                    .put(SlaveInformation.Rotating.name, resultSet.getBoolean(SlaveInformation.Rotating.name))
                    .put(SlaveInformation.Timestamp.name, resultSet.getLong(SlaveInformation.Timestamp.name))
            slaves.add(Slave().build(message = slave))
        }

        return slaves
    }

    @Synchronized
    override fun addSlave(slave: Slave): Boolean {
        val query = "INSERT INTO $tableName VALUES " +
                "('${slave.ssid}', " +
                "'${slave.macAddress}', " +
                "'${slave.macAddressTemporary}', " +
                "'${slave.ipAddress}', " +
                "'${slave.deviceImageHash}', " +
                "'${slave.goboImageHash}', " +
                "'${slave.status}', " +
                "'${slave.type}', " +
                "'${slave.device}', " +
                "'${slave.manufacturer}', " +
                "'${slave.positionX}', " +
                "'${slave.positionY}', " +
                "'${slave.positionZ}', " +
                "'${slave.rotationX}', " +
                "'${slave.rotationY}', " +
                "'${slave.rotationZ}', " +
                "'${slave.accelerationX}', " +
                "'${slave.accelerationY}', " +
                "'${slave.accelerationZ}', " +
                "'${slave.isRotating}', " +
                "'${System.currentTimeMillis()}')"
        return database.addToDatabase(query = query)
    }

    private fun createTable() {
        val query = "CREATE TABLE $tableName " +
                "(`${SlaveInformation.Ssid.name}` Integer, " +
                "`${SlaveInformation.MacAddress.name}` VARCHAR(255), " +
                "`${SlaveInformation.MacAddressTemporary.name}` VARCHAR(255), " +
                "`${SlaveInformation.IpAddress.name}` VARCHAR(255), " +
                "`${SlaveInformation.DeviceImageHash.name}` VARCHAR(255), " +
                "`${SlaveInformation.GoboImageHash.name}` VARCHAR(255), " +
                "`${SlaveInformation.Status.name}` VARCHAR(255), " +
                "`${SlaveInformation.Type.name}` VARCHAR(255), " +
                "`${SlaveInformation.Device.name}` VARCHAR(255), " +
                "`${SlaveInformation.Manufacturer.name}` VARCHAR(255), " +
                "`${SlaveInformation.PositionX.name}` Integer, " +
                "`${SlaveInformation.PositionY.name}` Integer, " +
                "`${SlaveInformation.PositionZ.name}` Integer, " +
                "`${SlaveInformation.RotationX.name}` Integer, " +
                "`${SlaveInformation.RotationY.name}` Integer, " +
                "`${SlaveInformation.RotationZ.name}` Integer, " +
                "`${SlaveInformation.AccelerationX.name}` Integer, " +
                "`${SlaveInformation.AccelerationY.name}` Integer, " +
                "`${SlaveInformation.AccelerationZ.name}` Integer, " +
                "`${SlaveInformation.Rotating.name}` VARCHAR(255), " +
                "`${SlaveInformation.Timestamp.name}` TEXT, " +
                "PRIMARY KEY (${SlaveInformation.Ssid.name}))"

        database.createTableInDatabase(query = query)
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): List<Slave> = createSlaveList(resultSet = getAllRecords())

    private fun getAllRecords(): ResultSet = database.getAllRecords()

    @Synchronized
    override fun deleteAllRecordsInDatabase() = database.deleteAllRecords()

    @Synchronized
    override fun dropTable() = database.dropTable()
}