package databasemanager.database

import apibuilder.slave.Slave
import databasemanager.database.interfaces.IDatabase
import enumstorage.slave.SlaveInformation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import propertystorage.DatabaseProperties
import java.sql.*

class Database: IDatabase {
    @Volatile
    private lateinit var connection: Connection

    @Volatile
    private lateinit var statement: Statement

    private val logger: Logger = LoggerFactory.getLogger(Database::class.java)
    private var tableName = String()

    @Synchronized
    override fun setTableName(tableName: String) {
        this.tableName = tableName
        logger.info("Table named $tableName")
    }

    @Synchronized
    override fun connectToDatabase() {
        try {
            logger.info("Connecting to database ...")
            connection = DriverManager.getConnection(DatabaseProperties.getUrl(), DatabaseProperties.getUsername(), DatabaseProperties.getPassword())
            statement = connection.createStatement()
            logger.info("Connected to database")
        } catch (ex: Exception) {
            logger.error("Error occurred: ${ex.message}")
        }
    }

    @Synchronized
    override fun checkIfRecordExists(ssid: Int): Boolean {
        val query = "SELECT * FROM $tableName WHERE ssid = $ssid"
        val resultSet = statement.executeQuery(query)
        return resultSet.next()
    }

    @Synchronized
    override fun checkWhetherTableExists(): Boolean {
        val metaData = connection.metaData
        val resultSet = metaData.getTables(null, null, tableName, arrayOf("TABLE"))
        return resultSet.next()
    }

    @Synchronized
    override fun addToDatabase(query: String): Boolean {
        return try {
            logger.info("Adding object to table '$tableName': $query")
            statement.executeUpdate(query)
            logger.info("Object added to table '$tableName'")
            true
        } catch (ex: Exception) {
            logger.error("Object not added to table '$tableName': ${ex.message}")
            false
        }
    }

    @Synchronized
    override fun createTableInDatabase(query: String) {
        try {
            statement.executeUpdate(query)
            logger.info("Table '$tableName' created")
        } catch (ex: Exception) {
            logger.error(ex.message)
        }
    }

    @Synchronized
    override fun updateBySsid(ssid: Int, field: String, value: String) {
        try {
            val query = "UPDATE $tableName SET $field = '$value' WHERE ssid = $ssid"
            statement.executeUpdate(query)
            logger.info("Field $field of device $ssid in table '$tableName' updated")
        } catch (ex: Exception) {
            logger.error("Item not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateInformation(slave: Slave) {
        try {
            val query = "UPDATE $tableName SET " +
                    "${SlaveInformation.Ssid.name} = '${slave.ssid}', " +
                    "${SlaveInformation.Type.name} = '${slave.type}', " +
                    "${SlaveInformation.Device.name} = '${slave.device}', " +
                    "${SlaveInformation.Manufacturer.name} = '${slave.manufacturer}', " +
                    "WHERE ${SlaveInformation.Ssid.name} = '${slave.ssid}'"
            statement.executeUpdate(query)
            logger.info("Information of device ${slave.ssid} in table '$tableName' updated")
        } catch (ex: Exception) {
            logger.error("Item not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateByMacAddress(macAddress: String, field: String, value: String) {
        try {
            val query = "UPDATE $tableName SET $field = '$value' WHERE ${SlaveInformation.MacAddressTemporary.name} = '$macAddress'"
            statement.executeUpdate(query)
            logger.info("Field $field of device $macAddress updated in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Item not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun renameTable(newTableName: String) {
        val query = "Alter TABLE $tableName RENAME TO $newTableName"
        statement.executeUpdate(query)
        setTableName(tableName = newTableName)
        logger.info("Table '$tableName' renamed to $newTableName")
    }

    @Synchronized
    override fun dropTable() {
        try {
            val query = "DROP TABLE $tableName "
            statement.executeUpdate(query)
            logger.info("Table '$tableName' dropped")
        } catch (ex: Exception) { }
    }

    @Synchronized
    override fun updateAll(field: String, value: String) {
        try {
            val query = "UPDATE $tableName SET $field = '$value'"
            statement.executeUpdate(query)
            logger.info("Field $field of devices updated in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateAllWhere(setField: String, setValue: String, whereField: String, whereValue: String) {
        try {
            val query = "UPDATE $tableName SET $setField = '$setValue' WHERE $whereField = '$whereValue'"
            statement.executeUpdate(query)
            logger.info("Field $setField of devices updated to $setValue in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateGeo(positionX: Int, positionY: Int, positionZ: Int, rotationX: Int, rotationY: Int,
                           rotationZ: Int, accelerationX: Int, accelerationY: Int, accelerationZ: Int, macAddress: String) {
        try {
            val query = "UPDATE $tableName SET " +
                    "${SlaveInformation.PositionX.name} = '$positionX', " +
                    "${SlaveInformation.PositionY.name} = '$positionY', " +
                    "${SlaveInformation.PositionZ.name} = '$positionZ', " +
                    "${SlaveInformation.RotationX.name} = '$rotationX', " +
                    "${SlaveInformation.RotationY.name} = '$rotationY', " +
                    "${SlaveInformation.RotationZ.name} = '$rotationZ', " +
                    "${SlaveInformation.AccelerationX.name} = '$accelerationX', " +
                    "${SlaveInformation.AccelerationY.name} = '$accelerationY', " +
                    "${SlaveInformation.AccelerationZ.name} = '$accelerationZ' " +
                    "WHERE ${SlaveInformation.MacAddressTemporary.name} = '$macAddress'"
            statement.executeUpdate(query)
            logger.info("Geo of device $macAddress updated in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateConfiguration(ddfhHash: String, ddfFile: String, ssid: Int) {
        try {
            val query = "UPDATE $tableName SET " +
                    "${SlaveInformation.DdfHash.name} = '$ddfhHash', " +
                    "${SlaveInformation.DdfFile.name} = '$ddfFile', " +
                    "WHERE ${SlaveInformation.Ssid.name} = '$ssid'"
            statement.executeUpdate(query)
            logger.info("Configuration of device $ssid updated in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateConfigurationSsid(ssid: Int) {
        try {
            val query = "UPDATE $tableName SET " +
                    "${SlaveInformation.Ssid.name} = '$ssid', " +
                    "WHERE ${SlaveInformation.Ssid.name} = '$ssid'"
            statement.executeUpdate(query)
            logger.info("Configuration SSID of device $ssid updated in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateRotating(isRotating: Boolean, ssid: Int) {
        try {
            val query = "UPDATE $tableName SET ${SlaveInformation.Rotating.name} = '$isRotating' " +
                    "WHERE ssid = $ssid"
            statement.executeUpdate(query)
            logger.info("Rotation of device SSID $ssid updated in table '$tableName' to $isRotating")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun updateTimestamp(macAddress: String) {
        try {
            val query = "UPDATE $tableName SET ${SlaveInformation.Timestamp.name} = '${System.currentTimeMillis()}' " +
                    "WHERE ${SlaveInformation.MacAddressTemporary.name} = '$macAddress'"
            statement.executeUpdate(query)
            logger.info("Timestamp of device $macAddress updated in table '$tableName'")
        } catch (ex: Exception) {
            logger.error("Items not not updated in table '$tableName': ${ex.message}")
        }
    }

    @Synchronized
    override fun getNumberOfRecords(): Int {
        val query = "SELECT * FROM $tableName"
        val resultSet = statement.executeQuery(query)
        var count = 0

        while (resultSet.next()) {
            count++
        }

        return count
    }

    @Synchronized
    override fun deleteRecordWhere(field: String, value: String) {
        val query = "DELETE FROM $tableName WHERE $field='$value'"
        statement.executeUpdate(query)
    }

    @Synchronized
    override fun getAllRecords(): ResultSet {
        val query = "SELECT * FROM $tableName"
        return statement.executeQuery(query)
    }

    @Synchronized
    override fun getById(field: String, id: Int): ResultSet {
        val query = "SELECT * FROM $tableName WHERE $field='$id'"
        return statement.executeQuery(query)
    }

    @Synchronized
    override fun deleteAllRecords() {
        try {
            statement.executeUpdate("TRUNCATE $tableName")
            logger.info("Table '$tableName cleared'")
        } catch (ex: Exception) {
            logger.error("Table '$tableName' not cleared: ${ex.message}")
        }
    }

    @Synchronized
    override fun selectEntryWhere(field: String, value: String): ResultSet {
        val query = "SELECT * FROM $tableName WHERE $field = '$value'"
        return statement.executeQuery(query)
    }
}