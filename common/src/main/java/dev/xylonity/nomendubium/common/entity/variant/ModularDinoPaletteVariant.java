package dev.xylonity.nomendubium.common.entity.variant;

import java.util.Locale;

public enum ModularDinoPaletteVariant {
    NORMAL,
    JUNGLE,
    GRASSLAND,
    DESERT,
    OCEANIC,
    SNIFFISH,
    TUNDRA,
    TROPICAL,
    DRYLANDS,
    PERMAFROST,
    VOLCANIC,
    SUNSET,
    ABYSSAL,
    AMBER,
    COPAL,
    JASPER,
    QUARTZ,
    AGATE,
    OPALINE,
    OBSIDIAN;

    private static final ModularDinoPaletteVariant[] VALUES = values();

    public static ModularDinoPaletteVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ModularDinoPaletteVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

    public String parsedName() {
        return name().toLowerCase(Locale.ROOT);
    }

}
