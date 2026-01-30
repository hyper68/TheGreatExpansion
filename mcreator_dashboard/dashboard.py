"""
MCreator Dashboard GUI
Interactive spreadsheet view with search, filter, and detail panels
"""
import tkinter as tk
from tkinter import ttk, messagebox, filedialog
from typing import Dict, List, Optional
import json
from pathlib import Path

from parser import MCreatorParser, Element


class ModDashboard:
    """Main dashboard GUI application"""

    def __init__(self, root: tk.Tk, elements_path: str):
        self.root = root
        self.root.title("MCreator Mod Dashboard - The Great Expansion")
        self.root.geometry("1400x800")

        self.elements_path = elements_path
        self.parser = MCreatorParser(elements_path)
        self.elements: Dict[str, Element] = {}
        self.filtered_elements: List[Element] = []

        # Current filter state
        self.search_var = tk.StringVar()
        self.search_var.trace('w', lambda *args: self.apply_filters())
        self.type_filter_var = tk.StringVar(value="All Types")

        self._create_ui()
        self.load_data()

    def _create_ui(self):
        """Create the UI layout"""
        # Menu bar
        menubar = tk.Menu(self.root)
        self.root.config(menu=menubar)

        file_menu = tk.Menu(menubar, tearoff=0)
        menubar.add_cascade(label="File", menu=file_menu)
        file_menu.add_command(label="Reload Data", command=self.load_data)
        file_menu.add_command(label="Export to Excel", command=self.export_excel)
        file_menu.add_command(label="Export to CSV", command=self.export_csv)
        file_menu.add_separator()
        file_menu.add_command(label="Exit", command=self.root.quit)

        tools_menu = tk.Menu(menubar, tearoff=0)
        menubar.add_cascade(label="Tools", menu=tools_menu)
        tools_menu.add_command(label="Generate Changelog", command=self.generate_changelog)
        tools_menu.add_command(label="Generate Flowchart", command=self.generate_flowchart)
        tools_menu.add_command(label="Create Audit Snapshot", command=self.create_audit)

        # Top frame - toolbar
        toolbar = ttk.Frame(self.root, padding="5")
        toolbar.pack(side=tk.TOP, fill=tk.X)

        # Search
        ttk.Label(toolbar, text="Search:").pack(side=tk.LEFT, padx=5)
        search_entry = ttk.Entry(toolbar, textvariable=self.search_var, width=30)
        search_entry.pack(side=tk.LEFT, padx=5)

        # Type filter
        ttk.Label(toolbar, text="Type:").pack(side=tk.LEFT, padx=5)
        self.type_combo = ttk.Combobox(toolbar, textvariable=self.type_filter_var, width=15, state='readonly')
        self.type_combo.pack(side=tk.LEFT, padx=5)
        self.type_combo.bind('<<ComboboxSelected>>', lambda e: self.apply_filters())

        # Refresh button
        ttk.Button(toolbar, text="🔄 Refresh", command=self.load_data).pack(side=tk.LEFT, padx=10)
        # Auto Recipe and Auto Balance buttons
        ttk.Button(toolbar, text="Auto Recipe", command=self.handle_auto_recipe).pack(side=tk.LEFT, padx=5)
        ttk.Button(toolbar, text="Auto Balance", command=self.handle_auto_balance).pack(side=tk.LEFT, padx=5)

        # Stats label
        self.stats_label = ttk.Label(toolbar, text="")
        self.stats_label.pack(side=tk.RIGHT, padx=10)

        # Main content - split pane
        main_pane = ttk.PanedWindow(self.root, orient=tk.HORIZONTAL)
        main_pane.pack(fill=tk.BOTH, expand=True, padx=5, pady=5)

        # Left side - table view
        table_frame = ttk.Frame(main_pane)
        main_pane.add(table_frame, weight=3)

        # Treeview with scrollbars
        tree_scroll_y = ttk.Scrollbar(table_frame)
        tree_scroll_y.pack(side=tk.RIGHT, fill=tk.Y)

        tree_scroll_x = ttk.Scrollbar(table_frame, orient=tk.HORIZONTAL)
        tree_scroll_x.pack(side=tk.BOTTOM, fill=tk.X)

        columns = ('Name', 'Type', 'File', 'Dependencies', 'Used In', 'Outputs')
        self.tree = ttk.Treeview(table_frame, columns=columns, show='tree headings',
                                 yscrollcommand=tree_scroll_y.set, xscrollcommand=tree_scroll_x.set)

        tree_scroll_y.config(command=self.tree.yview)
        tree_scroll_x.config(command=self.tree.xview)

        # Configure columns
        self.tree.column('#0', width=0, stretch=tk.NO)  # Hide tree column
        self.tree.column('Name', width=200, anchor=tk.W)
        self.tree.column('Type', width=100, anchor=tk.W)
        self.tree.column('File', width=150, anchor=tk.W)
        self.tree.column('Dependencies', width=200, anchor=tk.W)
        self.tree.column('Used In', width=150, anchor=tk.W)
        self.tree.column('Outputs', width=150, anchor=tk.W)

        # Headers
        for col in columns:
            self.tree.heading(col, text=col, command=lambda c=col: self.sort_by_column(c))

        self.tree.pack(fill=tk.BOTH, expand=True)
        self.tree.bind('<<TreeviewSelect>>', self.on_item_select)

        # Right side - details panel
        details_frame = ttk.Frame(main_pane)
        main_pane.add(details_frame, weight=1)

        ttk.Label(details_frame, text="Element Details", font=('Arial', 12, 'bold')).pack(pady=5)

        # Details text area
        details_scroll = ttk.Scrollbar(details_frame)
        details_scroll.pack(side=tk.RIGHT, fill=tk.Y)

        self.details_text = tk.Text(details_frame, wrap=tk.WORD, yscrollcommand=details_scroll.set,
                                    width=40, font=('Courier', 9))
        self.details_text.pack(fill=tk.BOTH, expand=True, padx=5, pady=5)
        details_scroll.config(command=self.details_text.yview)

        # Creation Queue panel below details
        ttk.Label(details_frame, text="Creation Queue", font=('Arial', 11, 'bold')).pack(pady=(6,0))
        cq_frame = ttk.Frame(details_frame)
        cq_frame.pack(fill=tk.BOTH, expand=False, padx=5, pady=5)

        self.creation_queue_list = tk.Listbox(cq_frame, height=6)
        self.creation_queue_list.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)

        cq_scroll = ttk.Scrollbar(cq_frame, orient=tk.VERTICAL, command=self.creation_queue_list.yview)
        cq_scroll.pack(side=tk.RIGHT, fill=tk.Y)
        self.creation_queue_list.config(yscrollcommand=cq_scroll.set)

        cq_buttons = ttk.Frame(details_frame)
        cq_buttons.pack(fill=tk.X, padx=5)
        ttk.Button(cq_buttons, text="Add Selected", command=self.add_selected_to_queue).pack(side=tk.LEFT, padx=3)
        ttk.Button(cq_buttons, text="Remove", command=self.remove_selected_from_queue).pack(side=tk.LEFT, padx=3)
        ttk.Button(cq_buttons, text="Mark Done", command=self.mark_queue_done).pack(side=tk.LEFT, padx=3)
        ttk.Button(cq_buttons, text="Export Queue", command=self.export_creation_queue).pack(side=tk.RIGHT, padx=3)

        # Status bar
        self.status_bar = ttk.Label(self.root, text="Ready", relief=tk.SUNKEN, anchor=tk.W)
        self.status_bar.pack(side=tk.BOTTOM, fill=tk.X)

    def load_data(self):
        """Load and parse all elements"""
        self.status_bar.config(text="Loading elements...")
        self.root.update()

        try:
            self.elements = self.parser.parse_all()
            self.populate_type_filter()
            self.apply_filters()

            stats = self.parser.get_statistics()
            self.stats_label.config(
                text=f"Total: {stats['total_elements']} | Recipes: {stats['total_recipes']}"
            )

            self.status_bar.config(text=f"Loaded {len(self.elements)} elements")
            # load creation queue
            self._load_creation_queue()
        except Exception as e:
            messagebox.showerror("Error", f"Failed to load elements: {e}")
            self.status_bar.config(text="Error loading elements")

    def populate_type_filter(self):
        """Populate the type filter combobox"""
        types = set(element.element_type for element in self.elements.values())
        type_list = ['All Types'] + sorted(types)
        self.type_combo['values'] = type_list

    def apply_filters(self):
        """Apply search and type filters"""
        search_term = self.search_var.get().lower()
        type_filter = self.type_filter_var.get()

        self.filtered_elements = []
        for element in self.elements.values():
            # Type filter
            if type_filter != "All Types" and element.element_type != type_filter:
                continue

            # Search filter
            if search_term:
                searchable = f"{element.name} {element.element_type} {element.file_name} " \
                           f"{' '.join(element.dependencies)} {' '.join(element.recipes_using)}"
                if search_term not in searchable.lower():
                    continue

            self.filtered_elements.append(element)

        self.populate_table()

    def populate_table(self):
        """Populate the treeview with filtered elements"""
        # Clear existing items
        for item in self.tree.get_children():
            self.tree.delete(item)

        # Add filtered elements
        for element in self.filtered_elements:
            deps = ', '.join(element.dependencies[:3])
            if len(element.dependencies) > 3:
                deps += f" (+{len(element.dependencies) - 3})"

            used_in = ', '.join(element.recipes_using[:2])
            if len(element.recipes_using) > 2:
                used_in += f" (+{len(element.recipes_using) - 2})"

            outputs = ', '.join(element.recipe_outputs[:2])
            if len(element.recipe_outputs) > 2:
                outputs += f" (+{len(element.recipe_outputs) - 2})"

            self.tree.insert('', tk.END, values=(
                element.name,
                element.element_type,
                element.file_name,
                deps or '-',
                used_in or '-',
                outputs or '-'
            ), tags=(element.file_name,))

        self.status_bar.config(text=f"Showing {len(self.filtered_elements)} of {len(self.elements)} elements")

    def sort_by_column(self, column: str):
        """Sort table by clicked column"""
        # Get current sort direction
        current_heading = self.tree.heading(column)
        current_text = current_heading['text']

        # Toggle sort direction
        reverse = '▼' in current_text

        # Sort filtered elements
        col_index = ['Name', 'Type', 'File', 'Dependencies', 'Used In', 'Outputs'].index(column)

        if col_index == 0:
            self.filtered_elements.sort(key=lambda e: e.name.lower(), reverse=reverse)
        elif col_index == 1:
            self.filtered_elements.sort(key=lambda e: e.element_type.lower(), reverse=reverse)
        elif col_index == 2:
            self.filtered_elements.sort(key=lambda e: e.file_name.lower(), reverse=reverse)

        # Update heading
        for col in ['Name', 'Type', 'File', 'Dependencies', 'Used In', 'Outputs']:
            text = col
            if col == column:
                text += ' ▼' if not reverse else ' ▲'
            self.tree.heading(col, text=text)

        self.populate_table()

    def on_item_select(self, event):
        """Handle item selection"""
        selection = self.tree.selection()
        if not selection:
            return

        item = self.tree.item(selection[0])
        file_name = item['tags'][0] if item['tags'] else None

        if file_name:
            # Find element by file name
            element = next((e for e in self.elements.values() if e.file_name == file_name), None)
            if element:
                self.show_element_details(element)

    def show_element_details(self, element: Element):
        """Display detailed information about an element"""
        self.details_text.delete('1.0', tk.END)

        details = f"╔═══ {element.name} ═══╗\n\n"
        details += f"Type: {element.element_type.upper()}\n"
        details += f"File: {element.file_name}\n"
        details += f"\n"

        # Type-specific details
        detail_dict = element.to_dict()
        for key, value in detail_dict.items():
            if key not in ['Name', 'Type', 'File']:
                details += f"{key}: {value}\n"

        details += f"\n═══ Dependencies ═══\n"
        if element.dependencies:
            for dep in element.dependencies:
                details += f"  • {dep}\n"
        else:
            details += "  (none)\n"

        details += f"\n═══ Used In Recipes ═══\n"
        if element.recipes_using:
            for recipe in element.recipes_using:
                details += f"  • {recipe}\n"
        else:
            details += "  (none)\n"

        details += f"\n═══ Recipe Outputs ═══\n"
        if element.recipe_outputs:
            for output in element.recipe_outputs:
                details += f"  • {output}\n"
        else:
            details += "  (none)\n"

        # Show raw JSON snippet
        details += f"\n═══ Raw Data (snippet) ═══\n"
        raw_snippet = json.dumps(element.raw_data, indent=2)[:500]
        details += raw_snippet + "...\n"

        self.details_text.insert('1.0', details)

    # --- Creation Queue management ---
    def _creation_queue_path(self):
        return Path(self.elements_path).parent / 'mcreator_dashboard' / 'data' / 'creation_queue.json'

    def _load_creation_queue(self):
        path = self._creation_queue_path()
        self.creation_queue = []
        try:
            if path.exists():
                with open(path, 'r', encoding='utf-8') as f:
                    self.creation_queue = json.load(f)
        except Exception:
            self.creation_queue = []

        self._refresh_queue_ui()

    def _save_creation_queue(self):
        path = self._creation_queue_path()
        path.parent.mkdir(parents=True, exist_ok=True)
        with open(path, 'w', encoding='utf-8') as f:
            json.dump(self.creation_queue, f, indent=2)

    def _refresh_queue_ui(self):
        self.creation_queue_list.delete(0, tk.END)
        for idx, item in enumerate(self.creation_queue):
            status = item.get('status', '')
            label = f"[{status}] {item.get('name')} - {item.get('note','') }"
            self.creation_queue_list.insert(tk.END, label)

    def add_selected_to_queue(self):
        sel = self.tree.selection()
        if not sel:
            messagebox.showinfo('Info', 'Select an element to add to queue')
            return
        file_name = self.tree.item(sel[0])['tags'][0]
        element = next((e for e in self.elements.values() if e.file_name == file_name), None)
        if not element:
            return
        entry = {'name': element.name, 'file': element.file_name, 'type': element.element_type, 'status': 'pending', 'note': ''}
        self.creation_queue.append(entry)
        self._save_creation_queue()
        self._refresh_queue_ui()

    def remove_selected_from_queue(self):
        sel = self.creation_queue_list.curselection()
        if not sel:
            return
        idx = sel[0]
        del self.creation_queue[idx]
        self._save_creation_queue()
        self._refresh_queue_ui()

    def mark_queue_done(self):
        sel = self.creation_queue_list.curselection()
        if not sel:
            return
        idx = sel[0]
        self.creation_queue[idx]['status'] = 'done'
        self._save_creation_queue()
        self._refresh_queue_ui()

    def export_creation_queue(self):
        path = filedialog.asksaveasfilename(defaultextension='.json', filetypes=[('JSON files','*.json')], initialfile='creation_queue.json')
        if path:
            with open(path, 'w', encoding='utf-8') as f:
                json.dump(self.creation_queue, f, indent=2)
            messagebox.showinfo('Export', f'Exported to {path}')

    def export_excel(self):
        """Export to Excel - delegate to exporter module"""
        from exporter import export_to_excel

        file_path = filedialog.asksaveasfilename(
            defaultextension=".xlsx",
            filetypes=[("Excel files", "*.xlsx"), ("All files", "*.*")],
            initialfile="mod_elements.xlsx"
        )

        if file_path:
            try:
                export_to_excel(self.elements, file_path)
                messagebox.showinfo("Success", f"Exported to {file_path}")
            except Exception as e:
                messagebox.showerror("Error", f"Export failed: {e}")

    def export_csv(self):
        """Export to CSV - delegate to exporter module"""
        from exporter import export_to_csv

        file_path = filedialog.asksaveasfilename(
            defaultextension=".csv",
            filetypes=[("CSV files", "*.csv"), ("All files", "*.*")],
            initialfile="mod_elements.csv"
        )

        if file_path:
            try:
                export_to_csv(self.elements, file_path)
                messagebox.showinfo("Success", f"Exported to {file_path}")
            except Exception as e:
                messagebox.showerror("Error", f"Export failed: {e}")

    def create_audit(self):
        """Create an audit snapshot"""
        from changelog_generator import AuditManager

        audit_dir = Path(self.elements_path).parent / 'mcreator_dashboard' / 'data' / 'audits'
        manager = AuditManager(str(audit_dir))

        try:
            snapshot_path = manager.create_snapshot(self.elements)
            messagebox.showinfo("Success", f"Audit snapshot created:\n{snapshot_path}")
        except Exception as e:
            messagebox.showerror("Error", f"Failed to create audit: {e}")

    def generate_changelog(self):
        """Generate changelog from last audit"""
        from changelog_generator import AuditManager

        audit_dir = Path(self.elements_path).parent / 'mcreator_dashboard' / 'data' / 'audits'
        manager = AuditManager(str(audit_dir))

        try:
            changelog = manager.generate_changelog(self.elements)

            if not changelog:
                messagebox.showinfo("No Changes", "No previous audit found or no changes detected.")
                return

            # Show changelog in a new window
            changelog_window = tk.Toplevel(self.root)
            changelog_window.title("Changelog")
            changelog_window.geometry("800x600")

            text = tk.Text(changelog_window, wrap=tk.WORD, font=('Courier', 9))
            text.pack(fill=tk.BOTH, expand=True, padx=10, pady=10)
            text.insert('1.0', changelog)

            # Add save button
            ttk.Button(changelog_window, text="Save to File",
                      command=lambda: self._save_changelog(changelog)).pack(pady=5)

        except Exception as e:
            messagebox.showerror("Error", f"Failed to generate changelog: {e}")

    def _save_changelog(self, changelog: str):
        """Save changelog to file"""
        file_path = filedialog.asksaveasfilename(
            defaultextension=".md",
            filetypes=[("Markdown files", "*.md"), ("Text files", "*.txt"), ("All files", "*.*")],
            initialfile="CHANGELOG.md"
        )

        if file_path:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(changelog)
            messagebox.showinfo("Success", f"Changelog saved to {file_path}")

    def generate_flowchart(self):
        """Generate interactive flowchart"""
        from flowchart_generator import FlowchartGenerator

        try:
            generator = FlowchartGenerator(self.elements)

            # Ask user for focus element (optional)
            focus = tk.simpledialog.askstring(
                "Flowchart Focus",
                "Enter element name to focus on (or leave empty for full graph):",
                parent=self.root
            )

            output_path = Path(self.elements_path).parent / 'mcreator_dashboard' / 'data' / 'flowchart.html'
            generator.generate_interactive_graph(str(output_path), focus_element=focus)

            messagebox.showinfo("Success", f"Flowchart generated:\n{output_path}\n\nOpening in browser...")

            # Open in browser
            import webbrowser
            webbrowser.open(f'file://{output_path.absolute()}')

        except Exception as e:
            messagebox.showerror("Error", f"Failed to generate flowchart: {e}")

    # --- Auto Recipe / Auto Balance handlers ---
    def handle_auto_recipe(self):
        """Propose a recipe for the currently selected element and add to creation queue."""
        sel = self.tree.selection()
        if not sel:
            messagebox.showinfo('Info', 'Select an element first')
            return
        file_name = self.tree.item(sel[0])['tags'][0]
        element = next((e for e in self.elements.values() if e.file_name == file_name), None)
        if not element:
            return

        # Build relationships to get AI suggestions
        from relationship_analyzer import RelationshipAnalyzer
        analyzer = RelationshipAnalyzer(self.elements)
        rel = analyzer.relationships.get(element.name, {})

        # Find incoming AI suggested sources
        incoming = rel.get('ai_suggested_in', [])

        proposed_inputs = []
        if incoming:
            # Use top 3 suggested sources as inputs
            incoming_sorted = sorted(incoming, key=lambda x: x.get('score', 0), reverse=True)
            for s in incoming_sorted[:3]:
                proposed_inputs.append(s.get('source'))

        # Fallback: find recipes from similarly-named items
        if not proposed_inputs:
            # find elements with recipes that share tokens in name
            tokens = set((element.name or '').lower().split())
            for other in self.elements.values():
                if other is element: continue
                if not getattr(other, 'recipe_outputs', []):
                    continue
                other_tokens = set((other.name or '').lower().split())
                if tokens & other_tokens:
                    # take inputs from one of its recipes if available
                    if isinstance(other, type(element)) and getattr(other, 'recipes_using', None) is not None:
                        # try to find a recipe element to copy inputs from
                        for rname in other.recipe_outputs:
                            if rname in self.elements and isinstance(self.elements[rname], tuple()):
                                pass
                    # naive: add the other as component
                    proposed_inputs.append(other.name)
                if len(proposed_inputs) >= 3:
                    break

        # Create a proposed recipe entry
        note = f"Proposed inputs: {', '.join(proposed_inputs)}"
        entry = {
            'name': f"Proposed recipe for {element.name}",
            'file': element.file_name,
            'type': 'proposed_recipe',
            'status': 'pending',
            'note': note,
            'recipe': {
                'inputs': proposed_inputs,
                'output': element.name,
                'output_count': 1
            }
        }

        self.creation_queue.append(entry)
        self._save_creation_queue()
        self._refresh_queue_ui()
        messagebox.showinfo('Auto Recipe', f'Proposed recipe added to creation queue:\n{note}')

    def handle_auto_balance(self):
        """Suggest balance changes comparing selected element to a target element."""
        sel = self.tree.selection()
        if not sel:
            messagebox.showinfo('Info', 'Select an element first')
            return
        file_name = self.tree.item(sel[0])['tags'][0]
        element = next((e for e in self.elements.values() if e.file_name == file_name), None)
        if not element:
            return

        # Ask for target element name
        target_name = tk.simpledialog.askstring('Auto Balance', 'Enter target element name to balance to:')
        if not target_name:
            return
        target = next((e for e in self.elements.values() if e.name == target_name or e.file_name == target_name), None)
        if not target:
            messagebox.showerror('Error', f'Target element not found: {target_name}')
            return

        # Only handle armor for now
        if getattr(element, 'element_type', '') == 'armor' and getattr(target, 'element_type', '') == 'armor':
            # Compute totals
            src_vals = getattr(element, 'protection_values', {})
            tgt_vals = getattr(target, 'protection_values', {})
            src_total = sum(src_vals.values())
            tgt_total = sum(tgt_vals.values())
            if src_total == 0:
                factor = 1.0
            else:
                factor = (tgt_total / src_total) if src_total else 1.0

            suggested = {}
            for piece, val in src_vals.items():
                suggested[piece] = max(0, int(round(val * factor)))

            # Durability and enchantability
            src_dur = getattr(element, 'durability', 0)
            tgt_dur = getattr(target, 'durability', 0)
            dur_suggestion = int(round(src_dur * (tgt_dur / src_dur))) if src_dur and tgt_dur else tgt_dur or src_dur

            src_ench = getattr(element, 'enchantability', 0)
            tgt_ench = getattr(target, 'enchantability', 0)
            ench_suggestion = int(round(src_ench * (tgt_ench / src_ench))) if src_ench and tgt_ench else tgt_ench or src_ench

            # Prepare note and ask user to add to creation queue
            note_lines = [f"Suggested protection: {suggested}", f"Suggested durability: {dur_suggestion}", f"Suggested enchantability: {ench_suggestion}"]
            note = '\n'.join(note_lines)

            if messagebox.askyesno('Auto Balance Proposal', f'Auto-balance suggestions for {element.name} vs {target.name}:\n\n{note}\n\nAdd proposal to creation queue?'):
                entry = {
                    'name': f"Balance {element.name} to {target.name}",
                    'file': element.file_name,
                    'type': 'balance_proposal',
                    'status': 'pending',
                    'note': note,
                    'suggested': {
                        'protection_values': suggested,
                        'durability': dur_suggestion,
                        'enchantability': ench_suggestion
                    }
                }
                self.creation_queue.append(entry)
                self._save_creation_queue()
                self._refresh_queue_ui()
                messagebox.showinfo('Added', 'Balance proposal added to creation queue')
            return

        # Fall back: simple numeric comparison for tools/items
        # Try to compare 'attack_strength' or similar
        src_attack = getattr(element, 'attack_strength', None) or element.raw_data.get('definition', {}).get('attack', None)
        tgt_attack = getattr(target, 'attack_strength', None) or target.raw_data.get('definition', {}).get('attack', None)
        if src_attack is not None and tgt_attack is not None:
            try:
                src_attack = float(src_attack)
                tgt_attack = float(tgt_attack)
            except Exception:
                messagebox.showerror('Auto Balance', 'Could not parse attack values for comparison.')
                return

            factor = tgt_attack / src_attack if src_attack else 1.0
            suggested_attack = round(src_attack * factor, 2)
            note = f"Suggested attack: {suggested_attack} (was {src_attack})"
            if messagebox.askyesno('Auto Balance Proposal', f'{note}\n\nAdd to creation queue?'):
                entry = {'name': f"Balance {element.name} attack to {target.name}", 'file': element.file_name, 'type': 'balance_proposal', 'status': 'pending', 'note': note, 'suggested': {'attack': suggested_attack}}
                self.creation_queue.append(entry)
                self._save_creation_queue()
                self._refresh_queue_ui()
                messagebox.showinfo('Added', 'Balance proposal added to creation queue')
            return

        messagebox.showinfo('Auto Balance', 'Auto-balance not supported for selected types yet (armor/tools supported).')


def main():
    """Main entry point"""
    import sys

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        # Default to elements directory relative to this script
        elements_path = str(Path(__file__).parent.parent / 'elements')

    root = tk.Tk()
    app = ModDashboard(root, elements_path)
    root.mainloop()


if __name__ == '__main__':
    main()
