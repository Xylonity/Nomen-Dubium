package dev.xylonity.nomendubium.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.xylonity.knightlib.api.item.KnightLibRenderedItem;
import dev.xylonity.nomendubium.client.item.MawItemRenderer;
import dev.xylonity.nomendubium.common.entity.FossilisedMawProjectileEntity;
import dev.xylonity.nomendubium.config.NomenDubiumConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Supplier;

public final class FossilisedMawItem extends DescribedItem implements KnightLibRenderedItem {

    public FossilisedMawItem(Properties properties) {
        super(properties);
    }

    @Override
    public Supplier<Object> rendererFactory() {
        return () -> new MawItemRenderer("fossilised_maw");
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
    public int getUseDuration(@NonNull ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(@NonNull ItemStack stack, @NonNull Level level, @NonNull LivingEntity livingEntity, int timeLeft) {
        if (!(livingEntity instanceof Player player)) {
            return;
        }

        final int charge = getUseDuration(stack) - timeLeft;
        if (charge < 12) {
            return;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Throws the maw
        final boolean pickup = player.getAbilities().instabuild;
        final ItemStack maw = stack.copyWithCount(1);
        if (!pickup) {
            stack.shrink(1);
        }

        final FossilisedMawProjectileEntity projectile = new FossilisedMawProjectileEntity(serverLevel, player, maw, pickup);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.2F, 0);
        serverLevel.addFreshEntity(projectile);

        serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1, 1);
        player.awardStat(Stats.ITEM_USED.get(this));

    }

    public static Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> createAttributes(float totalAttackDamage) {
        return ImmutableMultimap.<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier>builder()
            .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                totalAttackDamage - 1.0F, AttributeModifier.Operation.ADDITION))
            .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier",
                -3.2F, AttributeModifier.Operation.ADDITION))
            .build();
    }

    @Override
    public Multimap<net.minecraft.world.entity.ai.attributes.Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? createAttributes(NomenDubiumConfig.FOSSILISED_MAW_DAMAGE) : super.getDefaultAttributeModifiers(slot);

    }

    @Override
    protected int descriptionLineCount() {
        return 2;
    }

}
