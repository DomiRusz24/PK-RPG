package me.domirusz24.pkrpg;

import me.domirusz24.plugincore.managers.database.DataBaseTable;
import me.domirusz24.plugincore.managers.database.PlayerDataBaseTable;
import me.domirusz24.plugincore.managers.database.values.*;

public class RPGDataTables {

    public static DataBaseTable playerData = null;

    public static DataBaseTable[] getTables() {
        playerData = new PlayerDataBaseTable(PKRPG.SqlM) {
            @Override
            public DataBaseValue<?>[] _getValues() {
                return new DataBaseValue[]{
                        new StringValue("starting_element", 64),
                        new IntegerValue("experience"),
                        new IntegerValue("kills"),
                        new IntegerValue("deaths"),
                        new IntegerValue("level"),
                        new IntegerValue("skillpoints"),
                        new MediumTextValue("ability_prestige"),
                        new MediumTextValue("skills"),
                };
            }

            @Override
            public String getName() {
                return "pk_rpg_players";
            }
        };
        return new DataBaseTable[]{playerData};
    }

}
