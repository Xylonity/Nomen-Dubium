package dev.xylonity.nomendubium.common.entity.variant;

public enum ChimeraBodyVariant {
    HULKING,
    SHELLED,
    AVIAN,
    LANKY,
    PUFFY;

    private static final ChimeraBodyVariant[] VALUES = values();

    public static ChimeraBodyVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ChimeraBodyVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }
}
