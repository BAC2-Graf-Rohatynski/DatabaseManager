package databasemanager.database.type.interfaces

import apibuilder.database.type.item.TypeItem
import org.json.JSONArray

interface ITypeDatabaseHandler {
    fun addItem(item: TypeItem)
    fun deleteRecordWhere(item: TypeItem)
    fun getAllRecordsInDatabase(): JSONArray
    fun deleteAllRecordsInDatabase()
    fun updateById(item: TypeItem)
    fun dropTable()
    fun showEntries()
}