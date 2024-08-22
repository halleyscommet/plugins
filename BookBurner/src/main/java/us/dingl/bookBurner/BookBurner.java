package us.dingl.bookBurner;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.java.JavaPlugin;
import us.dingl.bookBurner.Commands.GiveShieldOfTheSMPCommand;
import us.dingl.bookBurner.Listeners.OnPlayerSendMessage;
import us.dingl.bookBurner.Listeners.ShiftRightClickWithShieldOfTheSMPInHandOrOffHandListener;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class BookBurner extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("BookBurner has been enabled!");

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

        // Make recipe
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "shield_of_the_smp"), shieldOfTheSMP);
        recipe.shape("eSe", "eEe", "eee");
        recipe.setIngredient('S', Material.SHIELD);
        recipe.setIngredient('e', Material.EMERALD);
        recipe.setIngredient('E', Material.ELYTRA);

        // Add recipe to server
        getServer().addRecipe(recipe);

        // Register commands
        Objects.requireNonNull(getCommand("giveshield")).setExecutor(new GiveShieldOfTheSMPCommand());

        // Register listeners
        getServer().getPluginManager().registerEvents(new ShiftRightClickWithShieldOfTheSMPInHandOrOffHandListener(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerSendMessage(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("BookBurner has been disabled!");
    }
}
