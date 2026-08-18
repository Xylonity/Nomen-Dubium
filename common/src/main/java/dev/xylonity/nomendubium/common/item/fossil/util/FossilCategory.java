package dev.xylonity.nomendubium.common.item.fossil.util;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.Locale;

// The geologist table chooses one of these when an encased fossil is inserted
public enum FossilCategory {
    BODY,
    HEAD,
    TAIL,
    BACK,
    // This category contains regular rewards instead of Chimera parts
    MISC;

    // Keeping this copy avoids creating a new array whenever the categories are needed
    private static final FossilCategory[] VALUES = values();
    // misc rewards are normal items
    // TODO: change rewards
    private static final Item[] MISC_ITEMS = {Items.BONE, Items.WHITE_BED};

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
        return this.name().toLowerCase(Locale.ROOT);
    }

    public ItemStack randomResult(RandomSource random) {
        if (this == MISC) {
            return new ItemStack(randomElement(MISC_ITEMS, random));
        }

        return NomenDubiumItems.FOSSIL.get().createStack(this.randomPart(random));
    }

    // The part name is also the value used by the fossil item model and translation
    public String randomPart(RandomSource random) {
        return switch (this) {
            case BODY -> partName(randomElement(ChimeraBodyVariant.values(), random).name(), "body");
            case HEAD -> partName(randomElement(ChimeraHeadVariant.values(), random).name(), "head");
            case TAIL -> partName(randomElement(ChimeraTailVariant.values(), random).name(), "tail");
            case BACK -> {
                // None part of the chimera back is not computed
                final ChimeraBackVariant[] variants = Arrays.stream(ChimeraBackVariant.values()).filter(variant -> variant != ChimeraBackVariant.NONE).toArray(ChimeraBackVariant[]::new);
                yield partName(randomElement(variants, random).name(), "back");
            }
            // Stub
            case MISC -> "misc";
        };

    }

    private static <T> T randomElement(T[] values, RandomSource random) {
        return values[random.nextInt(values.length)];
    }

    private static String partName(String variant, String category) {
        return variant.toLowerCase(Locale.ROOT) + "_" + category;
    }

}