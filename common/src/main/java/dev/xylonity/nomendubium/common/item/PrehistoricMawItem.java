package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.PrehistoricMawProjectileEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public final class PrehistoricMawItem extends Item {

    public PrehistoricMawItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public @NonNull ItemUseAnimation getUseAnimation(@NonNull ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public int getUseDuration(@NonNull ItemStack stack, @NonNull LivingEntity entity) {
        return 72000;
    }

    @Override
    public boolean releaseUsing(@NonNull ItemStack stack, @NonNull Level level, @NonNull LivingEntity livingEntity, int timeLeft) {
        if (!(livingEntity instanceof Player player)) {
            return false;
        }

        final int charge = getUseDuration(stack, livingEntity) - timeLeft;
        if (charge < 12) {
            return false;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return true;
        }

        // Throws the maw
        final boolean returns = !player.hasInfiniteMaterials();
        final ItemStack maw = stack.copyWithCount(1);
        if (returns) {
            stack.shrink(1);
        }

        Projectile.spawnProjectileFromRotation((projectileLevel, owner, projectileStack) ->
                new PrehistoricMawProjectileEntity(projectileLevel, owner, projectileStack, returns), serverLevel, maw, player, 0, 1.2f, 0
        );

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1F, 1.1F);
        player.awardStat(Stats.ITEM_USED.get(this));

        return true;
    }

}