package us.dingl.autoComp.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveRealCompEmeraldBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack compressedEmeraldBlock = new ItemStack(Material.EMERALD_BLOCK, 1);
            compressedEmeraldBlock.getItemMeta();
            ItemMeta meta;

            meta = compressedEmeraldBlock.getItemMeta();

            meta.displayName(Component.text("§e§lCompressed Emerald Block"));
            meta.setEnchantmentGlintOverride(true);

            compressedEmeraldBlock.setItemMeta(meta);
            player.getInventory().addItem(compressedEmeraldBlock);

            return true;
        }

        return false;
    }
}