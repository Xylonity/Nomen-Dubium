package dev.xylonity.nomendubium.common.entity.variant;

public enum ChimeraBackVariant {
    NONE,
    BONEY_PLATES,
    DORSAL_SCALES,
    SPIKES,
    SPINE_SAIL,
    THORNS;

    private static final ChimeraBackVariant[] VALUES = values();

    public static ChimeraBackVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ChimeraBackVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

}