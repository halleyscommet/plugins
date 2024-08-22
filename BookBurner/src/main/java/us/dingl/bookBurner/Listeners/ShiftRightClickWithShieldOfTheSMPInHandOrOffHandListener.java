package us.dingl.bookBurner.Listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.util.Vector;
import org.joml.Vector3d;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Random;

public class ShiftRightClickWithShieldOfTheSMPInHandOrOffHandListener implements Listener {

    private final Set<Player> playersWhoDashed0 = new HashSet<>();
    private final Set<Player> playersWhoDashed = new HashSet<>();
    private final Map<UUID, Long> knockbackCooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 3000; // 3 seconds
    private final Random random = new Random();

    @EventHandler
    public void onShiftRightClickWithShieldOfTheSMPInHandOrOffHand(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) { // event.getAction() == isRightClick()
            if (event.getPlayer().isSneaking()) {
                Player player = event.getPlayer();
                Vector3d playerDirection = new Vector3d(player.getLocation().getDirection().toVector3d());

                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                ItemStack offHandItem = player.getInventory().getItemInOffHand();

                if (player.isOnGround() && (isShieldOfTheSMP(mainHandItem) || isShieldOfTheSMP(offHandItem))) {
                    if (isFinite(playerDirection)) {
                        setVel(player, playerDirection, mainHandItem, offHandItem, playersWhoDashed0);
                        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 10);
                    } else {
                        player.sendMessage(Component.text("Player direction is not finite, cannot dash."));
                    }
                } else if (!player.isOnGround() && !playersWhoDashed.contains(player) && (isShieldOfTheSMP(mainHandItem) || isShieldOfTheSMP(offHandItem))) {
                    if (isFinite(playerDirection)) {
                        setVel(player, playerDirection, mainHandItem, offHandItem, playersWhoDashed);
                        player.getWorld().spawnParticle(Particle.EXPLOSION, player.getLocation(), 10);
                    } else {
                        player.sendMessage(Component.text("Player direction is not finite, cannot dash."));
                    }
                }
            }
        }
    }

    private void setVel(Player player, Vector3d playerDirection, ItemStack mainHandItem, ItemStack offHandItem, Set<Player> playersWhoDashed0) {
        player.setVelocity(Vector.fromJOML(playerDirection.mul(2)));
        playersWhoDashed0.add(player);
        if (isShieldOfTheSMP(mainHandItem)) {
            damageItemWithChance(mainHandItem, 1);
        } else if (isShieldOfTheSMP(offHandItem)) {
            damageItemWithChance(offHandItem, 1);
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1.0f, 0.5f);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isOnGround()) {
            if (canApplyKnockback(player)) {
                playersWhoDashed0.remove(player);
                playersWhoDashed.remove(player);
            }
        } else if (playersWhoDashed.contains(player) || playersWhoDashed0.contains(player)) {
            for (Player otherPlayer : player.getWorld().getPlayers()) {
                if (!player.equals(otherPlayer) && player.getBoundingBox().overlaps(otherPlayer.getBoundingBox())) {
                    if (!otherPlayer.isBlocking()) {
                        if (canApplyKnockback(player)) {
                            applyKnockback(player, otherPlayer);
                            knockbackCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                            otherPlayer.damage(4);
                        }
                    } else {
                        applyKnockback(otherPlayer, player);
                    }
                }
            }

            player.setFallDistance(0);
        }
    }

    @EventHandler
    public void onPlayerUseFirework(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.FIREWORK_ROCKET && player.isGliding()) {
            playersWhoDashed0.remove(player);
            playersWhoDashed.remove(player);
        }
    }

    private boolean isShieldOfTheSMP(ItemStack item) {
        if (item == null || item.getType() != Material.SHIELD || !item.hasItemMeta()) {
            return false;
        }
        return Objects.equals(item.getItemMeta().displayName(), Component.text("§6§lSHIELD OF THE SMP"));
    }

    private boolean canApplyKnockback(Player player) {
        return !knockbackCooldowns.containsKey(player.getUniqueId()) || (System.currentTimeMillis() - knockbackCooldowns.get(player.getUniqueId())) > COOLDOWN_TIME;
    }

    private void applyKnockback(Player source, Player target) {
        Vector3d knockbackDirection = new Vector3d(target.getLocation().toVector().subtract(source.getLocation().toVector()).normalize().toVector3d());
        if (isFinite(knockbackDirection)) {
            target.setVelocity(Vector.fromJOML(knockbackDirection.mul(3)));
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
            target.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10);
        } else {
            source.sendMessage(Component.text("Knockback direction is not finite, cannot apply knockback."));
        }
    }

    private boolean isFinite(Vector3d vector) {
        return Double.isFinite(vector.x) && Double.isFinite(vector.y) && Double.isFinite(vector.z);
    }

    private void damageItemWithChance(ItemStack item, int damageAmount) {
        if (item != null && item.hasItemMeta() && item.getItemMeta() instanceof Damageable meta) {
            int currentDamage = meta.getDamage();
            int maxDurability = item.getType().getMaxDurability();
            double damageProbability = (double) currentDamage / maxDurability;

            if (random.nextDouble() < damageProbability) {
                meta.setDamage(currentDamage + damageAmount);
                item.setItemMeta(meta);
            }
        }
    }
}