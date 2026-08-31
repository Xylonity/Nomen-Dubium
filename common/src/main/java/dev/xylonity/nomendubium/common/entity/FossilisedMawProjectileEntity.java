package dev.xylonity.nomendubium.common.entity;

import java.util.HashSet;
import java.util.Set;
import dev.xylonity.nomendubium.common.item.FossilisedMawItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public final class FossilisedMawProjectileEntity extends AbstractArrow implements ItemSupplier {

    private static final EntityDataAccessor<Integer> IMPACT_FACE = SynchedEntityData.defineId(FossilisedMawProjectileEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> IMPACT_ROLL = SynchedEntityData.defineId(FossilisedMawProjectileEntity.class, EntityDataSerializers.FLOAT);

    public static final float SPIN_DEGREES = 36.0F;
    public static final double EXTRA_DEPTH = 0.04D;

    private final Set<Integer> hitEntityIds = new HashSet<>();

    public FossilisedMawProjectileEntity(EntityType<? extends FossilisedMawProjectileEntity> type, Level level) {
        super(type, level);
    }

    public FossilisedMawProjectileEntity(ServerLevel level, LivingEntity owner, ItemStack thrownItem, boolean creativePickup) {
        super(NomenDubiumEntities.FOSSILISED_MAW.get(), owner, level, thrownItem.copyWithCount(1), null);
        pickup = creativePickup ? Pickup.CREATIVE_ONLY : Pickup.ALLOWED;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(IMPACT_FACE, -1);
        entityData.define(IMPACT_ROLL, 0F);
    }

    @Override
    public void tick() {
        super.tick();

        if (isEmbedded() && !isInGround() && !level().isClientSide()) {
            setImpactDirection(null);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        final Vec3 movement = getDeltaMovement();
        super.onHitBlock(hitResult);

        final Direction impactDirection = hitResult.getDirection();
        final Vec3 surfaceNormal = impactDirection.getUnitVec3();
        setImpactDirection(impactDirection);
        setImpactRoll(computeImpactRoll(impactDirection, movement));

        // The renderer compensates from this depth and leaves the top left part of the model slightly buried
        setPos(hitResult.getLocation().subtract(surfaceNormal.scale(EXTRA_DEPTH)));
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        final Entity hitEntity = hitResult.getEntity();
        if (!hitEntityIds.add(hitEntity.getId())) {
            return;
        }

        // Hurts and ignores entities on hit
        if (level() instanceof ServerLevel serverLevel) {
            hitEntity.hurtServer(serverLevel, damageSources().thrown(this, getOwner()), FossilisedMawItem.ATTACK_DAMAGE);
        }

    }

    @Override
    protected boolean canHitEntity(@NonNull Entity entity) {
        return entity != getOwner() && !hitEntityIds.contains(entity.getId()) && super.canHitEntity(entity);
    }

    @Override
    public void onSyncedDataUpdated(@NonNull EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (IMPACT_FACE.equals(dataAccessor)) {
            setBoundingBox(makeBoundingBox(position()));
        }

    }

    @Override
    protected @NonNull AABB makeBoundingBox(@NonNull Vec3 position) {
        if (!isEmbedded()) {
            return super.makeBoundingBox(position);
        }

        final Vec3 normal = getImpactDirection().getUnitVec3();
        final double offset = EXTRA_DEPTH + 0.38D;
        final double centerX = position.x + normal.x * offset;
        final double centerY = position.y + normal.y * offset;
        final double centerZ = position.z + normal.z * offset;
        final double halfSize = 0.85D * 0.5D;

        return new AABB(
                centerX - halfSize, centerY - halfSize, centerZ - halfSize,
                centerX + halfSize, centerY + halfSize, centerZ + halfSize
        );

    }

    @Override
    protected @NonNull ItemStack getDefaultPickupItem() {
        return new ItemStack(NomenDubiumItems.FOSSILISED_MAW.get());
    }

    @Override
    public @NonNull ItemStack getItem() {
        final ItemStack pickupStack = getPickupItemStackOrigin();
        return pickupStack.isEmpty() ? new ItemStack(NomenDubiumItems.FOSSILISED_MAW.get()) : pickupStack;
    }

    public boolean isEmbedded() {
        return entityData.get(IMPACT_FACE) != -1;
    }

    public Direction getImpactDirection() {
        final int face = entityData.get(IMPACT_FACE);
        return face == -1 ? Direction.UP : Direction.from3DDataValue(face);
    }

    public float getImpactRoll() {
        return entityData.get(IMPACT_ROLL);
    }

    private void setImpactDirection(Direction direction) {
        entityData.set(IMPACT_FACE, direction == null ? -1 : direction.get3DDataValue());
    }

    private void setImpactRoll(float roll) {
        entityData.set(IMPACT_ROLL, roll);
    }

    private float getImpactRollVariation() {
        final int seed = getUUID().hashCode() & 0xFFFF;
        final float value = seed / 65535F;
        return (value * 2F - 1F) * 7f;
    }

    private float computeImpactRoll(Direction impactDirection, Vec3 incomingMovement) {
        final float variation = getImpactRollVariation();
        if (!impactDirection.getAxis().isVertical()) {
            return Mth.wrapDegrees(tickCount * SPIN_DEGREES + variation);
        }

        final Vec3 outward = impactDirection.getUnitVec3();
        final Vec3 movement = incomingMovement.subtract(outward.scale(incomingMovement.dot(outward)));
        if (movement.lengthSqr() < 1.0E-6D) {
            return variation;
        }

        // Kinda randomized roll based on the direction the player is looking (when shooting the maw projectile)
        final Vec3 base = new Vec3(0, 0, 1);
        final Vec3 normalizedMovement = movement.normalize();
        final double sin = outward.dot(base.cross(normalizedMovement));
        final double cos = base.dot(normalizedMovement);
        final float heading = (float) Math.toDegrees(Math.atan2(sin, cos));
        return Mth.wrapDegrees(heading + variation);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        final int face = input.getIntOr("impact_face", -1);
        entityData.set(IMPACT_FACE, face >= 0 && face < Direction.values().length ? face : -1);
        entityData.set(IMPACT_ROLL, input.getFloatOr("impact_roll", 0.0F));
        setBoundingBox(makeBoundingBox(position()));
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (isEmbedded()) {
            output.putInt("impact_face", entityData.get(IMPACT_FACE));
            output.putFloat("impact_roll", getImpactRoll());
        }

    }

}
