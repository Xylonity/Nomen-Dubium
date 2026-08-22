package dev.xylonity.nomendubium.common.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

/// Consumable item that regenerates over time
public final class RegeneratingChopItem extends Item {

    public static final int DURABILITY_PER_USE = 33;

    public RegeneratingChopItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        if (remainingDurability(stack) < DURABILITY_PER_USE) {
            return InteractionResult.FAIL;
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
            stack.setDamageValue(Math.min(100, stack.getDamageValue() + DURABILITY_PER_USE));
        }

        return stack;
    }

    @Override
    public void inventoryTick(ItemStack stack, @NonNull ServerLevel level, @NonNull Entity entity, EquipmentSlot slot) {
        if (stack.getDamageValue() > 0 && entity.tickCount % 20 == 0) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }

    }

    private static int remainingDurability(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamageValue();
    }

}