package databasemanager.database.type

import apibuilder.database.type.item.TypeItem
import apibuilder.database.interfaces.IItem
import databasemanager.database.Database
import databasemanager.database.type.interfaces.ITypeDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.type.Type
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object TypeDatabaseHandler: ITypeDatabaseHandler {
    private var tableName = DatabaseType.Type.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(TypeDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Type.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            showEntries()
            logger.info("'${DatabaseType.Type.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun addItem(item: TypeItem) {
        logger.info("Adding item to table $tableName ...")
        val query = "INSERT INTO $tableName VALUES " +
                "('${item.id}', " +
                "'${item.typeDe}', " +
                "'${item.typeEn}', " +
                "'${item.isStandard}')"
        database.addToDatabase(query = query)
        logger.info("Item added to table $tableName")
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName " +
                "(`${Type.Id.name}` Integer, " +
                "`${Type.TypeDe.name}` VARCHAR(255), " +
                "`${Type.TypeEn.name}` VARCHAR(255), " +
                "`${Type.IsStandard.name}` VARCHAR(255), " +
                "PRIMARY KEY (${Type.Id.name}))"

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
    override fun deleteRecordWhere(item: TypeItem) {
        logger.info("Deleting item ${item.id} ...")
        val items = database.getById(field = Type.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Type.IsStandard.name)) {
                return logger.warn("Standard item cannot be deleted!")
            }
        }

        database.deleteRecordWhere(
                field = Type.Id.name,
                value = item.id.toString())

        logger.info("Item ${item.id} deleted")
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): JSONArray {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val jsonArray = JSONArray()

        while (resultSet.next()) {
            val item = TypeItem().build(
                    id = resultSet.getInt(Type.Id.name),
                    typeDe = resultSet.getString(Type.TypeDe.name),
                    typeEn = resultSet.getString(Type.TypeEn.name),
                    isStandard = resultSet.getBoolean(Type.IsStandard.name))

            jsonArray.put(item.toJson())
        }

        return jsonArray
    }

    private fun getAllRecords(): List<IItem> {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val list = mutableListOf<IItem>()

        while (resultSet.next()) {
            val item = TypeItem().build(
                    id = resultSet.getInt(Type.Id.name),
                    typeDe = resultSet.getString(Type.TypeDe.name),
                    typeEn = resultSet.getString(Type.TypeEn.name),
                    isStandard = resultSet.getBoolean(Type.IsStandard.name))

            list.add(item)
        }

        return list
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() {
        logger.info("Deleted all non-default items ...")
        database.deleteRecordWhere(
                field = Type.IsStandard.name,
                value = false.toString()
        )
        logger.info("Items deleted")
    }

    @Synchronized
    override fun updateById(item: TypeItem) {
        logger.info("Changing item ${item.id} ...")
        val items = database.getById(field = Type.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Type.IsStandard.name)) {
                return logger.warn("Standard item cannot be changed!")
            }
        }

        database.updateAllWhere(
                whereField = Type.Id.name,
                whereValue = item.id.toString(),
                setField = Type.TypeDe.name,
                setValue = item.typeDe)

        database.updateAllWhere(
                whereField = Type.Id.name,
                whereValue = item.id.toString(),
                setField = Type.TypeEn.name,
                setValue = item.typeEn)

        logger.info("Item ${item.id} changed")
    }

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }
}