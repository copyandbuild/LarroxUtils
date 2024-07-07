package dev.larrox.larroxutils

import dev.larrox.larroxutils.cmd.*
import dev.larrox.larroxutils.events.PlayerDeath
import dev.larrox.larroxutils.events.PlayerJoin
import dev.larrox.larroxutils.events.PlayerQuit
import dev.larrox.larroxutils.util.Config
import dev.larrox.larroxutils.util.Prefix
import dev.larrox.larroxutils.util.Timer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    var timer: Timer? = null
    var configuration: Config? = null
        private set
    var prfx: String = Prefix.PREFIX

    override fun onLoad() {
        instance = this
        configuration = Config()
    }

    override fun onEnable() {
        saveDefaultConfig()

        val console = Bukkit.getConsoleSender()

        console.sendMessage(ChatColor.RED.toString() + "${prfx} §7Activated!")
        val manager = Bukkit.getPluginManager()

        manager.registerEvents(PlayerDeath(), this)
        manager.registerEvents(PlayerQuit(), this)
        manager.registerEvents(PlayerJoin(), this)

        getCommand("timer")?.setExecutor(TimerCMD())
        getCommand("gm")?.setExecutor(GamemodeCMD())
        getCommand("larrox")?.setExecutor(LarroxCMD())
        getCommand("fly")?.setExecutor(FlyCMD())
        getCommand("flyspeed")?.setExecutor(FlySpeedCMD())
        getCommand("walkspeed")?.setExecutor(WalkSpeedCMD())
        getCommand("kick")?.setExecutor(KickPlayerCMD())
        getCommand("ban")?.setExecutor(BanCMD())
        timer = Timer()
    }

    override fun onDisable() {
        timer?.save()
        configuration?.save()

        val console = Bukkit.getConsoleSender()

        console.sendMessage(ChatColor.RED.toString() + "${prfx} §7Deactivated!")
    }

    companion object {
        var instance: Main? = null
            private set
    }
}