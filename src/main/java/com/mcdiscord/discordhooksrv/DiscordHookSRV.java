package com.mcdiscord.discordhooksrv;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcdiscord.discordhooksrv.commands.DiscordLinkCommand;
import com.mcdiscord.discordhooksrv.commands.ReloadCommand;
import com.mcdiscord.discordhooksrv.listeners.MessageListener;
import com.mcdiscord.discordhooksrv.managers.ConfigManager;
import com.mcdiscord.discordhooksrv.managers.LinkManager;

public class DiscordHookSRV extends JavaPlugin {
    private static DiscordHookSRV instance;
    private JDA jda;
    private ConfigManager configManager;
    private LinkManager linkManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        linkManager = new LinkManager(this, configManager);

        try {
            String token = configManager.getBotToken();
            if (token == null || token.equals("PASTE_YOUR_BOT_TOKEN_HERE")) {
                getLogger().severe("❌ Bot token not configured! Check config.yml");
                getPluginLoader().disablePlugin(this);
                return;
            }

            jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new MessageListener(this, configManager, linkManager))
                    .build();

            jda.awaitReady();
            getLogger().info("✅ Discord bot connected!");
        } catch (Exception e) {
            getLogger().severe("❌ Failed to connect Discord bot: " + e.getMessage());
            getPluginLoader().disablePlugin(this);
            return;
        }

        // Register commands
        getCommand("link").setExecutor(new DiscordLinkCommand(this, linkManager, configManager));
        getCommand("unlink").setExecutor(new DiscordLinkCommand(this, linkManager, configManager));
        getCommand("mcdiscord").setExecutor(new ReloadCommand(this, configManager));

        getLogger().info("✅ DiscordHookSRV enabled!");
    }

    @Override
    public void onDisable() {
        if (jda != null) {
            jda.shutdown();
        }
        getLogger().info("❌ DiscordHookSRV disabled!");
    }

    public static DiscordHookSRV getInstance() {
        return instance;
    }

    public JDA getJDA() {
        return jda;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }
}