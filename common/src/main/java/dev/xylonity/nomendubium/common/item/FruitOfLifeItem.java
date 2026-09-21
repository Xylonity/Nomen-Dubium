package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.item.util.ItemStackData;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

/// Mostly a copy of {@link dev.xylonity.nomendubium.common.item.fossil.FossilItem}
public final class FruitOfLifeItem extends DescribedItem {

    public FruitOfLifeItem(Properties properties) {
        super(properties);
    }

    public ItemStack createStack(@Nullable ChimeraPaletteVariant palette) {
        final ItemStack stack = new ItemStack(this);
        if (palette != null) {
            ItemStackData.update(stack, tag -> tag.putString("ChimeraPalette", palette.parsedName()));
        }

        return stack;
    }

    public static @Nullable ChimeraPaletteVariant getPalette(ItemStack stack) {
        final CompoundTag tag = ItemStackData.get(stack);
        return ChimeraPaletteVariant.byName(tag.contains("ChimeraPalette") ? tag.getString("ChimeraPalette") : null);
    }

    @Override
    protected void appendDescription(ItemStack stack, Consumer<Component> tooltip) {
        final ChimeraPaletteVariant palette = getPalette(stack);
        final String paletteName = palette == null ? "random" : palette.parsedName();
        tooltip.accept(Component.translatable(
            this.getDescriptionId() + ".tooltip.palette",
            Component.translatable("palette.nomendubium." + paletteName)
        ).withStyle(ChatFormatting.GRAY));
        super.appendDescription(stack, tooltip);
    }

}
