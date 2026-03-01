# Faction Enchants Plugin

## Overview
The Faction Enchants Plugin is a custom Minecraft plugin designed for faction servers, providing a robust enchantment system with various categories of enchantments. Players can enhance their gear with unique abilities, purchase enchant books, and combine them to create more powerful versions.

## Features
- **Custom Enchantments**: Implemented enchantments categorized into Simple, Unique, Elite, Ultimate, Legendary, and Soul.
- **Enchant Books**: Players can acquire enchant books that allow them to apply enchantments to their gear.
- **Commands**:
  - `/enchanter`: Allows players to purchase enchant books using XP.
  - `/alchemist`: Enables players to combine two identical enchant books to create a higher-level book.
  - `/fegiverandomgear <player> <material> [minEnchants] [maxEnchants]`: Admin command to generate random custom-enchanted gear.
- **Ability Management**: Different abilities are defined for various gear types, enhancing gameplay and strategy.

## Installation
1. Download the latest version of the Faction Enchants Plugin.
2. Place the plugin JAR file into the `plugins` folder of your Minecraft server.
3. Restart the server to generate the configuration files.
4. Customize the `config.yml` and `messages.yml` files as needed.

## Configuration
- The `config.yml` file allows server administrators to adjust settings such as enchantment rates and command permissions.
- The `messages.yml` file contains customizable messages for player interactions, enabling localization support.

## Usage
- Players can use the `/enchanter` command to purchase enchant books.
- To combine enchant books, players can use the `/alchemist` command, ensuring they have two identical books.

### Randomized Custom-Enchanted Gear (Envoys/GKits)
- The plugin now includes `RandomGearManager`, which can generate gear with randomized custom enchants.
- This can be used by envoy/gkit integrations to create pre-enchanted random loot.
- Java usage example:

```java
ItemStack base = new ItemStack(Material.DIAMOND_CHESTPLATE);
ItemStack randomGear = FactionEnchantsPlugin.getInstance()
        .getRandomGearManager()
        .generateRandomEnchantedGear(base);
```

- You can tune randomness in `config.yml` under `random-gear`:
  - `min-enchants`, `max-enchants`
  - tier weights under `random-gear.tier-weights`

## Development
This plugin is built using Java and follows the Bukkit/Spigot API conventions. Contributions and improvements are welcome. Please refer to the codebase for detailed implementation and structure.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.