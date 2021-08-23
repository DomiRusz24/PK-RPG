package me.domirusz24.pkrpg;

import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.event.AbilityStartEvent;
import com.projectkorra.projectkorra.event.EntityBendingDeathEvent;
import com.projectkorra.projectkorra.util.DamageHandler;
import me.domirusz24.pkrpg.abilities.AbilityPrestige;
import me.domirusz24.pkrpg.player.PKPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PKRPGListener implements Listener {

    @EventHandler
    public void onAbility(AbilityStartEvent event) {
        if (event.getAbility() instanceof CoreAbility) {
            if (event.getAbility() instanceof PassiveAbility) return;
            if (PKPlayer.exists(event.getAbility().getPlayer())) {
                CoreAbility ability = (CoreAbility) event.getAbility();
                PKPlayer.of(event.getAbility().getPlayer()).handle(ability);
            }
        }
    }


    @EventHandler
    public void onPKDeath(EntityBendingDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            PKPlayer player = PKPlayer.of((Player) event.getEntity());
            player.onDeath();
        }
        if (event.getEntity() instanceof LivingEntity) {
            PKPlayer killer = PKPlayer.of(event.getAttacker());
            killer.onKill((LivingEntity) event.getEntity());
            killer.getPrestige(event.getAbility().getName()).onKill((LivingEntity) event.getEntity());
        }
    }
}
