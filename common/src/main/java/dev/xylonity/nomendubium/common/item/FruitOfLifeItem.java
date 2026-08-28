package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.item.fossil.EncasedFossilItem;
import dev.xylonity.nomendubium.registry.NomenDubiumDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/// Mostly a copy of {@link EncasedFossilItem}
public final class FruitOfLifeItem extends Item {

    public FruitOfLifeItem(Properties properties) {
        super(properties);
    }

    public ItemStack createStack(@Nullable ChimeraPaletteVariant palette) {
        final ItemStack stack = new ItemStack(this);
        if (palette != null) {
            stack.set(NomenDubiumDataComponents.CHIMERA_PALETTE.get(), palette.parsedName());
        }

        return stack;
    }

    public static @Nullable ChimeraPaletteVariant getPalette(ItemStack stack) {
        return ChimeraPaletteVariant.byName(stack.get(NomenDubiumDataComponents.CHIMERA_PALETTE.get()));
    }

    @Override
    public @NonNull Component getName(@NonNull ItemStack stack) {
        final ChimeraPaletteVariant palette = getPalette(stack);
        final String paletteName = palette == null ? "random" : palette.parsedName();
        return Component.translatable(
            this.getDescriptionId() + ".variant",
            Component.translatable("palette.nomendubium." + paletteName)
        );

    }

}
