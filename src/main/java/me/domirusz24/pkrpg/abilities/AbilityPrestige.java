package me.domirusz24.pkrpg.abilities;

import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.attribute.AttributeModifier;
import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.pkrpg.config.PluginConfig;
import me.domirusz24.pkrpg.player.PKPlayer;
import me.domirusz24.pkrpg.util.UtilMethods;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;
import me.domirusz24.plugincore.config.language.dynamics.HoverableMessage;
import me.domirusz24.plugincore.core.placeholders.PlaceholderObject;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AbilityPrestige implements PlaceholderObject {

    private static final HashMap<String, HoverableMessage> STRING_BY_ATTRIBUTE = new HashMap<>();

    public static HoverableMessage getTranslated(String attribute) {
        if (!STRING_BY_ATTRIBUTE.containsKey(attribute)) {
            STRING_BY_ATTRIBUTE.put(attribute, HoverableMessage.of(PKRPG.plugin, attribute, "Attributes." + attribute, ""));
            ((PluginCore)PKRPG.plugin).configM.getLanguageConfig().save();
        }
        return STRING_BY_ATTRIBUTE.get(attribute);
    }

    public static AbilityPrestige from(PKPlayer player, String data) {
        String[] split1 = data.split("#");
        String[] split = split1[0].split(":");
        AbilityPrestige prestige = new AbilityPrestige(player, split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]));
        if (split1.length == 2) {
            for (String s : split1[1].split(";")) {
                String[] split2 = s.split(":");
                prestige.SKILL_POINTS.put(split2[0], Integer.parseInt(split2[1]));
            }
        }
        return prestige;
    }

    private final HashMap<String, Integer> SKILL_POINTS = new HashMap<>();

    private final PKPlayer player;

    private final String name;

    private int level;
    private int skillpoints;
    private int experience;
    private int kills;

    public AbilityPrestige(PKPlayer player, String name, int level, int experience, int kills, int skillpoints) {
        this.player = player;
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.kills = kills;
    }

    public String translateToString() {
        StringBuilder s = new StringBuilder(name + ":" + level + ":" + experience + ":" + kills + ":" + skillpoints + "#");
        for (String attri : SKILL_POINTS.keySet()) {
            s.append(attri).append(":").append(SKILL_POINTS.get(attri)).append(";");
        }
        return s.toString();
    }

    public void setKills(int kills) {
        this.kills = kills;
        updateData();
    }

    public static ConfigValue<List<String>> RANDOM_LEVEL_UP_MESSAGES = new ConfigValue<>("PKPlayer.Ability.OnLevel",
            Arrays.asList("&aYour ability %ability% leveled up to level %level%!", "&aYou feel as your %ability% is stronger... Your level is now %level%!"),
            ((PluginCore) PKRPG.plugin).configM.getLanguageConfig());


    public void onKill(LivingEntity entity) {
        setKills(kills + 1);
        int xp;
        if ((entity instanceof Player)) {
            xp = UtilMethods.randomNumber(PluginConfig.PLAYER_MIN_EXP.getValue(), PluginConfig.PLAYER_MAX_EXP.getValue());
        } else {
            xp = UtilMethods.randomNumber(PluginConfig.ENTITY_MIN_EXP.getValue(), PluginConfig.ENTITY_MAX_EXP.getValue());
        }
        setXP(experience + xp);
    }

    public void setXP(int xp) {
        setXP(xp, 0);
    }

    private void setXP(int xp, int levelCombo) {
        int levelNext = UtilMethods.getLevelRequirement(this.level + levelCombo + 1);
        if (xp >= levelNext) {
            setXP(xp - levelNext, levelCombo + 1);
            return;
        }
        this.experience = xp;
        levelUp(levelCombo);
        updateData();
    }

    public void setSkillPointAmount(int skillpoints) {
        this.skillpoints = skillpoints;
        updateData();
    }

    public void setSkillPoints(String attribute, int amount) {
        SKILL_POINTS.put(attribute, amount);
        updateData();
    }

    public void levelUp(int times) {
        if (times <= 0) return;
        this.level = level + times;
        this.skillpoints = skillpoints + times;
        updateData();
        String message = RANDOM_LEVEL_UP_MESSAGES.getValue().get((int) (RANDOM_LEVEL_UP_MESSAGES.getValue().size() * Math.random()));
        message = message.replaceAll("%level%", String.valueOf(level)).replaceAll("%ability%", getName());
        message = UtilMethods.translateColor(message);
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.getPlayer().sendMessage(message);
    }

    public void handle(CoreAbility ability) {
        for (String s : SKILL_POINTS.keySet()) {
            PluginConfig.AttributeData data = PluginConfig.getAttributeData(s);
            double amount = ((double) data.getAbilityPrestigePercent() / 100d) * (double) SKILL_POINTS.get(s);
            ability.addAttributeModifier(s, data.getPositive() ? 1 + amount : 1 - amount, AttributeModifier.MULTIPLICATION);
        }
    }

    private void updateData() {
        player.updatePrestige();
    }

    public CoreAbility getAbility() {
        return CoreAbility.getAbility(name);
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public int getExperience() {
        return experience;
    }

    public PKPlayer getPlayer() {
        return player;
    }

    public int getSkillPointAmount() {
        return skillpoints;
    }

    public int getSkillPoints(String attribute) {
        return SKILL_POINTS.getOrDefault(attribute, 0);
    }

    @Override
    public String onPlaceholderRequest(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "skillpoints":
                return String.valueOf(getSkillPointAmount());
            case "experience":
                return String.valueOf(getExperience());
            case "level":
                return String.valueOf(getLevel());
            case "kills":
                return String.valueOf(getKills());
            case "name":
                return getName();
        }
        return null;
    }

    @Override
    public String placeHolderPrefix() {
        return "ability";
    }

    @Override
    public PluginCore getCorePlugin() {
        return PKRPG.plugin;
    }
}
