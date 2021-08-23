package me.domirusz24.pkrpg.abilities;

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

public class PKAbilityPointPanel extends ChatGUI {

    private final AbilityPrestige prestige;

    private final PKPlayer pkPlayer;

    public PKAbilityPointPanel(AbilityPrestige prestige, Player player) {
        super(PKRPG.plugin, player);
        this.prestige = prestige;
        this.pkPlayer = PKPlayer.of(player);
        update();
    }

    @Language("GUI.PKAbilityPointPanel.Top")
    public static String LANG_TOP = "&6%player_name% - %element_color%%ability_name%||&7Level &6&l%ability_level%||&7Skill points available: &6&l%ability_skillpoints%|| ||&7Kills: &6%ability_kills%";

    @Language("GUI.PKAbilityPointPanel.Upgrade")
    public static String LANG_UPGRADE = "&7Click to upgrade to: &6%upgrade%";


    @Override
    protected void _update() {
        reset();
        line(pkPlayer.getStartingElement() == null ? "&7" : pkPlayer.getStartingElement().getColor().toString());
        newLine();
        tab(10).putX();
        newLine();
        addMessage(applyPlaceholders(LANG_TOP));
        newLine();
        addHoverMessage("&6[" + UtilMethods.getProgressBar((double) prestige.getExperience() / (double) UtilMethods.getLevelRequirement(prestige.getLevel() + 1), 100, "&a", "&7") + "&6]",
                "&6" + prestige.getExperience() + " &7/ &6" + UtilMethods.getLevelRequirement(prestige.getLevel() + 1));

        newLine(2);
        for (String attribute : UtilMethods.getAttributes(prestige.getAbility().getClass()).keySet()) {
            PluginConfig.AttributeData data = PluginConfig.getAttributeData(attribute);
            int level = prestige.getSkillPoints(attribute);
            String show = "&6" + (data.getPositive() ? "+" : "-") + (data.getAbilityPrestigePercent() * level) + "%\n" + data.getTranslated().getHover().get();
            tab().addHoverMessage("&7→ &6" + data.getTranslated().get() + " &7(&6" + level + "&7)", show);
            if (prestige.getSkillPointAmount() > 0) {
                show = LANG_UPGRADE.replaceAll("%upgrade%", (data.getPositive() ? "+" : "-") + (data.getAbilityPrestigePercent() * (level + 1)));
                addHoverAndClickMessage(" &d&l+", show, (p) -> {
                    prestige.setSkillPoints(attribute, level + 1);
                    prestige.setSkillPointAmount(prestige.getSkillPointAmount() - 1);
                    update();
                });
            }
            newLine();
        }
        newLine();
        line(pkPlayer.getStartingElement() == null ? "&7" : pkPlayer.getStartingElement().getColor().toString());
    }

    @Override
    public void onX() {
        unregister();
        new PKElementPointsPanel(player, prestige.getAbility().getElement());
    }

    private String applyPlaceholders(String message) {
        return PlaceholderAPI.setPlaceholders(player, PAPIManager.setPlaceholder(prestige, message.replaceAll("%element%", prestige.getAbility().getElement().getName()).replaceAll("%element_color%", prestige.getAbility().getElement().getColor().toString())));
    }

    @Override
    public boolean resetOnMove() {
        return true;
    }
}
