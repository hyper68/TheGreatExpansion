"""
Changelog Generator
Create audit snapshots and generate changelogs by comparing versions
"""
import json
from pathlib import Path
from datetime import datetime
from typing import Dict, List, Tuple, Optional, Any

from parser import Element


class AuditManager:
    """Manage audit snapshots and changelog generation"""

    def __init__(self, audit_dir: str):
        self.audit_dir = Path(audit_dir)
        self.audit_dir.mkdir(parents=True, exist_ok=True)

    def create_snapshot(self, elements: Dict[str, Element]) -> Path:
        """
        Create a timestamped snapshot of current elements

        Args:
            elements: Current parsed elements

        Returns:
            Path to the created snapshot file
        """
        timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
        snapshot_file = self.audit_dir / f"audit_{timestamp}.json"

        # Convert elements to serializable format
        snapshot_data = {
            'timestamp': timestamp,
            'datetime': datetime.now().isoformat(),
            'total_elements': len(elements),
            'elements': {
                name: self._element_to_dict(element)
                for name, element in elements.items()
            }
        }

        with open(snapshot_file, 'w', encoding='utf-8') as f:
            json.dump(snapshot_data, f, indent=2)

        print(f"[OK] Created audit snapshot: {snapshot_file}")
        return snapshot_file

    def get_latest_snapshot(self) -> Optional[Path]:
        """Get the most recent audit snapshot"""
        snapshots = sorted(self.audit_dir.glob("audit_*.json"))
        return snapshots[-1] if snapshots else None

    def get_all_snapshots(self) -> List[Path]:
        """Get all audit snapshots sorted by date"""
        return sorted(self.audit_dir.glob("audit_*.json"))

    def load_snapshot(self, snapshot_path: Path) -> Dict[str, Dict]:
        """Load a snapshot file"""
        with open(snapshot_path, 'r', encoding='utf-8') as f:
            return json.load(f)

    def generate_changelog(self, current_elements: Dict[str, Element],
                          compare_to: Optional[Path] = None) -> str:
        """
        Generate a markdown changelog comparing current state to a previous snapshot

        Args:
            current_elements: Current parsed elements
            compare_to: Specific snapshot to compare to (defaults to latest)

        Returns:
            Markdown-formatted changelog
        """
        # Get snapshot to compare against
        if compare_to is None:
            compare_to = self.get_latest_snapshot()

        if compare_to is None:
            return "No previous audit found. Create a snapshot first."

        # Load previous snapshot
        previous_snapshot = self.load_snapshot(compare_to)
        previous_elements = previous_snapshot['elements']

        # Generate changelog
        changelog = self._generate_changelog_markdown(
            current_elements,
            previous_elements,
            previous_snapshot.get('datetime', 'Unknown')
        )

        return changelog

    def _element_to_dict(self, element: Element) -> Dict[str, Any]:
        """Convert element to dictionary for serialization"""
        data = {
            'name': element.name,
            'type': element.element_type,
            'file_name': element.file_name,
            'dependencies': element.dependencies,
            'recipes_using': element.recipes_using,
            'recipe_outputs': element.recipe_outputs,
        }

        # Add type-specific fields
        if hasattr(element, 'hardness'):
            data['hardness'] = element.hardness
            data['resistance'] = element.resistance
            data['tool_type'] = element.tool_type

        if hasattr(element, 'recipe_type'):
            data['recipe_type'] = element.recipe_type
            data['inputs'] = element.inputs
            data['output'] = element.output
            data['output_count'] = element.output_count

        if hasattr(element, 'health'):
            data['health'] = element.health
            data['attack_strength'] = element.attack_strength

        return data

    def _generate_changelog_markdown(self, current_elements: Dict[str, Element],
                                    previous_elements: Dict[str, Dict],
                                    previous_date: str) -> str:
        """Generate markdown changelog"""
        current_names = set(current_elements.keys())
        previous_names = set(previous_elements.keys())

        # Find changes
        added = current_names - previous_names
        removed = previous_names - current_names
        potentially_modified = current_names & previous_names

        # Check for actual modifications
        modified = []
        for name in potentially_modified:
            current = current_elements[name]
            previous = previous_elements[name]

            if self._has_changes(current, previous):
                modified.append((name, current, previous))

        # Build changelog
        changelog = f"# Changelog\n\n"
        changelog += f"**Generated:** {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}\n\n"
        changelog += f"**Compared to:** {previous_date}\n\n"
        changelog += f"---\n\n"

        # Summary
        changelog += f"## Summary\n\n"
        changelog += f"- **Added:** {len(added)} elements\n"
        changelog += f"- **Removed:** {len(removed)} elements\n"
        changelog += f"- **Modified:** {len(modified)} elements\n"
        changelog += f"- **Unchanged:** {len(potentially_modified) - len(modified)} elements\n\n"

        # Added elements
        if added:
            changelog += f"## [+] Added Elements ({len(added)})\n\n"
            added_by_type = self._group_by_type(added, current_elements)

            for element_type, names in sorted(added_by_type.items()):
                changelog += f"### {element_type.title()}\n\n"
                for name in sorted(names):
                    element = current_elements[name]
                    changelog += f"- **{name}**"

                    # Add relevant details
                    if hasattr(element, 'recipe_type'):
                        changelog += f" - {element.recipe_type} recipe"
                    elif hasattr(element, 'creative_tabs') and element.creative_tabs:
                        changelog += f" - {', '.join(element.creative_tabs[:2])}"

                    changelog += "\n"

                changelog += "\n"

        # Removed elements
        if removed:
            changelog += f"## [-] Removed Elements ({len(removed)})\n\n"
            removed_by_type = {}
            for name in removed:
                element_type = previous_elements[name].get('type', 'unknown')
                if element_type not in removed_by_type:
                    removed_by_type[element_type] = []
                removed_by_type[element_type].append(name)

            for element_type, names in sorted(removed_by_type.items()):
                changelog += f"### {element_type.title()}\n\n"
                for name in sorted(names):
                    changelog += f"- **{name}**\n"
                changelog += "\n"

        # Modified elements
        if modified:
            changelog += f"## [~] Modified Elements ({len(modified)})\n\n"

            for name, current, previous in sorted(modified, key=lambda x: x[0]):
                changelog += f"### {name}\n\n"

                changes = self._get_detailed_changes(current, previous)
                for change in changes:
                    changelog += f"- {change}\n"

                changelog += "\n"

        if not added and not removed and not modified:
            changelog += "No changes detected.\n"

        return changelog

    def _group_by_type(self, names: set, elements: Dict[str, Element]) -> Dict[str, List[str]]:
        """Group element names by their type"""
        grouped = {}
        for name in names:
            element = elements[name]
            element_type = element.element_type
            if element_type not in grouped:
                grouped[element_type] = []
            grouped[element_type].append(name)
        return grouped

    def _has_changes(self, current: Element, previous: Dict) -> bool:
        """Check if an element has been modified"""
        # Compare key fields
        current_dict = self._element_to_dict(current)

        # Fields to compare
        compare_fields = ['dependencies', 'recipes_using', 'recipe_outputs']

        # Add type-specific fields
        if 'hardness' in previous:
            compare_fields.extend(['hardness', 'resistance', 'tool_type'])
        if 'recipe_type' in previous:
            compare_fields.extend(['recipe_type', 'inputs', 'output', 'output_count'])
        if 'health' in previous:
            compare_fields.extend(['health', 'attack_strength'])

        for field in compare_fields:
            if field in current_dict and field in previous:
                if current_dict[field] != previous[field]:
                    return True

        return False

    def _get_detailed_changes(self, current: Element, previous: Dict) -> List[str]:
        """Get list of specific changes between current and previous"""
        changes = []
        current_dict = self._element_to_dict(current)

        # Check all comparable fields
        comparable_fields = {
            'dependencies': 'Dependencies',
            'recipes_using': 'Used in recipes',
            'recipe_outputs': 'Recipe outputs',
            'hardness': 'Hardness',
            'resistance': 'Resistance',
            'tool_type': 'Tool type',
            'recipe_type': 'Recipe type',
            'inputs': 'Recipe inputs',
            'output': 'Recipe output',
            'output_count': 'Output count',
            'health': 'Health',
            'attack_strength': 'Attack strength',
        }

        for field, label in comparable_fields.items():
            if field not in current_dict or field not in previous:
                continue

            current_value = current_dict[field]
            previous_value = previous[field]

            if current_value != previous_value:
                # Format the change message
                if isinstance(current_value, list):
                    added_items = set(current_value) - set(previous_value)
                    removed_items = set(previous_value) - set(current_value)

                    if added_items:
                        changes.append(f"[+] {label}: Added {', '.join(sorted(added_items))}")
                    if removed_items:
                        changes.append(f"[-] {label}: Removed {', '.join(sorted(removed_items))}")
                else:
                    changes.append(f"[~] {label}: `{previous_value}` -> `{current_value}`")

        return changes


if __name__ == '__main__':
    # Test the changelog generator
    import sys
    from parser import MCreatorParser

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        elements_path = '../elements'

    # Parse current elements
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    # Create audit manager
    audit_dir = Path(elements_path).parent / 'mcreator_dashboard' / 'data' / 'audits'
    manager = AuditManager(str(audit_dir))

    # Create a snapshot
    print("\n=== Creating Audit Snapshot ===")
    snapshot_path = manager.create_snapshot(elements)

    # Generate changelog (if previous snapshot exists)
    print("\n=== Generating Changelog ===")
    all_snapshots = manager.get_all_snapshots()

    if len(all_snapshots) >= 2:
        # Compare to second-to-last snapshot
        previous_snapshot = all_snapshots[-2]
        print(f"Comparing to: {previous_snapshot.name}")

        changelog = manager.generate_changelog(elements, previous_snapshot)
        print("\n" + changelog)

        # Save changelog
        changelog_file = Path(elements_path).parent / 'mcreator_dashboard' / 'CHANGELOG.md'
        with open(changelog_file, 'w', encoding='utf-8') as f:
            f.write(changelog)
        print(f"\n[OK] Changelog saved to {changelog_file}")
    else:
        print("Not enough snapshots to generate changelog. Create at least 2 snapshots.")
