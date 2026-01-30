"""
MCreator Mod Dashboard - Main Entry Point
Launch the dashboard GUI or run CLI commands
"""
import sys
import argparse
from pathlib import Path


def launch_gui(elements_path: str):
    """Launch the GUI dashboard"""
    try:
        # Try modern dashboard first
        from dashboard_modern import ModernDashboard, BOOTSTRAP_AVAILABLE

        if BOOTSTRAP_AVAILABLE:
            # Bootstrap creates its own root
            app = ModernDashboard(None, elements_path)
            app.root.mainloop()
        else:
            # Use classic tkinter
            import tkinter as tk
            root = tk.Tk()
            app = ModernDashboard(root, elements_path)
            root.mainloop()
    except ImportError as e:
        # Fallback to classic dashboard
        print("Note: ttkbootstrap not installed. Using classic dashboard.")
        print("Run 'pip install ttkbootstrap pillow' for the modern UI.")
        import tkinter as tk
        from dashboard import ModDashboard
        root = tk.Tk()
        app = ModDashboard(root, elements_path)
        root.mainloop()


def export_excel_cli(elements_path: str, output_path: str):
    """Export to Excel via CLI"""
    from parser import MCreatorParser
    from exporter import export_to_excel

    print("Parsing elements...")
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    print(f"Exporting to {output_path}...")
    export_to_excel(elements, output_path)
    print("Done!")


def export_csv_cli(elements_path: str, output_path: str):
    """Export to CSV via CLI"""
    from parser import MCreatorParser
    from exporter import export_to_csv

    print("Parsing elements...")
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    print(f"Exporting to {output_path}...")
    export_to_csv(elements, output_path)
    print("Done!")


def create_audit_cli(elements_path: str):
    """Create audit snapshot via CLI"""
    from parser import MCreatorParser
    from changelog_generator import AuditManager

    print("Parsing elements...")
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    audit_dir = Path(elements_path).parent / 'mcreator_dashboard' / 'data' / 'audits'
    manager = AuditManager(str(audit_dir))

    print("Creating audit snapshot...")
    snapshot_path = manager.create_snapshot(elements)
    print(f"[OK] Snapshot saved to: {snapshot_path}")


def generate_changelog_cli(elements_path: str, output_path: str):
    """Generate changelog via CLI"""
    from parser import MCreatorParser
    from changelog_generator import AuditManager

    print("Parsing elements...")
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    audit_dir = Path(elements_path).parent / 'mcreator_dashboard' / 'data' / 'audits'
    manager = AuditManager(str(audit_dir))

    print("Generating changelog...")
    changelog = manager.generate_changelog(elements)

    if changelog:
        with open(output_path, 'w', encoding='utf-8') as f:
            f.write(changelog)
        print(f"[OK] Changelog saved to: {output_path}")
    else:
        print("No changes detected or no previous audit found.")


def generate_flowchart_cli(elements_path: str, output_path: str, focus: str = None, depth: int = 3):
    """Generate flowchart via CLI"""
    from parser import MCreatorParser
    from flowchart_generator import FlowchartGenerator

    print("Parsing elements...")
    parser = MCreatorParser(elements_path)
    elements = parser.parse_all()

    print("Generating flowchart...")
    generator = FlowchartGenerator(elements)
    generator.generate_interactive_graph(output_path, focus_element=focus, depth=depth)

    print(f"[OK] Flowchart saved to: {output_path}")
    print(f"Open this file in a web browser to view the interactive flowchart.")


def main():
    """Main CLI entry point"""
    parser = argparse.ArgumentParser(
        description="MCreator Mod Dashboard - Analyze and visualize your Minecraft mod",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  # Launch GUI
  python main.py

  # Export to Excel
  python main.py --export-excel output.xlsx

  # Generate changelog
  python main.py --changelog CHANGELOG.md

  # Generate flowchart
  python main.py --flowchart flowchart.html

  # Focus on specific element
  python main.py --flowchart flowchart.html --focus "Diamond Hammer"
        """
    )

    parser.add_argument(
        '--elements-path',
        default='../elements',
        help='Path to the elements directory (default: ../elements)'
    )

    parser.add_argument(
        '--export-excel',
        metavar='FILE',
        help='Export elements to Excel file'
    )

    parser.add_argument(
        '--export-csv',
        metavar='FILE',
        help='Export elements to CSV file'
    )

    parser.add_argument(
        '--audit',
        action='store_true',
        help='Create an audit snapshot'
    )

    parser.add_argument(
        '--changelog',
        metavar='FILE',
        help='Generate changelog and save to file'
    )

    parser.add_argument(
        '--flowchart',
        metavar='FILE',
        help='Generate interactive flowchart HTML'
    )

    parser.add_argument(
        '--focus',
        metavar='ELEMENT',
        help='Focus flowchart on specific element'
    )

    parser.add_argument(
        '--depth',
        type=int,
        default=3,
        help='Depth for focused flowchart (default: 3)'
    )

    args = parser.parse_args()

    # Resolve elements path
    elements_path = str(Path(args.elements_path).resolve())

    if not Path(elements_path).exists():
        print(f"Error: Elements directory not found: {elements_path}")
        print("Please specify the correct path with --elements-path")
        sys.exit(1)

    # Execute commands
    if args.export_excel:
        export_excel_cli(elements_path, args.export_excel)

    elif args.export_csv:
        export_csv_cli(elements_path, args.export_csv)

    elif args.audit:
        create_audit_cli(elements_path)

    elif args.changelog:
        generate_changelog_cli(elements_path, args.changelog)

    elif args.flowchart:
        generate_flowchart_cli(elements_path, args.flowchart, args.focus, args.depth)

    else:
        # No CLI arguments - launch GUI
        print("Launching MCreator Mod Dashboard...")
        launch_gui(elements_path)


if __name__ == '__main__':
    main()
