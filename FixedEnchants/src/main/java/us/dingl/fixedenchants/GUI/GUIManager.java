package us.dingl.fixedenchants.GUI;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.dingl.fixedenchants.FixedEnchants;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {

    private final FixedEnchants plugin;
    private final Map<String, Inventory> guis;

    public GUIManager(FixedEnchants plugin) {
        this.plugin = plugin;
        this.guis = new HashMap<>();
    }

    public void createGUI(String id, Player player, String title, int size) {
        CreateGUI createGUI = new CreateGUI(plugin);
        createGUI.createGUI(player, title, size);
        guis.put(id, player.getOpenInventory().getTopInventory());
    }

    public Inventory getGUI(String id) {
        return guis.get(id);
    }

    public Map<String, Inventory> getGUIs() {
        return guis;
    }

    public void removeGUIByID(String id) {
        guis.remove(id);
    }

    public void removeGUIByInventory(Inventory inventory) {
        guis.values().remove(inventory);
    }
}