package com.factionenchants.enchantments;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment {

    private final String id;
    private final String displayName;
    private final int maxLevel;
    private final EnchantTier tier;
    private final ApplicableGear[] applicableGear;

    public CustomEnchantment(String id, String displayName, int maxLevel, EnchantTier tier, ApplicableGear... applicableGear) {
        this.id = id;
        this.displayName = displayName;
        this.maxLevel = maxLevel;
        this.tier = tier;
        this.applicableGear = applicableGear;
    }

    /** Legacy hook — override freely; no longer abstract. */
    public void onActivate(Player player, int level, ItemStack item) {}

    /** Called when this enchant is on the held weapon and strikes a living entity. */
    public void onHitEntity(Player attacker, LivingEntity target, int level, EntityDamageByEntityEvent event) {}

    /** Called when this enchant is on armor and the wearer takes damage. */
    public void onHurtBy(Player defender, Entity attacker, int level, EntityDamageByEntityEvent event) {}

    /** Called when this enchant is on the bow/crossbow and an arrow hits an entity. */
    public void onArrowHit(Player shooter, LivingEntity target, int level, EntityDamageByEntityEvent event) {}

    /** Called when the holder of this enchant kills a living entity. */
    public void onKillEntity(Player killer, LivingEntity victim, int level, ItemStack weapon) {}

    /** Called periodically for passive effects (armour worn, tool in hand). */
    public void onTickPassive(Player player, int level, ItemStack equipment) {}

    /**
     * Soul charges consumed each time this enchant procs via an event  
     * (onHitEntity, onHurtBy, onKillEntity, onArrowHit).  Only applies
     * to SOUL-tier enchants; 0 = no soul cost.
     */
    public int getSoulCostPerProc() {
        return tier == EnchantTier.SOUL ? 3 : 0;
    }

    /**
     * Soul charges consumed every second this enchant's onTickPassive fires.
     * Only applies to SOUL-tier enchants; 0 = no soul cost.
     */
    public int getSoulCostPerTick() {
        return tier == EnchantTier.SOUL ? 1 : 0;
    }

    /**
     * Heroic enchants may require a prerequisite enchant on the item.
     * Return the enchant ID string, or null if there is no prerequisite.
     */
    public String getPrerequisiteEnchantId() { return null; }

    public boolean canApplyTo(ItemStack item) {
        if (item == null) return false;
        for (ApplicableGear gear : applicableGear) {
            if (gear.matches(item)) return true;
        }
        return false;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public int getMaxLevel() { return maxLevel; }
    public EnchantTier getTier() { return tier; }
    public ApplicableGear[] getApplicableGear() { return applicableGear; }
    public String getDescription() { return ""; }

    public String getApplicableGearDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < applicableGear.length; i++) {
            String n = applicableGear[i].name();
            String formatted = n.charAt(0) + n.substring(1).toLowerCase().replace("_", " ");
            sb.append(formatted);
            if (i < applicableGear.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public enum EnchantTier {
        SIMPLE("Simple", "f"),
        UNIQUE("Unique", "a"),
        ELITE("Elite", "b"),
        ULTIMATE("Ultimate", "e"),
        LEGENDARY("Legendary", "6"),
        SOUL("Soul", "c"),
        HEROIC("Heroic", "d"),
        MASTERY("Mastery", "4");

        private final String displayName;
        private final String color;

        EnchantTier(String displayName, String color) {
            this.displayName = displayName;
            this.color = color;
        }

        public String getDisplayName() { return displayName; }
        public String getColor() { return color; }
    }

    public enum ApplicableGear {
        SWORD, AXE, PICKAXE, SHOVEL, HOE,
        HELMET, CHESTPLATE, LEGGINGS, BOOTS,
        BOW, CROSSBOW, TRIDENT,
        WEAPON,   // sword + axe + trident
        TOOL_ALL, // pickaxe + shovel + axe + hoe
        ARMOR,    // helmet + chestplate + leggings + boots
        ALL;

        public boolean matches(ItemStack item) {
            if (item == null) return false;
            String type = item.getType().name();
            return switch (this) {
                case SWORD -> type.endsWith("_SWORD");
                case AXE -> type.endsWith("_AXE");
                case PICKAXE -> type.endsWith("_PICKAXE");
                case SHOVEL -> type.endsWith("_SHOVEL");
                case HOE -> type.endsWith("_HOE");
                case HELMET -> type.endsWith("_HELMET");
                case CHESTPLATE -> type.endsWith("_CHESTPLATE");
                case LEGGINGS -> type.endsWith("_LEGGINGS");
                case BOOTS -> type.endsWith("_BOOTS");
                case BOW -> type.equals("BOW");
                case CROSSBOW -> type.equals("CROSSBOW");
                case TRIDENT -> type.equals("TRIDENT");
                case WEAPON -> type.endsWith("_SWORD") || type.endsWith("_AXE") || type.equals("TRIDENT");
                case TOOL_ALL -> type.endsWith("_PICKAXE") || type.endsWith("_SHOVEL") || type.endsWith("_AXE") || type.endsWith("_HOE");
                case ARMOR -> type.endsWith("_HELMET") || type.endsWith("_CHESTPLATE") || type.endsWith("_LEGGINGS") || type.endsWith("_BOOTS");
                case ALL -> true;
            };
        }
    }
}
