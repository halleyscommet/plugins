package us.dingl.threatLevel.Listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public class CraftAttemptListener implements Listener {

    @EventHandler
    public void onCraftAttempt(CraftItemEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item != null && item.getType() == Material.DIAMOND_BLOCK) {
            CraftingInventory inventory = event.getInventory();
            for (ItemStack ingredient : inventory.getMatrix()) {
                if (isBloodDiamond(ingredient)) {
                    event.setCancelled(true);
                    player.sendMessage("§cYou cannot craft a diamond block using Blood Diamonds.");
                    break;
                }
            }
        }
    }

    private boolean isBloodDiamond(ItemStack item) {
        return item != null && item.getItemMeta() != null && Component.text("§4Blood Diamond").equals(item.getItemMeta().displayName());
    }
}