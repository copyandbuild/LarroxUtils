package dev.larrox.larroxutils.events

import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeath : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val killer = player.killer

        if (killer != null) {
            event.deathMessage = "§8[§c💀§8] §a" + player.name + "§7 wurde von §c" + killer.name + "§7 getötet."
            killer.health = 20.0
        } else {
            event.deathMessage = "§8[§c💀§8] §a" + player.name + "§7 ist gestorben."
        }
    }
}
