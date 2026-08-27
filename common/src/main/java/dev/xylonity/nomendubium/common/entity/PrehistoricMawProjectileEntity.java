package dev.xylonity.nomendubium.common.entity;

import java.util.HashSet;
import java.util.Set;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public final class PrehistoricMawProjectileEntity extends ThrowableProjectile implements ItemSupplier {

    private static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(PrehistoricMawProjectileEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int LAUNCH_TICKS = 24;
    private static final double LAUNCH_DISTANCE = 24.0D;

    private static final double STEERING = 0.14D;

    private boolean returnsToInventory = true;
    private ItemStack thrownItem = ItemStack.EMPTY;

    private final Set<Integer> hitEntityIds = new HashSet<>();

    private float angle;
    private float bankAngleOld;

    public PrehistoricMawProjectileEntity(EntityType<? extends PrehistoricMawProjectileEntity> type, Level level) {
        super(type, level);
    }

    public PrehistoricMawProjectileEntity(ServerLevel level, LivingEntity owner, ItemStack thrownItem, boolean returnsToInventory) {
        this(NomenDubiumEntities.PREHISTORIC_MAW.get(), level);
        setOwner(owner);
        setPos(owner.getX(), owner.getEyePosition().y - 0.1D, owner.getZ());
        this.thrownItem = thrownItem.copyWithCount(1);
        this.returnsToInventory = returnsToInventory;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        entityData.define(RETURNING, false);
    }

    @Override
    public void tick() {
        bankAngleOld = angle;

        final Entity owner = getOwner();
        // Aim just below eye level
        final Vec3 returnTarget = owner != null && owner.isAlive() ? owner.getEyePosition().add(0, -0.25, 0) : null;

        if (!isReturning()) {
            // Time and distance are independent things that trigger the return
            if (tickCount >= LAUNCH_TICKS ||
                owner != null && distanceToSqr(owner) >= LAUNCH_DISTANCE * LAUNCH_DISTANCE) {
                startReturning();
            }
            else if (returnTarget != null) {
                // Extra easing that slightly turns the projectile into the turn direction
                predictReturn(owner, returnTarget);
            }

        }

        if (isReturning() && returnTarget != null) {
            if (position().distanceToSqr(returnTarget) <= Math.pow(1.4, 2)) {
                if (level() instanceof ServerLevel serverLevel) {
                    returnToOwner(serverLevel, owner);
                }

                return;
            }

            final double distance = position().distanceTo(returnTarget);
            steerTowards(returnTarget, getReturnSteering(distance), 1.24);
        }

        super.tick();

        final float yaw = Mth.wrapDegrees(getYRot() - yRotO);
        final float targetB = Mth.clamp(-yaw * 6f, -28, 28);
        angle = Mth.lerp(0.18f, angle, targetB);

        // Drops the item after a given time
        if (!level().isClientSide() && tickCount >= 240 && level() instanceof ServerLevel serverLevel) {
            dropReturnedItem(serverLevel);
            discard();
        }

    }

    private void predictReturn(Entity owner, Vec3 target) {
        // Anticipation starts during the last eight ticks or blocks of the outbound phase
        final double progress = Mth.clamp((tickCount - (LAUNCH_TICKS - 8)) / (double) 8, 0, 1);
        final double distance = Mth.clamp((Math.sqrt(distanceToSqr(owner)) - (LAUNCH_DISTANCE - 8)) / 8, 0, 1);
        final double prediction = smoothstep(Math.max(progress, distance));
        if (prediction > 0) {
            final double speed = getDeltaMovement().length();
            steerTowards(target, STEERING * prediction, speed);
        }

    }

    private double getReturnSteering(double distance) {
        final double distanceProgress = Mth.clamp((distance - 7) / (18 - 7), 0, 1);
        final double smoothedProgress = smoothstep(distanceProgress);
        return 0.30D + (STEERING - 0.30D) * smoothedProgress;
    }

    private void steerTowards(Vec3 target, double steering, double targetSpeed) {
        final Vec3 toTarget = target.subtract(position());
        final double distance = toTarget.length();
        if (distance < 1.0E-6D) {
            return;
        }

        final Vec3 targetDirection = toTarget.normalize();

        // The offset fades near the owner producing a wide arc
        Vec3 tangent = new Vec3(-targetDirection.z, 0, targetDirection.x);
        if (tangent.lengthSqr() < 1.0E-6D) {
            tangent = Vec3.X_AXIS;
        }
        else {
            tangent = tangent.normalize();
        }

        // Random first direction
        final double curveDirection = (getId() & 1) == 0 ? 1.0D : -1.0D;
        final double curveProgress = Mth.clamp((distance - 4) / (18 - 4), 0, 1);
        final double curveStrength = 0.02D + (0.45 - 0.02D) * smoothstep(curveProgress);
        final Vec3 desiredDirection = targetDirection.add(tangent.scale(curveDirection * curveStrength)).normalize();

        final Vec3 currentMovement = getDeltaMovement();
        final double currentSpeed = currentMovement.length();
        final Vec3 currentDirection = currentSpeed < 1.0E-6D ? desiredDirection : currentMovement.scale(1.0D / currentSpeed);
        Vec3 computedDirection = currentDirection.lerp(desiredDirection, steering);
        if (computedDirection.lengthSqr() < 1.0E-6D) {
            computedDirection = desiredDirection;
        }

        // Speed changes more slowly than direction to keep the arc visually continuous
        final double speed = currentSpeed + (targetSpeed - currentSpeed) * 0.08;
        setDeltaMovement(computedDirection.normalize().scale(speed));
    }

    private static double smoothstep(double progress) {
        return progress * progress * (3 - 2 * progress);
    }

    @Override
    protected void onHitEntity(@NonNull EntityHitResult hitResult) {
        if (isReturning()) {
            return;
        }

        final Entity hitEntity = hitResult.getEntity();
        if (!hitEntityIds.add(hitEntity.getId())) {
            return;
        }

        final Entity owner = getOwner();
        if (level() instanceof ServerLevel serverLevel) {
            hitEntity.hurtServer(serverLevel, damageSources().thrown(this, owner), 7);
        }

    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        // Reflects only the component perpendicular to the impacted face
        final Vec3 movement = getDeltaMovement();
        final Direction.Axis axis = hitResult.getDirection().getAxis();
        Vec3 reflected = switch (axis) {
            case X -> new Vec3(-movement.x, movement.y, movement.z);
            case Y -> new Vec3(movement.x, -movement.y, movement.z);
            case Z -> new Vec3(movement.x, movement.y, -movement.z);
        };

        if (reflected.lengthSqr() < 1.0E-6D) {
            reflected = hitResult.getDirection().getUnitVec3();
        }

        setDeltaMovement(reflected.scale(0.9D));

        setPos(hitResult.getLocation().add(reflected.normalize().scale(0.08D)));
        if (!level().isClientSide()) {
            level().playSound(null, this, SoundEvents.TRIDENT_HIT_GROUND, SoundSource.PLAYERS, 0.8F, 1.15F);
        }

    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return !isReturning() && entity != getOwner() && !hitEntityIds.contains(entity.getId()) && super.canHitEntity(entity);
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    public @NonNull ItemStack getItem() {
        return thrownItem.isEmpty() ? new ItemStack(NomenDubiumItems.PREHISTORIC_MAW.get()) : thrownItem;
    }

    public boolean isReturning() {
        return entityData.get(RETURNING);
    }

    public void startReturning() {
        entityData.set(RETURNING, true);
    }

    public float getBankAngle(float partialTicks) {
        return Mth.lerp(partialTicks, bankAngleOld, angle);
    }

    private void returnToOwner(ServerLevel level, Entity owner) {
        if (returnsToInventory) {
            final ItemStack returnedStack = getItem().copyWithCount(1);
            if (!(owner instanceof Player player) || !player.getInventory().add(returnedStack)) {
                owner.spawnAtLocation(level, returnedStack);
            }

        }

        level.playSound(null, owner, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.7F, 1.3F);

        discard();
    }

    private void dropReturnedItem(ServerLevel level) {
        if (returnsToInventory) {
            spawnAtLocation(level, getItem().copyWithCount(1));
        }

    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(RETURNING, input.getBooleanOr("returning", false));
        returnsToInventory = input.getBooleanOr("returns_to_inventory", true);
        thrownItem = input.read("item", ItemStack.CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("returning", isReturning());
        output.putBoolean("returns_to_inventory", returnsToInventory);
        if (!thrownItem.isEmpty()) {
            output.store("item", ItemStack.CODEC, thrownItem);
        }

    }

}