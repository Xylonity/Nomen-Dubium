package dev.xylonity.nomendubium.common.entity.variant;

public enum ModularDinoBodyVariant {
    HULKING,
    SHELLED,
    AVIAN,
    LANKY,
    PUFFY;

    private static final ModularDinoBodyVariant[] VALUES = values();

    public static ModularDinoBodyVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ModularDinoBodyVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }
}
