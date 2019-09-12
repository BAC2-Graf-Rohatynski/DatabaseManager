package databasemanager.database.image

import apibuilder.database.image.item.ImageItem
import apibuilder.database.interfaces.IItem
import databasemanager.database.Database
import databasemanager.database.image.interfaces.IImageDatabaseHandler
import enumstorage.database.DatabaseType
import enumstorage.image.Image
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object ImageDatabaseHandler: IImageDatabaseHandler {
    private var tableName = DatabaseType.Image.name
    private var database: Database = Database()
    private val logger: Logger = LoggerFactory.getLogger(ImageDatabaseHandler::class.java)

    init {
        try {
            logger.info("Starting '${DatabaseType.Image.name}' database handler ...")
            database.setTableName(tableName = tableName)
            database.connectToDatabase()

            if (database.checkWhetherTableExists()) {
                logger.info("Table $tableName not created yet")
                dropTable()
                createTable()
            }

            showEntries()
            logger.info("'${DatabaseType.Image.name}' database started")
        } catch (ex: Exception) {
            logger.error("Error occurred while running database handler!\n${ex.message}")
        }
    }

    @Synchronized
    override fun addItem(item: ImageItem) {
        logger.info("Adding item to table $tableName ...")
        val query = "INSERT INTO $tableName VALUES " +
                "('${item.id}', " +
                "'${item.imageDe}', " +
                "'${item.imageEn}', " +
                "'${item.isStandard}', " +
                "'${item.fileName}', " +
                "'${item.fileStream}')"
        database.addToDatabase(query = query)
        logger.info("Item added to table $tableName")
    }

    private fun createTable() {
        logger.info("Creating table $tableName ...")
        val query = "CREATE TABLE $tableName " +
                "(`${Image.Id.name}` Integer, " +
                "`${Image.ImageDe.name}` VARCHAR(255), " +
                "`${Image.ImageEn.name}` VARCHAR(255), " +
                "`${Image.IsStandard.name}` VARCHAR(255), " +
                "`${Image.FileName.name}` VARCHAR(255), " +
                "`${Image.FileStream.name}` VARCHAR(4096), " +
                "PRIMARY KEY (${Image.Id.name}))"

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
    override fun deleteRecordWhere(item: ImageItem) {
        logger.info("Deleting item ${item.id} ...")
        val items = database.getById(field = Image.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Image.IsStandard.name)) {
                return logger.warn("Standard item cannot be deleted!")
            }
        }

        database.deleteRecordWhere(
                field = Image.Id.name,
                value = item.id.toString())

        logger.info("Item ${item.id} deleted")
    }

    @Synchronized
    override fun getAllRecordsInDatabase(): JSONArray {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val jsonArray = JSONArray()

        while (resultSet.next()) {
            val item = ImageItem().build(
                    id = resultSet.getInt(Image.Id.name),
                    imageDe = resultSet.getString(Image.ImageDe.name),
                    imageEn = resultSet.getString(Image.ImageEn.name),
                    isStandard = resultSet.getBoolean(Image.IsStandard.name),
                    fileName = resultSet.getString(Image.FileName.name),
                    fileStream = resultSet.getBytes(Image.FileStream.name))

            jsonArray.put(item.toJson())
        }

        return jsonArray
    }

    private fun getAllRecords(): List<IItem> {
        logger.info("Returning all items ...")
        val resultSet = database.getAllRecords()
        val list = mutableListOf<IItem>()

        while (resultSet.next()) {
            val item = ImageItem().build(
                    id = resultSet.getInt(Image.Id.name),
                    imageEn = resultSet.getString(Image.ImageEn.name),
                    imageDe = resultSet.getString(Image.ImageDe.name),
                    isStandard = resultSet.getBoolean(Image.IsStandard.name),
                    fileName = resultSet.getString(Image.FileName.name),
                    fileStream = resultSet.getBytes(Image.FileStream.name))

            list.add(item)
        }

        return list
    }

    @Synchronized
    override fun deleteAllRecordsInDatabase() {
        logger.info("Deleted all non-default items ...")
        database.deleteRecordWhere(
                field = Image.IsStandard.name,
                value = false.toString()
        )
        logger.info("Items deleted")
    }

    @Synchronized
    override fun updateById(item: ImageItem) {
        logger.info("Changing item ${item.id} ...")
        val items = database.getById(field = Image.Id.name, id = item.id)

        while (items.next()) {
            if (items.getBoolean(Image.IsStandard.name)) {
                return logger.warn("Standard item cannot be changed!")
            }
        }

        database.updateAllWhere(
                whereField = Image.Id.name,
                whereValue = item.id.toString(),
                setField = Image.ImageDe.name,
                setValue = item.imageDe)

        database.updateAllWhere(
                whereField = Image.Id.name,
                whereValue = item.id.toString(),
                setField = Image.ImageEn.name,
                setValue = item.imageEn)

        database.updateAllWhere(
                whereField = Image.Id.name,
                whereValue = item.id.toString(),
                setField = Image.FileName.name,
                setValue = item.fileName)

        database.updateAllWhere(
                whereField = Image.Id.name,
                whereValue = item.id.toString(),
                setField = Image.FileStream.name,
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