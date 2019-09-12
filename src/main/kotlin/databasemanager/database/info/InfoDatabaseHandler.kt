package databasemanager.database.info

import apibuilder.slave.Slave
import databasemanager.database.Database
import databasemanager.database.info.interfaces.IInfoDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.slave.SlaveInformation
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.sql.ResultSet

object InfoDatabaseHandler: IInfoDatabaseHandler {
    private var tableName = DatabaseType.Information.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(InfoDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Information.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            dropTable()
            createTable()

            logger.info("'${DatabaseType.Information.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun checkIfSlaveIsAlreadyAdded(ssid: Int): Boolean = database.checkIfRecordExists(ssid = ssid)

    @Synchronized
    override fun getNewSsid(): Int {
        val slaves = database.getAllRecords()
        val usedSsids = arrayListOf<Int>()
        var newSsid = 0

        while (slaves.next()) {
            usedSsids.add(slaves.getInt(SlaveInformation.Ssid.name))
        }

        while (usedSsids.contains(newSsid)) {
            newSsid++
        }

        logger.info("New SSID: $newSsid")
        return newSsid
    }

    private fun checkForRequiredValue(slave: Slave): Boolean {
        return if (
                slave.macAddress == String() ||
                slave.ipAddress == String() ||
                slave.ssid < 0
        ) {
            logger.error("Field ${SlaveInformation.MacAddress.name} | ${SlaveInformation.IpAddress.name} empty or " +
                    "field ${SlaveInformation.Ssid.name} below 0!")
            false
        } else {
            true
        }
    }

    @Synchronized
    override fun addSlave(slave: Slave): Boolean {
        logger.info("Adding item to table $tableName ...")
        return if (checkForRequiredValue(slave = slave)) {
            val query = "INSERT INTO $tableName VALUES " +
                    "('${slave.ssid}', " +
                    "'${slave.macAddress}', " +
                    "'${slave.macAddress}', " +
                    "'${slave.ipAddress}', " +
                    "'${slave.deviceImageHash}', " +
                    "'${slave.goboImageHash}', " +
                    "'${slave.status}', " +
                    "'${slave.type}', " +
                    "'${slave.device}', " +
                    "'${slave.manufacturer}', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'0', " +
                    "'${slave.isRotating}', " +
                    "'${System.currentTimeMillis()}')"
            val result = database.addToDatabase(query = query)
            logger.info("Item added to table $tableName")
            result
        } else {
            false
        }
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
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
        logger.info("Table $tableName created")
    }

    @Synchronized
    override fun showEnries() {
        logger.info("\n-------------------------------------------------------------\n")
        logger.info("Database entries of table $tableName")

        getAllRecordsInDatabase().forEach { slave ->
            logger.info("${SlaveInformation.Ssid.name}: ${slave.ssid} || " +
                    "${SlaveInformation.MacAddress.name}: ${slave.macAddress} || " +
                    "${SlaveInformation.MacAddressTemporary.name}: ${slave.macAddressTemporary}")
        }

        logger.info("\n\n-------------------------------------------------------------")
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
    override fun deleteRecordWhere(field: String, value: String) = database.deleteRecordWhere(
            field = field,
            value = value)

    @Synchronized
    override fun getAllRecordsInDatabase(): List<Slave> {
        val slaves = database.getAllRecords()
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() = database.deleteAllRecords()

    @Synchronized
    override fun getNumberOfSlaves(): Int = database.getNumberOfRecords()

    @Synchronized
    override fun updateBySsid(ssid: Int, field: String, value: String) = database.updateBySsid(
            ssid = ssid,
            field = field,
            value = value)

    @Synchronized
    override fun updateInformation(slave: Slave) = database.updateInformation(slave = slave)

    @Synchronized
    override fun updateByMacAddress(macAddress: String, field: String, value: String) = database.updateByMacAddress(
            macAddress = macAddress,
            field = field,
            value = value)

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }

    @Synchronized
    override fun updateAllWhere(setField: String, setValue: String, whereField: String, whereValue: String) = database.updateAllWhere(
            setField = setField,
            setValue = setValue,
            whereField = whereField,
            whereValue = whereValue)

    @Synchronized
    override fun updatePosition(slave: Slave) = database.updateGeo(
            positionX = slave.positionX,
            positionY = slave.positionY,
            positionZ = slave.positionZ,
            rotationX = slave.rotationX,
            rotationY = slave.rotationY,
            rotationZ = slave.rotationZ,
            accelerationX = slave.accelerationX,
            accelerationY = slave.accelerationY,
            accelerationZ = slave.accelerationZ,
            macAddress = slave.macAddress)

    @Synchronized
    override fun updateRotating(isRotating: Boolean, ssid: Int) = database.updateRotating(isRotating = isRotating, ssid = ssid)

    @Synchronized
    override fun updateTimestamp(macAddress: String) = database.updateTimestamp(macAddress = macAddress)

    @Synchronized
    override fun getSlaveBySsid(ssid: Int): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.Ssid.name,
                value = ssid.toString())
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun getSlaveByEnabledRotation(): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.Rotating.name,
                value = true.toString())
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun getSlaveByMacAddress(macAddress: String): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.MacAddressTemporary.name,
                value = macAddress)
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun getSlaveByManufacturer(manufacturer: String): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.Manufacturer.name,
                value = manufacturer)
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun getSlaveByDevice(device: String): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.Device.name,
                value = device)
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun getSlaveByType(type: String): List<Slave> {
        val slaves = database.selectEntryWhere(
                field = SlaveInformation.Type.name,
                value = type)
        return createSlaveList(resultSet = slaves)
    }

    @Synchronized
    override fun checkIfSsidIsUsed(ssid: Int): Boolean = !getSlaveBySsid(ssid = ssid).isEmpty()
}