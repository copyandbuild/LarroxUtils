package dev.larrox.larroxutils.cmd

import dev.larrox.larroxutils.util.Prefix
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.BanList
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnbanCMD : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val prfx = Prefix.PREFIX

        if (sender !is Player) {
            sender.sendMessage(prfx + "This command can only be used by players.")
            return true
        }

        val player = sender

        if (args.size < 1) {
            sender.sendMessage(prfx + "You must specify a player.")
            return false
        }

        val playerName = args[0]
        val target = Bukkit.getPlayer(playerName)

        if (target != null) {
            var reason = "Kein Grund angegeben"
            if (args.size > 1) {
                val reasonBuilder = StringBuilder()
                for (i in 1 until args.size) {
                    reasonBuilder.append(args[i]).append(" ")
                }
                reason = reasonBuilder.toString().trim { it <= ' ' }
            }

            Bukkit.getBanList(BanList.Type.NAME).pardon(target.name)
            target.kickPlayer("$prfx\n\n§cDu wurdest gebannt!\n§6Grund: §7$reason")
            player.spigot()
                .sendMessage(ChatMessageType.ACTION_BAR, TextComponent("§a" + target.name + "§7 wurde gebannt."))
            Bukkit.getLogger().info(player.name + " hat " + target.name + " mit Grund " + reason + " gebannt.")
        } else {
            sender.sendMessage("$prfx§cSpieler wurde nicht gefunden: §a$playerName")
        }

        return true
    }
}