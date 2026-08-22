package dev.xylonity.nomendubium.common.entity.variant;

import java.util.Arrays;

public enum ChimeraBackVariant implements ChimeraPartVariant {
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

    public static ChimeraBackVariant[] fossilValues() {
        return Arrays.stream(VALUES).filter(variant -> variant != NONE).toArray(ChimeraBackVariant[]::new);
    }

    public ChimeraBackVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

    @Override
    public ChimeraPartCategory category() {
        return ChimeraPartCategory.BACK;
    }

}