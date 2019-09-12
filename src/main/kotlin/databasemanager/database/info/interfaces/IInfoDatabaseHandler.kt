package databasemanager.database.info.interfaces

import apibuilder.slave.Slave

interface IInfoDatabaseHandler {
    fun checkIfSlaveIsAlreadyAdded(ssid: Int): Boolean
    fun getNewSsid(): Int
    fun showEnries()
    fun addSlave(slave: Slave): Boolean
    fun deleteRecordWhere(field: String, value: String)
    fun getAllRecordsInDatabase(): List<Slave>
    fun deleteAllRecordsInDatabase()
    fun getNumberOfSlaves(): Int
    fun updateBySsid(ssid: Int, field: String, value: String)
    fun updateByMacAddress(macAddress: String, field: String, value: String)
    fun updateInformation(slave: Slave)
    fun dropTable()
    fun updateAllWhere(setField: String, setValue: String, whereField: String, whereValue: String)
    fun updatePosition(slave: Slave)
    fun updateRotating(isRotating: Boolean, ssid: Int)
    fun updateTimestamp(macAddress: String)
    fun getSlaveBySsid(ssid: Int): List<Slave>
    fun getSlaveByEnabledRotation(): List<Slave>
    fun getSlaveByMacAddress(macAddress: String): List<Slave>
    fun getSlaveByManufacturer(manufacturer: String): List<Slave>
    fun getSlaveByDevice(device: String): List<Slave>
    fun getSlaveByType(type: String): List<Slave>
    fun checkIfSsidIsUsed(ssid: Int): Boolean
}