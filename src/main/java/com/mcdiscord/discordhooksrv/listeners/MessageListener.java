package com.mcdiscord.discordhooksrv.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import com.mcdiscord.discordhooksrv.managers.ConfigManager;
import com.mcdiscord.discordhooksrv.managers.LinkManager;
import com.mcdiscord.discordhooksrv.utility.EmbedManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageListener extends ListenerAdapter {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final LinkManager linkManager;

    public MessageListener(JavaPlugin plugin, ConfigManager configManager, LinkManager linkManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.linkManager = linkManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String message = event.getMessage().getContentRaw();

        if (message.equalsIgnoreCase(configManager.getIpCommandName()) && configManager.isIpCommandEnabled()) {
            event.getChannel().sendMessageEmbeds(
                EmbedManager.createEmbed(
                    "Minecraft Server",
                    "Join us at " + configManager.getServerIp(),
                    "DiscordHookSRV - Made By ArchiveAustin",
                    new java.awt.Color(88, 101, 242)
                )
            ).queue();
        } else if (message.equalsIgnoreCase(configManager.getOnlineCommandName()) && configManager.isOnlineCommandEnabled()) {
            int online = Bukkit.getOnlinePlayers().size();
            int max = Bukkit.getMaxPlayers();
            String players = Bukkit.getOnlinePlayers().stream()
                .map(p -> p.getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Nobody");
            
            event.getChannel().sendMessageEmbeds(
                EmbedManager.createServerStatusEmbed(online, max, players)
            ).queue();
        }
    }
}