package me.jordanfails.core

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import me.jordanfails.core.config.DatabaseConfig
import me.jordanfails.core.profile.ProfileHandler
import me.jordanfails.honey.DataHandler
import me.jordanfails.honey.connection.mongo.URIMongoConnectionPool
import org.bukkit.plugin.java.JavaPlugin

class PWCore : JavaPlugin() {

    companion object {
        lateinit var instance: PWCore
    }

    var dataHandler: DataHandler? = null
    var connectionPool: URIMongoConnectionPool? = null
    lateinit var gson: Gson

    override fun onEnable() {
        instance = this
        gson = GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .serializeNulls()
            .create()
        connectionPool = URIMongoConnectionPool().apply {
            this.databaseName = DatabaseConfig.Mongo.database.getString()
            this.uri = DatabaseConfig.Mongo.uri.getString()
        }
        ProfileHandler.loadIndexes()
        ProfileHandler.registerListeners()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
