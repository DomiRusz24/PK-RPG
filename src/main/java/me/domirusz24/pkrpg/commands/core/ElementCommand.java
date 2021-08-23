package me.domirusz24.pkrpg.commands.core;

import com.projectkorra.projectkorra.Element;
import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.command.abstractclasses.CustomBaseCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ElementCommand extends CustomBaseCommand<Element> {
    @Override
    public void onFail(CommandSender commandSender, String s) {}

    @Override
    public Collection<String> getInstances() {
        return Stream.of(Element.getElements()).map(Element::getName).collect(Collectors.toList());
    }

    @Override
    public Element translateInstance(String s) {
        return Element.getElement(s);
    }

    @Override
    protected String usage() {
        return "/" + getName() + " <element> ";
    }

    @Override
    public PluginCore getCorePlugin() {
        return PKRPG.plugin;
    }
}
