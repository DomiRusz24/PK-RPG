package me.domirusz24.pkrpg.abilities;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.pkrpg.config.PluginConfig;
import me.domirusz24.pkrpg.player.PKPlayer;
import me.domirusz24.pkrpg.util.UtilMethods;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.core.chatgui.ChatGUI;
import org.bukkit.entity.Player;

public class PKElementPointsPanel extends ChatGUI {

    private final PKPlayer pkPlayer;

    private final Element element;

    public PKElementPointsPanel(Player player, Element element) {
        super(PKRPG.plugin, player);
        this.pkPlayer = PKPlayer.of(player);
        this.element = element;
        update();
    }

    @Language("GUI.PKElementPointsPanel.Top")
    public static String LANG_TOP = "&6%player_name% - %element_color%%element%";

    @Language("GUI.PKElementPointsPanel.Upgrade")
    public static String LANG_UPGRADE = "&6Click to see %element_color%%ability% &6stats!";


    @Override
    protected void _update() {
        reset();
        line(element.getColor().toString());
        newLine();
        tab(10).putX();
        newLine();
        addMessage(applyPlaceholders(LANG_TOP));
        newLine(2);
        BendingPlayer bp = BendingPlayer.getBendingPlayer(player);
        for (CoreAbility ability : CoreAbility.getAbilities()) {
            if (ability.getElement().getName().equals(element.getName()) && bp.canBind(ability)) {
                if (UtilMethods.getAttributes(ability.getClass()) != null) {
                    AbilityPrestige prestige = pkPlayer.getPrestige(ability.getName());
                    tab().addHoverAndClickMessage(
                            "&7→ " + element.getColor().toString() + ability.getName() + " &7(&6" + prestige.getLevel() + "&7)",
                            applyPlaceholders(LANG_UPGRADE.replaceAll("%ability%", ability.getName())),
                            (p) -> {
                                unregister();
                                new PKAbilityPointPanel(prestige, player);
                            });
                    newLine();
                }
            }
        }
        newLine();
        line(element.getColor().toString());
    }

    private String applyPlaceholders(String message) {
        return PlaceholderAPI.setPlaceholders(player, message.replaceAll("%element%", element.getName()).replaceAll("%element_color%", element.getColor().toString()));
    }

    @Override
    public boolean resetOnMove() {
        return true;
    }
}
