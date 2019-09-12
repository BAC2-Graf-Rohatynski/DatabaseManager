package databasemanager.handler

import apibuilder.database.header.Header
import apibuilder.database.response.ResponseItem
import databasemanager.clients.Clients
import databasemanager.handler.action.add.*
import databasemanager.handler.action.delete.*
import databasemanager.handler.action.get.*
import databasemanager.handler.action.save.SaveSlaveBackup
import databasemanager.handler.action.update.*
import databasemanager.handler.interfaces.ICommandHandler
import enumstorage.database.DatabaseCommand
import enumstorage.database.DatabaseType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object CommandHandler: ICommandHandler {
    private val logger: Logger = LoggerFactory.getLogger(CommandHandler::class.java)

    @Synchronized
    override fun parseMessage(header: Header, message: String) {
        val response: ResponseItem = try {
            when (header.table) {
                DatabaseType.Information.name -> handleInformationRequest(message = message, header = header)
                DatabaseType.Configuration.name -> handleConfigurationRequest(message = message, header = header)
                DatabaseType.Color.name -> handleColorRequest(message = message, header = header)
                DatabaseType.Device.name -> handleDeviceRequest(message = message, header = header)
                DatabaseType.Image.name -> handleImageRequest(message = message, header = header)
                DatabaseType.Manufacturer.name -> handleManufacturerRequest(message = message, header = header)
                DatabaseType.Type.name -> handleTypeRequest(message = message, header = header)
                DatabaseType.Gobo.name -> handleGoboRequest(message = message, header = header)
                DatabaseType.DdfDatabaseStorage.name -> handleShowRequest(message = message, header = header)
                else -> throw Exception("Invalid table '${header.table}' received!")
            }
        } catch (ex: Exception) {
            logger.error("Error occurred while parsing message!\n${ex.message}")
            ResponseItem().create(message = message, socketId = header.socketId)
        }

        sendResponse(response = response)
    }

    private fun handleInformationRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                    DatabaseCommand.AddSlave.name -> AddSlave.build(message = message).run()
                    DatabaseCommand.CheckIfSlaveIsAvailable.name -> CheckIfSlaveIsAvailable.build(message = message).run()
                    DatabaseCommand.DeleteSlave.name -> DeleteSlave.build(message = message).run()
                    DatabaseCommand.GetAllSlaves.name -> GetAllSlaves.build(message = message).run()
                    DatabaseCommand.GetGeoByMacAddress.name -> GetGeoByMacAddress.build(message = message).run()
                    DatabaseCommand.GetNewSsid.name -> GetNewSsid.build(message = message).run()
                    DatabaseCommand.GetNumberOfSlaves.name -> GetNumberOfSlaves.build(message = message).run()

                    DatabaseCommand.GetSlaveByDevice.name -> GetSlaveByDevice.build(message = message).run()
                    DatabaseCommand.GetSlaveByEnabledRotation.name -> GetSlaveByEnabledRotation.build(message = message).run()
                    DatabaseCommand.GetSlaveByMacAddress.name -> GetSlaveByMacAddress.build(message = message).run()
                    DatabaseCommand.GetSlaveByManufacturer.name -> GetSlaveByManufacturer.build(message = message).run()
                    DatabaseCommand.GetSlaveBySsid.name -> GetSlaveBySsid.build(message = message).run()
                    DatabaseCommand.GetSlaveByType.name -> GetSlaveByType.build(message = message).run()
                    DatabaseCommand.SaveSlaveBackup.name -> SaveSlaveBackup.build(message = message).run()

                    DatabaseCommand.UpdateGeo.name -> UpdateGeo.build(message = message).run()
                    DatabaseCommand.UpdateRotation.name -> UpdateRotation.build(message = message).run()
                    DatabaseCommand.UpdateAllSlavesWhere.name -> UpdateAllSlavesWhere.build(message = message).run()
                    DatabaseCommand.UpdateSlaveByMacAddress.name -> UpdateSlaveByMacAddress.build(message = message).run()
                    DatabaseCommand.UpdateSlaveBySsid.name -> UpdateSlaveBySsid.build(message = message).run()
                    DatabaseCommand.UpdateTimestamp.name -> UpdateTimestamp.build(message = message).run()
                DatabaseCommand.UpdateInformationBySsid.name -> UpdateInformation.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun handleConfigurationRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddConfiguration.name -> AddConfiguration.build(message = message).run()
                DatabaseCommand.DeleteAllConfigurations.name -> DeleteAllConfigurations.build(message = message).run()
                DatabaseCommand.DeleteConfiguration.name -> DeleteConfiguration.build(message = message).run()
                DatabaseCommand.GetAllConfigurations.name -> GetAllConfigurations.build(message = message).run()
                DatabaseCommand.GetConfigurationByDdfHash.name -> GetConfigurationByDdfHash.build(message = message).run()
                DatabaseCommand.GetConfigurationBySsid.name -> GetConfigurationBySsid.build(message = message).run()
                DatabaseCommand.UpdateConfigurationBySsid.name -> UpdateConfiguration.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun handleColorRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddColor.name -> AddColor.build(message = message).run()
                DatabaseCommand.DeleteAllColors.name -> DeleteAllColors.build(message = message).run()
                DatabaseCommand.DeleteColor.name -> DeleteColor.build(message = message).run()
                DatabaseCommand.GetAllColors.name -> GetAllColors.build(message = message).run()
                DatabaseCommand.UpdateColorById.name -> UpdateColor.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun handleDeviceRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddDevice.name -> AddDevice.build(message = message).run()
                DatabaseCommand.DeleteAllDevices.name -> DeleteAllDevices.build(message = message).run()
                DatabaseCommand.DeleteDevice.name -> DeleteDevice.build(message = message).run()
                DatabaseCommand.GetAllDevices.name -> GetAllDevices.build(message = message).run()
                DatabaseCommand.UpdateDeviceById.name -> UpdateDevice.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun handleImageRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddImage.name -> AddImage.build(message = message).run()
                DatabaseCommand.DeleteAllImages.name -> DeleteAllImages.build(message = message).run()
                DatabaseCommand.DeleteImage.name -> DeleteImage.build(message = message).run()
                DatabaseCommand.GetAllImages.name -> GetAllImages.build(message = message).run()
                DatabaseCommand.UpdateImageById.name -> UpdateImage.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun handleManufacturerRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddManufacturer.name -> AddManufacturer.build(message = message).run()
                DatabaseCommand.DeleteAllManufacturers.name -> DeleteAllManufacturers.build(message = message).run()
                DatabaseCommand.DeleteManufacturer.name -> DeleteManufacturer.build(message = message).run()
                DatabaseCommand.GetAllManufacturers.name -> GetAllManufacturers.build(message = message).run()
                DatabaseCommand.UpdateManufacturerById.name -> UpdateManufacturer.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun handleTypeRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddType.name -> AddType.build(message = message).run()
                DatabaseCommand.DeleteAllTypes.name -> DeleteAllTypes.build(message = message).run()
                DatabaseCommand.DeleteType.name -> DeleteType.build(message = message).run()
                DatabaseCommand.GetAllTypes.name -> GetAllTypes.build(message = message).run()
                DatabaseCommand.UpdateTypeById.name -> UpdateType.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }


    private fun handleGoboRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.AddGobo.name -> AddGobo.build(message = message).run()
                DatabaseCommand.DeleteAllGobos.name -> DeleteAllGobos.build(message = message).run()
                DatabaseCommand.DeleteGobo.name -> DeleteGobo.build(message = message).run()
                DatabaseCommand.GetAllGobos.name -> GetAllGobos.build(message = message).run()
                DatabaseCommand.UpdateGoboById.name -> UpdateGobo.build(message = message).run()
                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }


    private fun handleShowRequest(message: String, header: Header): ResponseItem {
        return try {
            val value = when (header.command) {
                DatabaseCommand.DeleteShow.name -> DeleteShow.build(message = message).run()
                DatabaseCommand.EnableShow.name -> EnableShow.build(message = message).run()
                DatabaseCommand.GetAllShows.name -> GetAllShows.build(message = message).run()
                DatabaseCommand.GetEnabledShow.name -> GetEnabledShow.build(message = message).run()
                DatabaseCommand.IsShowAvailable.name -> IsShowAvailable.build(message = message).run()
                DatabaseCommand.AddShow.name -> AddShow.build(message = message).run()
                DatabaseCommand.RenameShow.name -> RenameShow.build(message = message).run()

                else -> throw Exception("Invalid command '${header.command}' detected!")
            }

            ResponseItem().create(message = message, value = value, socketId = header.socketId)
        } catch (ex: Exception) {
            logger.error("Error while executing request '${header.toJson()}: ${ex.message}")
            ResponseItem().create(message = message, value = false, socketId = header.socketId)
        }
    }

    private fun sendResponse(response: ResponseItem) {
        try {
            if (response.isResponse) {
                return logger.info("Response won't be forwarded: ${response.messageBody}")
            }

            if (response.isGetMessage) {
                Clients.sendToSingleClient(response = response)
            } else {
                Clients.sendToAllClients(response = response)
            }
        } catch (ex: Exception) {
            logger.error("Error while sending response!\n${ex.message}")
        }
    }
}