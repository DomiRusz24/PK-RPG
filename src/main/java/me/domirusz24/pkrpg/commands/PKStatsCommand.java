package me.domirusz24.pkrpg.commands;

import com.projectkorra.projectkorra.Element;
import me.domirusz24.pkrpg.abilities.PKElementPointsPanel;
import me.domirusz24.pkrpg.abilities.PKStatsPanel;
import me.domirusz24.pkrpg.commands.core.ElementCommand;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.BaseCommand;
import me.domirusz24.plugincore.config.annotations.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.permissions.PermissionDefault;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PKStatsCommand extends ElementCommand {

    @Override
    public void selfExecute(CommandSender commandSender, Element element) {
        if (isPlayer(commandSender)) {
            new PKElementPointsPanel((Player) commandSender, element);
        }
    }

    @Language("Command.PKStatsCommand.ElementNotExist")
    public static String LANG_ELEMENT_NOT_EXIST = "&cThat element doesn't exist!";

    @Override
    public void selfExecute(CommandSender commandSender, String s) {
        commandSender.sendMessage(LANG_ELEMENT_NOT_EXIST);
    }

    @Override
    public void selfExecute(CommandSender commandSender) {
        if (isPlayer(commandSender)) {
            new PKStatsPanel((Player) commandSender);
        }
    }

    @Override
    protected List<String> aliases() {
        return Collections.singletonList("pkstat");
    }

    @Override
    protected String name() {
        return "pkstats";
    }

    @Language("Command.PKStatsCommand.Description")
    public static String LANG_DESCRIPTION = "Allows you to spend your skill points.";

    @Override
    protected String description() {
        return LANG_DESCRIPTION;
    }

    @Override
    public PermissionDefault getPermissionDefault() {
        return PermissionDefault.TRUE;
    }
}
