"""
Advanced Relationship Analyzer
Deep analysis of all relationships between mod elements including recipes, machines, GUIs, drops, and more
"""
import json
from typing import Dict, List, Set, Tuple, Optional
from collections import defaultdict, deque
from pathlib import Path

from parser import Element, RecipeElement, MobElement, BlockElement
from ai_connector import find_connections


class RelationshipAnalyzer:
    """Analyzes and maps all relationships between mod elements"""

    def __init__(self, elements: Dict[str, Element]):
        self.elements = elements
        self.relationships = defaultdict(lambda: {
            'produces': [],           # What this element produces
            'consumed_by': [],        # What consumes this element
            'requires': [],           # What this element requires
            'enables': [],            # What this element enables
            'dropped_by': [],         # What mobs/blocks drop this
            'drops': [],              # What this drops
            'crafted_in': [],         # What machines can craft this
            'processes': [],          # What this machine processes
            'gui': None,              # Associated GUI
            'blocks': [],             # Blocks associated (for GUIs)
            'tier': 0,                # Crafting tier (0=raw, higher=more complex)
            'recipe_count': 0,        # How many recipes use this
            'complexity': 0,          # Overall complexity score
        })

        # Build a simple canonical lookup for element names (lowercase, stripped)
        self.lookup = {}
        for name, el in elements.items():
            key = self._canonical_name(name)
            self.lookup.setdefault(key, []).append(name)
            # also index file_name if present
            try:
                if hasattr(el, 'file_name') and el.file_name:
                    fk = self._canonical_name(el.file_name)
                    self.lookup.setdefault(fk, []).append(name)
            except Exception:
                pass

        self._analyze_all_relationships()

        # Use lightweight AI heuristics to suggest additional probable connections
        try:
            suggestions = find_connections(self.elements, self.relationships, threshold=0.62, max_suggestions=400)
            # Attach suggestions into relationships under 'ai_suggested'
            for s in suggestions:
                src = s['source']
                tgt = s['target']
                score = s['score']
                reason = s.get('reason', 'ai')

                # Record outgoing suggestion
                self.relationships[src].setdefault('ai_suggested_out', []).append({'target': tgt, 'score': score, 'reason': reason})
                # Record incoming suggestion
                self.relationships[tgt].setdefault('ai_suggested_in', []).append({'source': src, 'score': score, 'reason': reason})
        except Exception as e:
            print('AI suggestion step failed:', e)

    def _analyze_all_relationships(self):
        """Perform comprehensive relationship analysis"""
        print("Analyzing relationships...")

        # 1. Recipe relationships
        self._analyze_recipes()

        # 2. Machine and GUI relationships
        self._analyze_machines()

        # 3. Mob drops
        self._analyze_mob_drops()

        # 4. Block drops and loot tables
        self._analyze_block_drops()

        # 5. Tool relationships
        self._analyze_tools()

        # 6. Calculate tiers and complexity
        self._calculate_tiers()

        print(f"Analyzed {len(self.relationships)} element relationships")

    def _canonical_name(self, name: str) -> str:
        """Normalize a name for lookup (lowercase, strip non-alphanumerics)."""
        if not name:
            return ''
        s = name.lower()
        if ':' in s:
            s = s.split(':')[-1]
        import re
        s = re.sub(r'[^a-z0-9]+', ' ', s)
        s = ' '.join(s.split())
        return s

    def _analyze_recipes(self):
        """Analyze all recipe relationships in detail"""
        for name, element in self.elements.items():
            if not isinstance(element, RecipeElement):
                continue

            recipe_type = (element.recipe_type or '').lower()
            # Prefer resolved dependencies (parser may have populated these)
            inputs = getattr(element, 'dependencies', None) or element.inputs or []
            output = element.output

            # Map inputs -> recipe
            for input_item in inputs:
                if not input_item:
                    continue

                # If input_item isn't a known element key, try lookup by canonical name
                targets = [input_item] if input_item in self.elements else self.lookup.get(self._canonical_name(input_item), [input_item])
                for tgt in targets:
                    self.relationships[tgt]['consumed_by'].append({
                        'recipe': name,
                        'type': recipe_type,
                        'produces': output
                    })
                    self.relationships[tgt]['recipe_count'] += 1

            # Map recipe -> output
            if output:
                out_targets = [output] if output in self.elements else self.lookup.get(self._canonical_name(output), [output])
                for out in out_targets:
                    self.relationships[out]['produces'].append({
                        'recipe': name,
                        'type': recipe_type,
                        'inputs': inputs,
                        'count': element.output_count
                    })

                    # Map inputs as requirements for output (resolve where possible)
                    for input_item in inputs:
                        if input_item and input_item != out:
                            # expand input lookup
                            reqs = [input_item] if input_item in self.elements else self.lookup.get(self._canonical_name(input_item), [input_item])
                            for r in reqs:
                                if r not in self.relationships[out]['requires']:
                                    self.relationships[out]['requires'].append(r)

                # Special-case: smelting/furnace style recipes -> mark crafted_in Furnace
                if any(k in recipe_type for k in ('smelt', 'furnace', 'blast', 'smelting')):
                    for out in out_targets:
                        if 'Furnace' not in self.relationships[out]['crafted_in']:
                            self.relationships[out]['crafted_in'].append('Furnace')
                    for input_item in inputs:
                        in_targets = [input_item] if input_item in self.elements else self.lookup.get(self._canonical_name(input_item), [input_item])
                        for it in in_targets:
                            if 'Furnace' not in self.relationships[it]['enables']:
                                self.relationships[it]['enables'].append({'machine': 'Furnace', 'type': 'smelt_input'})

    def _analyze_machines(self):
        """Analyze machine and GUI relationships"""
        # Find all GUIs
        guis = {name: el for name, el in self.elements.items() if el.element_type == 'gui'}

        # Find all blocks with GUIs
        # Known machine keyword mapping to recipe keywords
        MACHINE_KEYWORDS = {
            'Furnace': ['smelt', 'furnace', 'blast', 'smelting'],
            'Grinder': ['grind', 'grinder', 'pulverize'],
            'Crafting Table': ['craft', 'crafting', 'shaped', 'shapeless'],
            'Machine': ['machine', 'processor', 'workbench', 'smelter']
        }

        for name, element in self.elements.items():
            if isinstance(element, BlockElement) and element.gui_bound_to:
                gui_name = element.gui_bound_to
                machine_name = name

                # Link machine to GUI
                self.relationships[machine_name]['gui'] = gui_name

                # Link GUI to machine
                if gui_name in self.relationships:
                    self.relationships[gui_name]['blocks'].append(machine_name)

                # Find recipes that likely run in this machine using improved heuristics
                for recipe_name, recipe_el in self.elements.items():
                    if not isinstance(recipe_el, RecipeElement):
                        continue

                    recipe_type = (recipe_el.recipe_type or '').lower()
                    machine_key = self._canonical_name(machine_name)

                    matched = False

                    # Direct substring heuristics
                    if recipe_type and machine_key and (recipe_type in machine_key or machine_key in recipe_type):
                        matched = True

                    # Keyword-based heuristics
                    if not matched:
                        for mname, keywords in MACHINE_KEYWORDS.items():
                            if any(k in recipe_type for k in keywords) and (mname.lower() in machine_name.lower() or machine_key in self._canonical_name(mname)):
                                matched = True
                                break

                    if matched:
                        self.relationships[machine_name]['processes'].append({
                            'recipe': recipe_name,
                            'inputs': getattr(recipe_el, 'dependencies', recipe_el.inputs),
                            'output': recipe_el.output
                        })

                        # Mark items as craftable in this machine
                        if recipe_el.output:
                            out_targets = [recipe_el.output] if recipe_el.output in self.elements else self.lookup.get(self._canonical_name(recipe_el.output), [recipe_el.output])
                            for out in out_targets:
                                if machine_name not in self.relationships[out]['crafted_in']:
                                    self.relationships[out]['crafted_in'].append(machine_name)

    def _analyze_mob_drops(self):
        """Analyze what mobs drop"""
        for name, element in self.elements.items():
            if isinstance(element, MobElement):
                for drop in element.mob_drops:
                    if drop:
                        self.relationships[drop]['dropped_by'].append({
                            'mob': name,
                            'type': 'mob_drop'
                        })
                        self.relationships[name]['drops'].append(drop)

    def _analyze_block_drops(self):
        """Analyze block drops and custom drops"""
        for name, element in self.elements.items():
            if isinstance(element, BlockElement):
                # Check for custom drops
                if hasattr(element, 'raw_data'):
                    definition = element.raw_data.get('definition', {})
                    custom_drop = definition.get('customDrop', {}).get('value', '')

                    if custom_drop and custom_drop.startswith('CUSTOM:'):
                        drop_item = custom_drop.replace('CUSTOM:', '')
                        self.relationships[drop_item]['dropped_by'].append({
                            'block': name,
                            'type': 'block_drop'
                        })
                        self.relationships[name]['drops'].append(drop_item)

    def _analyze_tools(self):
        """Analyze tool and material relationships"""
        for name, element in self.elements.items():
            if element.element_type == 'tool':
                # Find repair items
                if hasattr(element, 'raw_data'):
                    definition = element.raw_data.get('definition', {})
                    repair_items = definition.get('repairItems', [])

                    for repair_item_dict in repair_items:
                        repair_item = repair_item_dict.get('value', '')
                        if repair_item.startswith('CUSTOM:'):
                            repair_material = repair_item.replace('CUSTOM:', '')
                            self.relationships[repair_material]['enables'].append({
                                'tool': name,
                                'type': 'repair_material'
                            })
                            self.relationships[name]['requires'].append(repair_material)

    def _calculate_tiers(self):
        """Calculate crafting tiers (complexity levels)"""
        # Tier 0: Raw materials (no recipes produce them, or only simple smelting)
        # Tier 1: One step from raw materials
        # Tier 2+: Multiple steps from raw

        visited = set()

        def calculate_tier(item_name: str, path: Set[str] = None) -> int:
            if path is None:
                path = set()

            if item_name in visited:
                return self.relationships[item_name]['tier']

            if item_name in path:  # Circular dependency
                return 0

            path.add(item_name)

            produces = self.relationships[item_name].get('produces', [])

            if not produces:
                # Raw material or ore
                tier = 0
            else:
                # Find max tier of all inputs needed to craft this
                max_input_tier = 0
                for prod_info in produces:
                    inputs = prod_info.get('inputs', [])
                    for input_item in inputs:
                        if input_item and input_item != item_name:
                            input_tier = calculate_tier(input_item, path.copy())
                            max_input_tier = max(max_input_tier, input_tier)

                tier = max_input_tier + 1

            visited.add(item_name)
            self.relationships[item_name]['tier'] = tier
            return tier

        # Calculate tiers for all items
        for name in self.elements.keys():
            calculate_tier(name)

        # Calculate complexity (tier * recipe_count + number of requirements)
        for name, rel in self.relationships.items():
            tier = rel['tier']
            recipe_count = rel['recipe_count']
            requirement_count = len(rel['requires'])

            complexity = (tier * 10) + recipe_count + (requirement_count * 2)
            rel['complexity'] = complexity

    def get_crafting_chain(self, item_name: str, max_depth: int = 10) -> Dict:
        """Get the complete crafting chain for an item"""
        if item_name not in self.elements:
            return {}

        chain = {
            'item': item_name,
            'tier': self.relationships[item_name]['tier'],
            'recipes': [],
            'children': []
        }

        produces = self.relationships[item_name].get('produces', [])

        for recipe_info in produces:
            recipe_data = {
                'recipe': recipe_info['recipe'],
                'type': recipe_info['type'],
                'inputs': [],
                'output_count': recipe_info.get('count', 1)
            }

            # Recursively get crafting chains for inputs
            if max_depth > 0:
                for input_item in recipe_info.get('inputs', []):
                    if input_item and input_item != item_name:
                        input_chain = self.get_crafting_chain(input_item, max_depth - 1)
                        if input_chain:
                            recipe_data['inputs'].append(input_chain)

            chain['recipes'].append(recipe_data)

        return chain

    def get_usage_chain(self, item_name: str, max_depth: int = 10) -> Dict:
        """Get what an item can be used to create"""
        if item_name not in self.elements:
            return {}

        chain = {
            'item': item_name,
            'used_in': []
        }

        consumed_by = self.relationships[item_name].get('consumed_by', [])

        if max_depth > 0:
            for usage_info in consumed_by:
                output = usage_info.get('produces', '')
                if output and output != item_name:
                    usage_chain = self.get_usage_chain(output, max_depth - 1)
                    chain['used_in'].append({
                        'recipe': usage_info['recipe'],
                        'type': usage_info['type'],
                        'produces': output,
                        'chain': usage_chain
                    })

        return chain

    def find_shortest_path(self, from_item: str, to_item: str) -> List[str]:
        """Find shortest crafting path between two items"""
        if from_item not in self.elements or to_item not in self.elements:
            return []

        # BFS to find shortest path
        queue = deque([(from_item, [from_item])])
        visited = {from_item}

        while queue:
            current, path = queue.popleft()

            if current == to_item:
                return path

            # Check what this item can produce
            consumed_by = self.relationships[current].get('consumed_by', [])

            for usage_info in consumed_by:
                produces = usage_info.get('produces', '')
                if produces and produces not in visited:
                    visited.add(produces)
                    queue.append((produces, path + [produces]))

        return []  # No path found

    def get_raw_materials(self, item_name: str) -> Dict[str, int]:
        """Get all raw materials needed to craft an item"""
        if item_name not in self.elements:
            return {}

        materials = defaultdict(int)

        def collect_materials(item: str, count: int = 1, visited: Set[str] = None):
            if visited is None:
                visited = set()

            if item in visited:
                return

            visited.add(item)

            produces = self.relationships[item].get('produces', [])

            if not produces:
                # Raw material
                materials[item] += count
            else:
                # Get first recipe (simplest)
                recipe_info = produces[0]
                output_count = recipe_info.get('count', 1)
                multiplier = max(1, count // output_count)

                for input_item in recipe_info.get('inputs', []):
                    if input_item and input_item != item:
                        collect_materials(input_item, multiplier, visited.copy())

        collect_materials(item_name)
        return dict(materials)

    def get_all_related(self, item_name: str) -> Dict[str, List[str]]:
        """Get all items related to this item"""
        if item_name not in self.elements:
            return {}

        rel = self.relationships[item_name]

        return {
            'produces': [r['produces'] for r in rel.get('produces', []) if 'produces' in r],
            'required_for': [c['produces'] for c in rel.get('consumed_by', [])],
            'requires': list(set(rel.get('requires', []))),
            'dropped_by': [d['mob'] if 'mob' in d else d.get('block', '')
                          for d in rel.get('dropped_by', [])],
            'drops': rel.get('drops', []),
            'crafted_in': rel.get('crafted_in', []),
            'tier': rel['tier'],
            'complexity': rel['complexity']
        }

    def find_orphaned_items(self) -> List[str]:
        """Find items with no way to obtain them"""
        orphaned = []

        for name, element in self.elements.items():
            # Skip recipes, procedures, GUIs, etc.
            if element.element_type in ['recipe', 'procedure', 'gui', 'advancement']:
                continue

            rel = self.relationships[name]

            # Item is orphaned if it has no recipes producing it and isn't dropped
            has_recipe = len(rel.get('produces', [])) > 0
            has_drop = len(rel.get('dropped_by', [])) > 0

            if not has_recipe and not has_drop:
                orphaned.append(name)

        return orphaned

    def find_dead_end_items(self) -> List[str]:
        """Find items that can't be used for anything"""
        dead_ends = []

        for name, element in self.elements.items():
            # Skip recipes, procedures, GUIs, etc.
            if element.element_type in ['recipe', 'procedure', 'gui', 'advancement']:
                continue

            rel = self.relationships[name]

            # Item is dead-end if it's not used in any recipes
            used_in_recipes = len(rel.get('consumed_by', [])) > 0
            enables_items = len(rel.get('enables', [])) > 0

            if not used_in_recipes and not enables_items:
                dead_ends.append(name)

        return dead_ends

    def get_statistics(self) -> Dict:
        """Get relationship statistics"""
        stats = {
            'total_relationships': len(self.relationships),
            'total_recipes': len([e for e in self.elements.values() if e.element_type == 'recipe']),
            'orphaned_items': len(self.find_orphaned_items()),
            'dead_end_items': len(self.find_dead_end_items()),
            'max_tier': max(r['tier'] for r in self.relationships.values()),
            'avg_complexity': sum(r['complexity'] for r in self.relationships.values()) / len(self.relationships),
            'machines': len([e for e in self.elements.values()
                           if isinstance(e, BlockElement) and e.has_inventory]),
        }

        # Tier distribution
        tier_dist = defaultdict(int)
        for rel in self.relationships.values():
            tier_dist[rel['tier']] += 1

        stats['tier_distribution'] = dict(tier_dist)

        return stats

    def export_relationship_graph(self, output_file: str):
        """Export relationships to JSON for analysis"""
        export_data = {
            'elements': {
                name: {
                    'type': element.element_type,
                    'relationships': self.relationships[name]
                }
                for name, element in self.elements.items()
            },
            'statistics': self.get_statistics()
        }

        with open(output_file, 'w', encoding='utf-8') as f:
            json.dump(export_data, f, indent=2)

        print(f"[OK] Exported relationship graph to {output_file}")


if __name__ == '__main__':
    # Test the analyzer
    import sys
    from parser import MCreatorParser

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        elements_path = '../elements'

    # Parse elements
    print("Parsing elements...")
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    # Analyze relationships
    print("\nAnalyzing relationships...")
    analyzer = RelationshipAnalyzer(elements)

    # Get statistics
    print("\n=== Relationship Statistics ===")
    stats = analyzer.get_statistics()
    for key, value in stats.items():
        print(f"{key}: {value}")

    # Find orphaned items
    print("\n=== Orphaned Items (no recipe/drop) ===")
    orphaned = analyzer.find_orphaned_items()
    print(f"Found {len(orphaned)} orphaned items")
    if orphaned[:5]:
        print("Examples:", ', '.join(orphaned[:5]))

    # Find dead ends
    print("\n=== Dead End Items (not used in recipes) ===")
    dead_ends = analyzer.find_dead_end_items()
    print(f"Found {len(dead_ends)} dead-end items")
    if dead_ends[:5]:
        print("Examples:", ', '.join(dead_ends[:5]))

    # Test crafting chain
    print("\n=== Example: Diamond Hammer Crafting Chain ===")
    if 'Diamond Hammer' in elements:
        chain = analyzer.get_crafting_chain('Diamond Hammer', max_depth=3)
        print(json.dumps(chain, indent=2))

    # Export graph
    output_file = Path(elements_path).parent / 'mcreator_dashboard' / 'data' / 'relationships.json'
    analyzer.export_relationship_graph(str(output_file))
