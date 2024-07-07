package dev.larrox.larroxutils.cmd

import dev.larrox.larroxutils.Main
import dev.larrox.larroxutils.util.Prefix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class LarroxCMD : CommandExecutor, TabCompleter {
    private val prfx = Prefix.PREFIX

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 1 && "reload".equals(args[0], ignoreCase = true)) {
            if (sender.hasPermission("Larrox.help")) {
                JavaPlugin.getPlugin(Main::class.java).reloadConfig()
                sender.sendMessage(prfx + "Larrox-Utils neugeladen.")
            } else {
                sender.sendMessage(prfx + "Du hast keine Berechtigung für diesen Befehl.")
            }
            return true
        }

        if (args.size >= 2 && "server".equals(args[0], ignoreCase = true)) {
            if (sender.hasPermission("Larrox.admin")) {
                val subCommand = args[1].lowercase(Locale.getDefault())
                when (subCommand) {
                    "stop" -> {
                        sender.sendMessage(prfx + "Server wird gestoppt...")
                        Bukkit.shutdown()
                    }

                    "restart" -> {
                        sender.sendMessage(prfx + "Server wird neu gestartet...")
                        Bukkit.spigot().restart()
                    }

                    "reloadwhitelist" -> {
                        sender.sendMessage(prfx + "Whitelist wird neu geladen...")
                        Bukkit.reloadWhitelist()
                        sender.sendMessage(prfx + "Whitelist wurde erfolgreich neu geladen.")
                    }

                    "reloaddata" -> {
                        sender.sendMessage(prfx + "Serverdaten werden neu geladen...")
                        Bukkit.reloadData()
                        sender.sendMessage(prfx + "Serverdaten wurden erfolgreich neu geladen.")
                    }

                    "reload" -> {
                        sender.sendMessage(prfx + "Server wird neugeladen...")
                        val player = sender as Player

                        val currentgamemode = player.gameMode
                        Bukkit.reload()

                        for (player1 in Bukkit.getOnlinePlayers()) {
                            if (player1.isOp) {
                                sender.sendMessage(prfx + "Server wird neugeladen...")
                            }
                        }
                        sender.sendMessage(prfx + "Server wurde erfolgreich neu geladen.")
                    }

                    else -> sender.sendMessage(prfx + "Unbekannter Befehl für /larrox server.")
                }
            } else {
                sender.sendMessage(prfx + "Du hast keine Berechtigung für diesen Befehl.")
            }
            return true
        }

        return false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val completions: MutableList<String> = ArrayList()
        if (args.size == 1) {
            completions.add("reload")
            completions.add("server")
        } else if (args.size == 2 && "server".equals(
                args[0],
                ignoreCase = true
            ) && sender.hasPermission("Larrox.admin")
        ) {
            completions.add("stop")
            completions.add("restart")
            completions.add("reloadwhitelist")
            completions.add("reloaddata")
            completions.add("reload")
        }

        val result: MutableList<String> = ArrayList()
        val argument = args[args.size - 1].lowercase(Locale.getDefault())
        for (completion in completions) {
            if (completion.lowercase(Locale.getDefault()).startsWith(argument)) {
                result.add(completion)
            }
        }
        return result
    }
}
