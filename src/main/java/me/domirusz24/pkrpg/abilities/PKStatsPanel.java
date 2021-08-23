package me.domirusz24.pkrpg.abilities;

import com.projectkorra.projectkorra.attribute.Attribute;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.pkrpg.config.PluginConfig;
import me.domirusz24.pkrpg.manager.PAPIManager;
import me.domirusz24.pkrpg.player.PKPlayer;
import me.domirusz24.pkrpg.util.UtilMethods;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.core.chatgui.ChatGUI;
import org.bukkit.entity.Player;

public class PKStatsPanel extends ChatGUI {

    private final PKPlayer pkPlayer;

    public PKStatsPanel(Player player) {
        super(PKRPG.plugin, player);
        this.pkPlayer = PKPlayer.of(player);
        update();
    }

    @Language("GUI.PKStatsPanel.Top")
    public static String LANG_TOP = "&6%player_name% - &dStats||&7Level &6&l%pkrpg_level%||&7Skill points available: &6&l%pkrpg_skillpoints%|| ||&7Kills: &6%pkrpg_kills%";

    @Language("GUI.PKStatsPanel.Upgrade")
    public static String LANG_UPGRADE = "&7Click to upgrade to: &6%upgrade%";


    @Override
    protected void _update() {
        reset();
        line(pkPlayer.getStartingElement() == null ? "&7" : pkPlayer.getStartingElement().getColor().toString());
        newLine();
        tab(10).putX();
        newLine();
        addMessage(PlaceholderAPI.setPlaceholders(player, LANG_TOP));
        newLine();
        addHoverMessage("&6[" + UtilMethods.getProgressBar((double) pkPlayer.getExpierence() / (double) UtilMethods.getLevelRequirement(pkPlayer.getLevel() + 1), 100, "&a", "&7") + "&6]",
                "&6" + pkPlayer.getExpierence() + " &7/ &6" + UtilMethods.getLevelRequirement(pkPlayer.getLevel() + 1));

        newLine(2);
        for (String attribute : UtilMethods.BASE_ATTRIBUTES) {
            PluginConfig.AttributeData data = PluginConfig.getAttributeData(attribute);
            int level = pkPlayer.getSkillPoints(attribute);
            String show = "&6" + (data.getPositive() ? "+" : "-") + (data.getNormalPercent() * level) + "%\n" + data.getTranslated().getHover().get();
            tab().addHoverMessage("&7→ &6" + data.getTranslated().get() + " &7(&6" + level + "&7)", show);
            if (pkPlayer.getSkillPointsAvailable() > 0) {
                show = LANG_UPGRADE.replaceAll("%upgrade%", (data.getPositive() ? "+" : "-") + (data.getNormalPercent() * (level + 1)));
                addHoverAndClickMessage(" &d&l+", show, (p) -> {
                    pkPlayer.setSkillPoints(attribute, level + 1);
                    pkPlayer.setSkillPoints(pkPlayer.getSkillPointsAvailable() - 1);
                    update();
                });
            }
            newLine();
        }
        newLine();
        line(pkPlayer.getStartingElement() == null ? "&7" : pkPlayer.getStartingElement().getColor().toString());
    }

    private String applyPlaceholders(String message) {
        return PlaceholderAPI.setPlaceholders(player, message);
    }

    @Override
    public boolean resetOnMove() {
        return true;
    }
}
