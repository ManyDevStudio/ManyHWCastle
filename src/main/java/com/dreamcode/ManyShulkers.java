package com.dreamcode;

import co.aikar.commands.PaperCommandManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import com.dreamcode.command.ManyShulkersCommand;
import com.dreamcode.config.Config;
import com.dreamcode.listener.BreakListener;

public class ManyShulkers extends JavaPlugin {
    private Config config;
    private LootManager lootManager;
    private PaperCommandManager paperCommandManager;
    @Override
    public void onEnable() {
        config = new Config(this);
        config.load();
        lootManager = new LootManager(this);
        paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new ManyShulkersCommand(this));
        registerListeners();
    }

    @Override
    public void onDisable() {
        if (lootManager != null) {
            lootManager.unregisterAll();
        }
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new BreakListener(this), this);
    }

    @NotNull
    public Config getConfiguration() {
        return config;
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public PaperCommandManager getPaperCommandManager() {
        return paperCommandManager;
    }
}
