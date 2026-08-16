package dev.xylonity.nomendubium.common.entity.variant;

public enum ModularDinoTailVariant {
    SPIKED,
    STUBBY,
    CLUBBED,
    FAN,
    SPEARED;

    private static final ModularDinoTailVariant[] VALUES = values();

    public static ModularDinoTailVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ModularDinoTailVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

}
