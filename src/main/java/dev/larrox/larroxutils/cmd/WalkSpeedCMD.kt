package dev.larrox.larroxutils.cmd

import dev.larrox.larroxutils.util.Prefix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class WalkSpeedCMD : CommandExecutor, TabCompleter {
    var prfx: String = Prefix.PREFIX

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 0 || args.size > 2) {
            sender.sendMessage(prfx + "Benutzung: /walkspeed <Zahl|reset> [Spieler]")
            return true
        }

        val target: Player?
        if (args.size == 1) {
            if (sender !is Player) {
                sender.sendMessage(prfx + "Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
                return true
            }
            target = sender
        } else {
            if (!sender.hasPermission("larrox.walkspeed.other")) {
                sender.sendMessage(prfx + "Du hast keine Berechtigung, die laufgeschwindigkeit für andere Spieler zu ändern.")
                return true
            }
            target = Bukkit.getPlayer(args[1])
            if (target == null) {
                sender.sendMessage(prfx + "§e" + args[1] + "§7 wurde nicht gefunden.")
                return true
            }
        }

        if (args[0].equals("reset", ignoreCase = true)) {
            target.walkSpeed = 0.2f
            if (target === sender) {
                sender.sendMessage(prfx + "Deine laufgeschwindigkeit wurde zurückgesetzt.")
            } else {
                target.sendMessage(prfx + "Deine laufgeschwindigkeit wurde von §e" + sender.name + "§7 zurückgesetzt.")
                sender.sendMessage(prfx + "laufgeschwindigkeit für §e" + target.name + "§7 wurde zurückgesetzt.")
            }
        } else {
            var speed: Float
            try {
                speed = args[0].toFloat()
            } catch (e: NumberFormatException) {
                sender.sendMessage(prfx + "Bitte gib eine gültige Zahl an.")
                return true
            }

            if (speed < 0 || speed > 10) {
                sender.sendMessage(prfx + "Die Geschwindigkeit muss zwischen 0 und 10 liegen.")
                return true
            }

            speed = speed / 10
            target.walkSpeed = speed

            if (target === sender) {
                sender.sendMessage(prfx + "laufgeschwindigkeit auf §e" + (speed * 10) + "§7 gesetzt.")
            } else {
                target.sendMessage(prfx + "Deine laufgeschwindigkeit wurde von §e" + sender.name + "§7 auf §e" + (speed * 10) + "§7 gesetzt.")
                sender.sendMessage(prfx + "laufgeschwindigkeit für §e" + target.name + "§7 auf §e" + (speed * 10) + "§7 gesetzt.")
            }
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        val completions: MutableList<String> = ArrayList()

        if (args.size == 1) {
            completions.add("reset")
            completions.add("1")
            completions.add("2")
            completions.add("10")

            val result: MutableList<String> = ArrayList()
            for (completion in completions) {
                if (completion.lowercase(Locale.getDefault())
                        .startsWith(args[args.size - 1].lowercase(Locale.getDefault()))
                ) {
                    result.add(completion)
                }
            }
            return result
        }
        return completions
    }
}
