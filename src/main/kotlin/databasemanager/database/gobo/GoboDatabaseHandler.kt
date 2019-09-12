package databasemanager.database.gobo

import apibuilder.database.gobo.item.GoboItem
import apibuilder.database.interfaces.IItem
import databasemanager.database.Database
import databasemanager.database.gobo.interfaces.IGoboDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.gobo.Gobo
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object GoboDatabaseHandler: IGoboDatabaseHandler {
    private var tableName = DatabaseType.Gobo.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(GoboDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Gobo.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            showEntries()
            logger.info("'${DatabaseType.Gobo.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun addItem(item: GoboItem) {
        logger.info("Adding item to table $tableName ...")
        val query = "INSERT INTO $tableName VALUES " +
                "('${item.id}', " +
                "'${item.goboDe}', " +
                "'${item.goboDe}', " +
                "'${item.goboEn}', " +
                "'${item.fileName}', " +
                "'${item.fileStream}')"
        database.addToDatabase(query = query)
        logger.info("Item added to table $tableName")
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName " +
                "(`${Gobo.Id.name}` Integer, " +
                "`${Gobo.GoboDe.name}` VARCHAR(255), " +
                "`${Gobo.GoboEn.name}` VARCHAR(255), " +
                "`${Gobo.IsStandard.name}` VARCHAR(255), " +
                "`${Gobo.FileName.name}` VARCHAR(255), " +
                "`${Gobo.FileStream.name}` VARCHAR(4096), " +
                "PRIMARY KEY (${Gobo.Id.name}))"

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
    override fun deleteRecordWhere(item: GoboItem) {
        logger.info("Deleting item ${item.id} ...")
        val items = database.getById(field = Gobo.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Gobo.IsStandard.name)) {
                return logger.warn("Standard item cannot be deleted!")
            }
        }

        database.deleteRecordWhere(
                field = Gobo.Id.name,
                value = item.id.toString())

        logger.info("Item ${item.id} deleted")
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): JSONArray {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val jsonArray = JSONArray()

        while (resultSet.next()) {
            val item = GoboItem().build(
                    id = resultSet.getInt(Gobo.Id.name),
                    goboDe = resultSet.getString(Gobo.GoboDe.name),
                    goboEn = resultSet.getString(Gobo.GoboEn.name),
                    isStandard = resultSet.getBoolean(Gobo.IsStandard.name),
                    fileName = resultSet.getString(Gobo.FileName.name),
                    fileStream = resultSet.getBytes(Gobo.FileStream.name))

            jsonArray.put(item.toJson())
        }

        return jsonArray
    }

    private fun getAllRecords(): List<IItem> {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val list = mutableListOf<IItem>()

        while (resultSet.next()) {
            val item = GoboItem().build(
                    id = resultSet.getInt(Gobo.Id.name),
                    goboEn = resultSet.getString(Gobo.GoboEn.name),
                    goboDe = resultSet.getString(Gobo.GoboDe.name),
                    isStandard = resultSet.getBoolean(Gobo.IsStandard.name),
                    fileName = resultSet.getString(Gobo.FileName.name),
                    fileStream = resultSet.getBytes(Gobo.FileStream.name))

            list.add(item)
        }

        return list
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() {
        logger.info("Deleted all non-default items ...")
        database.deleteRecordWhere(
                field = Gobo.IsStandard.name,
                value = false.toString()
        )
        logger.info("Items deleted")
    }

    @Synchronized
    override fun updateById(item: GoboItem) {
        logger.info("Changing item ${item.id} ...")
        val items = database.getById(field = Gobo.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Gobo.IsStandard.name)) {
                return logger.warn("Standard item cannot be changed!")
            }
        }

        database.updateAllWhere(
                whereField = Gobo.Id.name,
                whereValue = item.id.toString(),
                setField = Gobo.GoboDe.name,
                setValue = item.goboDe)

        database.updateAllWhere(
                whereField = Gobo.Id.name,
                whereValue = item.id.toString(),
                setField = Gobo.GoboEn.name,
                setValue = item.goboEn)

        database.updateAllWhere(
                whereField = Gobo.Id.name,
                whereValue = item.id.toString(),
                setField = Gobo.FileName.name,
                setValue = item.fileName)

        database.updateAllWhere(
                whereField = Gobo.Id.name,
                whereValue = item.id.toString(),
                setField = Gobo.FileStream.name,
                setValue = item.fileStream.toString())

        logger.info("Item ${item.id} changed")
    }

    @Synchronized
    override fun dropTable() {
        logger.info("Dropping table ...")
        database.dropTable()
        logger.info("Table dropped")
    }
}