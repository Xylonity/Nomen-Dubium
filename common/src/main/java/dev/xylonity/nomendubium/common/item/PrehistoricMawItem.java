package dev.xylonity.nomendubium.common.item;

import dev.xylonity.knightlib.api.item.KnightLibRenderedItem;
import dev.xylonity.nomendubium.client.item.MawItemRenderer;
import dev.xylonity.nomendubium.common.entity.PrehistoricMawProjectileEntity;
import dev.xylonity.nomendubium.common.item.util.ItemStackData;
import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public final class PrehistoricMawItem extends DescribedItem implements KnightLibRenderedItem {

    private static final long DECAY = 20 * 60;

    public PrehistoricMawItem(Properties properties) {
        super(properties);
    }

    @Override
    public Supplier<Object> rendererFactory() {
        return () -> new MawItemRenderer("prehistoric_maw");
    }

    @Override
    public @NonNull InteractionResultHolder<ItemStack> use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    @Override
    public @NonNull UseAnim getUseAnimation(@NonNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NonNull ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(@NonNull ItemStack stack, @NonNull Level level, @NonNull LivingEntity livingEntity, int timeLeft) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        final int charge = getUseDuration(stack, livingEntity) - timeLeft;
        if (charge < 12) {
            return;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        applyElapsedDecay(stack, serverLevel.getGameTime());

        // Throws the maw
        final boolean returns = !player.getAbilities().instabuild;
        final float launchDamage = getAttackDamage(stack);

        setAttackDamage(stack, launchDamage - 0.25f);

        final ItemStack maw = stack.copyWithCount(1);
        if (returns) {
            stack.shrink(1);
        }

        final PrehistoricMawProjectileEntity projectile = new PrehistoricMawProjectileEntity(serverLevel, player, maw, returns, launchDamage);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.2F, 0);
        serverLevel.addFreshEntity(projectile);

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1F, 1.1F);
        player.awardStat(Stats.ITEM_USED.get(this));

    }

    @Override
    public boolean hurtEnemy(ItemStack stack, @NonNull LivingEntity target, @NonNull LivingEntity attacker) {
        final boolean result = super.hurtEnemy(stack, target, attacker);
        if (!attacker.level().isClientSide() && target.isDeadOrDying()) {
            recordKill(stack);
        }

        return result;
    }

    @Override
    public void inventoryTick(ItemStack stack, @NonNull Level level, @NonNull Entity entity, int slot, boolean selected) {
        if (!level.isClientSide()) applyElapsedDecay(stack, level.getGameTime());
    }

    public static float getAttackDamage(ItemStack stack) {
        final var tag = ItemStackData.get(stack);
        return tag.contains("PrehistoricMawDamage") ? Mth.clamp(tag.getFloat("PrehistoricMawDamage"), NomenDubiumConfig.PREHISTORIC_MAW_BASE_DAMAGE, NomenDubiumConfig.PREHISTORIC_MAW_MAX_DAMAGE) : NomenDubiumConfig.PREHISTORIC_MAW_BASE_DAMAGE;
    }

    public static void recordKill(ItemStack stack) {
        setAttackDamage(stack, getAttackDamage(stack) + 1);
    }

    private static void setAttackDamage(ItemStack stack, float damage) {
        final float clampedDamage = Mth.clamp(damage, NomenDubiumConfig.PREHISTORIC_MAW_BASE_DAMAGE, NomenDubiumConfig.PREHISTORIC_MAW_MAX_DAMAGE);
        ItemStackData.update(stack, tag -> tag.putFloat("PrehistoricMawDamage", clampedDamage));
        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, FossilisedMawItem.createAttributes(clampedDamage));

    }

    // Damage decreases per minute + when throwing the maw itself
    private static void applyElapsedDecay(ItemStack stack, long gameTime) {
        final var tag = ItemStackData.get(stack);
        final long lastDecayTick = tag.contains("PrehistoricMawLastDecayTick") ? tag.getLong("PrehistoricMawLastDecayTick") : -1L;
        if (lastDecayTick < 0 || gameTime < lastDecayTick) {
            ItemStackData.update(stack, data -> data.putLong("PrehistoricMawLastDecayTick", gameTime));
            return;
        }

        final long elapsedIntervals = (gameTime - lastDecayTick) / DECAY;
        if (elapsedIntervals == 0) {
            return;
        }

        setAttackDamage(stack, getAttackDamage(stack) - elapsedIntervals * 0.5f);
        ItemStackData.update(stack, data -> data.putLong("PrehistoricMawLastDecayTick", lastDecayTick + elapsedIntervals * DECAY));
    }

    @Override
    protected int descriptionLineCount() {
        return 2;
    }

}
