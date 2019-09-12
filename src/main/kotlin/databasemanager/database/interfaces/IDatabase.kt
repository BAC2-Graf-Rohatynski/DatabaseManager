package databasemanager.database.interfaces

import apibuilder.slave.Slave
import java.sql.ResultSet

interface IDatabase {
    fun setTableName(tableName: String)
    fun connectToDatabase()
    fun checkIfRecordExists(ssid: Int): Boolean
    fun checkWhetherTableExists(): Boolean
    fun addToDatabase(query: String): Boolean
    fun createTableInDatabase(query: String)
    fun updateBySsid(ssid: Int, field: String, value: String)
    fun updateByMacAddress(macAddress: String, field: String, value: String)
    fun dropTable()
    fun updateAll(field: String, value: String)
    fun updateAllWhere(setField: String, setValue: String, whereField: String, whereValue: String)
    fun updateGeo(positionX: Int, positionY: Int, positionZ: Int, rotationX: Int, rotationY: Int, rotationZ: Int,
                  accelerationX: Int, accelerationY: Int, accelerationZ: Int, macAddress: String)
    fun updateConfiguration(ddfhHash: String, ddfFile: String, ssid: Int)
    fun updateConfigurationSsid(ssid: Int)
    fun updateInformation(slave: Slave)
    fun updateRotating(isRotating: Boolean, ssid: Int)
    fun updateTimestamp(macAddress: String)
    fun getNumberOfRecords(): Int
    fun deleteRecordWhere(field: String, value: String)
    fun getAllRecords(): ResultSet
    fun deleteAllRecords()
    fun selectEntryWhere(field: String, value: String): ResultSet
    fun renameTable(newTableName: String)
    fun getById(field: String, id: Int): ResultSet
}