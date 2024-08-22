package us.dingl.threatLevel.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import us.dingl.threatLevel.ThreatLevel;

public class SetOptedInCommand implements CommandExecutor {

    private final ThreatLevel plugin;

    public SetOptedInCommand(ThreatLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            String playerName = player.getName();

            if (plugin.isOptedIn(playerName)) {
                commandSender.sendMessage("You are already opted in.");
            } else {
                plugin.setOptedIn(playerName, true);
                commandSender.sendMessage("You have been opted in.");
            }
        } else {
            commandSender.sendMessage("You must be a player to use this command.");
        }

        return false;
    }
}
