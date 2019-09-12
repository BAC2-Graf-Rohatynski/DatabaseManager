package databasemanager.database.manufacturer.interfaces

import apibuilder.database.manufacturer.item.ManufacturerItem
import org.json.JSONArray

interface IManufacturerDatabaseHandler {
    fun addItem(item: ManufacturerItem)
    fun deleteRecordWhere(item: ManufacturerItem)
    fun getAllRecordsInDatabase(): JSONArray
    fun deleteAllRecordsInDatabase()
    fun updateById(item: ManufacturerItem)
    fun dropTable()
    fun showEntries()
}