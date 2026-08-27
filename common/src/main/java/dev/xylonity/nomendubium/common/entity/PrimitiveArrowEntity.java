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
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public final class PrimitiveArrowEntity extends AbstractArrow implements ItemSupplier {

    public static final double BASE_DAMAGE = 5.2D;

    public PrimitiveArrowEntity(EntityType<? extends PrimitiveArrowEntity> type, Level level) {
        super(type, level);
        setBaseDamage(BASE_DAMAGE);
    }

    public PrimitiveArrowEntity(Level level, LivingEntity owner, ItemStack arrowStack, ItemStack weaponStack) {
        super(NomenDubiumEntities.PRIMITIVE_ARROW.get(), owner, level, arrowStack, weaponStack);
        setBaseDamage(BASE_DAMAGE);
    }

    public PrimitiveArrowEntity(Level level, double x, double y, double z, ItemStack arrowStack, ItemStack weaponStack) {
        super(NomenDubiumEntities.PRIMITIVE_ARROW.get(), x, y, z, level, arrowStack, weaponStack);
        setBaseDamage(BASE_DAMAGE);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        final Entity hitEntity = hitResult.getEntity();
        if (hitEntity instanceof LivingEntity livingEntity && hasEquippedArmor(livingEntity)) {
            // Breaks on entity hit if it has an armor equipped
            final Vec3 pos = hitResult.getLocation();
            if (!level().isClientSide()) {
                level().playSound(null, pos.x, pos.y, pos.z, SoundEvents.SHIELD_BLOCK.value(), SoundSource.NEUTRAL, 0.8F, 1.2F);
            }

            discard();

            return;
        }

        super.onHitEntity(hitResult);
    }

    private static boolean hasEquippedArmor(LivingEntity entity) {
        return hasArmor(entity, EquipmentSlot.HEAD) || hasArmor(entity, EquipmentSlot.CHEST) || hasArmor(entity, EquipmentSlot.LEGS) || hasArmor(entity, EquipmentSlot.FEET) || hasArmor(entity, EquipmentSlot.BODY);
    }

    private static boolean hasArmor(LivingEntity entity, EquipmentSlot slot) {
        final ItemStack equippedStack = entity.getItemBySlot(slot);
        if (equippedStack.isEmpty()) {
            return false;
        }

        final boolean[] providesArmor = {false};
        equippedStack.forEachModifier(slot, (attribute, modifier) -> {
            if (attribute.equals(Attributes.ARMOR) && modifier.amount() > 0) {
                providesArmor[0] = true;
            }

        });

        return providesArmor[0];
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
    protected @NonNull ItemStack getDefaultPickupItem() {
        return new ItemStack(NomenDubiumItems.PRIMITIVE_ARROW.get());
    }

    @Override
    public @NonNull ItemStack getItem() {
        final ItemStack pickupStack = getPickupItemStackOrigin();
        return pickupStack.isEmpty() ? new ItemStack(NomenDubiumItems.PRIMITIVE_ARROW.get()) : pickupStack;
    }

}
