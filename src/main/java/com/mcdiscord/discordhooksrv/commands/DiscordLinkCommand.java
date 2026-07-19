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
import net.dv8tion.jda.api.entities.User;

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
                player.sendMessage(EmbedManager.colorize("&c/link <code>"));
                player.sendMessage(EmbedManager.colorize("&7Get a code by typing /link in Discord"));
            } else {
                String code = args[0].toUpperCase();
                if (linkManager.validateCode(code, player.getUniqueId().toString())) {
                    String discordId = linkManager.getDiscordIdFromCode(code);
                    
                    // Send Discord DM
                    if (discordId != null && !discordId.isEmpty()) {
                        try {
                            User discordUser = DiscordHookSRV.getInstance().getJDA().getUserById(discordId);
                            if (discordUser != null) {
                                String message = configManager.getLinkSuccessDmMessage()
                                    .replace("{username}", player.getName());
                                discordUser.openPrivateChannel().queue(channel ->
                                    channel.sendMessage(message).queue()
                                );
                            }
                        } catch (Exception e) {
                            plugin.getLogger().warning("Failed to send DM: " + e.getMessage());
                        }
                    }
                    
                    linkManager.linkPlayer(player, discordId);
                    player.sendMessage(EmbedManager.colorize(configManager.getLinkSuccessMessage()));
                    plugin.getLogger().info(player.getName() + " linked their account!");
                } else {
                    player.sendMessage(EmbedManager.colorize(configManager.getLinkInvalidMessage()));
                }
            }
        } else if (command.getName().equalsIgnoreCase("unlink")) {
            String discordId = linkManager.getDiscordId(player);
            
            // Send Discord DM
            if (discordId != null && !discordId.isEmpty()) {
                try {
                    User discordUser = DiscordHookSRV.getInstance().getJDA().getUserById(discordId);
                    if (discordUser != null) {
                        discordUser.openPrivateChannel().queue(channel ->
                            channel.sendMessage(configManager.getUnlinkSuccessDmMessage()).queue()
                        );
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to send DM: " + e.getMessage());
                }
            }
            
            linkManager.unlinkPlayer(player);
            player.sendMessage(EmbedManager.colorize(configManager.getUnlinkSuccessMessage()));
            plugin.getLogger().info(player.getName() + " unlinked their account!");
        }

        return true;
    }
}