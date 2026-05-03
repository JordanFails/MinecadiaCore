package me.jordanfails.core.auctionhouse.data

import me.jordanfails.unify.utils.Expirable
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class AuctionHouseItem(
    val createdAt: Long = System.currentTimeMillis(),
    val _item: String?,
    var creator: UUID,
    var startingPrice: Double = 0.0,
    var expirable: Expirable = Expirable(
        createdAt,
        12.hours
    ),
    var auctionId: UUID = UUID.randomUUID(),
    var bids: MutableList<Bid> = mutableListOf(),
    var removed: Boolean = false,
    var soldPrice: Double = 0.0,
    var removedBy: UUID? = null,
    var removedReason: String? = null,
    var staffEdits: MutableList<StaffEdit> = mutableListOf()
) {

    constructor(item: ItemStack, creator: UUID, amount: Double) : this (
        _item = ItemSerializer.serialize(item),
        creator = creator,
        startingPrice = amount
    )

    constructor(item: ItemStack, creator: UUID, expirable: Expirable): this (
        _item = ItemSerializer.serialize(item),
        creator = creator,
        expirable = expirable
    )

    fun expiresIn(): String {
        val currentTime = System.currentTimeMillis()
        val expirationTime = expirable.addedAt + expirable.duration

        return if (currentTime >= expirationTime) {
            "Expired"
        } else {
            val remainingMillis = expirationTime - currentTime
            val duration = Duration.ofMillis(remainingMillis)

            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60

            if (hours > 0) {
                TimeUtil.formatDuration(duration.toMillis())
            } else {
                TimeUtil.formatDuration(duration.toMillis())
            }
        }
    }

    fun addStaffEdit(player: Player, reason: String, edit: AuctionChange) {
        staffEdits.add(
            StaffEdit(
                staff = player.uniqueId,
                reason = reason,
                change = edit
            )
        )
    }

    fun getItem(): ItemStack {
        val deserialized = ItemSerializer.deserialize(this._item) ?: throw IllegalStateException("Failed to deserialize item for auction $auctionId")
        return deserialized
    }

    fun shouldBeVisible(): Boolean {
        return !removed && expirable.isActive()
    }

}