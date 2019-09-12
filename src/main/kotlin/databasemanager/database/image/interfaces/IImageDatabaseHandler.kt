package databasemanager.database.image.interfaces

import apibuilder.database.image.item.ImageItem
import org.json.JSONArray

interface IImageDatabaseHandler {
    fun addItem(item: ImageItem)
    fun deleteRecordWhere(item: ImageItem)
    fun getAllRecordsInDatabase(): JSONArray
    fun deleteAllRecordsInDatabase()
    fun updateById(item: ImageItem)
    fun dropTable()
    fun showEntries()
}