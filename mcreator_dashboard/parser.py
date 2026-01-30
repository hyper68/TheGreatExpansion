"""
MCreator Element Parser
Parses .mod.json files and extracts structured data from all element types
"""
import json
import os
from pathlib import Path
from typing import Dict, List, Any, Optional
from dataclasses import dataclass, field, asdict
from datetime import datetime


@dataclass
class Element:
    """Base class for all MCreator elements"""
    name: str
    element_type: str
    file_name: str
    dependencies: List[str] = field(default_factory=list)
    recipes_using: List[str] = field(default_factory=list)
    recipe_outputs: List[str] = field(default_factory=list)
    raw_data: Dict = field(default_factory=dict)

    def to_dict(self) -> Dict:
        """Convert to dictionary for export"""
        return {
            'Name': self.name,
            'Type': self.element_type,
            'File': self.file_name,
            'Dependencies': ', '.join(self.dependencies),
            'Used In Recipes': ', '.join(self.recipes_using),
            'Recipe Outputs': ', '.join(self.recipe_outputs),
        }


@dataclass
class BlockElement(Element):
    hardness: float = 0.0
    resistance: float = 0.0
    tool_type: str = ""
    creative_tabs: List[str] = field(default_factory=list)
    has_inventory: bool = False
    gui_bound_to: str = ""

    def to_dict(self) -> Dict:
        d = super().to_dict()
        d.update({
            'Hardness': self.hardness,
            'Resistance': self.resistance,
            'Tool': self.tool_type,
            'Creative Tabs': ', '.join(self.creative_tabs),
            'Has GUI': 'Yes' if self.has_inventory and self.gui_bound_to else 'No',
        })
        return d


@dataclass
class RecipeElement(Element):
    recipe_type: str = ""
    inputs: List[str] = field(default_factory=list)
    output: str = ""
    output_count: int = 1

    def to_dict(self) -> Dict:
        d = super().to_dict()
        d.update({
            'Recipe Type': self.recipe_type,
            'Inputs': ' + '.join(self.inputs),
            'Output': self.output,
            'Output Count': self.output_count,
        })
        return d


@dataclass
class MobElement(Element):
    health: float = 10.0
    attack_strength: float = 0.0
    spawn_biomes: List[str] = field(default_factory=list)
    mob_drops: List[str] = field(default_factory=list)

    def to_dict(self) -> Dict:
        d = super().to_dict()
        d.update({
            'Health': self.health,
            'Attack': self.attack_strength,
            'Spawn Biomes': ', '.join(self.spawn_biomes),
            'Drops': ', '.join(self.mob_drops),
        })
        return d


@dataclass
class ArmorElement(Element):
    durability: int = 0
    protection_values: Dict[str, int] = field(default_factory=dict)
    enchantability: int = 0
    repair_items: List[str] = field(default_factory=list)

    def to_dict(self) -> Dict:
        d = super().to_dict()
        d.update({
            'Durability': self.durability,
            'Enchantability': self.enchantability,
            'Repair Items': ', '.join(self.repair_items),
        })
        return d


class MCreatorParser:
    """Parse MCreator mod elements and build relationships"""

    def __init__(self, elements_path: str):
        self.elements_path = Path(elements_path)
        self.elements: Dict[str, Element] = {}
        self.recipes: Dict[str, RecipeElement] = {}
        # Lookup map from canonical keys (and file stems) to element display names
        # A single canonical key may map to multiple element names, so we store lists
        self.lookup: Dict[str, List[str]] = {}

    def parse_all(self) -> Dict[str, Element]:
        """Parse all .mod.json files in the elements directory"""
        if not self.elements_path.exists():
            raise FileNotFoundError(f"Elements directory not found: {self.elements_path}")

        json_files = list(self.elements_path.glob("*.mod.json"))
        print(f"Found {len(json_files)} element files")

        # First pass: parse all elements
        for json_file in json_files:
            try:
                element = self._parse_element(json_file)
                if element:
                    # Store element by its display name
                    self.elements[element.name] = element

                    # Index lookup keys: canonicalized display name and file stem
                    can = self._canonical_name(element.name)
                    self._add_lookup_key(can, element.name)
                    self._add_lookup_key(json_file.stem, element.name)

                    # Also index the file-defined name if present in raw data
                    try:
                        def_name = element.raw_data.get('definition', {}).get('name', '')
                        if def_name:
                            self._add_lookup_key(self._canonical_name(def_name), element.name)
                    except Exception:
                        pass

                    if isinstance(element, RecipeElement):
                        self.recipes[element.name] = element
            except Exception as e:
                print(f"Error parsing {json_file.name}: {e}")

        # Second pass: build relationships
        self._build_relationships()

        print(f"Successfully parsed {len(self.elements)} elements")
        return self.elements

    def _parse_element(self, file_path: Path) -> Optional[Element]:
        """Parse a single .mod.json file"""
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)

        element_type = data.get('_type', 'unknown')
        definition = data.get('definition', {})
        file_name = file_path.stem

        # Parse based on element type
        if element_type == 'block':
            return self._parse_block(definition, file_name, data)
        elif element_type == 'recipe':
            return self._parse_recipe(definition, file_name, data)
        elif element_type == 'livingentity':
            return self._parse_mob(definition, file_name, data)
        elif element_type == 'armor':
            return self._parse_armor(definition, file_name, data)
        elif element_type == 'dimension':
            return self._parse_dimension(definition, file_name, data)
        elif element_type == 'potion':
            return self._parse_potion(definition, file_name, data)
        elif element_type == 'tool':
            return self._parse_tool(definition, file_name, data)
        elif element_type == 'item':
            return self._parse_item(definition, file_name, data)
        elif element_type == 'procedure':
            return self._parse_procedure(definition, file_name, data)
        else:
            # Generic element
            name = definition.get('name', file_name)
            return Element(name=name, element_type=element_type, file_name=file_name, raw_data=data)

    def _parse_block(self, definition: Dict, file_name: str, raw_data: Dict) -> BlockElement:
        """Parse block element"""
        name = definition.get('name', file_name)
        creative_tabs = [tab.get('value', '') for tab in definition.get('creativeTabs', [])]

        return BlockElement(
            name=name,
            element_type='block',
            file_name=file_name,
            hardness=definition.get('hardness', 0.0),
            resistance=definition.get('resistance', 0.0),
            tool_type=definition.get('destroyTool', ''),
            creative_tabs=creative_tabs,
            has_inventory=definition.get('hasInventory', False),
            gui_bound_to=definition.get('guiBoundTo', ''),
            raw_data=raw_data
        )

    def _parse_recipe(self, definition: Dict, file_name: str, raw_data: Dict) -> RecipeElement:
        """Parse recipe element"""
        name = definition.get('name', file_name)
        recipe_type = definition.get('recipeType', 'Unknown')

        # Extract inputs (support different slot/stack formats)
        inputs = []
        recipe_slots = definition.get('recipeSlots', []) or definition.get('recipeInputs', [])
        for slot in recipe_slots:
            # slot may be a dict with 'value', or a nested stack structure
            slot_value = ''
            if isinstance(slot, dict):
                slot_value = slot.get('value', '') or slot.get('item', '') or slot.get('stack', '')
            else:
                slot_value = str(slot)

            if slot_value:
                inputs.append(self._clean_item_name(slot_value))

        # Extract output
        output_stack = definition.get('recipeReturnStack', {})
        output = self._clean_item_name(output_stack.get('value', ''))
        output_count = definition.get('recipeRetstackSize', 1)

        return RecipeElement(
            name=name,
            element_type='recipe',
            file_name=file_name,
            recipe_type=recipe_type,
            inputs=[i for i in inputs if i],  # Filter empty
            output=output,
            output_count=output_count,
            raw_data=raw_data
        )

    def _parse_mob(self, definition: Dict, file_name: str, raw_data: Dict) -> MobElement:
        """Parse living entity (mob) element"""
        name = definition.get('mobName', file_name)
        spawn_biomes = [biome.get('value', '') for biome in definition.get('restrictionBiomes', [])]

        mob_drop = definition.get('mobDrop', {}).get('value', '')
        drops = [self._clean_item_name(mob_drop)] if mob_drop else []

        return MobElement(
            name=name,
            element_type='mob',
            file_name=file_name,
            health=definition.get('health', 10.0),
            attack_strength=definition.get('attackStrength', 0.0),
            spawn_biomes=[self._clean_item_name(b) for b in spawn_biomes],
            mob_drops=drops,
            raw_data=raw_data
        )

    def _parse_armor(self, definition: Dict, file_name: str, raw_data: Dict) -> ArmorElement:
        """Parse armor element"""
        # Armor sets have multiple pieces
        name = definition.get('helmetName', file_name).replace(' Helmet', '').replace(' Armor', '')

        repair_items = [self._clean_item_name(item.get('value', ''))
                       for item in definition.get('repairItems', [])]

        protection_values = {
            'helmet': definition.get('damageValueHelmet', 0),
            'chestplate': definition.get('damageValueBody', 0),
            'leggings': definition.get('damageValueLeggings', 0),
            'boots': definition.get('damageValueBoots', 0),
        }

        return ArmorElement(
            name=name,
            element_type='armor',
            file_name=file_name,
            durability=definition.get('maxDamage', 0),
            protection_values=protection_values,
            enchantability=definition.get('enchantability', 0),
            repair_items=repair_items,
            raw_data=raw_data
        )

    def _parse_dimension(self, definition: Dict, file_name: str, raw_data: Dict) -> Element:
        """Parse dimension element"""
        # Dimensions use the igniter name if available
        name = definition.get('igniterName', file_name).replace(' Portal Igniter', '')

        biomes = [self._clean_item_name(b.get('value', '')) for b in definition.get('biomesInDimension', [])]
        dependencies = biomes  # Dimensions depend on their biomes

        return Element(
            name=name,
            element_type='dimension',
            file_name=file_name,
            dependencies=dependencies,
            raw_data=raw_data
        )

    def _parse_potion(self, definition: Dict, file_name: str, raw_data: Dict) -> Element:
        """Parse potion element"""
        name = definition.get('potionName', file_name).replace('Potion of ', '')

        effects = [effect.get('effect', {}).get('value', '')
                  for effect in definition.get('effects', [])]

        return Element(
            name=name,
            element_type='potion',
            file_name=file_name,
            dependencies=effects,
            raw_data=raw_data
        )

    def _parse_tool(self, definition: Dict, file_name: str, raw_data: Dict) -> Element:
        """Parse tool element"""
        name = definition.get('name', file_name)

        repair_items = [self._clean_item_name(item.get('value', ''))
                       for item in definition.get('repairItems', [])]

        return Element(
            name=name,
            element_type='tool',
            file_name=file_name,
            dependencies=repair_items,
            raw_data=raw_data
        )

    def _parse_item(self, definition: Dict, file_name: str, raw_data: Dict) -> Element:
        """Parse item element"""
        name = definition.get('name', file_name)
        return Element(
            name=name,
            element_type='item',
            file_name=file_name,
            raw_data=raw_data
        )

    def _parse_procedure(self, definition: Dict, file_name: str, raw_data: Dict) -> Element:
        """Parse procedure element"""
        name = file_name  # Procedures typically use file name
        return Element(
            name=name,
            element_type='procedure',
            file_name=file_name,
            raw_data=raw_data
        )

    def _clean_item_name(self, item_ref: str) -> str:
        """Clean item reference to just the name"""
        if not item_ref:
            return ""

        # Handle different reference formats
        # CUSTOM:ItemName -> ItemName
        # Items.ITEM_NAME -> Item Name
        # TAG:namespace:tag_name -> tag_name
        # Blocks.BLOCK_NAME -> Block Name

        val = item_ref

        # Strip wrappers like CUSTOM:, TAG:, namespace:mod:item
        if val.startswith('CUSTOM:'):
            val = val[len('CUSTOM:'):]
        if val.startswith('TAG:'):
            # TAG:namespace:tag_name -> tag_name
            parts = val.split(':')
            if parts:
                val = parts[-1]

        # If namespaced like modid:item_name -> take last part
        if ':' in val and not val.upper().startswith('TAG:'):
            parts = val.split(':')
            val = parts[-1]

        # Handle dotted references like Items.ITEM_NAME or Blocks.BLOCK_NAME
        if '.' in val:
            val = val.split('.')[-1]

        # Normalize underscores and casing
        val = val.replace('_', ' ').strip()

        # Collapse multiple spaces
        val = ' '.join(val.split())

        # Title-case common display names but preserve uppercase acronyms
        return val.title()

    def _canonical_name(self, name: str) -> str:
        """Return a normalized canonical key for lookup (lowercase, no punctuation)."""
        if not name:
            return ''
        s = name.lower()
        # Remove namespace if present
        if ':' in s:
            s = s.split(':')[-1]
        # Replace non-alphanumeric with spaces
        import re
        s = re.sub(r'[^a-z0-9]+', ' ', s)
        s = ' '.join(s.split())
        return s

    def _add_lookup_key(self, key: str, element_name: str):
        """Add a key to the lookup map mapping to an element name."""
        if not key:
            return
        k = self._canonical_name(key)
        if not k:
            return
        self.lookup.setdefault(k, [])
        if element_name not in self.lookup[k]:
            self.lookup[k].append(element_name)

    def _resolve_item(self, item_ref: str) -> List[str]:
        """Resolve an item reference (from recipes) to element display names.

        Returns a list (possibly empty) of matching element keys.
        """
        results = []
        if not item_ref:
            return results

        # Try direct match by display name
        if item_ref in self.elements:
            results.append(item_ref)

        # Try file stem match
        if item_ref in [e.file_name for e in self.elements.values()]:
            for el in self.elements.values():
                if el.file_name == item_ref and el.name not in results:
                    results.append(el.name)

        # Try canonical lookup
        can = self._canonical_name(item_ref)
        if can in self.lookup:
            for name in self.lookup[can]:
                if name not in results:
                    results.append(name)

        return results

    def _build_relationships(self):
        """Build recipe relationships between elements"""
        # For each recipe, link inputs to outputs
        for recipe in self.recipes.values():
            # Resolve inputs to actual element names and add relationships
            resolved_inputs = []
            for input_item in recipe.inputs:
                matches = self._resolve_item(input_item)
                if matches:
                    for m in matches:
                        self.elements[m].recipes_using.append(recipe.name)
                        resolved_inputs.append(m)
                else:
                    # If no match, keep the original (unresolved) string for traceability
                    resolved_inputs.append(input_item)

            # Add dependencies to the recipe itself (resolved where possible)
            recipe.dependencies = resolved_inputs

            # Resolve and mark output relationships
            output_matches = self._resolve_item(recipe.output)
            if output_matches:
                for out in output_matches:
                    self.elements[out].recipe_outputs.append(recipe.name)
            else:
                # No element matched the output; nothing to append
                pass

    def get_statistics(self) -> Dict[str, Any]:
        """Get statistics about the mod"""
        type_counts = {}
        for element in self.elements.values():
            element_type = element.element_type
            type_counts[element_type] = type_counts.get(element_type, 0) + 1

        return {
            'total_elements': len(self.elements),
            'total_recipes': len(self.recipes),
            'type_counts': type_counts,
            'orphaned_items': self._find_orphaned_items(),
        }

    def _find_orphaned_items(self) -> List[str]:
        """Find items that aren't used in any recipes"""
        orphaned = []
        for name, element in self.elements.items():
            # Skip recipes and procedures
            if element.element_type in ['recipe', 'procedure', 'advancement']:
                continue

            # Item is orphaned if it has no recipes using it and isn't a recipe output
            if not element.recipes_using and not element.recipe_outputs:
                orphaned.append(name)

        return orphaned


if __name__ == '__main__':
    # Test the parser
    import sys

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        elements_path = '../elements'

    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    print("\n=== Statistics ===")
    stats = parser.get_statistics()
    print(f"Total Elements: {stats['total_elements']}")
    print(f"Total Recipes: {stats['total_recipes']}")
    print("\nElements by Type:")
    for element_type, count in sorted(stats['type_counts'].items()):
        print(f"  {element_type}: {count}")

    print(f"\nOrphaned Items: {len(stats['orphaned_items'])}")
    if stats['orphaned_items'][:5]:
        print("  Examples:", ', '.join(stats['orphaned_items'][:5]))
