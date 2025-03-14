package com.dreamcode.command;

import com.dreamcode.ManyShulkers;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@Command(name = "manyshulkers")
@AllArgsConstructor
@Permission("manyshulkers.admin")
public class ManyShulkersCommand {
    private final ManyShulkers plugin;

    @Execute(name = "start")
    public void start(@Context CommandSender sender) {
        plugin.getLootManager().setRegenerationEnabled(true);
        sender.sendMessage(plugin.getConfiguration().getMessageStartRegeneration());
    }

    @Execute(name = "stop")
    public void stop(@Context CommandSender sender) {
        plugin.getLootManager().setRegenerationEnabled(false);
        sender.sendMessage(plugin.getConfiguration().getMessageStopRegeneration());
    }

    @Execute(name = "reload")
    public void reload(@Context CommandSender sender) {
        plugin.getConfiguration().load();
        sender.sendMessage(plugin.getConfiguration().getMessageStartReload());
    }
}
