# Quick Start Guide

Get up and running with MCreator Mod Dashboard in 5 minutes!

## ⚡ Fast Setup

### 1. Install Dependencies
```bash
cd mcreator_dashboard
pip install -r requirements.txt
```

### 2. Launch Dashboard
```bash
python main.py
```

That's it! The dashboard will automatically find your elements folder.

## 🎯 Quick Tasks

### View All Your Mod Elements
1. Launch: `python main.py`
2. Use the search bar to find specific items
3. Click on any element to see detailed info

### Export to Spreadsheet
**From GUI:**
- File → Export to Excel

**From Command Line:**
```bash
python main.py --export-excel my_mod.xlsx
```

### Generate Changelog
**First time:**
```bash
python main.py --audit
```

**After making changes:**
```bash
python main.py --changelog CHANGELOG.md
```

### Create Flowchart
```bash
python main.py --flowchart flowchart.html
```

Then open `flowchart.html` in your browser!

## 🎨 Visual Guide

### Dashboard Layout
```
┌─────────────────────────────────────────────────────────────┐
│  [Search: ____] [Type: All Types ▼]  [🔄 Refresh]  Stats   │
├─────────────────────────────────┬───────────────────────────┤
│  Name       Type    File        │  Element Details          │
│  ─────────────────────────────  │  ═════════════════        │
│  • Block 1  block   Block1.mod  │  Name: Selected Element   │
│  • Item 1   item    Item1.mod   │  Type: block              │
│  • Recipe 1 recipe  Rec1.mod    │  Properties...            │
│  ...                            │                           │
│                                 │  Dependencies:            │
│                                 │   • Item A                │
│                                 │   • Item B                │
└─────────────────────────────────┴───────────────────────────┘
```

## 🔥 Pro Tips

### Filter by Type
Click the "Type" dropdown to see only:
- Blocks
- Items
- Recipes
- Mobs
- etc.

### Sort Columns
Click any column header to sort by that column

### Quick Search
Type in the search box - results update instantly!

### Focus Flowcharts
For complex mods, focus on specific items:
```bash
python main.py --flowchart diamond_hammer.html --focus "Diamond Hammer"
```

## 📝 Common Workflows

### Before a Release
```bash
# 1. Create audit snapshot
python main.py --audit

# 2. Make your changes in MCreator

# 3. Generate changelog
python main.py --changelog CHANGELOG.md

# 4. Export documentation
python main.py --export-excel release_v1.0.xlsx
```

### Analyzing Recipe Chains
1. Launch GUI: `python main.py`
2. Tools → Generate Flowchart
3. Enter item name when prompted (or leave empty for full graph)
4. Open generated HTML in browser
5. Drag nodes around to see connections!

## 🆘 Need Help?

### Dashboard won't start?
```bash
# Make sure you're in the right folder
cd mcreator_dashboard

# Check Python version (need 3.8+)
python --version

# Reinstall dependencies
pip install -r requirements.txt --upgrade
```

### Can't find elements?
```bash
# Specify the path manually
python main.py --elements-path "../elements"
```

### Want to see sample output?
```bash
# Generate everything
python main.py --export-excel sample.xlsx
python main.py --changelog sample_changelog.md
python main.py --flowchart sample_flowchart.html
```

## 🎓 Next Steps

Once you're comfortable with basics:
1. Read the full [README.md](README.md) for advanced features
2. Explore the `data/audits/` folder to see historical snapshots
3. Customize the flowchart colors in `flowchart_generator.py`
4. Add custom element type parsing in `parser.py`

---

**Happy Modding! 🎮**
