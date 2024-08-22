package us.dingl.autoComp.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.dingl.autoComp.AutoComp;

import java.util.Collections;
import java.util.Objects;

public class AutoCompRightClickListener implements Listener {

    private final AutoComp plugin;

    public AutoCompRightClickListener(AutoComp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            ItemStack item = event.getItem();
            if (item != null && item.getType() == Material.ANVIL) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.hasDisplayName() && Objects.equals(meta.displayName(), Component.text("§6§lAUTOCOMPACTOR"))
                        && meta.hasLore() && Objects.requireNonNull(meta.lore()).contains(Component.text("Automatically compacts items in your inventory!"))
                        && meta.hasRarity() && meta.getRarity() == ItemRarity.EPIC
                        && meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride()) {
                    event.setCancelled(true);

                    Player player = event.getPlayer();
                    Inventory inventory = Bukkit.createInventory(player, 9, Component.text("§6§lAUTOCOMPACTOR§r Menu"));

                    ItemStack pane = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                    ItemMeta paneMeta = pane.getItemMeta();
                    paneMeta.displayName(Component.text("Enable §6§lAUTOCOMPACTOR"));
                    if (plugin.isOptedIn(player.getName())) {
                        pane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                        paneMeta.displayName(Component.text("Disable §6§lAUTOCOMPACTOR"));
                    }

                    paneMeta.lore(Collections.singletonList(Component.text("Click to toggle §6§lAUTOCOMPACTOR§r.")));

                    pane.setItemMeta(paneMeta);

                    inventory.setItem(4, pane);
                    player.openInventory(inventory);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(Component.text("§6§lAUTOCOMPACTOR§r Menu"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && (event.getCurrentItem().getType() == Material.LIME_STAINED_GLASS_PANE || event.getCurrentItem().getType() == Material.RED_STAINED_GLASS_PANE)) {
                Player player = (Player) event.getWhoClicked();
                boolean isOptedIn = plugin.isOptedIn(player.getName());
                plugin.setOptedIn(player.getName(), !isOptedIn);
                player.closeInventory();
            }
        }
    }
}