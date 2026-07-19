# DiscordHookSRV

Production-ready Minecraft ↔ Discord integration plugin.

**Paper 1.21.1 | JDA 5.6.1 | Java 21**

## Features

✅ Account Linking with secure one-time codes  
✅ Prefix Commands (!ip, !online) with configurable embeds  
✅ Rank Synchronization (LuckPerms)  
✅ Nickname Synchronization  
✅ Persistent link storage  
✅ Safe startup with proper error handling  
✅ Plugin shows RED when Discord connection fails  

## Quick Start

```bash
./gradlew shadowJar
```

Place `DiscordHookSRV-shadow.jar` in plugins folder.

## Configuration

1. Edit `plugins/DiscordHookSRV/config.yml`
2. Add your Discord bot token
3. Configure server IP and role IDs
4. Restart server

## Commands

**Minecraft:**
- `/link` - Generate linking code
- `/link <code>` - Complete link
- `/unlink` - Unlink account

**Discord:**
- `!ip` - Show server IP
- `!online` - Show online players

## Status

- ✅ Plugin appears **GREEN** when Discord connects
- ❌ Plugin appears **RED** when Discord token invalid/missing

See config.yml for all customization options.
