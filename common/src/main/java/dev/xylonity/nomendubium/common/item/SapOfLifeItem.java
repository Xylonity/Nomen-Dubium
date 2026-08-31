package dev.xylonity.nomendubium.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class SapOfLifeItem extends DescribedItem {

    private static final String TAG_REGENERATION = "nomendubium_sap_of_life_regeneration";

    public SapOfLifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull ItemStack finishUsingItem(@NonNull ItemStack stack, @NonNull Level level, @NonNull LivingEntity entity) {
        final ItemStack result = super.finishUsingItem(stack, level, entity);
        if (!level.isClientSide() && entity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, MobEffectInstance.INFINITE_DURATION));
            player.addTag(TAG_REGENERATION);
        }

        return result;
    }

    public static void stopRegenerationAfterDamage(LivingEntity entity, float damageTaken) {
        if (damageTaken <= 0 || !(entity instanceof Player) || !entity.removeTag(TAG_REGENERATION)) {
            return;
        }

        entity.removeEffect(MobEffects.REGENERATION);
    }

    @Override
    protected int descriptionLineCount() {
        return 2;
    }

}
