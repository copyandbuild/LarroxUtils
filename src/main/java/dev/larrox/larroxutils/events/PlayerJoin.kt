package dev.larrox.larroxutils.events

import dev.larrox.larroxutils.util.Timer
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin : Listener {
    private val timer = Timer()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = "§8[§a+§8] §7" + event.player.name + "§7"

        if (!(timer.isTimerHidden)) {
            timer.removeActionbar()
            timer.isTimerHidden = true
        }
    }
}
