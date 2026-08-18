package dev.xylonity.nomendubium.common.item.fossil;

import dev.xylonity.nomendubium.registry.NomenDubiumDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public final class FossilItem extends Item {

    public FossilItem(Properties properties) {
        super(properties);
    }

    public ItemStack createStack(String part) {
        final ItemStack stack = new ItemStack(this);
        stack.set(NomenDubiumDataComponents.FOSSIL_PART.get(), part);
        return stack;
    }

    public static String getPart(ItemStack stack) {
        return stack.get(NomenDubiumDataComponents.FOSSIL_PART.get());
    }

    @Override
    public @NonNull Component getName(ItemStack stack) {
        final String part = getPart(stack);
        return part == null ? super.getName(stack) : Component.translatableWithFallback(this.getDescriptionId() + "." + part, super.getName(stack).getString());
    }

}
