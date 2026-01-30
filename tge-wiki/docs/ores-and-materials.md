---
sidebar_position: 3
---

# Ores and Materials

The Great Expansion adds 8 new ore types to Minecraft, each generating at specific Y-levels in the Overworld.

## Ore Types

### Bauxite
**Bauxite** is an ore block that can be smelted to obtain Aluminum Ingots.

#### Generation
- Y-level: -16 to 128
- Veins per chunk: 7
- Ore per vein: 9
- Generates in: Stone, Deepslate

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Iron Pickaxe
- Drops: Bauxite (1)
- Experience: 0

#### Usage
Bauxite can be smelted in a furnace to produce Aluminum Ingots (1:1 ratio).

---

### Cassiterite Ore
**Cassiterite Ore** is an ore block that drops itself when mined. It can be smelted to obtain Tin Ingots.

#### Generation
- Y-level: Varies
- Generates in: Stone

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Stone Pickaxe
- Drops: Cassiterite Ore (1)
- Experience: 0

#### Usage
Cassiterite Ore can be smelted to produce Tin Ingots.

---

### Ilmenite Ore
**Ilmenite Ore** is a rare ore block that can be smelted to obtain Titanium Ingots.

#### Generation
- Y-level: Deep underground
- Rarity: Rare

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Iron Pickaxe
- Drops: Ilmenite Ore (1)
- Experience: 0

#### Usage
Ilmenite Ore is the only source of Titanium Ingots, required for crafting Titanium tools and armor.

---

### Galena Ore
**Galena Ore** is an ore block that can be smelted to obtain Lead Ingots.

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Iron Pickaxe
- Drops: Galena Ore (1)
- Experience: 0

---

### Cinnabar Ore
**Cinnabar Ore** is an ore block that can be smelted to obtain Mercury Ingots.

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Iron Pickaxe
- Drops: Cinnabar Ore (1)
- Experience: 0

---

### Aqualith Ore
**Aqualith Ore** is a rare ore that generates underwater. It drops Aqualith when mined.

#### Generation
- Biome: Ocean, River
- Location: Underwater

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Iron Pickaxe
- Drops: Aqualith Ore (1)
- Experience: 0

#### Usage
Aqualith can be crafted into blocks for storage or used to craft Aqualith tools and armor.

---

### Cryonite Ore
**Cryonite Ore** is an ore found in frozen biomes.

#### Generation
- Biome: Snowbound, frozen biomes
- Y-level: Underground

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Iron Pickaxe
- Drops: Cryonite Ore (1)
- Experience: 0

---

### Permafrost Iron Ore
**Permafrost Iron Ore** is a variant of Iron Ore found in frozen biomes.

#### Generation
- Biome: Snowbound, Wild Tundra
- Y-level: Surface to underground

#### Properties
- Hardness: 1.0
- Blast resistance: 10.0
- Tool required: Stone Pickaxe
- Drops: Permafrost Iron Ore (1)
- Experience: 0

#### Usage
Functions identically to regular Iron Ore when smelted.

---

## Ingots

### Metal Ingots

| Ingot | Source Ore | Smelting Time | Fuel Required |
|-------|------------|---------------|---------------|
| Aluminum Ingot | Bauxite | 10 seconds | 1 item |
| Tin Ingot | Cassiterite Ore | 10 seconds | 1 item |
| Lead Ingot | Galena Ore | 10 seconds | 1 item |
| Titanium Ingot | Ilmenite Ore | 10 seconds | 1 item |
| Mercury Ingot | Cinnabar Ore | 10 seconds | 1 item |
| Steel Ingot | Special Recipe | Varies | Varies |
| Silicon Ingot | Silicon Dust | 10 seconds | 1 item |

### Processing
All ore blocks can be processed in two ways:

1. **Direct Smelting**: Ore → 1 Ingot (100% yield)
2. **Crushing then Smelting**: Ore → Crusher → 2 Dust → 2 Ingots (200% yield)

The Crusher method provides double the output of raw smelting.

---

## Other Materials

### Silicon Dust
Silicon Dust is obtained by crushing Sand in a Crusher. It can be smelted into Silicon Ingots.

### Steel Powder
Steel Powder is a crafting ingredient used in the production of Steel Ingots via the Induction Furnace.

### Aqualith
Aqualith is a gem obtained from Aqualith Ore. It can be crafted into Aqualith Blocks (1:9 ratio) for storage.

---

## Data Values

### Block IDs
All ore blocks use the namespace `the_great_expansion:` followed by their block name in lowercase with underscores.

Examples:
- `the_great_expansion:bauxite`
- `the_great_expansion:cassiterite_ore`
- `the_great_expansion:ilmenite_ore`

### Item Properties
- Stack size: 64 (all ores and ingots)
- Rarity: Common
- Fireproof: No

---

## See also
- [Tools and Weapons](./tools-and-weapons)
- [Armor](./armor)
- [Machines](./machines)
