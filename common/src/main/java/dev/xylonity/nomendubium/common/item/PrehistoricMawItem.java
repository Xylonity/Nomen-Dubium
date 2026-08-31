package dev.xylonity.nomendubium.common.item;

import dev.xylonity.nomendubium.common.entity.PrehistoricMawProjectileEntity;
import dev.xylonity.nomendubium.registry.NomenDubiumDataComponents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public final class PrehistoricMawItem extends DescribedItem {

    public static final float BASE_ATTACK_DAMAGE = 9;
    public static final float MAX_ATTACK_DAMAGE = 14;

    private static final long DECAY = 20 * 60;

    public PrehistoricMawItem(Properties properties) {
        super(properties.attributes(FossilisedMawItem.createAttributes(BASE_ATTACK_DAMAGE)));
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

        applyElapsedDecay(stack, serverLevel.getGameTime());

        // Throws the maw
        final boolean returns = !player.hasInfiniteMaterials();
        final float launchDamage = getAttackDamage(stack);
        setAttackDamage(stack, launchDamage - 0.25f);
        final ItemStack maw = stack.copyWithCount(1);
        if (returns) {
            stack.shrink(1);
        }

        Projectile.spawnProjectileFromRotation((projectileLevel, owner, projectileStack) ->
                new PrehistoricMawProjectileEntity(projectileLevel, owner, projectileStack, returns, launchDamage), serverLevel, maw, player, 0, 1.2f, 0
        );

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1F, 1.1F);
        player.awardStat(Stats.ITEM_USED.get(this));

        return true;
    }

    @Override
    public void hurtEnemy(ItemStack stack, @NonNull LivingEntity target, @NonNull LivingEntity attacker) {
        super.hurtEnemy(stack, target, attacker);
        if (!attacker.level().isClientSide() && target.isDeadOrDying()) {
            recordKill(stack);
        }

    }

    @Override
    public void inventoryTick(ItemStack stack, @NonNull ServerLevel level, @NonNull Entity entity, EquipmentSlot slot) {
        applyElapsedDecay(stack, level.getGameTime());
    }

    public static float getAttackDamage(ItemStack stack) {
        final Float storedDamage = stack.get(NomenDubiumDataComponents.PREHISTORIC_MAW_DAMAGE.get());
        return storedDamage == null ? BASE_ATTACK_DAMAGE : Math.clamp(storedDamage, BASE_ATTACK_DAMAGE, MAX_ATTACK_DAMAGE);
    }

    public static void recordKill(ItemStack stack) {
        setAttackDamage(stack, getAttackDamage(stack) + 1);
    }

    private static void setAttackDamage(ItemStack stack, float damage) {
        final float clampedDamage = Math.clamp(damage, BASE_ATTACK_DAMAGE, MAX_ATTACK_DAMAGE);
        stack.set(NomenDubiumDataComponents.PREHISTORIC_MAW_DAMAGE.get(), clampedDamage);
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, FossilisedMawItem.createAttributes(clampedDamage));
    }

    // Damage decreases per minute + when throwing the maw itself
    private static void applyElapsedDecay(ItemStack stack, long gameTime) {
        final Long lastDecayTick = stack.get(NomenDubiumDataComponents.PREHISTORIC_MAW_LAST_DECAY_TICK.get());
        if (lastDecayTick == null || gameTime < lastDecayTick) {
            stack.set(NomenDubiumDataComponents.PREHISTORIC_MAW_LAST_DECAY_TICK.get(), gameTime);
            return;
        }

        final long elapsedIntervals = (gameTime - lastDecayTick) / DECAY;
        if (elapsedIntervals == 0) {
            return;
        }

        setAttackDamage(stack, getAttackDamage(stack) - elapsedIntervals * 0.5f);
        stack.set(NomenDubiumDataComponents.PREHISTORIC_MAW_LAST_DECAY_TICK.get(), lastDecayTick + elapsedIntervals * DECAY);
    }

    @Override
    protected int descriptionLineCount() {
        return 2;
    }

}
