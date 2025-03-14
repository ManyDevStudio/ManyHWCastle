package com.dreamcode.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import com.dreamcode.ManyShulkers;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@AllArgsConstructor
@CommandAlias("manyshulkers")
@CommandPermission("manyshulkers.admin")
public class ManyShulkersCommand extends BaseCommand {
    private final ManyShulkers plugin;

    @Subcommand("start")
    public void start(CommandSender sender) {
        plugin.getLootManager().setRegenerationEnabled(true);
        sender.sendMessage(plugin.getConfiguration().getMessageStartRegeneration());
    }

    @Subcommand("stop")
    public void stop(CommandSender sender) {
        plugin.getLootManager().setRegenerationEnabled(false);
        sender.sendMessage(plugin.getConfiguration().getMessageStopRegeneration());
    }

    @Subcommand("reload")
    public void reload(CommandSender sender) {
        plugin.getConfiguration().load();
        sender.sendMessage(plugin.getConfiguration().getMessageStartReload());
    }
}
