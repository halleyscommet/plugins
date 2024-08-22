package us.dingl.autoComp;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import us.dingl.autoComp.commands.GiveAutoCompCommand;
import us.dingl.autoComp.commands.GiveRealCompDiamondBlockCommand;
import us.dingl.autoComp.commands.GiveRealCompEmeraldBlockCommand;
import us.dingl.autoComp.listeners.AutoCompInventoryRightClickListener;
import us.dingl.autoComp.listeners.AutoCompPlaceListener;
import us.dingl.autoComp.listeners.AutoCompRightClickListener;
import us.dingl.autoComp.listeners.DetectIfAutoCompInInventory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class AutoComp extends JavaPlugin {

    private FileConfiguration playersConfig;
    private File playersFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("AutoComp has been enabled!");

        // Create plugin data folder
        createPluginDataFolder();

        // Load players configuration
        loadPlayersConfig();

        // Register commands and listeners
        registerCommandsAndListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("AutoComp has been disabled!");
    }

    private void createPluginDataFolder() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                getLogger().info("Plugin data folder created successfully.");
                writeFileInDataFolder();
            } else {
                getLogger().severe("Failed to create plugin data folder.");
            }
        }
    }

    private void writeFileInDataFolder() {
        playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            try {
                if (playersFile.createNewFile()) {
                    getLogger().info("players.yml file created successfully.");
                } else {
                    getLogger().severe("Failed to create players.yml file.");
                }
                playersConfig = YamlConfiguration.loadConfiguration(playersFile);
                playersConfig.set("opted-in", List.of());
                playersConfig.set("opted-out", List.of());
                playersConfig.save(playersFile);
            } catch (IOException e) {
                getLogger().severe("Failed to write file: " + e.getMessage());
            }
        }
    }

    private void loadPlayersConfig() {
        playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            writeFileInDataFolder();
        } else {
            playersConfig = YamlConfiguration.loadConfiguration(playersFile);
        }
    }

    public boolean isOptedIn(String playerName) {
        List<String> optedIn = playersConfig.getStringList("opted-in");
        return optedIn.contains(playerName);
    }

    public void setOptedIn(String playerName, boolean optedIn) {
        List<String> optedInList = playersConfig.getStringList("opted-in");
        List<String> optedOutList = playersConfig.getStringList("opted-out");

        if (optedIn) {
            if (!optedInList.contains(playerName)) {
                optedInList.add(playerName);
                optedOutList.remove(playerName);
            }
        } else {
            if (!optedOutList.contains(playerName)) {
                optedOutList.add(playerName);
                optedInList.remove(playerName);
            }
        }

        playersConfig.set("opted-in", optedInList);
        playersConfig.set("opted-out", optedOutList);
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save players configuration: " + e.getMessage());
        }
    }

    private void registerCommandsAndListeners() {
        Objects.requireNonNull(getCommand("giveautocomp")).setExecutor(new GiveAutoCompCommand());
        Objects.requireNonNull(getCommand("giverealcompdiamondblock")).setExecutor(new GiveRealCompDiamondBlockCommand());
        Objects.requireNonNull(getCommand("giverealcompemeraldblock")).setExecutor(new GiveRealCompEmeraldBlockCommand());

        getServer().getPluginManager().registerEvents(new AutoCompPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new AutoCompRightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new AutoCompInventoryRightClickListener(this), this);
        getServer().getPluginManager().registerEvents(new DetectIfAutoCompInInventory(this), this);
    }
}