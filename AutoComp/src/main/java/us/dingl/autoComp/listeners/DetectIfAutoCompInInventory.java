package us.dingl.autoComp.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.dingl.autoComp.AutoComp;

import java.util.Objects;

public class DetectIfAutoCompInInventory implements Listener {

    private final AutoComp plugin;

    public DetectIfAutoCompInInventory(AutoComp plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCrouch(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (plugin.isOptedIn(player.getName()) && hasAutoCompItem(player.getInventory())) {
            for (ItemStack item : player.getInventory().getContents()) {
                if (item != null && item.getType() == Material.DIAMOND) {
                    int diamondCount = item.getAmount();
                    int blocksToAdd = diamondCount / 9;
                    int diamondsToRemove = blocksToAdd * 9;

                    if (blocksToAdd > 0) {
                        item.setAmount(diamondCount - diamondsToRemove);
                        player.getInventory().addItem(new ItemStack(Material.DIAMOND_BLOCK, blocksToAdd));
                    }
                }

                if (item != null && item.getType() == Material.EMERALD) {
                    int emeraldCount = item.getAmount();
                    int blocksToAdd = emeraldCount / 9;
                    int emeraldsToRemove = blocksToAdd * 9;

                    if (blocksToAdd > 0) {
                        item.setAmount(emeraldCount - emeraldsToRemove);
                        player.getInventory().addItem(new ItemStack(Material.EMERALD_BLOCK, blocksToAdd));
                    }
                }

                if (item != null && item.getType() == Material.RAW_IRON) {
                    int ironCount = item.getAmount();
                    int blocksToAdd = ironCount / 9;
                    int ironToRemove = blocksToAdd * 9;

                    if (blocksToAdd > 0) {
                        item.setAmount(ironCount - ironToRemove);
                        player.getInventory().addItem(new ItemStack(Material.IRON_BLOCK, blocksToAdd));
                    }
                }

                if (item != null && item.getType() == Material.RAW_GOLD) {
                    int goldCount = item.getAmount();
                    int blocksToAdd = goldCount / 9;
                    int goldToRemove = blocksToAdd * 9;

                    if (blocksToAdd > 0) {
                        item.setAmount(goldCount - goldToRemove);
                        player.getInventory().addItem(new ItemStack(Material.GOLD_BLOCK, blocksToAdd));
                    }
                }

                if (item != null && item.getType() == Material.LAPIS_LAZULI) {
                    int lapisCount = item.getAmount();
                    int blocksToAdd = lapisCount / 9;
                    int lapisToRemove = blocksToAdd * 9;

                    if (blocksToAdd > 0) {
                        item.setAmount(lapisCount - lapisToRemove);
                        player.getInventory().addItem(new ItemStack(Material.LAPIS_BLOCK, blocksToAdd));
                    }
                }

                if (item != null && item.getType() == Material.DIAMOND_BLOCK) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && Objects.equals(meta.displayName(), Component.text("§e§lCompressed Diamond Block"))) {
                        continue;
                    }

                    int diamondBlockCount = item.getAmount();

                    if (diamondBlockCount == 64) {
                        item.setAmount(0);

                        ItemStack compressedDiamondBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);
                        meta = compressedDiamondBlock.getItemMeta();

                        meta.displayName(Component.text("§e§lCompressed Diamond Block"));
                        meta.setEnchantmentGlintOverride(true);

                        compressedDiamondBlock.setItemMeta(meta);
                        player.getInventory().addItem(compressedDiamondBlock);
                    }
                }

                if (item != null && item.getType() == Material.EMERALD_BLOCK) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null && meta.hasDisplayName() && Objects.equals(meta.displayName(), Component.text("§e§lCompressed Emerald Block"))) {
                        continue;
                    }

                    int emeraldBlockCount = item.getAmount();

                    if (emeraldBlockCount == 64) {
                        item.setAmount(0);

                        ItemStack compressedDiamondBlock = new ItemStack(Material.EMERALD_BLOCK, 1);
                        meta = compressedDiamondBlock.getItemMeta();

                        meta.displayName(Component.text("§e§lCompressed Emerald Block"));
                        meta.setEnchantmentGlintOverride(true);

                        compressedDiamondBlock.setItemMeta(meta);
                        player.getInventory().addItem(compressedDiamondBlock);
                    }
                }
            }
        }
    }

    private boolean hasAutoCompItem(Inventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.ANVIL) {
                ItemMeta meta = item.getItemMeta();

                if (meta != null && meta.hasDisplayName() && Objects.equals(meta.displayName(), Component.text("§6§lAUTOCOMPACTOR"))
                        && meta.hasLore() && Objects.requireNonNull(meta.lore()).contains(Component.text("Automatically compacts items in your inventory!"))
                        && meta.hasRarity() && meta.getRarity() == ItemRarity.EPIC
                        && meta.hasEnchantmentGlintOverride() && meta.getEnchantmentGlintOverride()) {
                    return true;
                }
            }
        }
        return false;
    }
}
