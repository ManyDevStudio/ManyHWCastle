package com.dreamcode;

import com.dreamcode.command.ManyShulkersCommand;
import com.dreamcode.config.Config;
import com.dreamcode.listener.BreakListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class ManyShulkers extends JavaPlugin {
    private Config configuration;
    private LootManager lootManager;
    public void onEnable() {
        configuration = new Config(this);
        configuration.load();
        lootManager = new LootManager(this);
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        if (lootManager != null) {
            lootManager.unregisterAll();
        }
    }

    private void registerCommands() {
        ManyShulkersCommand command = new ManyShulkersCommand(this);
        getCommand("manyshulkers").setExecutor(command);
        getCommand("manyshulkers").setTabCompleter(command);
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new BreakListener(this), this);
    }

}
