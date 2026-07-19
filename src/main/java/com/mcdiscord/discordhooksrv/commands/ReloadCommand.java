package com.mcdiscord.discordhooksrv.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcdiscord.discordhooksrv.managers.ConfigManager;
import com.mcdiscord.discordhooksrv.utility.EmbedManager;

public class ReloadCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    public ReloadCommand(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("discordhooksrv.reload")) {
            sender.sendMessage(EmbedManager.colorize("&cYou don't have permission to use this command."));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            try {
                configManager.reload();
                sender.sendMessage(EmbedManager.colorize(configManager.getReloadSuccessMessage()));
                plugin.getLogger().info("Configuration reloaded by " + sender.getName());
            } catch (Exception e) {
                sender.sendMessage(EmbedManager.colorize(configManager.getReloadFailedMessage()));
                plugin.getLogger().severe("Failed to reload configuration: " + e.getMessage());
            }
        } else {
            sender.sendMessage(EmbedManager.colorize("&cUsage: /mcdiscord reload"));
        }

        return true;
    }
}
