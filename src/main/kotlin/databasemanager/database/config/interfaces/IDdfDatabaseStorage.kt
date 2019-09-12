package databasemanager.database.config.interfaces

import java.sql.ResultSet

interface IDdfDatabaseStorage {
    fun addDatabase(databaseName: String): Boolean
    fun dropTable()
    fun deleteDdfDatabase(databaseName: String)
    fun getAllDdfDatabases(): ResultSet
    fun renameDdfDatabase(databaseName: String, newDatabaseName: String)
    fun enableDatabase(databaseName: String)
    fun getEnabledDatabase(): String
}