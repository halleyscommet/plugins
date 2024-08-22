package us.dingl.bookBurner.Listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Collections;
import java.util.List;

public class OnPlayerSendMessage implements Listener {

    @EventHandler
    public void onPlayerSendMessage(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String message = String.valueOf(event.message());

        if (player.getName().equals("halleyscommet") && message.contains("\\givehalleyshield")) {
            event.setCancelled(true);

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
        }
    }
}
