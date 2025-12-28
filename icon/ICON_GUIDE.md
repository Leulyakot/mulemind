# MuleMind Connector Icons

## Icon Files

The MuleMind connector includes the following icon files:

```
icon/
├── icon.svg           # Vector source (64x64)
├── icon-large.png     # Large icon (48x48) - Used in Anypoint Studio palette
└── icon-small.png     # Small icon (24x24) - Used in flow canvas
```

## Icon Design

### Concept
The MuleMind icon represents:
- **Brain** 🧠 - Intelligence, AI, cognitive processing
- **Lightning Bolt** ⚡ - Speed, efficiency, "lightning-fast"
- **Blue Circle** - Technology, trust, professionalism

### Color Scheme
- **Primary Blue**: `#0066FF` - Main brand color
- **Dark Blue**: `#003D99` - Border/outline
- **Bright Cyan**: `#00D9FF` - Lightning accent
- **White**: `#FFFFFF` - Brain detail

### Design Elements
1. **Circular Background** - Clean, modern look
2. **Stylized Brain** - Two hemispheres with convolution details
3. **Lightning Bolt** - Small accent in bottom-right indicating speed

## Usage in MuleSoft

### In Anypoint Studio

The icons will appear in:

1. **Mule Palette** - Shows `icon-large.png` (48x48)
   ```
   Extensions > MuleMind
   ```

2. **Flow Canvas** - Shows `icon-small.png` (24x24)
   ```
   When you drag operations into your flow
   ```

3. **Connector Configuration** - Shows `icon-large.png`
   ```
   In the connector configuration dialog
   ```

### XML Namespace

The icon is associated with the namespace:
```xml
xmlns:mulemind="http://www.mulesoft.org/schema/mule/mulemind"
```

## Customizing Icons

### Creating Your Own

If you want to customize the icon:

1. **Edit the SVG** (`icon.svg`)
   - Open in any vector editor (Inkscape, Figma, Adobe Illustrator)
   - Maintain 64x64 viewBox
   - Keep colors consistent with brand

2. **Generate PNGs**
   ```bash
   # Using ImageMagick
   convert icon.svg -resize 48x48 icon-large.png
   convert icon.svg -resize 24x24 icon-small.png
   
   # Or use the included Python script
   python3 create_icons.py
   ```

3. **Update connector metadata**
   - Ensure paths in `connector-metadata.json` are correct

### Icon Requirements

MuleSoft connector icons must:
- ✅ Be in PNG format (with transparency)
- ✅ Have two sizes: 48x48 (large) and 24x24 (small)
- ✅ Use clear, recognizable imagery
- ✅ Work well at small sizes
- ✅ Be visually distinct from other connectors
- ✅ Follow brand guidelines

### Best Practices

1. **Keep it Simple** - Icons should be recognizable at small sizes
2. **Use Solid Colors** - Gradients don't always scale well
3. **High Contrast** - Ensure icon is visible on light/dark backgrounds
4. **Meaningful** - Icon should relate to connector purpose
5. **Professional** - Clean, polished appearance

## Brand Consistency

The MuleMind icon should be used consistently across:

- 📦 Maven/Exchange listings
- 📚 Documentation
- 🌐 Website/landing pages
- 💬 Social media
- 📊 Presentations
- 🎓 Tutorials

## File Locations

In the connector package:
```
mulemind-connector/
├── icon/
│   ├── icon.svg           # Source vector file
│   ├── icon-large.png     # 48x48 for Studio palette
│   └── icon-small.png     # 24x24 for canvas
├── connector-metadata.json # Metadata with icon references
└── src/main/resources/
    └── icon/              # (Copy icons here for packaging)
```

## Testing Your Icons

### In Anypoint Studio

1. Build the connector: `mvn clean install`
2. Add to your Mule project
3. Open Mule Palette
4. Look for "MuleMind" category
5. Verify icon appears correctly
6. Drag operation to canvas
7. Check small icon displays properly

### Tips

- **Clear cache** if icons don't update in Studio
- **Restart Studio** after installing new connector version
- **Check logs** for icon loading errors
- **Verify file paths** in metadata

## Alternative Icon Styles

If you want to create variations:

### Minimalist Version
- Simple brain outline
- No lightning bolt
- Solid background

### Detailed Version
- More brain convolutions
- Gradient background
- Multiple accent colors

### Monochrome Version
- Single color (blue)
- Good for documentation
- High contrast

## Icon Generator Script

Included Python script for creating icons:

```python
# See /tmp/create_icons.py
# Generates both PNG sizes from parameters
# Easy to customize colors, sizes, shapes
```

## Resources

- **MuleSoft Icon Guidelines**: https://docs.mulesoft.com/
- **Design Tools**: Figma, Inkscape, Adobe Illustrator
- **Icon Optimization**: TinyPNG, SVGO
- **Vector Editing**: Inkscape (free), Adobe Illustrator

---

**Remember**: Icons are often the first thing users see. A professional, memorable icon helps your connector stand out! 🎨✨
