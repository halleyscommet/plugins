package us.dingl.autoComp.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class AutoCompPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (item.getType() == Material.ANVIL) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null && meta.hasDisplayName() && Objects.equals(meta.displayName(), Component.text("§6§lAUTOCOMPACTOR"))
                    && meta.hasLore() && Objects.requireNonNull(meta.lore()).contains(Component.text("Automatically compacts items in your inventory!"))
                    && meta.hasRarity() && meta.getRarity() == ItemRarity.EPIC
                    && meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Component.text("You cannot place the §6§lAUTOCOMPACTOR§r item!"));
            }
        }
    }
}