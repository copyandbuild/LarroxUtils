package dev.larrox.larroxutils.util

import dev.larrox.larroxutils.Main
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class Timer {
    var isRunning: Boolean = false
    var time: Int = 0
    var isTimerHidden: Boolean = false

    init {
        val config = JavaPlugin.getPlugin(Main::class.java).configuration
        this.time = config?.config?.getInt("timer.time") ?: 0
        run()
    }

    fun sendActionBar() {
        for (player in Bukkit.getOnlinePlayers()) {
            if (!isRunning) {
                player.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR, TextComponent(
                        "${ChatColor.GRAY}Der ${ChatColor.GREEN}Timer §7ist${ChatColor.RED} pausiert§7."
                    )
                )
                continue
            }

            val hours = time / 3600
            val minutes = (time % 3600) / 60
            val seconds = time % 60

            val timeString = String.format("%02dh %02dm %02ds", hours, minutes, seconds)
            player.spigot().sendMessage(
                ChatMessageType.ACTION_BAR, TextComponent(
                    "${ChatColor.DARK_AQUA}${ChatColor.BOLD}$timeString"
                )
            )
        }
    }

    fun removeActionbar() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(""))
        }
        isTimerHidden = true
    }

    private fun run() {
        object : BukkitRunnable() {
            override fun run() {
                if (!isTimerHidden) {
                    sendActionBar()
                }

                if (isRunning) {
                    time += 1
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Main::class.java), 20, 20)
    }

    fun save() {
        val config = JavaPlugin.getPlugin(Main::class.java).configuration
        config?.config?.set("timer.time", time)
    }
}
