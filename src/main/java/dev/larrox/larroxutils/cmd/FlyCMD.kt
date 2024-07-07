package dev.larrox.larroxutils.cmd

import dev.larrox.larroxutils.util.Prefix
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FlyCMD : CommandExecutor {
    var prfx: String = Prefix.PREFIX

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.size == 0 && sender !is Player) {
            sender.sendMessage(prfx + "Dieser Befehl kann nur von einem Spieler ausgeführt werden.")
            return true
        }

        val target: Player?
        if (args.size == 0) {
            target = sender as Player
        } else {
            if (!sender.hasPermission("larrox.fly.other")) {
                sender.sendMessage(prfx + "Du hast keine Berechtigung, den Flugmodus für andere Spieler zu ändern.")
                return true
            }
            target = Bukkit.getPlayer(args[0])
            if (target == null) {
                sender.sendMessage(prfx + "Spieler " + args[0] + " wurde nicht gefunden.")
                return true
            }
        }

        val canFly = !target.allowFlight
        target.allowFlight = canFly
        target.isFlying = canFly

        if (target === sender) {
            sender.sendMessage(prfx + "Flugmodus " + (if (canFly) "§aaktiviert" else "§cdeaktiviert") + "§7.")
        } else {
            target.sendMessage(prfx + "Flugmodus " + (if (canFly) "§aaktiviert" else "§cdeaktiviert") + "§7 von §e" + sender.name + "§7.")
            sender.sendMessage(prfx + "Flugmodus " + (if (canFly) "§aaktiviert" else "§cdeaktiviert") + "§7 für §e" + target.name + "§7.")
        }

        return true
    }
}
