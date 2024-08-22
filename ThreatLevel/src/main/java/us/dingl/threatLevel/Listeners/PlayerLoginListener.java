package us.dingl.threatLevel.Listeners;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.dingl.threatLevel.ThreatLevel;

import java.util.Objects;
import java.util.logging.Logger;

public class PlayerLoginListener implements Listener {

    private final ThreatLevel plugin;
    private final Logger logger;

    public PlayerLoginListener(ThreatLevel plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        int playerThreatLevel = plugin.getThreatLevel(playerName);

        logger.info("Player " + playerName + " has a threat level of " + playerThreatLevel);

        plugin.setOptedIn(playerName, plugin.isOptedIn(playerName));

        if (playerThreatLevel > 0) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    double newHealth = 20 - playerThreatLevel;
                    Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
                    player.sendHealthUpdate();
                }
            }.runTaskLater(plugin, 20L);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    double newHealth = 20;
                    Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(newHealth);
                    player.sendHealthUpdate();
                }
            }.runTaskLater(plugin, 20L);
        }
    }
}