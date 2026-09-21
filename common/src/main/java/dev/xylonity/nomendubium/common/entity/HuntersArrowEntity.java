package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public final class HuntersArrowEntity extends AbstractArrow implements ItemSupplier {

    public static final double BASE_DAMAGE = 5.2D;

    public HuntersArrowEntity(EntityType<? extends HuntersArrowEntity> type, Level level) {
        super(type, level);
        setBaseDamage(BASE_DAMAGE);
    }

    public HuntersArrowEntity(Level level, LivingEntity owner, ItemStack arrowStack, ItemStack weaponStack) {
        super(NomenDubiumEntities.HUNTERS_ARROW.get(), owner, level);
        setBaseDamage(BASE_DAMAGE);
    }

    public HuntersArrowEntity(Level level, double x, double y, double z, ItemStack arrowStack, ItemStack weaponStack) {
        super(NomenDubiumEntities.HUNTERS_ARROW.get(), x, y, z, level);
        setBaseDamage(BASE_DAMAGE);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        final Entity hitEntity = hitResult.getEntity();
        // Breaks on entity hit if it has an armor equipped
        if (hitEntity instanceof LivingEntity livingEntity && hasEquippedArmor(livingEntity)) {
            final Vec3 pos = hitResult.getLocation();
            if (!level().isClientSide()) {
                level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.SHIELD_BLOCK, SoundSource.NEUTRAL, 0.8F, 1.2F);
            }

            discard();

            return;
        }

        super.onHitEntity(hitResult);
    }

    private static boolean hasEquippedArmor(LivingEntity entity) {
        return hasArmor(entity, EquipmentSlot.HEAD) || hasArmor(entity, EquipmentSlot.CHEST) || hasArmor(entity, EquipmentSlot.LEGS) || hasArmor(entity, EquipmentSlot.FEET);
    }

    private static boolean hasArmor(LivingEntity entity, EquipmentSlot slot) {
        final ItemStack equippedStack = entity.getItemBySlot(slot);
        if (equippedStack.isEmpty()) {
            return false;
        }

        return equippedStack.getAttributeModifiers(slot).get(Attributes.ARMOR).stream()
            .anyMatch(modifier -> modifier.getAmount() > 0);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
        final Vec3 movement = getDeltaMovement();
        if (movement.lengthSqr() > 1.0E-6D) {
            setDeltaMovement(movement.normalize().scale(velocity));
        }

    }

    @Override
    public void setCritArrow(boolean critical) {
        super.setCritArrow(false);
    }

    @Override
    protected @NonNull ItemStack getPickupItem() {
        return new ItemStack(NomenDubiumItems.HUNTERS_ARROW.get());
    }

    @Override
    public @NonNull ItemStack getItem() {
        return new ItemStack(NomenDubiumItems.HUNTERS_ARROW.get());
    }

}
