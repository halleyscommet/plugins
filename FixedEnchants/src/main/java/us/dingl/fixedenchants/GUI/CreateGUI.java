package us.dingl.fixedenchants.GUI;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import us.dingl.fixedenchants.FixedEnchants;

import static org.bukkit.Bukkit.createInventory;

public class CreateGUI {

    private final FixedEnchants plugin;

    public CreateGUI(FixedEnchants plugin) {
        this.plugin = plugin;
    }

    public void createGUI(Player player, String title, int size) {
        Inventory inventory = createInventory(player, size, Component.text(title));

        ItemStack[] contents = new GetUIContents(plugin).getUIContents("enchant_table").toArray(new ItemStack[0]);
        inventory.setContents(contents);

        player.openInventory(inventory);

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new GUIListener(inventory), plugin);
    }
}