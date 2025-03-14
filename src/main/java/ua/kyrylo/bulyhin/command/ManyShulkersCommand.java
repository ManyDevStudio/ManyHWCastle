package ua.kyrylo.bulyhin.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import ua.kyrylo.bulyhin.ManyShulkers;

@CommandAlias("manyshulkers")
@CommandPermission("manyshulkers.admin")
public class ManyShulkersCommand extends BaseCommand {
    private final ManyShulkers plugin;

    public ManyShulkersCommand(ManyShulkers plugin) {
        this.plugin = plugin;
    }

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
