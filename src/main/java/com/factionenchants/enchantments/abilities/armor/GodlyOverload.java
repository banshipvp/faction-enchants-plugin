package com.factionenchants.enchantments.abilities.armor;

import com.factionenchants.enchantments.CustomEnchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Locale;

public class GodlyOverload extends CustomEnchantment {

    public GodlyOverload() {
        super("godly_overload", "Godly Overload", 3, EnchantTier.HEROIC, ApplicableGear.CHESTPLATE);
    }

    @Override
    public String getDescription() {
        return "Adds extra hearts on top of Overload plus combat boosts.";
    }

    @Override
    public void onTickPassive(Player player, int level, ItemStack equipment) {
        int overloadLevel = resolveOverloadLevel(equipment);
        int baseAmplifier = Math.max(0, overloadLevel - 1);
        int totalHealthBoostAmplifier = baseAmplifier + Math.max(1, level);

        player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 60, totalHealthBoostAmplifier, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, level + 1, true, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, level - 1, true, false));
    }

    private int resolveOverloadLevel(ItemStack equipment) {
        if (equipment == null || !equipment.hasItemMeta()) {
            return 0;
        }

        ItemMeta meta = equipment.getItemMeta();
        if (meta == null || !meta.hasLore()) {
            return 0;
        }

        List<String> lore = meta.getLore();
        if (lore == null) {
            return 0;
        }

        for (String line : lore) {
            if (line == null) {
                continue;
            }

            String stripped = line.replaceAll("§[0-9a-fk-or]", "").trim();
            String lower = stripped.toLowerCase(Locale.ROOT);
            if (!lower.startsWith("overload ")) {
                continue;
            }

            String[] parts = stripped.split("\\s+");
            if (parts.length < 2) {
                continue;
            }

            int parsed = parseLevelToken(parts[parts.length - 1]);
            if (parsed > 0) {
                return parsed;
            }
        }

        return 0;
    }

    private int parseLevelToken(String token) {
        if (token == null || token.isBlank()) {
            return 0;
        }

        String normalized = token.trim().toUpperCase(Locale.ROOT);
        return switch (normalized) {
            case "I" -> 1;
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
            case "VI" -> 6;
            case "VII" -> 7;
            case "VIII" -> 8;
            case "IX" -> 9;
            case "X" -> 10;
            default -> {
                try {
                    yield Integer.parseInt(normalized);
                } catch (NumberFormatException ignored) {
                    yield 0;
                }
            }
        };
    }

    @Override
    public String getPrerequisiteEnchantId() {
        return "overload";
    }
}
