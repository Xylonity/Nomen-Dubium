package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

/// Consumable item that regenerates over time
public final class RegeneratingChopItem extends DescribedItem {

    public RegeneratingChopItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResultHolder<ItemStack> use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (remainingDurability(stack) < durabilityPerUse(stack)) {
            return InteractionResultHolder.fail(stack);
        }

        return super.use(level, player, hand);
    }

    @Override
    public @NonNull ItemStack finishUsingItem(ItemStack stack, @NonNull Level level, @NonNull LivingEntity livingEntity) {
        final int originalCount = stack.getCount();

        super.finishUsingItem(stack, level, livingEntity);

        // Vanilla consumes the item once used, so here it is restored
        stack.setCount(Math.max(1, originalCount));
        if (!level.isClientSide()) {
            stack.setDamageValue(Math.min(stack.getMaxDamage(), stack.getDamageValue() + durabilityPerUse(stack)));
        }

        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, @NonNull Level level, @NonNull Entity entity, int slot, boolean selected) {
        if (stack.getDamageValue() > 0 && entity.tickCount % NomenDubiumConfig.REGENERATING_CHOP_REGENERATION_INTERVAL == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }

    }

    private static int remainingDurability(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    /// Durability consumed per bite, so a fully regenerated chop can be eaten the configured amount of times
    private static int durabilityPerUse(ItemStack stack) {
        return Math.max(1, stack.getMaxDamage() / NomenDubiumConfig.REGENERATING_CHOP_USES);
    }

    @Override
    protected void appendDescription(ItemStack stack, Consumer<Component> tooltip) {
        tooltip.accept(Component.translatable(this.getDescriptionId() + ".tooltip.1", NomenDubiumConfig.REGENERATING_CHOP_USES).withStyle(ChatFormatting.DARK_GRAY));
        tooltip.accept(Component.translatable(this.getDescriptionId() + ".tooltip.2").withStyle(ChatFormatting.DARK_GRAY));
    }

}
