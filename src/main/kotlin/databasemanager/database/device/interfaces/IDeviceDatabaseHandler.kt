package databasemanager.database.device.interfaces

import apibuilder.database.device.item.DeviceItem
import org.json.JSONArray

interface IDeviceDatabaseHandler {
    fun addItem(item: DeviceItem)
    fun deleteRecordWhere(item: DeviceItem)
    fun getAllRecordsInDatabase(): JSONArray
    fun deleteAllRecordsInDatabase()
    fun updateById(item: DeviceItem)
    fun dropTable()
    fun showEntries()
}