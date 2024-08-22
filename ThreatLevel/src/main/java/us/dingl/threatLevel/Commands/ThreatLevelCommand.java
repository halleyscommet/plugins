package us.dingl.threatLevel.Commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import us.dingl.threatLevel.ThreatLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ThreatLevelCommand implements CommandExecutor, TabCompleter {

    private final ThreatLevel plugin;

    public ThreatLevelCommand(ThreatLevel plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        double newHealth;

        if (player.hasPermission("threatlevel.op")) {
            if (strings.length == 0) {
                player.sendMessage("§cUsage: /threatlevel <set/add/remove/optin/optout> <player> <level>");
            } else {
                switch (strings[0]) {
                    case "optin" -> {
                        if (strings.length == 2) {
                            Player target = plugin.getServer().getPlayer(strings[1]);

                            if (target != null) {
                                String targetName = target.getName();

                                plugin.setOptedIn(targetName, true);
                                player.sendMessage("§a" + target.getName() + " has been opted in.");
                            } else {
                                player.sendMessage("§cPlayer not found.");
                            }
                        } else {
                            player.sendMessage("§cUsage: /threatlevel optin <player>");
                        }
                    }
                    case "optout" -> {
                        if (strings.length == 2) {
                            Player target = plugin.getServer().getPlayer(strings[1]);

                            if (target != null) {
                                String targetName = target.getName();

                                plugin.setOptedIn(targetName, false);
                                player.sendMessage("§a" + target.getName() + " has been opted out.");
                            } else {
                                player.sendMessage("§cPlayer not found.");
                            }
                        } else {
                            player.sendMessage("§cUsage: /threatlevel optout <player>");
                        }
                    }
                    case "set" -> {
                        if (strings.length == 3) {
                            Player target = plugin.getServer().getPlayer(strings[1]);

                            if (target != null) {
                                String targetName = target.getName();
                                int level = Integer.parseInt(strings[2]);

                                plugin.setThreatLevel(targetName, level);
                                player.sendMessage("§a" + target.getName() + "'s threat level has been set to " + level + ".");

                                if (level > 0) {
                                    newHealth = 20 - level;
                                } else {
                                    newHealth = 20;
                                }
                                Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
                                target.sendHealthUpdate();
                            } else {
                                player.sendMessage("§cPlayer not found.");
                            }
                        } else {
                            player.sendMessage("§cUsage: /threatlevel set <player> <level>");
                        }
                    }
                    case "add" -> {
                        if (strings.length == 3) {
                            Player target = plugin.getServer().getPlayer(strings[1]);

                            if (target != null) {
                                String targetName = target.getName();
                                int level = Integer.parseInt(strings[2]);

                                int currentLevel = plugin.getThreatLevel(targetName);
                                plugin.setThreatLevel(targetName, currentLevel + level);
                                player.sendMessage("§a" + level + " has been added to " + target.getName() + "'s threat level.");

                                if (currentLevel + level > 0) {
                                    newHealth = 20 - (currentLevel + level);
                                } else {
                                    newHealth = 20;
                                }
                                Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
                                target.sendHealthUpdate();
                            } else {
                                player.sendMessage("§cPlayer not found.");
                            }
                        } else {
                            player.sendMessage("§cUsage: /threatlevel add <player> <level>");
                        }
                    }
                    case "remove" -> {
                        if (strings.length == 3) {
                            Player target = plugin.getServer().getPlayer(strings[1]);

                            if (target != null) {
                                String targetName = target.getName();
                                int level = Integer.parseInt(strings[2]);

                                int currentLevel = plugin.getThreatLevel(targetName);
                                plugin.setThreatLevel(targetName, currentLevel - level);
                                player.sendMessage("§a" + level + " has been removed from " + target.getName() + "'s threat level.");

                                if (currentLevel - level > 0) {
                                    newHealth = 20 - (currentLevel - level);
                                } else {
                                    newHealth = 20;
                                }
                                Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
                                target.sendHealthUpdate();
                            } else {
                                player.sendMessage("§cPlayer not found.");
                            }
                        } else {
                            player.sendMessage("§cUsage: /threatlevel remove <player> <level>");
                        }
                    }
                    default ->
                            player.sendMessage("§cUsage: /threatlevel <set/add/remove/optin/optout> <player> <level>");
                }
            }
        } else {
            commandSender.sendMessage("§cYou do not have permission to use this command.");
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("set", "add", "remove", "optin", "optout");
        } else if (args.length == 2) {
            List<String> playerNames = new ArrayList<>();
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                playerNames.add(player.getName());
            }
            return playerNames;
        } else if (args.length == 3 && (args[0].equals("set") || args[0].equals("add") || args[0].equals("remove"))) {
            return List.of("0", "1", "2", "3", "4", "5");
        } else {
            return List.of();
        }
    }
}