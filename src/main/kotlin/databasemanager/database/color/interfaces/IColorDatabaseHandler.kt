package databasemanager.database.color.interfaces

import apibuilder.database.color.item.ColorItem
import org.json.JSONArray

interface IColorDatabaseHandler {
    fun addItem(item: ColorItem)
    fun deleteRecordWhere(item: ColorItem)
    fun getAllRecordsInDatabase(): JSONArray
    fun deleteAllRecordsInDatabase()
    fun updateById(item: ColorItem)
    fun dropTable()
    fun showEntries()
}