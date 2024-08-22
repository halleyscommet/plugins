package us.dingl.fixedenchants;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import us.dingl.fixedenchants.Commands.GetGUIsCommand;
import us.dingl.fixedenchants.Commands.StartWebServerCommand;
import us.dingl.fixedenchants.Commands.StopWebServerCommand;
import us.dingl.fixedenchants.GUI.GUIManager;
import us.dingl.fixedenchants.Listeners.RightClickEnchantTableListener;
import us.dingl.fixedenchants.WS.WebServerHandler;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class FixedEnchants extends JavaPlugin {

    private GUIManager guiManager;
    private File playersFile;
    private YamlConfiguration playersConfig;
    public WebServerHandler webServerHandler;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("FixedEnchants has been enabled!");

        // Initialize GUI Manager
        guiManager = new GUIManager(this);

        // Create plugin data folder
        createPluginDataFolder();

        // Load players config
        loadPlayersConfig();

        // Register listeners
        registerListeners();

        // Register commands
        registerCommands();

        // Get port from config
        int port = getPort();
        getLogger().info("Port: " + port);

        // Start web server
        webServerHandler = new WebServerHandler(port);
        try {
            webServerHandler.start();
            getLogger().info("Web server started successfully!");
        } catch (Exception e) {
            getLogger().severe("Failed to start web server: " + e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("FixedEnchants has been disabled!");

        // Stop web server
        try {
            webServerHandler.stop();
            getLogger().info("Web server stopped successfully!");
        } catch (Exception e) {
            getLogger().severe("Failed to stop web server: " + e.getMessage());
        }
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
        playersFile = new File(getDataFolder(), "config.yml");
        if (!playersFile.exists()) {
            try {
                if (playersFile.createNewFile()) {
                    getLogger().info("config.yml file created successfully.");
                } else {
                    getLogger().severe("Failed to create config.yml file.");
                }
                playersConfig = YamlConfiguration.loadConfiguration(playersFile);
                playersConfig.set("server.port", 8080);
                playersConfig.save(playersFile);
            } catch (IOException e) {
                getLogger().severe("Failed to write file: " + e.getMessage());
            }
        }
    }

    private void loadPlayersConfig() {
        playersFile = new File(getDataFolder(), "config.yml");
        if (!playersFile.exists()) {
            writeFileInDataFolder();
        } else {
            playersConfig = YamlConfiguration.loadConfiguration(playersFile);
        }
    }

    public WebServerHandler getWebServerHandler() {
        return webServerHandler;
    }

    public int getPort() {
        return playersConfig.getInt("server.port", 8080);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new RightClickEnchantTableListener(guiManager), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("getguis")).setExecutor(new GetGUIsCommand(this));
        Objects.requireNonNull(getCommand("startwebserver")).setExecutor(new StartWebServerCommand(this));
        Objects.requireNonNull(getCommand("stopwebserver")).setExecutor(new StopWebServerCommand(this));
    }

    public GUIManager getGuiManager() {
        return guiManager;
    }
}