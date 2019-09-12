package databasemanager.database.info.interfaces

import apibuilder.slave.Slave

interface IInfoBackupDatabaseHandler {
    fun getAllRecordsInDatabase(): List<Slave>
    fun addSlave(slave: Slave): Boolean
    fun deleteAllRecordsInDatabase()
    fun dropTable()
}