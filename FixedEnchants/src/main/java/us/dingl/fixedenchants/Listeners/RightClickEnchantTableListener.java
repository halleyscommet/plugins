package us.dingl.fixedenchants.Listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import us.dingl.fixedenchants.GUI.GUIManager;

import java.util.Objects;

import static org.bukkit.event.Event.Result.DENY;

public class RightClickEnchantTableListener implements Listener {

    private final GUIManager guiManager;

    public RightClickEnchantTableListener(GUIManager guiManager) {
        this.guiManager = guiManager;
    }

    @EventHandler
    public void onRightClickEnchantTable(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();

            if (block != null && Objects.equals(block.getType().getBlockTranslationKey(), "block.minecraft.enchanting_table")) {
                event.setUseInteractedBlock(DENY);
                event.setUseItemInHand(DENY);

                Player player = event.getPlayer();
                guiManager.createGUI("enchant_table", player, "Enchanting Table", 54);
            }
        }
    }
}