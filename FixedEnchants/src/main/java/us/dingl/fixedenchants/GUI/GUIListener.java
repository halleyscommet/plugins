package us.dingl.fixedenchants.GUI;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class GUIListener implements Listener {

    private final Inventory guiInventory;

    public GUIListener(Inventory guiInventory) {
        this.guiInventory = guiInventory;
    }

    GUIManager guiManager = new GUIManager(null);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().equals(guiInventory)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().equals(guiInventory)) {
            event.getInventory().clear();
            guiManager.removeGUIByInventory(guiInventory);
        }
    }
}