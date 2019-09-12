package databasemanager.database.color

import apibuilder.database.color.item.ColorItem
import apibuilder.database.interfaces.IItem
import databasemanager.database.Database
import databasemanager.database.color.interfaces.IColorDatabaseHandler
import enumstorage.color.Color
import enumstorage.database.DatabaseType
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object ColorDatabaseHandler: IColorDatabaseHandler {
    private var tableName = DatabaseType.Color.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(ColorDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Color.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            showEntries()
            logger.info("'${DatabaseType.Color.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun addItem(item: ColorItem) {
        logger.info("Adding item to table $tableName ...")
        val query = "INSERT INTO $tableName VALUES " +
                "('${item.id}', " +
                "'${item.colorDe}', " +
                "'${item.colorEn}', " +
                "'${item.isStandard}', " +
                "'${item.hex}')"
        database.addToDatabase(query = query)
        logger.info("Item added to table $tableName")
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName " +
                "(`${Color.Id.name}` Integer, " +
                "`${Color.ColorDe.name}` VARCHAR(255), " +
                "`${Color.ColorEn.name}` VARCHAR(255), " +
                "`${Color.IsStandard.name}` VARCHAR(255), " +
                "`${Color.Hex.name}` VARCHAR(255), " +
                "PRIMARY KEY (${Color.Id.name}))"

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
    override fun deleteRecordWhere(item: ColorItem) {
        logger.info("Deleting item ${item.id} ...")
        val items = database.getById(field = Color.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Color.IsStandard.name)) {
                return logger.warn("Standard item cannot be deleted!")
            }
        }

        database.deleteRecordWhere(
                field = Color.Id.name,
                value = item.id.toString())

        logger.info("Item ${item.id} deleted")
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): JSONArray {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val jsonArray = JSONArray()

        while (resultSet.next()) {
            val item = ColorItem().build(
                    id = resultSet.getInt(Color.Id.name),
                    colorDe = resultSet.getString(Color.ColorDe.name),
                    colorEn = resultSet.getString(Color.ColorEn.name),
                    isStandard = resultSet.getBoolean(Color.IsStandard.name),
                    hex = resultSet.getString(Color.Hex.name))

            jsonArray.put(item.toJson())
        }

        return jsonArray
    }

    private fun getAllRecords(): List<IItem> {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val list = mutableListOf<IItem>()

        while (resultSet.next()) {
            val item = ColorItem().build(
                    id = resultSet.getInt(Color.Id.name),
                    colorEn = resultSet.getString(Color.ColorEn.name),
                    colorDe = resultSet.getString(Color.ColorDe.name),
                    isStandard = resultSet.getBoolean(Color.IsStandard.name),
                    hex = resultSet.getString(Color.Hex.name))

            list.add(item)
        }

        return list
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() {
        logger.info("Deleted all non-default items ...")
        database.deleteRecordWhere(
                field = Color.IsStandard.name,
                value = false.toString()
        )
        logger.info("Items deleted")
    }

    @Synchronized
    override fun updateById(item: ColorItem) {
        logger.info("Changing item ${item.id} ...")
        val items = database.getById(field = Color.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Color.IsStandard.name)) {
                return logger.warn("Standard item cannot be changed!")
            }
        }

        database.updateAllWhere(
                whereField = Color.Id.name,
                whereValue = item.id.toString(),
                setField = Color.ColorDe.name,
                setValue = item.colorDe)

        database.updateAllWhere(
                whereField = Color.Id.name,
                whereValue = item.id.toString(),
                setField = Color.ColorEn.name,
                setValue = item.colorEn)

        database.updateAllWhere(
                whereField = Color.Id.name,
                whereValue = item.id.toString(),
                setField = Color.Hex.name,
                setValue = item.hex)

        logger.info("Item ${item.id} changed")
    }

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }
}