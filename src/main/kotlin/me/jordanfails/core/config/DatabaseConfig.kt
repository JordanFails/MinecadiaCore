package me.jordanfails.core.config

import me.jordanfails.core.PWCore
import me.jordanfails.unify.config.AbstractConfig
import me.jordanfails.unify.config.Config
import me.jordanfails.unify.config.ConfigValue
import sun.security.provider.ConfigFile

object DatabaseConfig {
    val config = Config(PWCore.instance, "database")
    object Mongo {
        val database = ConfigValue("mongo.database", "", config)
        val uri      = ConfigValue("mongo.uri", "", config)
    }
}