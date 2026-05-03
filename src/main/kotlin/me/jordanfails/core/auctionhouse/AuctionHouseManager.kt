package me.jordanfails.finallycustom.auctionhouse

import com.mongodb.client.model.Indexes
import me.jordanfails.finallycustom.FinallyCustom
import me.jordanfails.core.auctionhouse.data.AuctionHouseItem
import me.jordanfails.honey.DataStoreType
import java.util.*
import java.util.concurrent.CompletableFuture

object AHManager {
    private val handler = FinallyCustom.instance.dataHandler.createStoreType<UUID, AuctionHouseItem>(DataStoreType.MONGO) {
        it.id = "auctionhouse_map_1"
    }
    val collection = FinallyCustom.instance.connectionPool!!.getCollection("auctionhouse_map_1")
    var cache = mutableMapOf<UUID, AuctionHouseItem>()

    fun getValues(): CompletableFuture<Collection<AuctionHouseItem>> {
        return handler.retrieveAllAsync()
    }

    fun addItem(item: AuctionHouseItem) {
        cache[item.auctionId] = item
    }

    fun byId(uuid: UUID): AuctionHouseItem? {
        return handler.retrieveAsync(uuid).get()
    }

    fun save(item: AuctionHouseItem) {
        cache[item.auctionId] = item
        handler.store(item.auctionId, item)
    }

    fun saveSync(item: AuctionHouseItem) {
        cache[item.auctionId] = item
        handler.store(item.auctionId, item)
    }

    fun delete(item: AuctionHouseItem) {
        handler.delete(item.auctionId)
    }


    fun getActiveItems(uuid: UUID): List<AuctionHouseItem> {
        val items = getValues().get().filter {
            it.creator == uuid && (!it.removed)
        }

        return items
    }

    fun loadIndexes() {
        val fields = listOf("auctionId")

        for (f in fields) {
            collection.createIndex(Indexes.descending(f))
        }
    }

}