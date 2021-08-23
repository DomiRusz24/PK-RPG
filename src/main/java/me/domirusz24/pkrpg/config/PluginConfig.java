package me.domirusz24.pkrpg.config;

import me.domirusz24.pkrpg.PKRPG;
import me.domirusz24.pkrpg.abilities.AbilityPrestige;
import me.domirusz24.plugincore.PluginCore;
import me.domirusz24.plugincore.config.AbstractConfig;
import me.domirusz24.plugincore.config.configvalue.ConfigValue;
import me.domirusz24.plugincore.config.language.dynamics.HoverableMessage;

import java.util.HashMap;

public class PluginConfig {
    public static ConfigValue<Integer> PLAYER_MIN_EXP = new ConfigValue<>("Experience.Player.Min", 40, ((PluginCore) PKRPG.plugin).configM.getConfig());
    public static ConfigValue<Integer> PLAYER_MAX_EXP = new ConfigValue<>("Experience.Player.Min", 50, ((PluginCore) PKRPG.plugin).configM.getConfig());

    public static ConfigValue<Integer> ENTITY_MIN_EXP = new ConfigValue<>("Experience.Entity.Min", 25, ((PluginCore) PKRPG.plugin).configM.getConfig());
    public static ConfigValue<Integer> ENTITY_MAX_EXP = new ConfigValue<>("Experience.Entity.Max", 30, ((PluginCore) PKRPG.plugin).configM.getConfig());

    public static ConfigValue<Double> LEVEL_MULTI = new ConfigValue<>("Experience.LevelMulti", 30d, ((PluginCore) PKRPG.plugin).configM.getConfig());

    private static HashMap<String, AttributeData> ATTRIBUTE_DATA = new HashMap<>();

    public static AttributeData getAttributeData(String attribute) {
        if (!ATTRIBUTE_DATA.containsKey(attribute)) {
            ATTRIBUTE_DATA.put(attribute, new AttributeData(attribute));
        }
        return ATTRIBUTE_DATA.get(attribute);
    }

    public static class AttributeData {

        private final ConfigValue<Boolean> positive;

        private final ConfigValue<Integer> abilityPrestigePercent;

        private final ConfigValue<Integer> normalPercent;

        private final String attribute;

        private AttributeData(String attribute) {
            this.attribute = attribute;
            String path = "AttributeData." + attribute + ".";
            AbstractConfig c = ((PluginCore) PKRPG.plugin).configM.getConfig();
            positive = new ConfigValue<>(path + "positive", true, c);
            abilityPrestigePercent = new ConfigValue<>(path + "AbilityPrestigePercent", 5, c);
            normalPercent = new ConfigValue<>(path + "NormalPercent", 1, c);
        }

        public HoverableMessage getTranslated() {
            return AbilityPrestige.getTranslated(attribute);
        }

        public String getAttribute() {
            return attribute;
        }

        public boolean getPositive() {
            return positive.getValue();
        }

        public int getAbilityPrestigePercent() {
            return abilityPrestigePercent.getValue();
        }

        public int getNormalPercent() {
            return normalPercent.getValue();
        }
    }
}
