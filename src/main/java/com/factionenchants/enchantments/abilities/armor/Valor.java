package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Valor extends CustomEnchantment {

    public Valor() {
        super("valor", "Valor", 5, EnchantTier.ULTIMATE, ApplicableGear.ARMOR);
    }

    @Override
    public String getDescription() {
        return "Reduces incoming damage while wielding a non-heroic sword. This enchantment is stackable.";
    }

    @Override
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {
        ItemStack held = defender.getInventory().getItemInMainHand();
        if (!held.getType().name().endsWith("_SWORD")) return;

        // Check item is "non-heroic" — no HEROIC tier enchant in its lore
        boolean isHeroic = held.hasItemMeta()
                && held.getItemMeta().hasLore()
                && held.getItemMeta().getLore().stream()
                        .anyMatch(l -> l.contains("Heroic"));
        if (isHeroic) return;

        // Reduce incoming damage by 3% per level (stackable across armor pieces)
        event.setDamage(event.getDamage() * (1.0 - level * 0.03));
    }
}
