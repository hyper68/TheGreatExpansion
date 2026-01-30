import json
from pathlib import Path
p = Path(__file__).parent.parent / 'data' / 'relationships_test.json'
if not p.exists():
    print('relationships_test.json not found:', p)
    raise SystemExit(1)

data = json.loads(p.read_text(encoding='utf-8'))
els = data.get('elements', {})

total = len(els)
recipes = [ (name, e) for name,e in els.items() if e.get('type')=='recipe']
num_recipes = len(recipes)
no_requires = sum(1 for e in els.values() if not e.get('relationships', {}).get('requires'))
recipes_zero_inputs = sum(1 for name,e in recipes if not e.get('relationships', {}).get('requires'))

# duplicated consumed_by entries detection (by identical dicts)
dup_elements = []
for name,e in els.items():
    cb = e.get('relationships', {}).get('consumed_by', [])
    if isinstance(cb, list) and len(cb) > 1:
        seen = set()
        dup = False
        for item in cb:
            s = json.dumps(item, sort_keys=True)
            if s in seen:
                dup = True
                break
            seen.add(s)
        if dup:
            dup_elements.append(name)

# elements with many repeated produces entries
many_produces = sorted(((name, len(e.get('relationships', {}).get('produces', []))) for name,e in els.items()), key=lambda x:-x[1])[:10]

print('Total elements:', total)
print('Total recipes:', num_recipes)
print('Elements with no requires:', no_requires)
print('Recipes with zero inputs:', recipes_zero_inputs)
print('Elements with duplicated consumed_by entries:', len(dup_elements))
print('Sample duplicated elements (first 10):')
for x in dup_elements[:10]:
    print(' -', x)
print('\nTop 10 elements by produces count:')
for name,c in many_produces:
    if c>0:
        print(f' - {name}: {c}')
    
# Exit with 0
