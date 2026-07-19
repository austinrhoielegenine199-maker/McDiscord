package com.mcdiscord.discordhooksrv.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class LinkManager {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final Map<String, String> linkCodes = new HashMap<>();
    private final Map<String, Long> codeExpiration = new HashMap<>();
    private final Map<String, String> playerToDiscord = new HashMap<>();

    public LinkManager(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        loadLinks();
    }

    public String generateLinkCode() {
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        long expiration = System.currentTimeMillis() + (configManager.getLinkCodeExpirationMinutes() * 60 * 1000);
        linkCodes.put(code, "");
        codeExpiration.put(code, expiration);
        return code;
    }

    public boolean validateCode(String code, String discordId) {
        if (!linkCodes.containsKey(code)) {
            return false;
        }
        if (System.currentTimeMillis() > codeExpiration.getOrDefault(code, 0L)) {
            linkCodes.remove(code);
            codeExpiration.remove(code);
            return false;
        }
        linkCodes.put(code, discordId);
        return true;
    }

    public void linkPlayer(Player player, String discordId) {
        playerToDiscord.put(player.getUniqueId().toString(), discordId);
        saveLinks();
    }

    public void unlinkPlayer(Player player) {
        playerToDiscord.remove(player.getUniqueId().toString());
        saveLinks();
    }

    public String getDiscordId(Player player) {
        return playerToDiscord.get(player.getUniqueId().toString());
    }

    private void saveLinks() {
        try {
            File linksFile = new File(plugin.getDataFolder(), "links.yml");
            if (!linksFile.exists()) {
                linksFile.createNewFile();
            }
            FileWriter writer = new FileWriter(linksFile);
            Yaml yaml = new Yaml();
            yaml.dump(playerToDiscord, writer);
            writer.close();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to save links: " + e.getMessage());
        }
    }

    private void loadLinks() {
        try {
            File linksFile = new File(plugin.getDataFolder(), "links.yml");
            if (!linksFile.exists()) {
                return;
            }
            FileReader reader = new FileReader(linksFile);
            Yaml yaml = new Yaml();
            Map<String, String> data = yaml.load(reader);
            if (data != null) {
                playerToDiscord.putAll(data);
            }
            reader.close();
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to load links: " + e.getMessage());
        }
    }
}