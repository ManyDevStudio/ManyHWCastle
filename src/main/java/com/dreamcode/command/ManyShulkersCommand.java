package com.dreamcode.command;

import com.dreamcode.ManyShulkers;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class ManyShulkersCommand implements CommandExecutor, TabCompleter {
    private final List<String> commands = Arrays.asList("start", "stop", "reload");
    private final ManyShulkers plugin;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args)  {
        if (!sender.hasPermission("manyshulkers.admin")) {
            sender.sendMessage(plugin.getConfiguration().getMessageNoPermission());
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(plugin.getConfiguration().getMessageNoCommand());
            return true;
        }
        switch (args[0]) {
            case "start": {
                plugin.getLootManager().setRegenerationEnabled(true);
                sender.sendMessage(plugin.getConfiguration().getMessageStartRegeneration());
                break;
            }
            case "stop": {
                plugin.getLootManager().setRegenerationEnabled(false);
                sender.sendMessage(plugin.getConfiguration().getMessageStopRegeneration());
                break;
            }
            case "reload": {
                plugin.getConfiguration().load();
                sender.sendMessage(plugin.getConfiguration().getMessageStartReload());
                break;
            }
            default: {
                sender.sendMessage(plugin.getConfiguration().getMessageNoCommand());
                break;
            }
        }
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length <= 1) {
            return commands;
        }
        return Collections.emptyList();
    }
}
