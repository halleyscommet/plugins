package us.dingl.bookBurner.Commands;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class GiveShieldOfTheSMPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission("bookburner.giveshield")) {
                // Make variables
                ItemStack shieldOfTheSMP = new ItemStack(Material.SHIELD);
                BlockStateMeta meta = (BlockStateMeta) shieldOfTheSMP.getItemMeta();
                Banner banner = (Banner) meta.getBlockState();
                List<Component> lore = new java.util.ArrayList<>(Collections.emptyList());

                // Set banner patterns
                banner.setBaseColor(DyeColor.BLACK);
                banner.addPattern(new Pattern(DyeColor.BLUE, PatternType.GRADIENT_UP));
                banner.addPattern(new Pattern(DyeColor.LIGHT_BLUE, PatternType.GRADIENT));
                banner.addPattern(new Pattern(DyeColor.CYAN, PatternType.CIRCLE));
                banner.addPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM));
                banner.addPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_DOWNRIGHT));
                banner.addPattern(new Pattern(DyeColor.RED, PatternType.STRIPE_TOP));
                banner.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
                banner.update();

                // Set item lore
                lore.add(Component.text(""));
                lore.add(Component.text("§a§oA shield that represents the SMP"));
                lore.add(Component.text(""));
                lore.add(Component.text("Sneak + Right Click to charge"));

                // Set item meta
                meta.displayName(Component.text("§6§lSHIELD OF THE SMP"));
                meta.addEnchant(Enchantment.MENDING, 1, true);
                meta.addEnchant(Enchantment.UNBREAKING, 3, true);
                meta.setBlockState(banner);
                meta.lore(lore);

                // Add meta to item
                shieldOfTheSMP.setItemMeta(meta);

                player.getInventory().addItem(shieldOfTheSMP);
            } else {
                player.sendMessage("You do not have permission to execute this command!");
            }
        } else {
            commandSender.sendMessage("Only players can execute this command!");
        }

        return false;
    }
}
