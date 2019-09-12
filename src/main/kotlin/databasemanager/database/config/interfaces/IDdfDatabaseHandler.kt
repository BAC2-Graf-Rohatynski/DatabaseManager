package databasemanager.database.config.interfaces

import apibuilder.slave.Slave

interface IDdfDatabaseHandler {
    fun addConfig(ssid: Int, ddfHash: String, ddfFile: String): Boolean
    fun renameTable(newDatabaseName: String)
    fun dropTable()
    fun showEntries()
    fun deleteRecordWhere(field: String, value: String)
    fun getAllRecordsInDatabase(): List<Slave>
    fun deleteAllRecordsInDatabase()
    fun getConfigurationBySsid(ssid: Int): List<Slave>
    fun getConfigurationByDdfHash(ddfHash: String): List<Slave>
    fun updateConfiguration(ssid: Int, ddfHash: String, ddfFile: String)
    fun updateConfigurationSsid(ssid: Int)
}