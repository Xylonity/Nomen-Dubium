package dev.xylonity.nomendubium.common.entity.variant;

import java.util.Locale;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public enum ChimeraPaletteVariant {
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

    private static final ChimeraPaletteVariant[] VALUES = values();

    public static ChimeraPaletteVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public static @Nullable ChimeraPaletteVariant byName(@Nullable String name) {
        if (name == null) {
            return null;
        }

        for (final ChimeraPaletteVariant variant : VALUES) {
            if (variant.parsedName().equals(name)) {
                return variant;
            }

        }

        return null;
    }

    public static ChimeraPaletteVariant random(RandomSource random) {
        return VALUES[random.nextInt(VALUES.length)];
    }

    public ChimeraPaletteVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

    public String parsedName() {
        return name().toLowerCase(Locale.ROOT);
    }

}
