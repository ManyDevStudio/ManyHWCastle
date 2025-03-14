package com.dreamcode;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.message.LiteMessages;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import com.dreamcode.command.ManyShulkersCommand;
import com.dreamcode.config.Config;
import com.dreamcode.listener.BreakListener;

@Getter
public class ManyShulkers extends JavaPlugin {
    private Config configuration;
    private LootManager lootManager;
    private LiteCommands<CommandSender> commandsManager;
    @Override
    public void onEnable() {
        configuration = new Config(this);
        configuration.load();
        lootManager = new LootManager(this);
        commandsManager =  LiteBukkitFactory.builder("ManyShulkers", this)
                .commands(
                        new ManyShulkersCommand(this))
                .message(LiteMessages.MISSING_PERMISSIONS, configuration.getMessageNoPermission())
                .build();
        registerListeners();
    }

    @Override
    public void onDisable() {
        if (lootManager != null) {
            lootManager.unregisterAll();
        }
        if (commandsManager != null) {
            commandsManager.unregister();
        }
    }
    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new BreakListener(this), this);
    }

}
