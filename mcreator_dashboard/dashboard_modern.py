"""
MCreator Modern Dashboard - Dark Mode Edition
Sleek, modern UI with dark theme and enhanced visuals
"""
import tkinter as tk
from tkinter import ttk, messagebox, filedialog
from typing import Dict, List, Optional
import json
from pathlib import Path

try:
    import ttkbootstrap as ttkb
    from ttkbootstrap.constants import *
    BOOTSTRAP_AVAILABLE = True
except ImportError:
    BOOTSTRAP_AVAILABLE = False
    print("Warning: ttkbootstrap not available, using standard tkinter")

from parser import MCreatorParser, Element


class ModernDashboard:
    """Modern sleek dashboard with dark mode"""

    def __init__(self, root, elements_path: str):
        self.elements_path = elements_path
        self.parser = MCreatorParser(elements_path)
        self.elements: Dict[str, Element] = {}
        self.filtered_elements: List[Element] = []
        self.current_theme = "dark"  # dark or light

        # Colors for dark theme
        self.dark_theme = {
            'bg': '#1e1e1e',
            'bg_secondary': '#252526',
            'bg_tertiary': '#2d2d30',
            'fg': '#cccccc',
            'fg_secondary': '#858585',
            'accent': '#007acc',
            'accent_hover': '#1e8ad6',
            'success': '#4ec9b0',
            'warning': '#dcdcaa',
            'error': '#f48771',
            'border': '#3e3e42',
        }

        # Colors for light theme
        self.light_theme = {
            'bg': '#ffffff',
            'bg_secondary': '#f3f3f3',
            'bg_tertiary': '#e8e8e8',
            'fg': '#1e1e1e',
            'fg_secondary': '#616161',
            'accent': '#0078d4',
            'accent_hover': '#106ebe',
            'success': '#107c10',
            'warning': '#d83b01',
            'error': '#a80000',
            'border': '#d4d4d4',
        }

        # Initialize root window
        if BOOTSTRAP_AVAILABLE:
            self.root = ttkb.Window(themename="darkly")
            if root is not None:
                root.destroy()  # Destroy the passed-in root if it exists
        else:
            self.root = root
            self._apply_custom_dark_theme()

        self.root.title("MCreator Mod Dashboard - The Great Expansion")
        self.root.geometry("1600x900")

        # Search and filter vars
        self.search_var = tk.StringVar()
        self.search_var.trace('w', lambda *args: self.apply_filters())
        self.type_filter_var = tk.StringVar(value="All Types")

        self._create_modern_ui()
        self.load_data()

    def _apply_custom_dark_theme(self):
        """Apply custom dark theme if bootstrap not available"""
        style = ttk.Style()
        style.theme_use('clam')

        # Configure dark theme colors
        style.configure('.',
                       background=self.dark_theme['bg'],
                       foreground=self.dark_theme['fg'],
                       fieldbackground=self.dark_theme['bg_secondary'],
                       bordercolor=self.dark_theme['border'],
                       borderwidth=1)

        style.configure('TFrame', background=self.dark_theme['bg'])
        style.configure('TLabel', background=self.dark_theme['bg'], foreground=self.dark_theme['fg'])
        style.configure('TButton',
                       background=self.dark_theme['accent'],
                       foreground='white',
                       borderwidth=0,
                       focuscolor='none',
                       padding=10)
        style.map('TButton',
                 background=[('active', self.dark_theme['accent_hover'])])

        style.configure('Treeview',
                       background=self.dark_theme['bg_secondary'],
                       foreground=self.dark_theme['fg'],
                       fieldbackground=self.dark_theme['bg_secondary'],
                       borderwidth=0)
        style.configure('Treeview.Heading',
                       background=self.dark_theme['bg_tertiary'],
                       foreground=self.dark_theme['fg'],
                       borderwidth=1,
                       relief='flat')
        style.map('Treeview', background=[('selected', self.dark_theme['accent'])])

        # Configure root window
        self.root.configure(bg=self.dark_theme['bg'])

    def _create_modern_ui(self):
        """Create modern sleek UI"""
        # Custom fonts
        title_font = ('Segoe UI', 16, 'bold')
        header_font = ('Segoe UI', 11, 'bold')
        normal_font = ('Segoe UI', 10)
        mono_font = ('Consolas', 9)

        # Top bar with gradient effect
        top_bar = tk.Frame(self.root, bg=self.dark_theme['bg_tertiary'], height=80)
        top_bar.pack(side=tk.TOP, fill=tk.X)
        top_bar.pack_propagate(False)

        # Title section
        title_frame = tk.Frame(top_bar, bg=self.dark_theme['bg_tertiary'])
        title_frame.pack(side=tk.LEFT, padx=20, pady=10)

        title_label = tk.Label(title_frame,
                              text="🎮 MCreator Dashboard",
                              font=title_font,
                              bg=self.dark_theme['bg_tertiary'],
                              fg=self.dark_theme['fg'])
        title_label.pack(anchor='w')

        subtitle_label = tk.Label(title_frame,
                                 text="The Great Expansion",
                                 font=('Segoe UI', 9, 'italic'),
                                 bg=self.dark_theme['bg_tertiary'],
                                 fg=self.dark_theme['fg_secondary'])
        subtitle_label.pack(anchor='w')

        # Stats in top bar
        self.stats_frame = tk.Frame(top_bar, bg=self.dark_theme['bg_tertiary'])
        self.stats_frame.pack(side=tk.RIGHT, padx=20, pady=10)

        self.stats_labels = {}
        stat_items = [('Total', '0'), ('Blocks', '0'), ('Items', '0'), ('Recipes', '0')]
        for i, (label_text, value) in enumerate(stat_items):
            stat_card = tk.Frame(self.stats_frame, bg=self.dark_theme['bg_secondary'],
                               relief='flat', borderwidth=0)
            stat_card.grid(row=0, column=i, padx=8)

            value_label = tk.Label(stat_card,
                                  text=value,
                                  font=('Segoe UI', 14, 'bold'),
                                  bg=self.dark_theme['bg_secondary'],
                                  fg=self.dark_theme['success'])
            value_label.pack(pady=(8, 2))

            text_label = tk.Label(stat_card,
                                text=label_text,
                                font=('Segoe UI', 8),
                                bg=self.dark_theme['bg_secondary'],
                                fg=self.dark_theme['fg_secondary'])
            text_label.pack(pady=(0, 8))

            self.stats_labels[label_text] = value_label

        # Toolbar
        toolbar = tk.Frame(self.root, bg=self.dark_theme['bg_secondary'], height=60)
        toolbar.pack(side=tk.TOP, fill=tk.X)
        toolbar.pack_propagate(False)

        toolbar_inner = tk.Frame(toolbar, bg=self.dark_theme['bg_secondary'])
        toolbar_inner.pack(fill=tk.BOTH, expand=True, padx=20, pady=10)

        # Search with icon
        search_frame = tk.Frame(toolbar_inner, bg=self.dark_theme['bg_secondary'])
        search_frame.pack(side=tk.LEFT, fill=tk.Y)

        search_label = tk.Label(search_frame, text="🔍",
                              bg=self.dark_theme['bg_secondary'],
                              font=('Segoe UI', 12))
        search_label.pack(side=tk.LEFT, padx=(0, 5))

        search_entry = tk.Entry(search_frame,
                               textvariable=self.search_var,
                               font=normal_font,
                               bg=self.dark_theme['bg_tertiary'],
                               fg=self.dark_theme['fg'],
                               insertbackground=self.dark_theme['fg'],
                               relief='flat',
                               width=30)
        search_entry.pack(side=tk.LEFT, ipady=8)

        # Type filter dropdown
        filter_frame = tk.Frame(toolbar_inner, bg=self.dark_theme['bg_secondary'])
        filter_frame.pack(side=tk.LEFT, padx=20, fill=tk.Y)

        filter_label = tk.Label(filter_frame, text="📁",
                              bg=self.dark_theme['bg_secondary'],
                              font=('Segoe UI', 12))
        filter_label.pack(side=tk.LEFT, padx=(0, 5))

        if BOOTSTRAP_AVAILABLE:
            self.type_combo = ttkb.Combobox(filter_frame,
                                           textvariable=self.type_filter_var,
                                           width=15,
                                           state='readonly',
                                           bootstyle="dark")
        else:
            self.type_combo = ttk.Combobox(filter_frame,
                                          textvariable=self.type_filter_var,
                                          width=15,
                                          state='readonly')
        self.type_combo.pack(side=tk.LEFT, ipady=4)
        self.type_combo.bind('<<ComboboxSelected>>', lambda e: self.apply_filters())

        # Action buttons
        button_frame = tk.Frame(toolbar_inner, bg=self.dark_theme['bg_secondary'])
        button_frame.pack(side=tk.RIGHT)

        self._create_action_button(button_frame, "🔄 Refresh", self.load_data, self.dark_theme['accent'])
        self._create_action_button(button_frame, "📊 Export", self.show_export_menu, self.dark_theme['success'])
        self._create_action_button(button_frame, "📝 Changelog", self.generate_changelog_menu, self.dark_theme['warning'])
        self._create_action_button(button_frame, "🔗 Flowchart", self.generate_flowchart, '#8b5cf6')
        # Auto Recipe / Auto Balance buttons
        self._create_action_button(button_frame, "Auto Recipe", self.handle_auto_recipe, '#f59e0b')
        self._create_action_button(button_frame, "Auto Balance", self.handle_auto_balance, '#fb7185')

        # Theme toggle
        theme_btn = tk.Button(button_frame,
                             text="🌙" if self.current_theme == "dark" else "☀️",
                             command=self.toggle_theme,
                             font=('Segoe UI', 14),
                             bg=self.dark_theme['bg_tertiary'],
                             fg=self.dark_theme['fg'],
                             activebackground=self.dark_theme['bg'],
                             relief='flat',
                             cursor='hand2',
                             width=3,
                             height=1)
        theme_btn.pack(side=tk.LEFT, padx=5)

        # Main content area
        content = tk.Frame(self.root, bg=self.dark_theme['bg'])
        content.pack(fill=tk.BOTH, expand=True, padx=20, pady=(10, 20))

        # Create paned window for resizable split
        if BOOTSTRAP_AVAILABLE:
            paned = ttkb.Panedwindow(content, orient=tk.HORIZONTAL, bootstyle="dark")
        else:
            paned = ttk.PanedWindow(content, orient=tk.HORIZONTAL)
        paned.pack(fill=tk.BOTH, expand=True)

        # Left panel - table
        left_panel = tk.Frame(paned, bg=self.dark_theme['bg'])
        paned.add(left_panel, weight=3)

        # Table header
        table_header = tk.Frame(left_panel, bg=self.dark_theme['bg'])
        table_header.pack(side=tk.TOP, fill=tk.X, pady=(0, 10))

        tk.Label(table_header,
                text="Elements",
                font=header_font,
                bg=self.dark_theme['bg'],
                fg=self.dark_theme['fg']).pack(side=tk.LEFT)

        self.count_label = tk.Label(table_header,
                                    text="(0 items)",
                                    font=('Segoe UI', 9),
                                    bg=self.dark_theme['bg'],
                                    fg=self.dark_theme['fg_secondary'])
        self.count_label.pack(side=tk.LEFT, padx=10)

        # Treeview with modern styling
        tree_frame = tk.Frame(left_panel, bg=self.dark_theme['bg_secondary'],
                            relief='flat', borderwidth=1)
        tree_frame.pack(fill=tk.BOTH, expand=True)

        tree_scroll_y = ttk.Scrollbar(tree_frame)
        tree_scroll_y.pack(side=tk.RIGHT, fill=tk.Y)

        tree_scroll_x = ttk.Scrollbar(tree_frame, orient=tk.HORIZONTAL)
        tree_scroll_x.pack(side=tk.BOTTOM, fill=tk.X)

        columns = ('Name', 'Type', 'Dependencies', 'Used In')
        self.tree = ttk.Treeview(tree_frame,
                                columns=columns,
                                show='tree headings',
                                yscrollcommand=tree_scroll_y.set,
                                xscrollcommand=tree_scroll_x.set,
                                selectmode='browse')

        tree_scroll_y.config(command=self.tree.yview)
        tree_scroll_x.config(command=self.tree.xview)

        # Configure columns
        self.tree.column('#0', width=0, stretch=tk.NO)
        self.tree.column('Name', width=250, anchor=tk.W)
        self.tree.column('Type', width=120, anchor=tk.W)
        self.tree.column('Dependencies', width=200, anchor=tk.W)
        self.tree.column('Used In', width=150, anchor=tk.W)

        # Headers
        for col in columns:
            self.tree.heading(col, text=col, command=lambda c=col: self.sort_by_column(c))

        self.tree.pack(fill=tk.BOTH, expand=True, padx=1, pady=1)
        self.tree.bind('<<TreeviewSelect>>', self.on_item_select)

        # Right panel - details
        right_panel = tk.Frame(paned, bg=self.dark_theme['bg'])
        paned.add(right_panel, weight=1)

        # Details header
        details_header = tk.Frame(right_panel, bg=self.dark_theme['bg'])
        details_header.pack(side=tk.TOP, fill=tk.X, pady=(0, 10))

        tk.Label(details_header,
                text="Details",
                font=header_font,
                bg=self.dark_theme['bg'],
                fg=self.dark_theme['fg']).pack(side=tk.LEFT)

        # Details panel with modern styling
        details_frame = tk.Frame(right_panel,
                                bg=self.dark_theme['bg_secondary'],
                                relief='flat',
                                borderwidth=1)
        details_frame.pack(fill=tk.BOTH, expand=True)

        details_scroll = ttk.Scrollbar(details_frame)
        details_scroll.pack(side=tk.RIGHT, fill=tk.Y)

        self.details_text = tk.Text(details_frame,
                                    wrap=tk.WORD,
                                    yscrollcommand=details_scroll.set,
                                    font=mono_font,
                                    bg=self.dark_theme['bg_secondary'],
                                    fg=self.dark_theme['fg'],
                                    insertbackground=self.dark_theme['fg'],
                                    relief='flat',
                                    padx=15,
                                    pady=15,
                                    spacing1=2,
                                    spacing3=2)
        self.details_text.pack(fill=tk.BOTH, expand=True, padx=1, pady=1)
        details_scroll.config(command=self.details_text.yview)

        # Creation Queue panel below details (modern)
        cq_label = tk.Label(right_panel, text="Creation Queue", font=header_font, bg=self.dark_theme['bg'], fg=self.dark_theme['fg'])
        cq_label.pack(pady=(8, 2), anchor='w', padx=6)

        cq_frame = tk.Frame(right_panel, bg=self.dark_theme['bg_secondary'])
        cq_frame.pack(fill=tk.BOTH, expand=False, padx=6, pady=(0, 8))

        self.creation_queue_list = tk.Listbox(cq_frame, height=6, bg=self.dark_theme['bg_secondary'], fg=self.dark_theme['fg'], relief='flat')
        self.creation_queue_list.pack(side=tk.LEFT, fill=tk.BOTH, expand=True, padx=(6,0), pady=6)

        cq_scroll = ttk.Scrollbar(cq_frame, orient=tk.VERTICAL, command=self.creation_queue_list.yview)
        cq_scroll.pack(side=tk.RIGHT, fill=tk.Y, padx=(0,6), pady=6)
        self.creation_queue_list.config(yscrollcommand=cq_scroll.set)

        cq_buttons = tk.Frame(right_panel, bg=self.dark_theme['bg'])
        cq_buttons.pack(fill=tk.X, padx=6, pady=(0,8))

        self._create_action_button(cq_buttons, "Add Selected", self.add_selected_to_queue, self.dark_theme['accent'])
        self._create_action_button(cq_buttons, "Remove", self.remove_selected_from_queue, self.dark_theme['error'])
        self._create_action_button(cq_buttons, "Mark Done", self.mark_queue_done, self.dark_theme['success'])
        self._create_action_button(cq_buttons, "Export Queue", self.export_creation_queue, self.dark_theme['bg_tertiary'])

        # Configure text tags for syntax highlighting
        self.details_text.tag_configure('header', font=('Segoe UI', 12, 'bold'),
                                       foreground=self.dark_theme['accent'])
        self.details_text.tag_configure('subheader', font=('Segoe UI', 10, 'bold'),
                                       foreground=self.dark_theme['success'])
        self.details_text.tag_configure('key', foreground=self.dark_theme['warning'])
        self.details_text.tag_configure('value', foreground=self.dark_theme['fg'])

        # Status bar at bottom
        status_bar = tk.Frame(self.root, bg=self.dark_theme['bg_tertiary'], height=30)
        status_bar.pack(side=tk.BOTTOM, fill=tk.X)
        status_bar.pack_propagate(False)

        self.status_label = tk.Label(status_bar,
                                     text="Ready",
                                     font=('Segoe UI', 9),
                                     bg=self.dark_theme['bg_tertiary'],
                                     fg=self.dark_theme['fg_secondary'],
                                     anchor='w')
        self.status_label.pack(side=tk.LEFT, padx=20, fill=tk.X, expand=True)

        version_label = tk.Label(status_bar,
                                text="v1.0.0",
                                font=('Segoe UI', 8),
                                bg=self.dark_theme['bg_tertiary'],
                                fg=self.dark_theme['fg_secondary'])
        version_label.pack(side=tk.RIGHT, padx=20)

    def _create_action_button(self, parent, text, command, color):
        """Create a styled action button"""
        btn = tk.Button(parent,
                       text=text,
                       command=command,
                       font=('Segoe UI', 9, 'bold'),
                       bg=color,
                       fg='white',
                       activebackground=color,
                       activeforeground='white',
                       relief='flat',
                       cursor='hand2',
                       padx=15,
                       pady=8)
        btn.pack(side=tk.LEFT, padx=5)

        # Hover effects
        def on_enter(e):
            btn.configure(bg=self._brighten_color(color))

        def on_leave(e):
            btn.configure(bg=color)

        btn.bind('<Enter>', on_enter)
        btn.bind('<Leave>', on_leave)

        return btn

    def _brighten_color(self, hex_color, factor=1.2):
        """Brighten a hex color"""
        hex_color = hex_color.lstrip('#')
        rgb = tuple(int(hex_color[i:i+2], 16) for i in (0, 2, 4))
        rgb = tuple(min(int(c * factor), 255) for c in rgb)
        return f'#{rgb[0]:02x}{rgb[1]:02x}{rgb[2]:02x}'

    def toggle_theme(self):
        """Toggle between dark and light themes"""
        # This would require significant refactoring
        # For now, show a message
        messagebox.showinfo("Theme Toggle",
                          "Theme toggle will be fully implemented in the next update!\n\n"
                          "Current theme: Dark Mode")

    def load_data(self):
        """Load and parse all elements"""
        self.status_label.config(text="Loading elements...")
        self.root.update()

        try:
            self.elements = self.parser.parse_all()
            self.populate_type_filter()
            self.apply_filters()

            stats = self.parser.get_statistics()

            # Update stats in top bar
            self.stats_labels['Total'].config(text=str(stats['total_elements']))
            self.stats_labels['Recipes'].config(text=str(stats['total_recipes']))
            self.stats_labels['Blocks'].config(text=str(stats['type_counts'].get('block', 0)))
            self.stats_labels['Items'].config(text=str(stats['type_counts'].get('item', 0)))

            self.status_label.config(text=f"✓ Loaded {len(self.elements)} elements")
            # load creation queue
            try:
                self._load_creation_queue()
            except Exception:
                pass
        except Exception as e:
            messagebox.showerror("Error", f"Failed to load elements: {e}")
            self.status_label.config(text="Error loading elements")

    def populate_type_filter(self):
        """Populate type filter combobox"""
        types = set(element.element_type for element in self.elements.values())
        type_list = ['All Types'] + sorted(types)
        self.type_combo['values'] = type_list

    def apply_filters(self):
        """Apply search and type filters"""
        search_term = self.search_var.get().lower()
        type_filter = self.type_filter_var.get()

        self.filtered_elements = []
        for element in self.elements.values():
            if type_filter != "All Types" and element.element_type != type_filter:
                continue

            if search_term:
                searchable = f"{element.name} {element.element_type} {element.file_name} " \
                           f"{' '.join(element.dependencies)} {' '.join(element.recipes_using)}"
                if search_term not in searchable.lower():
                    continue

            self.filtered_elements.append(element)

        self.populate_table()

    def populate_table(self):
        """Populate treeview with filtered elements"""
        for item in self.tree.get_children():
            self.tree.delete(item)

        # Color-code by element type
        type_colors = {
            'block': '#8BC34A',
            'item': '#2196F3',
            'recipe': '#FF9800',
            'tool': '#9C27B0',
            'mob': '#F44336',
            'armor': '#E91E63',
        }

        for element in self.filtered_elements:
            deps = ', '.join(element.dependencies[:2])
            if len(element.dependencies) > 2:
                deps += f" +{len(element.dependencies) - 2}"

            used_in = ', '.join(element.recipes_using[:2])
            if len(element.recipes_using) > 2:
                used_in += f" +{len(element.recipes_using) - 2}"

            # Add visual indicators
            type_icon = {
                'block': '🧱',
                'item': '⚙️',
                'recipe': '📋',
                'tool': '🔧',
                'mob': '👾',
                'armor': '🛡️',
                'dimension': '🌐',
                'potion': '🧪'
            }.get(element.element_type, '📄')

            self.tree.insert('', tk.END, values=(
                f"{type_icon} {element.name}",
                element.element_type.upper(),
                deps or '-',
                used_in or '-'
            ), tags=(element.file_name,))

        self.count_label.config(text=f"({len(self.filtered_elements)} items)")
        self.status_label.config(text=f"Showing {len(self.filtered_elements)} of {len(self.elements)} elements")

    def sort_by_column(self, column: str):
        """Sort table by column"""
        col_index = ['Name', 'Type', 'Dependencies', 'Used In'].index(column)

        if col_index == 0:
            self.filtered_elements.sort(key=lambda e: e.name.lower())
        elif col_index == 1:
            self.filtered_elements.sort(key=lambda e: e.element_type.lower())

        self.populate_table()

    def on_item_select(self, event):
        """Handle item selection"""
        selection = self.tree.selection()
        if not selection:
            return

        item = self.tree.item(selection[0])
        file_name = item['tags'][0] if item['tags'] else None

        if file_name:
            element = next((e for e in self.elements.values() if e.file_name == file_name), None)
            if element:
                self.show_element_details(element)

    def show_element_details(self, element: Element):
        """Display element details with syntax highlighting"""
        self.details_text.delete('1.0', tk.END)

        # Header
        self.details_text.insert('end', f"{element.name}\n", 'header')
        self.details_text.insert('end', f"Type: {element.element_type.upper()}\n\n", 'subheader')

        # Properties
        detail_dict = element.to_dict()
        for key, value in detail_dict.items():
            if key not in ['Name', 'Type', 'File'] and value:
                self.details_text.insert('end', f"{key}: ", 'key')
                self.details_text.insert('end', f"{value}\n", 'value')

        # Dependencies
        self.details_text.insert('end', "\n━━━ Dependencies ━━━\n", 'subheader')
        if element.dependencies:
            for dep in element.dependencies:
                self.details_text.insert('end', f"  ▸ {dep}\n", 'value')
        else:
            self.details_text.insert('end', "  (none)\n", 'value')

        # Used in recipes
        self.details_text.insert('end', "\n━━━ Used In Recipes ━━━\n", 'subheader')
        if element.recipes_using:
            for recipe in element.recipes_using:
                self.details_text.insert('end', f"  ▸ {recipe}\n", 'value')
        else:
            self.details_text.insert('end', "  (none)\n", 'value')

        # Produces
        self.details_text.insert('end', "\n━━━ Produces ━━━\n", 'subheader')
        if element.recipe_outputs:
            for output in element.recipe_outputs:
                self.details_text.insert('end', f"  ▸ {output}\n", 'value')
        else:
            self.details_text.insert('end', "  (none)\n", 'value')

    def show_export_menu(self):
        """Show export options menu"""
        menu = tk.Menu(self.root, tearoff=0,
                      bg=self.dark_theme['bg_tertiary'],
                      fg=self.dark_theme['fg'],
                      activebackground=self.dark_theme['accent'],
                      activeforeground='white')
        menu.add_command(label="📊 Export to Excel", command=self.export_excel)
        menu.add_command(label="📄 Export to CSV", command=self.export_csv)

        # Show menu at cursor
        try:
            menu.tk_popup(self.root.winfo_pointerx(), self.root.winfo_pointery())
        finally:
            menu.grab_release()

    def export_excel(self):
        """Export to Excel"""
        from exporter import export_to_excel

        file_path = filedialog.asksaveasfilename(
            defaultextension=".xlsx",
            filetypes=[("Excel files", "*.xlsx"), ("All files", "*.*")],
            initialfile="mod_elements.xlsx"
        )

        if file_path:
            try:
                self.status_label.config(text="Exporting to Excel...")
                self.root.update()
                export_to_excel(self.elements, file_path)
                self.status_label.config(text=f"✓ Exported to {Path(file_path).name}")
                messagebox.showinfo("Success", f"Exported to {file_path}")
            except Exception as e:
                messagebox.showerror("Error", f"Export failed: {e}")
                self.status_label.config(text="Export failed")

    def export_csv(self):
        """Export to CSV"""
        from exporter import export_to_csv

        file_path = filedialog.asksaveasfilename(
            defaultextension=".csv",
            filetypes=[("CSV files", "*.csv"), ("All files", "*.*")],
            initialfile="mod_elements.csv"
        )

        if file_path:
            try:
                self.status_label.config(text="Exporting to CSV...")
                self.root.update()
                export_to_csv(self.elements, file_path)
                self.status_label.config(text=f"✓ Exported to {Path(file_path).name}")
                messagebox.showinfo("Success", f"Exported to {file_path}")
            except Exception as e:
                messagebox.showerror("Error", f"Export failed: {e}")
                self.status_label.config(text="Export failed")

    def generate_changelog_menu(self):
        """Generate changelog"""
        from changelog_generator import AuditManager

        audit_dir = Path(self.elements_path).parent / 'mcreator_dashboard' / 'data' / 'audits'
        manager = AuditManager(str(audit_dir))

        try:
            # First create an audit
            self.status_label.config(text="Creating audit snapshot...")
            self.root.update()
            manager.create_snapshot(self.elements)

            # Then generate changelog
            self.status_label.config(text="Generating changelog...")
            self.root.update()
            changelog = manager.generate_changelog(self.elements)

            if not changelog or "No changes detected" in changelog:
                self.status_label.config(text="No changes detected")
                messagebox.showinfo("Changelog", "No changes detected since last audit.")
                return

            # Show changelog window
            self.show_changelog_window(changelog)
            self.status_label.config(text="✓ Changelog generated")

        except Exception as e:
            messagebox.showerror("Error", f"Failed to generate changelog: {e}")
            self.status_label.config(text="Changelog generation failed")

    def show_changelog_window(self, changelog: str):
        """Show changelog in modern window"""
        window = tk.Toplevel(self.root)
        window.title("Changelog")
        window.geometry("900x700")
        window.configure(bg=self.dark_theme['bg'])

        # Header
        header = tk.Frame(window, bg=self.dark_theme['bg_tertiary'], height=60)
        header.pack(fill=tk.X)
        header.pack_propagate(False)

        tk.Label(header,
                text="📝 Changelog",
                font=('Segoe UI', 14, 'bold'),
                bg=self.dark_theme['bg_tertiary'],
                fg=self.dark_theme['fg']).pack(side=tk.LEFT, padx=20, pady=15)

        # Changelog text
        text_frame = tk.Frame(window, bg=self.dark_theme['bg_secondary'])
        text_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)

        scroll = ttk.Scrollbar(text_frame)
        scroll.pack(side=tk.RIGHT, fill=tk.Y)

        text = tk.Text(text_frame,
                      wrap=tk.WORD,
                      font=('Consolas', 9),
                      bg=self.dark_theme['bg_secondary'],
                      fg=self.dark_theme['fg'],
                      yscrollcommand=scroll.set,
                      relief='flat',
                      padx=20,
                      pady=20)
        text.pack(fill=tk.BOTH, expand=True)
        scroll.config(command=text.yview)

        text.insert('1.0', changelog)
        text.config(state='disabled')

        # Save button
        save_btn = tk.Button(window,
                            text="💾 Save to File",
                            command=lambda: self._save_changelog(changelog),
                            font=('Segoe UI', 10, 'bold'),
                            bg=self.dark_theme['success'],
                            fg='white',
                            relief='flat',
                            cursor='hand2',
                            padx=20,
                            pady=10)
        save_btn.pack(pady=(0, 20))

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
            self.status_label.config(text="Generating flowchart...")
            self.root.update()

            generator = FlowchartGenerator(self.elements)
            output_path = Path(self.elements_path).parent / 'mcreator_dashboard' / 'data' / 'flowchart.html'
            generator.generate_interactive_graph(str(output_path))

            self.status_label.config(text="✓ Flowchart generated")
            messagebox.showinfo("Success",
                              f"Flowchart generated!\n\nOpening in browser...")

            # Open in browser
            import webbrowser
            webbrowser.open(f'file://{output_path.absolute()}')

        except Exception as e:
            messagebox.showerror("Error", f"Failed to generate flowchart: {e}")

    # --- Creation Queue management (modern) ---
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
        try:
            self.creation_queue_list.delete(0, tk.END)
            for idx, item in enumerate(getattr(self, 'creation_queue', [])):
                status = item.get('status', '')
                label = f"[{status}] {item.get('name')} - {item.get('note','') }"
                self.creation_queue_list.insert(tk.END, label)
        except Exception:
            pass

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

    # --- Auto Recipe / Auto Balance handlers (modern) ---
    def handle_auto_recipe(self):
        sel = self.tree.selection()
        if not sel:
            messagebox.showinfo('Info', 'Select an element first')
            return
        file_name = self.tree.item(sel[0])['tags'][0]
        element = next((e for e in self.elements.values() if e.file_name == file_name), None)
        if not element:
            return

        from relationship_analyzer import RelationshipAnalyzer
        analyzer = RelationshipAnalyzer(self.elements)
        rel = analyzer.relationships.get(element.name, {})

        incoming = rel.get('ai_suggested_in', [])

        proposed_inputs = []
        if incoming:
            incoming_sorted = sorted(incoming, key=lambda x: x.get('score', 0), reverse=True)
            for s in incoming_sorted[:3]:
                proposed_inputs.append(s.get('source'))

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
        sel = self.tree.selection()
        if not sel:
            messagebox.showinfo('Info', 'Select an element first')
            return
        file_name = self.tree.item(sel[0])['tags'][0]
        element = next((e for e in self.elements.values() if e.file_name == file_name), None)
        if not element:
            return

        target_name = tk.simpledialog.askstring('Auto Balance', 'Enter target element name to balance to:')
        if not target_name:
            return
        target = next((e for e in self.elements.values() if e.name == target_name or e.file_name == target_name), None)
        if not target:
            messagebox.showerror('Error', f'Target element not found: {target_name}')
            return

        if getattr(element, 'element_type', '') == 'armor' and getattr(target, 'element_type', '') == 'armor':
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

            src_dur = getattr(element, 'durability', 0)
            tgt_dur = getattr(target, 'durability', 0)
            dur_suggestion = int(round(src_dur * (tgt_dur / src_dur))) if src_dur and tgt_dur else tgt_dur or src_dur

            src_ench = getattr(element, 'enchantability', 0)
            tgt_ench = getattr(target, 'enchantability', 0)
            ench_suggestion = int(round(src_ench * (tgt_ench / src_ench))) if src_ench and tgt_ench else tgt_ench or src_ench

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
    """Main entry point for modern dashboard"""
    import sys

    if len(sys.argv) > 1:
        elements_path = sys.argv[1]
    else:
        elements_path = str(Path(__file__).parent.parent / 'elements')

    if BOOTSTRAP_AVAILABLE:
        root = None  # Will be created by ModernDashboard
    else:
        root = tk.Tk()

    app = ModernDashboard(root, elements_path)

    if BOOTSTRAP_AVAILABLE:
        app.root.mainloop()
    else:
        root.mainloop()


if __name__ == '__main__':
    main()
