package us.dingl.threatLevel.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.dingl.threatLevel.ThreatLevel;

public class SetOptedOutCommand implements CommandExecutor {

    private final ThreatLevel plugin;

    public SetOptedOutCommand(ThreatLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            String playerName = player.getName();

            if (!plugin.isOptedIn(playerName)) {
                commandSender.sendMessage("§cYou are already opted out.");
            } else {
                if (plugin.getThreatLevel(playerName) > 0) {
                    commandSender.sendMessage("§cYou cannot opt out while you have a threat level.");
                } else {
                    plugin.setOptedIn(playerName, false);
                    commandSender.sendMessage("§aYou have been opted out.");
                }
            }
        } else {
            commandSender.sendMessage("§cYou must be a player to use this command.");
        }

        return false;
    }
}
