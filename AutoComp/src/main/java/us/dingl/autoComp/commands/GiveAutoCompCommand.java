package us.dingl.autoComp.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;


public class GiveAutoCompCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack AutoComp = new ItemStack(Material.ANVIL);
            ItemMeta meta = AutoComp.getItemMeta();

            List<Component> lore = new java.util.ArrayList<>();
            lore.add(Component.text("Automatically compacts items in your inventory!"));
            lore.add(Component.text("Sneak to compact"));

            meta.displayName(Component.text("§6§lAUTOCOMPACTOR"));
            meta.setRarity(ItemRarity.EPIC);
            meta.lore(lore);
            meta.setEnchantmentGlintOverride(true);

            AutoComp.setItemMeta(meta);

            player.getInventory().addItem(AutoComp);

            return true;
        }

        return false;
    }
}