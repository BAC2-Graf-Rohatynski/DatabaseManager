package databasemanager.database.device

import apibuilder.database.device.item.DeviceItem
import apibuilder.database.interfaces.IItem
import databasemanager.database.Database
import databasemanager.database.device.interfaces.IDeviceDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.device.Device
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object DeviceDatabaseHandler: IDeviceDatabaseHandler {
    private var tableName = DatabaseType.Device.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(DeviceDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Device.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            showEntries()
            logger.info("'${DatabaseType.Device.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun addItem(item: DeviceItem) {
        logger.info("Adding item to table $tableName ...")
        val query = "INSERT INTO $tableName VALUES " +
                "('${item.id}', " +
                "'${item.deviceDe}', " +
                "'${item.deviceEn}', " +
                "'${item.isStandard}')"
        database.addToDatabase(query = query)
        logger.info("Item added to table $tableName")
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName " +
                "(`${Device.Id.name}` Integer, " +
                "`${Device.DeviceDe.name}` VARCHAR(255), " +
                "`${Device.DeviceEn.name}` VARCHAR(255), " +
                "`${Device.IsStandard.name}` VARCHAR(255), " +
                "PRIMARY KEY (${Device.Id.name}))"

        database.createTableInDatabase(query = query)
        logger.info("Table $tableName created")
    }

    @Synchronized
    override fun showEntries() {
        logger.info("\n-------------------------------------------------------------\n")
        logger.info("Database entries of table $tableName")

        getAllRecords().forEach { item ->
            logger.info(item.toJson().toString())
        }

        logger.info("\n\n-------------------------------------------------------------")
    }

    @Synchronized
    override fun deleteRecordWhere(item: DeviceItem) {
        logger.info("Deleting item ${item.id} ...")
        val items = database.getById(field = Device.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Device.IsStandard.name)) {
                return logger.warn("Standard item cannot be deleted!")
            }
        }

        database.deleteRecordWhere(
                field = Device.Id.name,
                value = item.id.toString())

        logger.info("Item ${item.id} deleted")
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): JSONArray {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val jsonArray = JSONArray()

        while (resultSet.next()) {
            val item = DeviceItem().build(
                    id = resultSet.getInt(Device.Id.name),
                    deviceDe = resultSet.getString(Device.DeviceDe.name),
                    deviceEn = resultSet.getString(Device.DeviceEn.name),
                    isStandard = resultSet.getBoolean(Device.IsStandard.name))

            jsonArray.put(item.toJson())
        }

        return jsonArray
    }

    private fun getAllRecords(): List<IItem> {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val list = mutableListOf<IItem>()

        while (resultSet.next()) {
            val item = DeviceItem().build(
                    id = resultSet.getInt(Device.Id.name),
                    deviceEn = resultSet.getString(Device.DeviceEn.name),
                    deviceDe = resultSet.getString(Device.DeviceDe.name),
                    isStandard = resultSet.getBoolean(Device.IsStandard.name))

            list.add(item)
        }

        return list
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() {
        logger.info("Deleted all non-default items ...")
        database.deleteRecordWhere(
                field = Device.IsStandard.name,
                value = false.toString()
        )
        logger.info("Items deleted")
    }

    @Synchronized
    override fun updateById(item: DeviceItem) {
        logger.info("Changing item ${item.id} ...")
        val items = database.getById(field = Device.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Device.IsStandard.name)) {
                return logger.warn("Standard item cannot be changed!")
            }
        }

        database.updateAllWhere(
                whereField = Device.Id.name,
                whereValue = item.id.toString(),
                setField = Device.DeviceDe.name,
                setValue = item.deviceDe)

        database.updateAllWhere(
                whereField = Device.Id.name,
                whereValue = item.id.toString(),
                setField = Device.DeviceEn.name,
                setValue = item.deviceEn)

        logger.info("Item ${item.id} changed")
    }

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }
}