package us.dingl.fixedenchants.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import us.dingl.fixedenchants.FixedEnchants;

public class StopWebServerCommand implements CommandExecutor {

    private final FixedEnchants plugin;

    public StopWebServerCommand(FixedEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("fixedenchants.admin")) {
            if (!plugin.webServerHandler.isRunning()) {
                commandSender.sendMessage("§cWeb server is not running!");
            } else {
                try {
                    plugin.webServerHandler.stop();
                    commandSender.sendMessage("§aWeb server stopped successfully!");
                } catch (Exception e) {
                    commandSender.sendMessage("§cFailed to stop web server: " + e.getMessage());
                }
            }
        } else {
            commandSender.sendMessage("§cYou do not have permission to use this command!");
        }

        return true;
    }
}
