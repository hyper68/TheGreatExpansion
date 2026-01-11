import json
import os
from collections import defaultdict

elements_dir = "elements"
output = []

# Categories to organize
categories = defaultdict(list)

for filename in sorted(os.listdir(elements_dir)):
    if not filename.endswith('.mod.json'):
        continue

    filepath = os.path.join(elements_dir, filename)
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            data = json.load(f)

        element_type = data.get('_type', 'unknown')
        definition = data.get('definition', {})

        # Extract key info based on type
        info = {
            'filename': filename,
            'type': element_type,
            'name': definition.get('name', filename.replace('.mod.json', ''))
        }

        # Add type-specific details
        if element_type == 'block':
            info['hardness'] = definition.get('hardness')
            info['tool'] = definition.get('destroyTool')
            info['tool_tier'] = definition.get('vanillaToolTier')
            info['generates'] = definition.get('generateFeature', False)
            if info['generates']:
                info['gen_height'] = f"{definition.get('minGenerateHeight')}-{definition.get('maxGenerateHeight')}"
                info['frequency'] = definition.get('frequencyPerChunks')

        elif element_type == 'item':
            info['rarity'] = definition.get('rarity')
            info['stack_size'] = definition.get('stackSize')

        elif element_type == 'tool':
            info['efficiency'] = definition.get('efficiency')
            info['attack_speed'] = definition.get('attackSpeed')
            info['damage'] = definition.get('damageVsEntity')
            info['durability'] = definition.get('usageCount')
            info['enchantability'] = definition.get('enchantability')

        elif element_type == 'armor':
            info['helmet_defense'] = definition.get('helmetArmor')
            info['body_defense'] = definition.get('bodyArmor')
            info['leggings_defense'] = definition.get('leggingsArmor')
            info['boots_defense'] = definition.get('bootsArmor')
            info['toughness'] = definition.get('toughness')
            info['durability'] = definition.get('maxDamage')

        elif element_type == 'recipe':
            info['recipe_type'] = definition.get('recipeType')
            info['cooking_time'] = definition.get('cookingTime')
            info['xp_reward'] = definition.get('xpReward')

        elif element_type == 'fluid':
            info['luminosity'] = definition.get('luminosity')
            info['density'] = definition.get('density')
            info['viscosity'] = definition.get('viscosity')
            info['temperature'] = definition.get('temperature')

        elif element_type == 'biome':
            info['temperature'] = definition.get('temperature')
            info['rainfall'] = definition.get('rainfallType')

        categories[element_type].append(info)

    except Exception as e:
        print(f"Error parsing {filename}: {e}")

# Write organized output
with open('element_catalog.txt', 'w', encoding='utf-8') as out:
    out.write("="*80 + "\n")
    out.write("COMPLETE MOD ELEMENT CATALOG\n")
    out.write("="*80 + "\n\n")

    for cat_type, items in sorted(categories.items()):
        out.write(f"\n{'='*80}\n")
        out.write(f"{cat_type.upper()} ({len(items)} items)\n")
        out.write(f"{'='*80}\n\n")

        for item in items:
            out.write(f"--- {item['name']} ---\n")
            for key, value in item.items():
                if key not in ['name', 'filename'] and value is not None:
                    out.write(f"  {key}: {value}\n")
            out.write(f"  File: {item['filename']}\n\n")

print(f"Cataloged {sum(len(v) for v in categories.values())} elements across {len(categories)} types")
