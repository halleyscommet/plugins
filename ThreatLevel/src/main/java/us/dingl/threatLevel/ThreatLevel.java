package us.dingl.threatLevel;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import us.dingl.threatLevel.Commands.*;
import us.dingl.threatLevel.Listeners.CraftAttemptListener;
import us.dingl.threatLevel.Listeners.PlayerDeathListener;
import us.dingl.threatLevel.Listeners.PlayerLoginListener;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public final class ThreatLevel extends JavaPlugin {

    private FileConfiguration playersConfig;
    private File playersFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("ThreatLevel has been enabled!");

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
        getLogger().info("ThreatLevel has been disabled!");
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
        playersFile = new File(getDataFolder(), "threat.yml");
        if (!playersFile.exists()) {
            try {
                if (playersFile.createNewFile()) {
                    getLogger().info("threat.yml file created successfully.");
                } else {
                    getLogger().severe("Failed to create threat.yml file.");
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
        playersFile = new File(getDataFolder(), "threat.yml");
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

    public void setThreatLevel(String playerName, int level) {
        playersConfig.set("threat-level." + playerName, level);
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save players configuration: " + e.getMessage());
        }
    }

    public int getThreatLevel(String playerName) {
        return playersConfig.getInt("threat-level." + playerName, 0);
    }

    public void setWarTime(boolean warTime) {
        playersConfig.set("war-time", warTime);
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            getLogger().severe("Failed to save players configuration: " + e.getMessage());
        }
    }

    public boolean isWarTime() {
        return playersConfig.getBoolean("war-time", false);
    }

    private void registerCommandsAndListeners() {
        // Register commands
        Objects.requireNonNull(getCommand("optin")).setExecutor(new SetOptedInCommand(this));
        Objects.requireNonNull(getCommand("optout")).setExecutor(new SetOptedOutCommand(this));
        Objects.requireNonNull(getCommand("threatlevel")).setExecutor(new ThreatLevelCommand(this));
        Objects.requireNonNull(getCommand("setwartime")).setExecutor(new SetWarTimeCommand(this));
        Objects.requireNonNull(getCommand("checkthreatlevel")).setExecutor(new CheckThreatLevelCommand(this));

        // Register tab completers
        Objects.requireNonNull(getCommand("threatlevel")).setTabCompleter(new ThreatLevelCommand(this));
        Objects.requireNonNull(getCommand("setwartime")).setTabCompleter(new SetWarTimeCommand(this));
        Objects.requireNonNull(getCommand("checkthreatlevel")).setTabCompleter(new CheckThreatLevelCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftAttemptListener(), this);
    }
}
