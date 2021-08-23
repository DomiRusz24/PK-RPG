package me.domirusz24.pkrpg.util;

import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import me.domirusz24.pkrpg.config.PluginConfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class UtilMethods extends me.domirusz24.plugincore.util.UtilMethods {

    public static final String[] BASE_ATTRIBUTES = new String[] {
            Attribute.DAMAGE,
            Attribute.COOLDOWN,
            Attribute.DURATION,
            Attribute.SPEED,
            Attribute.RANGE,
            Attribute.KNOCKBACK
    };

    public static Map<Class<? extends CoreAbility>, Map<String, Field>> ATTRIBUTE_FIELDS;

    static {
        try {
            Field f = CoreAbility.class.getDeclaredField("ATTRIBUTE_FIELDS");
            f.setAccessible(true);
            ATTRIBUTE_FIELDS = (Map<Class<? extends CoreAbility>, Map<String, Field>>) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public UtilMethods(me.domirusz24.plugincore.util.UtilMethods utilMethods) {
        super(utilMethods);
    }

    public static int getLevelRequirement(int level) {
        if (level == 1 || level == 0) {
           return 50;
        } else {
            return (int) ((2 * level - 1) * PluginConfig.LEVEL_MULTI.getValue());
        }
    }

    public static Map<String, Field> getAttributes(Class<? extends CoreAbility> clazz) {
        return ATTRIBUTE_FIELDS.getOrDefault(clazz, null);
    }
}
