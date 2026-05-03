package me.jordanfails.core.profile

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import me.jordanfails.core.PWCore
import me.jordanfails.core.profile.listener.ProfileListener
import me.jordanfails.honey.DataStoreType
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

object ProfileHandler {
    val handler = PWCore.instance.dataHandler!!.createStoreType<UUID, Profile>(DataStoreType.MONGO) {
        it.id = "profiles"
    }

    val collection = PWCore.instance.connectionPool!!.getCollection(handler.id)
    val cache = ConcurrentHashMap<UUID, Profile>()

    fun loadIndexes() {
        val indexes = listOf("lowercasedUsername")
        indexes.forEach {
            collection.createIndex(Indexes.descending(it))
        }
    }

    fun registerListeners() {
        PWCore.instance.server.pluginManager.registerEvents(ProfileListener, PWCore.instance)
//        PWCore.instance.server.pluginManager.registerEvents(PlayerDeathListener(), PWCore.instance)
    }

    fun getValues(): CompletableFuture<Collection<Profile>> {
        return handler.retrieveAllAsync()
    }

    fun byId(uuid: UUID): Profile {
        return cache.computeIfAbsent(uuid) {
            return@computeIfAbsent handler.retrieveAsync(uuid).get()!!
        }
    }

    fun save(profile: Profile) {
        cache[profile.uuid] = profile
        handler.store(profile.uuid, profile)
    }

    fun saveSync(profile: Profile) {
        cache[profile.uuid] = profile
        handler.storeAsync(profile.uuid, profile)
    }

    fun deleteProfile(uuid: UUID) {
        cache.remove(uuid)
        handler.deleteAsync(uuid)
    }

    fun byUsernameWithList(name: String): CompletableFuture<List<Profile>> {
        return CompletableFuture.supplyAsync {
            val cacheProfiles = this.cache.values.filter {
                it.name.equals(name, ignoreCase = true)
            }.toMutableList()

            val mongoProfiles = this.collection.find(
                Filters.eq("lowercasedUsername", name.lowercase())
            )

            for (doc in mongoProfiles) {
                val prof = PWCore.instance.gson!!.fromJson(
                    doc.toJson(),
                    Profile::class.java
                )
                if (cacheProfiles.none { it.uuid == prof.uuid }) {
                    cacheProfiles.add(prof)
                }
            }
            cacheProfiles
        }
    }

    fun loadProfile(uuid: UUID, username: String): Profile {
        val cachedProfile = cache[uuid]
        if (cachedProfile != null) {
            return cachedProfile
        }

        val retrievedProfile = handler.retrieve(uuid)

        return retrievedProfile ?: Profile(uuid, username)
    }

}