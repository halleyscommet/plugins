package us.dingl.fixedenchants.GUI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import us.dingl.fixedenchants.FixedEnchants;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.kyori.adventure.text.Component;

public class GetUIContents {

    private final Logger logger = Logger.getLogger(GetUIContents.class.getName());
    private final FixedEnchants plugin;

    public GetUIContents(FixedEnchants plugin) {
        this.plugin = plugin;
    }

    final List<ItemStack> getUIContents(String guiName) {
        List<ItemStack> itemStacks = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(guiName + ".json");
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + guiName + ".json");
            }

            InputStreamReader reader = new InputStreamReader(inputStream);
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                Type listType = new TypeToken<List<ItemData>>() {
                }.getType();
                List<ItemData> items = new Gson().fromJson(entry.getValue(), listType);

                for (ItemData itemData : items) {
                    Material material = Material.matchMaterial(itemData.item);
                    if (material != null) {
                        ItemStack itemStack = new ItemStack(material, itemData.stack);
                        ItemMeta meta = itemStack.getItemMeta();

                        if (itemData.properties != null) {
                            if (itemData.properties.containsKey("no_item_tooltip")) {
                                boolean noTooltip = (boolean) itemData.properties.get("no_item_tooltip");
                                if (noTooltip) {
                                    meta.setHideTooltip(true);
                                }
                            }
                            if (itemData.properties.containsKey("display_name")) {
                                String displayName = (String) itemData.properties.get("display_name");
                                meta.displayName(Component.text(displayName));
                            }
                            if (itemData.properties.containsKey("cancel_event")) {
                                boolean cancelEvent = (boolean) itemData.properties.get("cancel_event");
                                if (cancelEvent) {
                                    registerCancelEventListener(itemStack);
                                }
                            }
                            if (itemData.properties.containsKey("close_gui")) {
                                boolean closeGui = (boolean) itemData.properties.get("close_gui");
                                if (closeGui) {
                                    registerCloseGuiListener(itemStack);
                                }
                            }
                        }

                        itemStack.setItemMeta(meta);

                        for (int i = 0; i < itemData.weight; i++) {
                            itemStacks.add(itemStack);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.severe("Error loading GUI contents: " + e.getMessage());
        }

        return itemStacks;
    }

    private void registerCancelEventListener(ItemStack itemStack) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getCurrentItem() != null && event.getCurrentItem().isSimilar(itemStack)) {
                    event.setCancelled(true);
                }
            }
        }, plugin);
    }

    private void registerCloseGuiListener(ItemStack itemStack) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getCurrentItem() != null && event.getCurrentItem().isSimilar(itemStack)) {
                    event.getWhoClicked().closeInventory();
                    plugin.getGuiManager().removeGUIByInventory(event.getInventory());
                }
            }
        }, plugin);
    }
}