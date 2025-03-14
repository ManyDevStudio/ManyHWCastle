package com.dreamcode;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.dreamcode.command.ManyShulkersCommand;
import com.dreamcode.config.Config;
import com.dreamcode.listener.BreakListener;

@Getter
public class ManyShulkers extends JavaPlugin {
    private Config configuration;
    private LootManager lootManager;
    private PaperCommandManager paperCommandManager;
    @Override
    public void onEnable() {
        configuration = new Config(this);
        configuration.load();
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

}
