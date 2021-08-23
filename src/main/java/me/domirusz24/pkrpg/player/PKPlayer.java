package me.domirusz24.pkrpg.player;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.attribute.AttributeModifier;
import me.clip.placeholderapi.PlaceholderAPI;
import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.pkrpg.RPGDataTables;
import me.domirusz24.pkrpg.abilities.AbilityPrestige;
import me.domirusz24.pkrpg.config.PluginConfig;
import me.domirusz24.pkrpg.util.UtilMethods;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.attributes.PlayerAttribute;
import me.domirusz24.plugincore.attributes.SQLAttribute;
import me.domirusz24.plugincore.config.annotations.Language;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;
import me.domirusz24.plugincore.core.players.PlayerData;
import me.domirusz24.plugincore.managers.database.DataBaseTable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

public class PKPlayer extends PlayerData {

    public static PKPlayer of(Player player) {
        return of(player.getName(), player.getUniqueId());
    }

    public static PKPlayer of(String name, UUID uuid) {
        return (PKPlayer) PKRPG.playerDataM.getPlayer(name, uuid);
    }

    public static boolean exists(Player player) {
        return exists(player.getUniqueId());
    }

    public static boolean exists(UUID uuid) {
        return PKRPG.playerDataM.exists(uuid);
    }

    public PKPlayer(String name, UUID uuid) {
        super(PKRPG.plugin, name, uuid);
    }

    private Element startingElement = null;

    private int skillpoints = 0;

    private int experience = 0;

    private int level = 1;

    private int deaths = 0;

    private int kills = 0;

    private final HashMap<String, AbilityPrestige> ABILITY_PRESTIGE = new HashMap<>();

    private final HashMap<String, Integer> SKILL_POINTS = new HashMap<>();

    public void onDeath() {
        setDeath(deaths + 1);
    }

    public static ConfigValue<List<String>> RANDOM_KILL_MESSAGES_ENTITIES = new ConfigValue<>("PKPlayer.OnKill.Entity",
            Arrays.asList("&7You have gained %xp% XP.", "&7The power surges through you... You gain %xp% XP"),
            ((PluginCore) PKRPG.plugin).configM.getLanguageConfig());

    public static ConfigValue<List<String>> RANDOM_KILL_MESSAGES_PLAYERS = new ConfigValue<>("PKPlayer.OnKill.Player",
            Arrays.asList("&7You have obliterated %player_name%, you gained %xp% XP.", "&7%player_name% didn't stand a chance! You gained %xp% XP."),
            ((PluginCore) PKRPG.plugin).configM.getLanguageConfig());

    public void onKill(LivingEntity entity) {
        setKills(kills + 1);
        int xp;
        if ((entity instanceof Player)) {
            xp = UtilMethods.randomNumber(PluginConfig.PLAYER_MIN_EXP.getValue(), PluginConfig.PLAYER_MAX_EXP.getValue());
            List<String> l = RANDOM_KILL_MESSAGES_PLAYERS.getValue();
            getPlayer().sendMessage(PlaceholderAPI.setPlaceholders((Player) entity, UtilMethods.translateColor(l.get((int) (l.size() * Math.random()))).replaceAll("%xp%", String.valueOf(xp))));
        } else {
            xp = UtilMethods.randomNumber(PluginConfig.ENTITY_MIN_EXP.getValue(), PluginConfig.ENTITY_MAX_EXP.getValue());
            List<String> l = RANDOM_KILL_MESSAGES_ENTITIES.getValue();
            getPlayer().sendMessage(UtilMethods.translateColor(l.get((int) (l.size() * Math.random()))).replaceAll("%xp%", String.valueOf(xp)));
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
        getAttribute(PlayerAttribute.SQL).setIntegerValue("experience", xp);
        levelUp(levelCombo);
    }

    public static ConfigValue<List<String>> RANDOM_LEVEL_UP_MESSAGES = new ConfigValue<>("PKPlayer.OnLevel",
            Arrays.asList("&aYou have leveled up to level %level%!", "&aYou feel the chi flowing through you... Your level is now %level%!"),
            ((PluginCore) PKRPG.plugin).configM.getLanguageConfig());

    @Language("PKPlayer.LevelUp")
    public static String LANG_LEVELUP = "&a&lLevel up!";

    public void levelUp(int times) {
        if (times <= 0) return;
        setLevel(level + times);
        setSkillPoints(skillpoints + times);
        getPlayer().playSound(getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        String message = RANDOM_LEVEL_UP_MESSAGES.getValue().get((int) (RANDOM_LEVEL_UP_MESSAGES.getValue().size() * Math.random()));
        message = message.replaceAll("%level%", String.valueOf(level));
        message = UtilMethods.translateColor(message);
        getPlayer().sendTitle("", LANG_LEVELUP, 5, 40, 5);
        getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        getPlayer().sendMessage(message);
    }

    public void setDeath(int deaths) {
        this.deaths = deaths;
        getAttribute(PlayerAttribute.SQL).setIntegerValue("deaths", deaths);
    }

    private void setLevel(int level) {
        this.level = level;
        getAttribute(PlayerAttribute.SQL).setIntegerValue("level", level);
    }

    public void setKills(int kills) {
        this.kills = kills;
        getAttribute(PlayerAttribute.SQL).setIntegerValue("kills", kills);
    }

    public void setSkillPoints(int skillpoints) {
        this.skillpoints = skillpoints;
        getAttribute(PlayerAttribute.SQL).setIntegerValue("skillpoints", skillpoints);
    }

    public void setStartingElement(Element startingElement) {
        this.startingElement = startingElement;
        getAttribute(PlayerAttribute.SQL).setStringValue("starting_element", startingElement.getName());
    }

    public void updatePrestige() {
        StringBuilder b = new StringBuilder();
        for (AbilityPrestige value : ABILITY_PRESTIGE.values()) {
            b.append(value.translateToString()).append(";");
        }
        getAttribute(PlayerAttribute.SQL).setStringValue("ability_prestige", b.toString());
    }

    public void updateSkillPoints() {
        StringBuilder b = new StringBuilder();
        for (String s : SKILL_POINTS.keySet()) {
            b.append(s).append(":").append(SKILL_POINTS.get(s)).append(";");
        }
        getAttribute(PlayerAttribute.SQL).setStringValue("skills", b.toString());
    }

    @Override
    protected void onSqlLoad() {
        SQLAttribute sql = getAttribute(PlayerAttribute.SQL);
        startingElement = Element.getElement(sql.getStringValue("starting_element"));
        experience = sql.getIntegerValue("experience");
        kills = sql.getIntegerValue("kills");
        deaths = sql.getIntegerValue("deaths");
        level = sql.getIntegerValue("level");
        if (level == 0) level = 1;
        skillpoints = sql.getIntegerValue("skillpoints");
        if (!sql.getStringValue("ability_prestige").equals("NONE")) {
            for (String s : sql.getStringValue("ability_prestige").split(";")) {
                AbilityPrestige prestige = AbilityPrestige.from(this, s);
                ABILITY_PRESTIGE.put(prestige.getName(), prestige);
            }
        }
        if (!sql.getStringValue("skills").equals("NONE")) {
            for (String s : sql.getStringValue("skills").split(";")) {
                System.out.println(s);
                String[] split = s.split(":");
                SKILL_POINTS.put(split[0], Integer.parseInt(split[1]));
            }
        }
    }

    @Override
    protected void onPlayerJoin() {

    }

    public int getKills() {
        return kills;
    }

    public int getLevel() {
        return level;
    }

    public Element getStartingElement() {
        return startingElement;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getExpierence() {
        return experience;
    }

    public int getSkillPointsAvailable() {
        return skillpoints;
    }

    public int getSkillPoints(String attribute) {
        return SKILL_POINTS.getOrDefault(attribute, 0);
    }

    public void setSkillPoints(String attribute, int amount) {
        SKILL_POINTS.put(attribute, amount);
        updateSkillPoints();
    }

    public void handle(CoreAbility ability) {

        if (startingElement != null && ability.getElement().getName().equals(startingElement.getName())) {
            for (String attri : UtilMethods.getAttributes(ability.getClass()).keySet()) {
                PluginConfig.AttributeData data = PluginConfig.getAttributeData(attri);
                double amount = (double) data.getAbilityPrestigePercent() / 100d;
                ability.addAttributeModifier(attri, data.getPositive() ? 1 + amount : 1 - amount, AttributeModifier.MULTIPLICATION);
            }
        }

        getPrestige(ability.getName()).handle(ability);
    }

    public AbilityPrestige getPrestige(String name) {
        if (!ABILITY_PRESTIGE.containsKey(name)) {
            ABILITY_PRESTIGE.put(name, new AbilityPrestige(this, name, 1, 0, 0, 0));
        }
        return ABILITY_PRESTIGE.getOrDefault(name, null);
    }

    @Override
    public DataBaseTable getTable() {
        return RPGDataTables.playerData;
    }
}
