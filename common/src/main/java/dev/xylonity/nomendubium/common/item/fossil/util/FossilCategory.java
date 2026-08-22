package dev.xylonity.nomendubium.common.item.fossil.util;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPartVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/// The paleontology table chooses one of these when an encased fossil is inserted
public enum FossilCategory {
    BODY(ChimeraBodyVariant.values()),
    HEAD(ChimeraHeadVariant.values()),
    TAIL(ChimeraTailVariant.values()),
    BACK(ChimeraBackVariant.fossilValues()),
    // This category contains regular rewards instead of Chimera parts
    MISC();

    // Keeping this copy avoids creating a new array whenever the categories are needed
    private static final FossilCategory[] VALUES = values();
    // misc rewards are normal items
    // TODO: change rewards
    private static final Item[] MISC_ITEMS = {
            NomenDubiumItems.AMBER.get(),
            NomenDubiumItems.FOSSIL_BONE.get(),
            NomenDubiumItems.FOSSILISED_MAW.get(),
            NomenDubiumItems.PRIMITIVE_ARROW.get()
    };
    private final ChimeraPartVariant[] partVariants;

    FossilCategory(ChimeraPartVariant... partVariants) {
        this.partVariants = partVariants;
    }

    // Every category currently has the same chance of being chosen
    public static FossilCategory random(RandomSource random) {
        return VALUES[random.nextInt(VALUES.length)];
    }

    public static FossilCategory index(int index) {
        return index < 0 || index >= VALUES.length ? MISC : VALUES[index];
    }

    // The category is stored on the encased fossil (data component)
    public static FossilCategory name(String name) {
        if (name != null) {
            for (final FossilCategory category : VALUES) {
                if (category.serializedName().equals(name)) {
                    return category;
                }

            }

        }

        return null;
    }

    public String serializedName() {
        return this == MISC ? "misc" : partVariants[0].category().serializedName();
    }

    public ItemStack randomResult(RandomSource random) {
        if (this == MISC) {
            return new ItemStack(randomElement(MISC_ITEMS, random));
        }

        return NomenDubiumItems.FOSSIL.get().createStack(this.randomPart(random));
    }

    // The part name is also the value used by the fossil item model and translation
    public String randomPart(RandomSource random) {
        return this == MISC ? "misc" : randomElement(partVariants, random).fossilPart();

    }

    private static <T> T randomElement(T[] values, RandomSource random) {
        return values[random.nextInt(values.length)];
    }

}
