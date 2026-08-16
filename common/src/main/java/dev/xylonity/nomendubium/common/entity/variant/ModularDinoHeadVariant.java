package dev.xylonity.nomendubium.common.entity.variant;

public enum ModularDinoHeadVariant {
    CRUNCHING,
    SHIELDED,
    SNARLED,
    BEAKED,
    SNORTING;

    private static final ModularDinoHeadVariant[] VALUES = values();

    public static ModularDinoHeadVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ModularDinoHeadVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

}