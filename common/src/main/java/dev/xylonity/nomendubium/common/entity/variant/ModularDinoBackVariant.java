package dev.xylonity.nomendubium.common.entity.variant;

public enum ModularDinoBackVariant {
    NONE,
    BONEY_PLATES,
    DORSAL_SCALES,
    SPIKES,
    SPINE_SAIL,
    THORNS;

    private static final ModularDinoBackVariant[] VALUES = values();

    public static ModularDinoBackVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ModularDinoBackVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

}