package us.dingl.fixedenchants.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.dingl.fixedenchants.FixedEnchants;

public class StartWebServerCommand implements CommandExecutor {

    private final FixedEnchants plugin;

    public StartWebServerCommand(FixedEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("fixedenchants.admin")) {
            if (plugin.webServerHandler.isRunning()) {
                commandSender.sendMessage("§cWeb server is already running!");
            } else {
                try {
                    plugin.webServerHandler.start();
                    commandSender.sendMessage("§aWeb server started successfully!");
                } catch (Exception e) {
                    commandSender.sendMessage("§cFailed to start web server: " + e.getMessage());
                }
            }
        } else {
            commandSender.sendMessage("§cYou do not have permission to use this command!");
        }

        return true;
    }
}
