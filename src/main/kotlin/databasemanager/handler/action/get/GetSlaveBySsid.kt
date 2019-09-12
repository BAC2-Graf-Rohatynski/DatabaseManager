package databasemanager.handler.action.get

import databasemanager.database.info.InfoDatabaseHandler
import databasemanager.handler.interfaces.ICommandHandlerAction
import org.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import apibuilder.database.information.GetSlaveBySsidItem
import enumstorage.database.DatabaseCommand

object GetSlaveBySsid: ICommandHandlerAction {
    private val logger: Logger = LoggerFactory.getLogger(GetSlaveBySsid::class.java)
    private lateinit var item: GetSlaveBySsidItem

    @Synchronized
    override fun run(): Any {
        logger.info("Command '${DatabaseCommand.GetSlaveBySsid.name}' will be executed ...")
        val jsonArray = JSONArray()

        InfoDatabaseHandler.getSlaveBySsid(ssid = item.ssid).forEach { slave ->
            jsonArray.put(slave.toJson())
        }

        return jsonArray
    }

    @Synchronized
    override fun build(message: String): ICommandHandlerAction {
        item = GetSlaveBySsidItem().toObject(message = message)
        return this
    }
}