package dev.xylonity.nomendubium.common.entity.variant;

public enum ChimeraHeadVariant implements ChimeraPartVariant {
    CRUNCHING,
    SHIELDED,
    SNARLED,
    BEAKED,
    SNORTING;

    private static final ChimeraHeadVariant[] VALUES = values();

    public static ChimeraHeadVariant index(int index) {
        return VALUES[Math.floorMod(index, VALUES.length)];
    }

    public ChimeraHeadVariant next() {
        return index(ordinal() + 1);
    }

    public int index() {
        return ordinal();
    }

    @Override
    public ChimeraPartCategory category() {
        return ChimeraPartCategory.HEAD;
    }

}