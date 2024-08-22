package us.dingl.threatLevel.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.dingl.threatLevel.ThreatLevel;

import java.util.List;

public class SetWarTimeCommand implements CommandExecutor, TabCompleter {

    private final ThreatLevel plugin;

    public SetWarTimeCommand(ThreatLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("threatlevel.op")) {
            if (strings.length == 0) {
                if (plugin.isWarTime()) {
                    plugin.setWarTime(false);
                    commandSender.sendMessage("§aWar time has been set to false.");
                } else {
                    plugin.setWarTime(true);
                    commandSender.sendMessage("§aWar time has been set to true.");
                }
            } else {
                if (strings[0].equalsIgnoreCase("true")) {
                    if (plugin.isWarTime()) {
                        commandSender.sendMessage("§cWar time is already set to true.");
                    } else {
                        plugin.setWarTime(true);
                        commandSender.sendMessage("§aWar time has been set to true.");
                    }
                } else if (strings[0].equalsIgnoreCase("false")) {
                    if (!plugin.isWarTime()) {
                        commandSender.sendMessage("§cWar time is already set to false.");
                    } else {
                        plugin.setWarTime(false);
                        commandSender.sendMessage("§aWar time has been set to false.");
                    }
                } else {
                    commandSender.sendMessage("§cUsage: /setwartime <true/false>");
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return List.of("true", "false");
        } else {
            return List.of();
        }
    }
}
