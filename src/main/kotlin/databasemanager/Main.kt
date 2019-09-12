package databasemanager

import org.apache.log4j.BasicConfigurator

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    DatabaseManagerRunner.start()
}