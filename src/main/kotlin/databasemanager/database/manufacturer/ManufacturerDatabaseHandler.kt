package databasemanager.database.manufacturer

import apibuilder.database.manufacturer.item.ManufacturerItem
import apibuilder.database.interfaces.IItem
import databasemanager.database.Database
import databasemanager.database.manufacturer.interfaces.IManufacturerDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.manufacturer.Manufacturer
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object ManufacturerDatabaseHandler: IManufacturerDatabaseHandler {
    private var tableName = DatabaseType.Manufacturer.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(ManufacturerDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Manufacturer.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            showEntries()
            logger.info("'${DatabaseType.Manufacturer.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun addItem(item: ManufacturerItem) {
        logger.info("Adding item to table $tableName ...")
        val query = "INSERT INTO $tableName VALUES " +
                "('${item.id}', " +
                "'${item.manufacturerDe}', " +
                "'${item.manufacturerEn}', " +
                "'${item.isStandard}')"
        database.addToDatabase(query = query)
        logger.info("Item added to table $tableName")
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName " +
                "(`${Manufacturer.Id.name}` Integer, " +
                "`${Manufacturer.ManufacturerDe.name}` VARCHAR(255), " +
                "`${Manufacturer.ManufacturerEn.name}` VARCHAR(255), " +
                "`${Manufacturer.IsStandard.name}` VARCHAR(255), " +
                "PRIMARY KEY (${Manufacturer.Id.name}))"

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
    override fun deleteRecordWhere(item: ManufacturerItem) {
        logger.info("Deleting item ${item.id} ...")
        val items = database.getById(field = Manufacturer.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Manufacturer.IsStandard.name)) {
                return logger.warn("Standard item cannot be deleted!")
            }
        }

        database.deleteRecordWhere(
                field = Manufacturer.Id.name,
                value = item.id.toString())

        logger.info("Item ${item.id} deleted")
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): JSONArray {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val jsonArray = JSONArray()

        while (resultSet.next()) {
            val item = ManufacturerItem().build(
                    id = resultSet.getInt(Manufacturer.Id.name),
                    manufacturerDe = resultSet.getString(Manufacturer.ManufacturerDe.name),
                    manufacturerEn = resultSet.getString(Manufacturer.ManufacturerEn.name),
                    isStandard = resultSet.getBoolean(Manufacturer.IsStandard.name))

            jsonArray.put(item.toJson())
        }

        return jsonArray
    }

    private fun getAllRecords(): List<IItem> {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val list = mutableListOf<IItem>()

        while (resultSet.next()) {
            val item = ManufacturerItem().build(
                    id = resultSet.getInt(Manufacturer.Id.name),
                    manufacturerDe = resultSet.getString(Manufacturer.ManufacturerDe.name),
                    manufacturerEn = resultSet.getString(Manufacturer.ManufacturerEn.name),
                    isStandard = resultSet.getBoolean(Manufacturer.IsStandard.name))

            list.add(item)
        }

        return list
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() {
        logger.info("Deleted all non-default items ...")
        database.deleteRecordWhere(
                field = Manufacturer.IsStandard.name,
                value = false.toString()
        )
        logger.info("Items deleted")
    }

    @Synchronized
    override fun updateById(item: ManufacturerItem) {
        logger.info("Changing item ${item.id} ...")
        val items = database.getById(field = Manufacturer.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Manufacturer.IsStandard.name)) {
                return logger.warn("Standard item cannot be changed!")
            }
        }

        database.updateAllWhere(
                whereField = Manufacturer.Id.name,
                whereValue = item.id.toString(),
                setField = Manufacturer.ManufacturerDe.name,
                setValue = item.manufacturerDe)

        database.updateAllWhere(
                whereField = Manufacturer.Id.name,
                whereValue = item.id.toString(),
                setField = Manufacturer.ManufacturerEn.name,
                setValue = item.manufacturerEn)

        logger.info("Item ${item.id} changed")
    }

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }
}