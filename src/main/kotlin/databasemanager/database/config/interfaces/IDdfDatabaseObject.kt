package databasemanager.database.config.interfaces

import databasemanager.database.config.DdfDatabaseHandler

interface IDdfDatabaseObject {
    fun getDatabaseByKey(show: String): DdfDatabaseHandler
    fun newDatabase(databaseName: String): Boolean
    fun renameDatabase(databaseName: String, newDatabaseName: String): Boolean
    fun deleteDatabase(databaseName: String): Boolean
    fun isShowAvailable(databaseName: String): Boolean
    fun getAllDatabasesKeys(): List<String>
    fun getAllDatabases(): List<DdfDatabaseHandler>
    fun clearDatabase(databaseName: String): Boolean
}