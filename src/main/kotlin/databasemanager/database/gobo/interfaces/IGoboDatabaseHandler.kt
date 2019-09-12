package databasemanager.database.gobo.interfaces

import apibuilder.database.gobo.item.GoboItem
import org.json.JSONArray

interface IGoboDatabaseHandler {
    fun addItem(item: GoboItem)
    fun deleteRecordWhere(item: GoboItem)
    fun getAllRecordsInDatabase(): JSONArray
    fun deleteAllRecordsInDatabase()
    fun updateById(item: GoboItem)
    fun dropTable()
    fun showEntries()
}