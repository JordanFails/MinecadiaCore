package me.jordanfails.core.profile.listener

import me.jordanfails.core.profile.ProfileHandler
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent


object ProfileListener : Listener {
    @EventHandler
    fun onAsyncPreJoin(event: AsyncPlayerPreLoginEvent) {
        val start = System.currentTimeMillis()
        if(event.loginResult != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return
        }

        val profile = ProfileHandler.loadProfile(event.uniqueId, event.name)
//        val auctionProfile = AuctionProfileHandler.loadProfile(event.uniqueId, event.name)
//        val minigameProfile = MinigameProfileHandler.loadProfile(event.uniqueId, event.name)

        Bukkit.getLogger().info(
            "Loaded Finally Custom profile for ${event.name} in ${System.currentTimeMillis() - start}ms"
        )

        profile.name = event.name
//        auctionProfile.name = event.name
//        minigameProfile.name = event.name
//        if(profile.canLMS == null) {
//            profile.canLMS = true
//        }
//        if(profile.playTimeSessions == null) {
//            profile.playTimeSessions = mutableListOf()
//        }

//        profile.addNewSession()
        ProfileHandler.save(profile)
//        AuctionProfileHandler.save(auctionProfile)
//        MinigameProfileHandler.save(minigameProfile)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
//        val ahProfile = event.player.getAuctionProfile() ?: throw IllegalStateException("Auction profile for ${event.player.name} is null on join.")
//        if(ahProfile.unclaimedAHItems!!.isNotEmpty()) {
//            event.player.sendMessage(CC.translate("&a&lAuction House &8➼ &fYou have &a${ahProfile.unclaimedAHItems!!.size} &funclaimed auction item${if(ahProfile.unclaimedAHItems!!.size == 1) "" else "s"}."))
//        }
//        EEquip.clearPlayer(event.player)
//        EEquip.loadPlayer(event.player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val profile = ProfileHandler.byId(event.player.uniqueId)
//        val auctionProfile = event.player.getAuctionProfile() ?: return
//        val minigameProfile = event.player.getMinigameProfile() ?: return
//        val currentSession = profile.getMostRecentPlaytimeSession()
//        currentSession?.finished = System.currentTimeMillis()
        ProfileHandler.save(profile)
//        AuctionProfileHandler.save(auctionProfile)
    }
}