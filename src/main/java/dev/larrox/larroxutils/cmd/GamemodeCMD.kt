package dev.larrox.larroxutils.cmd

import dev.larrox.larroxutils.util.Prefix
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class GamemodeCMD : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<String>): Boolean {
        val prfx = Prefix.PREFIX

        if (sender !is Player && args.size < 2) {
            sender.sendMessage(prfx + "Du musst ein Spieler sein oder einen Spieler angeben, um diesen Command zu nutzen.")
            return true
        }

        if (args.size == 0) {
            sender.sendMessage(prfx + "Gebe einen Spielmodus an.")
            return true
        }

        val targetPlayer: Player?
        if (args.size == 1) {
            targetPlayer = sender as Player
        } else {
            targetPlayer = Bukkit.getPlayer(args[1])
            if (targetPlayer == null) {
                sender.sendMessage(prfx + "Spieler nicht gefunden.")
                return true
            }
        }

        val mode = args[0]
        var gameMode: GameMode? = null

        gameMode = when (mode.lowercase(Locale.getDefault())) {
            "0", "survival" -> GameMode.SURVIVAL
            "1", "creative" -> GameMode.CREATIVE
            "2", "adventure" -> GameMode.ADVENTURE
            "3", "spectator" -> GameMode.SPECTATOR
            else -> {
                sender.sendMessage(prfx + "Ungültiger Gamemode. Benutze: /gamemode <0/1/2/3> oder /gm <0/1/2/3> oder /gamemode <creative/survival/adventure/spectator>")
                return true
            }
        }
        targetPlayer.gameMode = gameMode
        targetPlayer.sendMessage(prfx + "Dein Spielmodus wurde auf §e" + gameMode.name.lowercase(Locale.getDefault()) + " §7gesetzt.")
        if (targetPlayer != sender) {
            sender.sendMessage(
                prfx + "Der Spielmodus von §e" + targetPlayer.name + " §7wurde auf §e" + gameMode.name.lowercase(
                    Locale.getDefault()
                ) + " §7gesetzt."
            )
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
            completions.add("0")
            completions.add("1")
            completions.add("2")
            completions.add("3")
            completions.add("survival")
            completions.add("creative")
            completions.add("adventure")
            completions.add("spectator")
            completions.add("§c§nWähle einen Spielmodus...")
        } else if (args.size == 2) {
            for (player in Bukkit.getOnlinePlayers()) {
                completions.add(player.name)
            }
        }
        return completions
    }
}