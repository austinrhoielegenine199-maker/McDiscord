package com.mcdiscord.discordhooksrv.utility;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.ChatColor;

import java.awt.Color;

public class EmbedManager {
    
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static MessageEmbed createEmbed(String title, String description, String footer, Color color) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle(title)
            .setDescription(description)
            .setFooter(footer)
            .setColor(color);
        return embed.build();
    }

    public static MessageEmbed createServerStatusEmbed(int online, int max, String players) {
        EmbedBuilder embed = new EmbedBuilder()
            .setTitle("Server Status")
            .setDescription("Status: Online ✅")
            .addField("Players", online + "/" + max, true)
            .addField("Online Players", players, false)
            .setFooter("DiscordHookSRV - Made By ArchiveAustin")
            .setColor(new Color(87, 242, 135));
        return embed.build();
    }
}