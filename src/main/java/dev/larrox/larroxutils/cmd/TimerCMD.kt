package dev.larrox.larroxutils.cmd

import dev.larrox.larroxutils.Main
import dev.larrox.larroxutils.util.Prefix
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import java.util.*

class TimerCMD : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val prfx = Prefix.PREFIX
        val timer = Main.instance?.timer

        if (timer == null) {
            sender.sendMessage("${ChatColor.RED}$prfx Timer is not initialized.")
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        when (args[0].lowercase(Locale.getDefault())) {
            "resume" -> {
                if (timer.isRunning) {
                    sender.sendMessage("${ChatColor.RED}$prfx Der Timer läuft bereits.")
                    return true
                }
                timer.isRunning = true
                val message = if (timer.time != 0) {
                    "${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§7 fortgesetzt."
                } else {
                    "${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§7 gestartet."
                }
                Bukkit.broadcastMessage(message)
            }

            "pause" -> {
                if (!timer.isRunning) {
                    sender.sendMessage("${ChatColor.RED}$prfx Der Timer läuft nicht.")
                    return true
                }
                timer.isRunning = false
                timer.save()
                Bukkit.broadcastMessage("${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§7 pausiert.")
            }

            "settime" -> {
                if (args.size != 2) {
                    sender.sendMessage(
                        "${ChatColor.GRAY}$prfx Verwendung${ChatColor.DARK_GRAY}: " +
                                "§6/timer §8settime §f<Zeit>"
                    )
                    return true
                }

                try {
                    val time = args[1].toInt()
                    timer.isRunning = false
                    timer.time = time
                    val message = if (time != 1) {
                        "${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§f auf §6§l${time}§f Sekunden gesetzt."
                    } else {
                        "${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§f auf §6§l${time}§f Sekunde gesetzt."
                    }
                    Bukkit.broadcastMessage(message)
                    timer.isRunning = true
                } catch (e: NumberFormatException) {
                    sender.sendMessage("${ChatColor.RED}$prfx Dein Parameter 2 muss eine Zahl sein.")
                }
            }

            "reset" -> {
                timer.isRunning = false
                timer.time = 0
                Bukkit.broadcastMessage("${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§7 zurückgesetzt.")
            }

            "hide" -> {
                if (timer.isRunning) {
                    sender.sendMessage("${prfx} Der Timer kann während er läuft nicht ausgeblendet werden.")
                    return true
                }

                if (timer.isTimerHidden) {
                    sender.sendMessage("${prfx} Der Timer wird bereits versteckt.")
                } else {
                    timer.removeActionbar()
                    timer.isTimerHidden = true
                    Bukkit.broadcastMessage("${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§7 ausgeblendet.")
                }
            }

            "show" -> {
                if (!timer.isTimerHidden) {
                    sender.sendMessage("${prfx} Der Timer wird bereits eingeblendet.")
                } else {
                    timer.isTimerHidden = false
                    timer.sendActionBar()
                    Bukkit.broadcastMessage("${ChatColor.GRAY}$prfx Der Timer wurde von §f§l${sender.name}§7 eingeblendet.")
                }
            }

            "help" -> sendUsage(sender)
            else -> sendUsage(sender)
        }
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        val prfx = Prefix.PREFIX
        sender.sendMessage(
            """${ChatColor.GRAY}${prfx}Verwendung${ChatColor.DARK_GRAY}: ${ChatColor.GOLD}
 /timer §8resume §7- Timer Fortsetzen/Starten
 §6/timer §8pause §7- Timer Pausieren
 §6/timer §8settime §f<Zeit in sec> §7- Setze die Zeit auf die angegebene Zeit
 §6/timer §8reset §7- Setze den Timer Zurück
 §6/timer §8show §7- Zeige den Timer
 §6/timer §8hide §7- Verstecke den Timer
 §6/timer §8help §7- Zeige das help Menü"""
        )
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val completions: MutableList<String> = ArrayList()

        if (args.size == 1) {
            completions.addAll(listOf("resume", "pause", "settime", "reset", "hide", "show", "help"))
        } else if (args.size == 2 && args[0].equals("settime", ignoreCase = true)) {
            completions.addAll(listOf("120", "60", "${ChatColor.UNDERLINE}§cZeit in Sekunden"))
        }

        return completions.filter { it.startsWith(args.last(), ignoreCase = true) }
    }
}
