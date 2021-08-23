package me.domirusz24.pkrpg.manager;

import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.pkrpg.player.PKPlayer;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import org.bukkit.entity.Player;

public class PAPIManager extends me.domirusz24.plugincore.managers.PAPIManager {
    public PAPIManager(PluginCore plugin) {
        super(plugin);
    }

    @Override
    protected String onPlaceholderRequest(String s, String s1) {
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String s) {
        PKPlayer pk = PKPlayer.of(player);
        s = s.toLowerCase();
        switch (s) {
            case "skillpoints":
                return String.valueOf(pk.getSkillPointsAvailable());
            case "experience":
                return String.valueOf(pk.getExpierence());
            case "level":
                return String.valueOf(pk.getLevel());
            case "deaths":
                return String.valueOf(pk.getDeaths());
            case "kills":
                return String.valueOf(pk.getKills());
            case "starting_element":
                return pk.getStartingElement().getPrefix();
        }
        return null;
    }

    public static String setPlaceholder(PlaceholderObject object, String text) {
        return PKRPG.papiM.setPlaceholders(object, text);
    }
}
