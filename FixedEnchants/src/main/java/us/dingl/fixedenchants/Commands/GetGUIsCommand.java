package us.dingl.fixedenchants.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import us.dingl.fixedenchants.FixedEnchants;
import us.dingl.fixedenchants.GUI.GUIManager;

import java.util.Map;

public class GetGUIsCommand implements CommandExecutor {

    private final FixedEnchants plugin;

    public GetGUIsCommand(FixedEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender.hasPermission("fixedenchants.getguis")) {
            GUIManager guiManager = new GUIManager(plugin);

            Map<String, Inventory> guis = guiManager.getGUIs();

            commandSender.sendMessage("GUIs:");
            for (String key : guis.keySet()) {
                commandSender.sendMessage(key);
            }

            plugin.getLogger().info("GUIs:\n" + guis.keySet());
        } else {
            commandSender.sendMessage("You do not have permission to use this command.");
        }

        return true;
    }
}
