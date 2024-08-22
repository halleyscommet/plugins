package us.dingl.threatLevel.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.dingl.threatLevel.ThreatLevel;

import java.util.ArrayList;
import java.util.List;

public class CheckThreatLevelCommand implements CommandExecutor, TabCompleter {

    private final ThreatLevel plugin;

    public CheckThreatLevelCommand(ThreatLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 0) {
            commandSender.sendMessage("§aYour threat level is: §r " + plugin.getThreatLevel(commandSender.getName()));
            return true;
        } else if (strings.length == 1) {
            commandSender.sendMessage("§a" + strings[0] + "'s threat level is: §r " + plugin.getThreatLevel(strings[0]));
            return true;
        } else {
            commandSender.sendMessage("§cUsage: /checkthreatlevel <player>");
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        }

        return List.of();
    }
}
