package me.domirusz24.pkrpg.commands.core;

import com.projectkorra.projectkorra.Element;
import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.CustomBaseSubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

public abstract class ElementSubCommand extends CustomBaseSubCommand<Element> {

    @Override
    public Element translateInstance(String s) {
        return Element.getElement(s);
    }

    @Override
    public PluginCore getCorePlugin() {
        return PKRPG.plugin;
    }
}
