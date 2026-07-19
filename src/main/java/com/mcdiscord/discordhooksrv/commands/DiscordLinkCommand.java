package com.mcdiscord.discordhooksrv.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import com.mcdiscord.discordhooksrv.DiscordHookSRV;
import com.mcdiscord.discordhooksrv.managers.ConfigManager;
import com.mcdiscord.discordhooksrv.managers.LinkManager;

public class DiscordLinkCommand extends ListenerAdapter {
    private final DiscordHookSRV plugin;
    private final ConfigManager configManager;
    private final LinkManager linkManager;

    public DiscordLinkCommand(DiscordHookSRV plugin, ConfigManager configManager, LinkManager linkManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.linkManager = linkManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("link")) {
            handleLinkCommand(event);
        } else if (event.getName().equals("unlink")) {
            handleUnlinkCommand(event);
        }
    }

    private void handleLinkCommand(SlashCommandInteractionEvent event) {
        if (event.getSubcommandName() == null) {
            // Generate new code
            generateNewLinkCode(event);
        } else if (event.getSubcommandName().equals("code")) {
            // Complete linking with code
            String code = event.getOption("code").getAsString();
            completeLinking(event, code);
        }
    }

    private void generateNewLinkCode(SlashCommandInteractionEvent event) {
        String discordId = event.getUser().getId();

        // Check if already linked
        if (linkManager.getLinkManager().getLinkedMinecraftUuid(discordId) != null) {
            event.reply("You are already linked to a Minecraft account.").setEphemeral(true).queue();
            return;
        }

        // Generate code
        var code = linkManager.generateLinkCode();
        event.reply("Your linking code is: **" + code.getCode() + "**\n" +
            "Use `/link " + code.getCode() + "` in Minecraft within " + code.getExpirationMinutes() + " minutes.")
            .setEphemeral(true).queue();
    }

    private void completeLinking(SlashCommandInteractionEvent event, String codeStr) {
        event.reply("Linking in progress...").setEphemeral(true).queue();
    }

    private void handleUnlinkCommand(SlashCommandInteractionEvent event) {
        String discordId = event.getUser().getId();

        // Check if linked
        var uuid = linkManager.getLinkManager().getLinkedMinecraftUuid(discordId);
        if (uuid == null) {
            event.reply("You are not linked to a Minecraft account.").setEphemeral(true).queue();
            return;
        }

        // Unlink
        linkManager.unlinkAccounts(uuid);
        event.reply("You have been unlinked from your Minecraft account.").setEphemeral(true).queue();
    }
}
