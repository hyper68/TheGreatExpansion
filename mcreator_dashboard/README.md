# MCreator Mod Dashboard

A comprehensive Python dashboard for analyzing, tracking, and visualizing your MCreator Minecraft mod. Built for **The Great Expansion** mod.

## ✨ NEW: Modern Dark Mode UI

**Version 2.0** features a completely redesigned interface with:
- 🌙 **Sleek dark theme** - Professional, easy on the eyes
- 🎨 **Color-coded elements** - Visual icons for blocks, items, recipes
- 📊 **Real-time statistics** - Beautiful stat cards in the header
- 🚀 **Enhanced performance** - Smooth, responsive interface
- 💅 **Modern styling** - Professional button designs and layouts

See [MODERN_UI_FEATURES.md](MODERN_UI_FEATURES.md) for full details!

## 🎯 Features

### 📊 Interactive Dashboard
- **Spreadsheet View**: Browse all mod elements in a sortable, searchable table
- **Real-time Filtering**: Filter by element type (blocks, items, recipes, mobs, etc.)
- **Search**: Quickly find elements by name or property
- **Details Panel**: View comprehensive information about any element
- **Statistics**: Track total elements, recipes, and relationships

### 📤 Export Capabilities
- **Excel Export**: Multi-sheet workbooks organized by element type
  - Separate sheets for blocks, items, recipes, mobs, armor, etc.
  - Statistics sheet with mod analysis
  - Auto-formatted columns and headers
- **CSV Export**: Single-file export for easy data manipulation

### 📝 Auto Changelog
- **Audit Snapshots**: Create timestamped snapshots of your mod state
- **Automatic Comparison**: Detect added, removed, and modified elements
- **Detailed Change Tracking**: See exactly what changed (properties, recipes, dependencies)
- **Markdown Output**: Professional changelog format ready for release notes

### 🔄 Interactive Flowchart
- **Visual Relationships**: See how elements connect (recipes, dependencies, drops)
- **Interactive HTML**: Zoom, pan, drag nodes, click for details
- **Color-Coded**: Different colors for blocks, items, recipes, mobs, etc.
- **Focused Views**: Zoom in on specific items and their recipe chains
- **Recipe Chains**: Trace the full crafting tree for any item

## 🚀 Installation

### Prerequisites
- Python 3.8 or higher
- pip (Python package manager)

### Setup

1. **Navigate to the dashboard directory:**
   ```bash
   cd mcreator_dashboard
   ```

2. **Install dependencies:**
   ```bash
   pip install -r requirements.txt
   ```

3. **You're ready to go!**

## 💻 Usage

### GUI Mode (Recommended)

Launch the interactive dashboard:
```bash
python main.py
```

Or specify a custom elements path:
```bash
python main.py --elements-path "C:\path\to\your\elements"
```

#### GUI Features:
- **Search Bar**: Type to filter elements instantly
- **Type Filter**: Dropdown to show only specific element types
- **Table**: Click column headers to sort
- **Details Panel**: Click any row to see full details
- **Menu Bar**:
  - File → Export to Excel/CSV
  - Tools → Generate Changelog, Generate Flowchart, Create Audit

### Command Line Mode

#### Export to Excel
```bash
python main.py --export-excel mod_elements.xlsx
```

#### Export to CSV
```bash
python main.py --export-csv mod_elements.csv
```

#### Create Audit Snapshot
```bash
python main.py --audit
```

#### Generate Changelog
```bash
python main.py --changelog CHANGELOG.md
```

#### Generate Flowchart
```bash
# Full flowchart
python main.py --flowchart flowchart.html

# Focus on specific element
python main.py --flowchart flowchart.html --focus "Diamond Hammer" --depth 2
```

## 📋 Workflow Examples

### Release Workflow

1. **Before making changes**, create an audit:
   ```bash
   python main.py --audit
   ```

2. **Make your mod changes** in MCreator

3. **Generate changelog** to see what changed:
   ```bash
   python main.py --changelog CHANGELOG.md
   ```

4. **Export documentation** for your release:
   ```bash
   python main.py --export-excel release_v0.5.0.xlsx
   ```

5. **Create flowchart** for visualization:
   ```bash
   python main.py --flowchart mod_flowchart.html
   ```

### Development Workflow

1. **Launch the dashboard**:
   ```bash
   python main.py
   ```

2. **Browse your elements** to understand current state

3. **Plan new features** by checking dependencies and recipes

4. **After adding content**, click "🔄 Refresh" to reload

5. **Generate flowchart** to visualize new recipe chains

## 📊 Supported Element Types

The dashboard parses and analyzes all MCreator element types:

- ✅ **Blocks** - Including machines, storage blocks, decorative blocks
- ✅ **Items** - Tools, materials, consumables
- ✅ **Recipes** - Crafting, smelting, custom recipes
- ✅ **Living Entities (Mobs)** - Health, drops, spawn biomes
- ✅ **Armor** - Protection values, durability, repair items
- ✅ **Dimensions** - Portal setup, biomes, world generation
- ✅ **Potions** - Effects, duration, amplifiers
- ✅ **Procedures** - Event triggers and logic
- ✅ **Biomes** - World generation features
- ✅ **Advancements** - Achievement tracking

## 🎨 Flowchart Legend

### Node Colors
- 🟢 **Green** - Blocks
- 🔵 **Blue** - Items
- 🟠 **Orange** - Recipes
- 🟣 **Purple** - Tools
- 🔴 **Red** - Mobs/Entities
- 🔷 **Cyan** - Dimensions
- 🟪 **Deep Purple** - Potions

### Edge Types
- **Solid Green Arrow** → Produces (recipe output)
- **Grey Arrow** → Recipe input
- **Dashed Blue Arrow** ⇢ Depends on

### Controls
- **Drag** - Pan the view
- **Scroll** - Zoom in/out
- **Click Node** - Show details
- **Drag Node** - Rearrange layout

## 📁 File Structure

```
mcreator_dashboard/
├── main.py                    # Main entry point
├── parser.py                  # Element parser
├── dashboard.py               # GUI dashboard
├── exporter.py                # Excel/CSV export
├── changelog_generator.py     # Changelog generation
├── flowchart_generator.py     # Flowchart visualization
├── requirements.txt           # Python dependencies
├── README.md                  # This file
└── data/
    ├── audits/               # Audit snapshots
    ├── cache/                # Cached data
    ├── flowchart.html        # Generated flowchart
    └── CHANGELOG.md          # Generated changelog
```

## 🔧 Advanced Features

### Focused Flowcharts

To see the full recipe chain for a specific item:

```bash
python main.py --flowchart steel_ingot_chain.html --focus "Steel Ingot" --depth 5
```

This will show:
- All items needed to craft Steel Ingot
- All recipes that use Steel Ingot
- Up to 5 levels deep in the dependency tree

### Batch Operations

Create a script to automate your workflow:

```bash
# audit.bat or audit.sh
python main.py --audit
python main.py --changelog CHANGELOG.md
python main.py --export-excel release_docs.xlsx
python main.py --flowchart full_mod_graph.html
echo "Release documentation generated!"
```

### Custom Parsing

Extend `parser.py` to add custom element type parsing:

```python
def _parse_custom_element(self, definition: Dict, file_name: str, raw_data: Dict) -> Element:
    """Parse your custom element type"""
    # Add custom parsing logic
    pass
```

## 🐛 Troubleshooting

### "Elements directory not found"
- Make sure you're running from the correct directory
- Specify the full path: `--elements-path "C:\full\path\to\elements"`

### GUI doesn't launch
- Ensure tkinter is installed: `python -m tkinter`
- On Linux: `sudo apt-get install python3-tk`

### Flowchart is empty
- Check that you have recipes in your mod
- Try focusing on a specific item: `--focus "ItemName"`

### Export fails
- Ensure you have write permissions to the output directory
- Check that the file isn't open in Excel

## 📈 Statistics

For **The Great Expansion** mod:
- **271 elements** total
- Multiple element types: blocks, items, recipes, mobs, dimensions
- Complex recipe chains and dependencies
- Custom dimensions (The Skylands)

## 🤝 Contributing

This tool is customized for The Great Expansion but can be adapted for any MCreator mod:

1. Fork the repository
2. Modify for your needs
3. Share improvements!

## 📝 License

Free to use and modify for your MCreator projects.

## 🎮 About The Great Expansion

This dashboard was built for **The Great Expansion**, a comprehensive Minecraft mod that adds:
- New dimensions (The Skylands)
- Industrial machinery (Induction Furnace, Refinery, Crusher)
- New ores and materials (Aqualith, Cryonite, Titanium)
- Custom mobs and biomes
- Advanced crafting chains

---

**Made with ❤️ for MCreator modders**

For questions or issues, check the generated logs or inspect the element JSON files directly.
