package com.mcdiscord.discordhooksrv.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcdiscord.discordhooksrv.DiscordHookSRV;
import com.mcdiscord.discordhooksrv.managers.ConfigManager;
import com.mcdiscord.discordhooksrv.managers.LinkManager;
import com.mcdiscord.discordhooksrv.utility.EmbedManager;

public class DiscordLinkCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final LinkManager linkManager;
    private final ConfigManager configManager;

    public DiscordLinkCommand(JavaPlugin plugin, LinkManager linkManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.linkManager = linkManager;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(EmbedManager.colorize("&cOnly players can use this command!"));
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("link")) {
            if (args.length == 0) {
                String code = linkManager.generateLinkCode();
                player.sendMessage(EmbedManager.colorize("&a&lLink Code: " + code));
                player.sendMessage(EmbedManager.colorize("&7Use /discord link " + code + " in Discord"));
            } else {
                String code = args[0];
                if (linkManager.validateCode(code, player.getUniqueId().toString())) {
                    linkManager.linkPlayer(player, player.getUniqueId().toString());
                    player.sendMessage(EmbedManager.colorize(configManager.getLinkSuccessMessage()));
                } else {
                    player.sendMessage(EmbedManager.colorize(configManager.getLinkInvalidMessage()));
                }
            }
        } else if (command.getName().equalsIgnoreCase("unlink")) {
            linkManager.unlinkPlayer(player);
            player.sendMessage(EmbedManager.colorize(configManager.getUnlinkSuccessMessage()));
        }

        return true;
    }
}