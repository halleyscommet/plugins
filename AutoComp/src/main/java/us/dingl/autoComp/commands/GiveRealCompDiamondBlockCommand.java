package us.dingl.autoComp.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveRealCompDiamondBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack compressedDiamondBlock = new ItemStack(Material.DIAMOND_BLOCK, 1);
            compressedDiamondBlock.getItemMeta();
            ItemMeta meta;

            meta = compressedDiamondBlock.getItemMeta();

            meta.displayName(Component.text("§e§lCompressed Diamond Block"));
            meta.setEnchantmentGlintOverride(true);

            compressedDiamondBlock.setItemMeta(meta);
            player.getInventory().addItem(compressedDiamondBlock);

            return true;
        }

        return false;
    }
}