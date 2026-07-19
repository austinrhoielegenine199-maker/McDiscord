package com.mcdiscord.discordhooksrv.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public String getBotToken() {
        return config.getString("bot-token", "PASTE_YOUR_BOT_TOKEN_HERE");
    }

    public String getServerIp() {
        return config.getString("server-ip", "your.server.ip");
    }

    public String getLinkedRoleId() {
        return config.getString("linked-role-id", "DISCORD_ROLE_ID");
    }

    public int getLinkCodeExpirationMinutes() {
        return config.getInt("link-code-expiration-minutes", 5);
    }

    public boolean isNicknameSyncEnabled() {
        return config.getBoolean("nickname-sync.enabled", true);
    }

    public boolean isResetNicknameOnUnlink() {
        return config.getBoolean("nickname-sync.reset-on-unlink", true);
    }

    public boolean isRankSyncEnabled() {
        return config.getBoolean("rank-sync.enabled", true);
    }

    public String getRoleIdForGroup(String group) {
        return config.getString("rank-sync.groups." + group, "DISCORD_ROLE_ID");
    }

    public boolean isIpCommandEnabled() {
        return config.getBoolean("ip.enabled", true);
    }

    public String getIpCommandName() {
        return config.getString("ip.command", "!ip");
    }

    public String getOnlineCommandName() {
        return config.getString("online.command", "!online");
    }

    public boolean isOnlineCommandEnabled() {
        return config.getBoolean("online.enabled", true);
    }

    public String getLinkSuccessMessage() {
        return config.getString("messages.link-success", "&aYour Minecraft account has been linked successfully!");
    }

    public String getLinkInvalidMessage() {
        return config.getString("messages.link-invalid", "&cThat linking code is invalid or expired.");
    }

    public String getAlreadyLinkedMessage() {
        return config.getString("messages.already-linked", "&cThis account is already linked.");
    }

    public String getUnlinkSuccessMessage() {
        return config.getString("messages.unlink-success", "&aYour account has been unlinked.");
    }

    public String getLinkUsageMessage() {
        return config.getString("messages.link-usage", "&cUsage: /link <code>");
    }

    public String getReloadSuccessMessage() {
        return config.getString("messages.reload-success", "&a&lDiscordHookSRV reloaded successfully!");
    }

    public String getReloadFailedMessage() {
        return config.getString("messages.reload-failed", "&c&lFailed to reload DiscordHookSRV!");
    }

    public String getLinkSuccessDmMessage() {
        return config.getString("discord.link-success-dm", "Your Minecraft account \"{username}\" has been successfully linked to Discord.");
    }

    public String getUnlinkSuccessDmMessage() {
        return config.getString("discord.unlink-success-dm", "Your Minecraft account has been unlinked from Discord.");
    }

    public org.bukkit.configuration.ConfigurationSection getEmbedConfig(String command) {
        return config.getConfigurationSection(command) != null ? 
            config.getConfigurationSection(command).getConfigurationSection("embed") : null;
    }

    public boolean isDebugEnabled() {
        return config.getBoolean("debug", false);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }
}
