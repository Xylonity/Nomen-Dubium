package dev.xylonity.nomendubium.common.entity.variant;

public enum ChimeraTailVariant implements ChimeraPartVariant {
    SPIKED,
    STUBBY,
    CLUBBED,
    FAN,
    SPEARED;

    private static final ChimeraTailVariant[] VALUES = values();

    public static ChimeraTailVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ChimeraTailVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

    @Override
    public ChimeraPartCategory category() {
        return ChimeraPartCategory.TAIL;
    }

}
