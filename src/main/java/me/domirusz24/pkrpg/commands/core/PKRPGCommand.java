package me.domirusz24.pkrpg.commands.core;

import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.BaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

public class PKRPGCommand extends BaseCommand {
    @Override
    public void selfExecute(CommandSender commandSender) {
        help(commandSender, false);
    }

    @Override
    protected String name() {
        return "pkrpg";
    }

    @Override
    protected String description() {
        return "PKRPG control";
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return null;
    }

    @Override
    public PluginCore getCorePlugin() {
        return PKRPG.plugin;
    }
}
