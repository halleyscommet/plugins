package us.dingl.threatLevel.Listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import us.dingl.threatLevel.ThreatLevel;

import java.util.List;
import java.util.Objects;

import static us.dingl.threatLevel.ItemStackSplitter.splitItemStack;

public class PlayerDeathListener implements Listener {

    private final ThreatLevel plugin;

    public PlayerDeathListener(ThreatLevel plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        String victimName = victim.getName();
        int victimThreatLevel = plugin.getThreatLevel(victimName);
        List<ItemStack> bountyItems = getItemStacks(victimThreatLevel);
        double newHealth;

        if (victimThreatLevel > 0) {
            newHealth = 20 - victimThreatLevel;
        } else {
            newHealth = 20;
        }
        Objects.requireNonNull(victim.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
        victim.sendHealthUpdate();

        if (victim.getKiller() instanceof Player killer) {
            String killerName = killer.getName();
            int killerThreatLevel = plugin.getThreatLevel(killerName);

            if (killerThreatLevel > 0) {
                newHealth = 20 - killerThreatLevel;
            } else {
                newHealth = 20;
            }
            Objects.requireNonNull(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
            killer.sendHealthUpdate();

            if (killerName.equals(victimName)) {
                killer.sendMessage("§cNice try, exploiter :3");
            } else if (!plugin.isWarTime()) {
                if (!plugin.isOptedIn(victimName)) {
                    plugin.setThreatLevel(killerName, killerThreatLevel + 3);
                    killer.sendMessage("§cYou have gained 3 threat levels for killing an opted out player.");
                    if (!plugin.isOptedIn(killerName)) {
                        plugin.setOptedIn(killerName, true);
                        killer.sendMessage("§cYou have been opted in for killing a player.");
                    }
                } else if (plugin.getThreatLevel(victimName) > 0) {
                    killer.sendMessage("§aYou have not gained any threat level as the person you have killed had threat level.");
                    if (victimThreatLevel > 0) {
                        for (ItemStack itemStack : bountyItems) {
                            victim.getWorld().dropItemNaturally(victim.getLocation(), itemStack);
                        }
                    }
                    if (!plugin.isOptedIn(killerName)) {
                        plugin.setOptedIn(killerName, true);
                        killer.sendMessage("§cYou have been opted in for killing a player.");
                    }
                    plugin.setThreatLevel(victimName, 0);
                } else if (plugin.getThreatLevel(victimName) == 0 && plugin.isOptedIn(victimName)) {
                    plugin.setThreatLevel(killerName, killerThreatLevel + 1);
                    killer.sendMessage("§aYou have gained 1 threat level as the person you have killed had no threat level.");
                    if (!plugin.isOptedIn(killerName)) {
                        plugin.setOptedIn(killerName, true);
                        killer.sendMessage("§cYou have been opted in for killing a player.");
                    }
                }
            } else {
                killer.sendMessage("§aYou have not gained any threat level as it is war time.");
            }
        }
    }

    private static @NotNull List<ItemStack> getItemStacks(int victimThreatLevel) {
        int victimBounty = 0;

        if (victimThreatLevel == 1) {
            victimBounty += 4;
        } else if (victimThreatLevel == 2) {
            victimBounty += 8;
        } else if (victimThreatLevel == 3) {
            victimBounty += 12;
        } else if (victimThreatLevel == 4) {
            victimBounty += 16;
        } else if (victimThreatLevel >= 5) {
            victimBounty += 16;
            victimBounty += 4 * (victimThreatLevel - 4);
        }

        if (victimBounty > 0) {
            ItemStack bountyItem = new ItemStack(Material.DIAMOND);
            ItemMeta meta = bountyItem.getItemMeta();
            List<Component> lore = List.of(Component.text("§7Bounty: §c" + victimBounty));

            meta.displayName(Component.text("§4Blood Diamond"));
            meta.lore(lore);

            bountyItem.setItemMeta(meta);

            return splitItemStack(bountyItem, victimBounty);
        } else {
            return List.of();
        }
    }
}
