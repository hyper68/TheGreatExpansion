# Getting Started with MCreator Mod Dashboard

Welcome! This guide will help you get up and running with the dashboard for your "The Great Expansion" mod.

## Installation (One-Time Setup)

### Step 1: Install Dependencies

Double-click the `install.bat` file in the `mcreator_dashboard` folder.

**OR** run manually:
```bash
cd mcreator_dashboard
pip install -r requirements.txt
```

This will install:
- pandas (spreadsheet export)
- openpyxl (Excel files)
- networkx (graph analysis)
- pyvis (interactive flowcharts)
- matplotlib (visualizations)

## Quick Start

### Launch the Dashboard

**Easy way:**
Double-click `run_dashboard.bat`

**Manual way:**
```bash
cd mcreator_dashboard
python main.py
```

The dashboard will open showing all 271 elements from your mod!

## What You Can Do

### 1. Browse Your Mod

- **Search**: Type in the search box to find elements instantly
- **Filter**: Use the dropdown to show only blocks, items, recipes, etc.
- **Sort**: Click any column header to sort
- **Details**: Click a row to see full element details

### 2. Export Documentation

**From the GUI:**
- File → Export to Excel
- File → Export to CSV

**From command line:**
```bash
python main.py --export-excel my_mod_v0.5.xlsx
python main.py --export-csv my_mod_v0.5.csv
```

Your spreadsheet will have:
- All Elements sheet
- Separate sheets for each type (blocks, items, recipes, etc.)
- Statistics sheet with analysis

### 3. Track Changes (Changelog)

**First time - Create a baseline:**
```bash
python main.py --audit
```

**After making mod changes:**
```bash
python main.py --changelog CHANGELOG.md
```

The changelog will show:
- Added elements
- Removed elements
- Modified elements (with specific changes)

### 4. Visualize Recipe Chains

**Full mod flowchart:**
```bash
python main.py --flowchart flowchart.html
```

**Focus on specific item:**
```bash
python main.py --flowchart diamond_hammer.html --focus "Diamond Hammer"
```

Then open the HTML file in your web browser to see an interactive graph!

## Example Workflow

### For a Mod Release

1. **Before starting work:**
   ```bash
   python main.py --audit
   ```

2. **Make your changes in MCreator**

3. **Generate documentation:**
   ```bash
   python main.py --changelog CHANGELOG_v0.6.md
   python main.py --export-excel mod_docs_v0.6.xlsx
   python main.py --flowchart mod_graph_v0.6.html
   ```

4. **Review the changes:**
   - Open `CHANGELOG_v0.6.md` to see what changed
   - Open `mod_docs_v0.6.xlsx` in Excel
   - Open `mod_graph_v0.6.html` in a browser

5. **Include in your release:**
   - Add changelog to release notes
   - Share spreadsheet with your team
   - Post flowchart on your mod page

## Your Mod Statistics

Based on current analysis:
- **Total Elements:** 269
- **Recipes:** 78
- **Blocks:** 90
- **Items:** 27
- **Mobs:** 2 (Sub Zombie, Sand Crab)
- **Dimensions:** 1 (The Skylands)
- **And more!**

## Need Help?

### Dashboard won't start?
- Make sure you ran `install.bat` first
- Check that Python 3.8+ is installed: `python --version`

### Can't find elements?
The dashboard automatically looks in `../elements` relative to the `mcreator_dashboard` folder. If your elements are elsewhere, specify the path:
```bash
python main.py --elements-path "C:\path\to\elements"
```

### Want to customize?
- Edit `parser.py` to parse additional element properties
- Edit `flowchart_generator.py` to change colors and shapes
- Edit `exporter.py` to customize spreadsheet format

## Tips & Tricks

1. **Create audits regularly** to track your mod's evolution over time

2. **Use focused flowcharts** to understand specific recipe chains:
   ```bash
   python main.py --flowchart steel.html --focus "Steel Ingot" --depth 4
   ```

3. **Export before major refactors** so you have documentation of the old state

4. **Share flowcharts** with players to help them understand crafting chains

5. **Use CSV exports** if you want to analyze data in other tools

## What's Next?

Check out the full [README.md](README.md) for advanced features and detailed documentation.

---

**Happy Modding! 🎮**
*Made for The Great Expansion*
