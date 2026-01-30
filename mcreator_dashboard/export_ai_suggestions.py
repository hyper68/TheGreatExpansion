"""Export AI suggestions to CSV for review."""
import csv
import sys
from pathlib import Path

def load_relationships(path: Path):
    import json
    with open(path, 'r', encoding='utf-8') as f:
        return json.load(f)


def export_csv(relationships: dict, out_path: Path):
    rows = []
    for name, rel in relationships.items():
        for s in rel.get('ai_suggested_out', []):
            rows.append({
                'source': name,
                'target': s.get('target'),
                'score': s.get('score'),
                'reason': s.get('reason')
            })

    # sort by score desc
    rows.sort(key=lambda r: float(r.get('score') or 0), reverse=True)

    with open(out_path, 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['source', 'target', 'score', 'reason']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        for r in rows:
            writer.writerow(r)

    print(f"[OK] Exported {len(rows)} AI suggestions to {out_path}")


if __name__ == '__main__':
    # Usage: python export_ai_suggestions.py <relationships_json> [out.csv]
    if len(sys.argv) < 2:
        print('Usage: export_ai_suggestions.py <relationships_json> [out.csv]')
        sys.exit(1)

    rel_path = Path(sys.argv[1])
    out = Path(sys.argv[2]) if len(sys.argv) > 2 else Path('data') / 'ai_suggestions.csv'

    if not rel_path.exists():
        print('Relationships JSON not found:', rel_path)
        sys.exit(1)

    rel = load_relationships(rel_path)
    # If structure wraps relationships under elements -> name -> relationships, accept both
    if 'elements' in rel and isinstance(rel['elements'], dict):
        # map to simple relationships mapping name -> rel.relationships
        simple = {name: data.get('relationships', {}) for name, data in rel['elements'].items()}
    else:
        simple = rel

    export_csv(simple, out)
