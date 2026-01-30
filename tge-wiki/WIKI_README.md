# The Great Expansion Wiki

This is the comprehensive documentation site for **The Great Expansion** Minecraft mod, built with [Docusaurus](https://docusaurus.io/).

## 🌐 Live Site

Visit the wiki at: **https://hyper68.github.io/TheGreatExpansion/**

## 📚 What's Documented

The wiki provides extensive documentation for all aspects of The Great Expansion mod:

- **Getting Started** - Installation and first steps
- **Ores & Materials** - 15+ new ores and processing
- **Tools & Weapons** - Aqualith, Titanium, and specialized tools
- **Armor** - Complete armor sets with stats
- **Machines & Automation** - 10+ machines and power systems
- **Biomes & World Generation** - New biomes and structures
- **Blocks & Building** - Decorative and functional blocks
- **Plants & Flora** - New vegetation and trees
- **Mobs & Entities** - New creatures and NPCs
- **Recipe Guides** - Crafting chains and processing
- **Tips & Tricks** - Expert strategies and optimization

## 🚀 Local Development

### Installation

```bash
cd tge-wiki
npm install
```

### Start Development Server

```bash
npm start
```

This command starts a local development server and opens a browser window. Most changes are reflected live without having to restart the server.

### Build

```bash
npm run build
```

This command generates static content into the `build` directory that can be served using any static hosting service.

### Deployment

The site automatically deploys to GitHub Pages when changes are pushed to the main branch.

## 📝 Contributing

To add or update documentation:

1. Navigate to `/tge-wiki/docs/`
2. Edit the relevant `.md` file
3. Follow the Docusaurus markdown format
4. Test locally with `npm start`
5. Commit and push your changes

### Adding New Pages

1. Create a new `.md` file in `/tge-wiki/docs/`
2. Add frontmatter with `sidebar_position`:
   ```md
   ---
   sidebar_position: X
   ---
   # Your Page Title
   ```
3. The page will automatically appear in the sidebar

## 🛠️ Technical Details

- **Framework:** Docusaurus 3.9.2
- **Node Version:** 20.x
- **Package Manager:** npm
- **Deployment:** GitHub Actions → GitHub Pages
- **Base URL:** `/TheGreatExpansion/`

## 📖 Documentation Structure

```
tge-wiki/
├── docs/              # Documentation pages
│   ├── intro.md      # Main landing page
│   ├── getting-started.md
│   ├── ores-and-materials.md
│   ├── tools-and-weapons.md
│   ├── armor.md
│   ├── machines.md
│   ├── biomes.md
│   ├── blocks.md
│   ├── plants.md
│   ├── mobs.md
│   ├── recipes.md
│   └── guides.md
├── src/               # React components and pages
├── static/            # Static assets (images, etc.)
└── docusaurus.config.ts  # Site configuration
```

## 🎨 Customization

### Theme

The site uses a dark theme that respects system preferences. Customize in `docusaurus.config.ts`:

```typescript
colorMode: {
  respectPrefersColorScheme: true,
}
```

### Navigation

Edit navigation items in `docusaurus.config.ts` under `themeConfig.navbar`.

### Footer

Customize footer links in `docusaurus.config.ts` under `themeConfig.footer`.

## 🔗 Useful Links

- [Docusaurus Documentation](https://docusaurus.io/docs)
- [Markdown Features](https://docusaurus.io/docs/markdown-features)
- [The Great Expansion Repository](https://github.com/hyper68/TheGreatExpansion)
- [Hyper Mods Homepage](https://hyper68.github.io/TheGreatExpansion/)

## 📄 License

Documentation content is part of The Great Expansion mod project.

---

Built with ❤️ by Hyper Mods using Docusaurus
