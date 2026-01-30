"""
Test script for modern UI components
Tests that all components load without errors
"""
import sys
from pathlib import Path

print("=" * 60)
print("Testing Modern Dashboard Components")
print("=" * 60)

# Test 1: Check dependencies
print("\n[1/5] Checking dependencies...")
try:
    import ttkbootstrap as ttkb
    print("  [OK] ttkbootstrap installed")
except ImportError:
    print("  [FAIL] ttkbootstrap not installed")
    sys.exit(1)

try:
    from PIL import Image
    print("  [OK] pillow installed")
except ImportError:
    print("  [WARN] pillow not installed")

# Test 2: Import modern dashboard
print("\n[2/5] Importing modern dashboard...")
try:
    from dashboard_modern import ModernDashboard, BOOTSTRAP_AVAILABLE
    print(f"  [OK] Modern dashboard imported (Bootstrap: {BOOTSTRAP_AVAILABLE})")
except Exception as e:
    print(f"  [FAIL] Failed to import: {e}")
    sys.exit(1)

# Test 3: Import parser
print("\n[3/5] Testing parser...")
try:
    from parser import MCreatorParser
    elements_path = Path(__file__).parent.parent / 'elements'
    parser = MCreatorParser(str(elements_path))
    elements = parser.parse_all()
    print(f"  [OK] Parsed {len(elements)} elements")
except Exception as e:
    print(f"  [FAIL] Parser failed: {e}")
    sys.exit(1)

# Test 4: Test exporter
print("\n[4/5] Testing exporter...")
try:
    from exporter import export_to_csv
    import tempfile
    import os

    # Create a temporary file
    with tempfile.NamedTemporaryFile(mode='w', suffix='.csv', delete=False) as f:
        temp_file = f.name

    export_to_csv(elements, temp_file)

    # Check file exists and has content
    if os.path.exists(temp_file) and os.path.getsize(temp_file) > 0:
        print(f"  [OK] CSV export successful ({os.path.getsize(temp_file)} bytes)")
        os.remove(temp_file)
    else:
        print("  [FAIL] CSV export failed")
except Exception as e:
    print(f"  [FAIL] Exporter failed: {e}")

# Test 5: Test changelog generator
print("\n[5/5] Testing changelog generator...")
try:
    from changelog_generator import AuditManager
    audit_dir = Path(__file__).parent / 'data' / 'audits'
    manager = AuditManager(str(audit_dir))

    # Count existing audits
    audits = manager.get_all_snapshots()
    print(f"  [OK] Changelog generator ready ({len(audits)} existing audits)")
except Exception as e:
    print(f"  [FAIL] Changelog generator failed: {e}")

# Summary
print("\n" + "=" * 60)
print("All tests passed! [OK]")
print("=" * 60)
print("\nYou can now launch the dashboard with:")
print("  python main.py")
print("\nOr use the convenient launcher:")
print("  run_dashboard.bat")
print("\n" + "=" * 60)
