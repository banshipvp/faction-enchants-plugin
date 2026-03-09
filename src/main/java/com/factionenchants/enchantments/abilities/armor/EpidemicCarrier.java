package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EpidemicCarrier extends CustomEnchantment {

    public EpidemicCarrier() {
        super("epidemic_carrier", "Epidemic Carrier", 8, EnchantTier.HEROIC, ApplicableGear.LEGGINGS);
    }

    @Override
    public String getDescription() {
        return "Spawns multiple creepers on death.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        // Creeper spawning handled in EnchantListener on death
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "plague_carrier";
    }
}
