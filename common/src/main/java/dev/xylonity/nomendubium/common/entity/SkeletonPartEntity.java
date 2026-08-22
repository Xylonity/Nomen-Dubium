package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPartCategory;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public final class SkeletonPartEntity extends Entity {

    private static final EntityDataAccessor<Integer> PART_TYPE = SynchedEntityData.defineId(SkeletonPartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PARENT_ID = SynchedEntityData.defineId(SkeletonPartEntity.class, EntityDataSerializers.INT);

    private final Map<ChimeraPartCategory, UUID> attachments = new EnumMap<>(ChimeraPartCategory.class);

    private UUID parentUuid;
    private boolean dismantling;

    public SkeletonPartEntity(EntityType<? extends SkeletonPartEntity> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        entityData.define(PART_TYPE, SkeletonPartType.HULKING_BODY.index());
        entityData.define(PARENT_ID, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            setDeltaMovement(Vec3.ZERO);
        }

        if (!level().isClientSide() && !getPartType().isBody()) {
            final SkeletonPartEntity parent = getParentBody();
            if (parent != null) {
                moveTo(parent);
            }

        }

    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void onSyncedDataUpdated(@NonNull EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (PART_TYPE.equals(dataAccessor)) {
            refreshBoundingBox();
        }

    }

    @Override
    protected @NonNull AABB makeBoundingBox(@NonNull Vec3 position) {
        return getPartType().interactionBox(position, getYRot());
    }

    @Override
    public void setYRot(float yaw) {
        super.setYRot(yaw);
        refreshBoundingBox();
    }

    @Override
    public @NonNull InteractionResult interact(Player player, @NonNull InteractionHand hand, @NonNull Vec3 location) {
        final ItemStack heldItem = player.getItemInHand(hand);
        final SkeletonPartType heldPart = heldItem.is(NomenDubiumItems.FOSSIL.get()) ? SkeletonPartType.byFossilPart(FossilItem.getPart(heldItem)) : null;

        // Interacting with a part that's not a body
        if (heldPart != null && !heldPart.isBody()) {
            final SkeletonPartEntity body = getPartType().isBody() ? this : getParentBody();
            if (body != null) {
                if (level().isClientSide()) {
                    return InteractionResult.SUCCESS;
                }

                // Replaces the actual piece with the held one
                return body.attachOrReplace(player, heldItem, heldPart) ? InteractionResult.SUCCESS_SERVER : InteractionResult.FAIL;
            }

        }

        if (level().isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        dismantle(player);

        return InteractionResult.SUCCESS_SERVER;
    }

    @Override
    public boolean skipAttackInteraction(@NonNull Entity attacker) {
        // Normal attack dismantles the part
        if (attacker instanceof Player player) {
            if (!level().isClientSide()) {
                dismantle(player);
            }

            return true;
        }

        return super.skipAttackInteraction(attacker);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        // Normal attack dismantles the part
        if (source.getEntity() instanceof Player player) {
            dismantle(player);
            return true;
        }

        return false;
    }

    @Override
    public ItemStack getPickResult() {
        return fossilStack();
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        setPartType(SkeletonPartType.byFossilPart(input.getStringOr("part", SkeletonPartType.HULKING_BODY.fossilPart())));
        parentUuid = getUuidPer(input, "parent");
        entityData.set(PARENT_ID, -1);

        attachments.clear();

        readAttachment(input, ChimeraPartCategory.HEAD);
        readAttachment(input, ChimeraPartCategory.TAIL);
        readAttachment(input, ChimeraPartCategory.BACK);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        output.putString("part", getPartType().fossilPart());
        if (parentUuid != null) {
            output.putString("parent", parentUuid.toString());
        }

        for (final Map.Entry<ChimeraPartCategory, UUID> attachment : attachments.entrySet()) {
            output.putString(attachment.getKey().name().toLowerCase(), attachment.getValue().toString());
        }

    }

    @Override
    public void onRemoval(@NonNull RemovalReason reason) {
        // If the body is dismantled, every attached part is removed too
        if (!dismantling && reason.shouldDestroy() && level() instanceof ServerLevel) {
            if (getPartType().isBody()) {
                removeAttachedParts(null);
            }
            else {
                final SkeletonPartEntity parent = getParentBody();
                if (parent != null) {
                    parent.clearAttachment(getPartType().category(), getUUID());
                }

            }

        }

        super.onRemoval(reason);
    }

    public SkeletonPartType getPartType() {
        return SkeletonPartType.index(entityData.get(PART_TYPE));
    }

    public void setPartType(@Nullable SkeletonPartType partType) {
        entityData.set(PART_TYPE, (partType == null ? SkeletonPartType.HULKING_BODY : partType).index());
        refreshBoundingBox();
    }

    public void setParentBody(SkeletonPartEntity parent) {
        parentUuid = parent.getUUID();
        entityData.set(PARENT_ID, parent.getId());
        moveTo(parent);
    }

    private void refreshBoundingBox() {
        setBoundingBox(makeBoundingBox(position()));
    }

    private boolean attachOrReplace(Player player, ItemStack heldItem, SkeletonPartType newPart) {
        if (!getPartType().isBody() || newPart.isBody()) {
            return false;
        }

        final ChimeraPartCategory category = newPart.category();
        final SkeletonPartEntity existing = getAttachedPart(category);
        // Replace
        if (existing != null) {
            existing.returnFossil(player);
            existing.setPartType(newPart);
            existing.setParentBody(this);

            existing.playPlaceSound();
        }
        // Attach
        else {
            final SkeletonPartEntity attachment = new SkeletonPartEntity(NomenDubiumEntities.SKELETON_PART.get(), level());
            attachment.setPartType(newPart);
            attachment.setYRot(getYRot());
            attachment.setParentBody(this);

            attachments.put(category, attachment.getUUID());

            level().addFreshEntity(attachment);

            attachment.playPlaceSound();
        }

        if (!player.hasInfiniteMaterials()) {
            heldItem.shrink(1);
        }

        return true;
    }

    private void dismantle(Player player) {
        if (isRemoved()) {
            return;
        }

        dismantling = true;

        playBreakSound();

        if (getPartType().isBody()) {
            removeAttachedParts(player);
        }
        else {
            final SkeletonPartEntity parent = getParentBody();
            if (parent != null) {
                parent.clearAttachment(getPartType().category(), getUUID());
            }

        }

        returnFossil(player);

        discard();
    }

    private void removeAttachedParts(@Nullable Player player) {
        // Per category (there is only 1 max part per attached category)
        for (final ChimeraPartCategory category : new ChimeraPartCategory[] { ChimeraPartCategory.HEAD, ChimeraPartCategory.TAIL, ChimeraPartCategory.BACK }) {
            final SkeletonPartEntity attachment = getAttachedPart(category);
            if (attachment != null) {
                attachment.dismantling = true;
                attachment.returnFossil(player);
                attachment.discard();
            }

        }

        attachments.clear();
    }

    private void returnFossil(@Nullable Player player) {
        final ItemStack fossil = fossilStack();
        if (player != null && player.isCreative()) {
            return;
        }

        if (player != null && player.getInventory().add(fossil)) {
            return;
        }

        if (!fossil.isEmpty() && level() instanceof ServerLevel serverLevel) {
            spawnAtLocation(serverLevel, fossil);
        }

    }

    private ItemStack fossilStack() {
        return NomenDubiumItems.FOSSIL.get().createStack(getPartType().fossilPart());
    }

    private SkeletonPartEntity getAttachedPart(ChimeraPartCategory category) {
        final UUID uuid = attachments.get(category);
        if (uuid == null || !(level() instanceof ServerLevel serverLevel)) {
            return null;
        }

        final Entity entity = serverLevel.getEntityInAnyDimension(uuid);
        if (entity instanceof SkeletonPartEntity part && !part.isRemoved()) {
            return part;
        }

        attachments.remove(category);

        return null;
    }

    private SkeletonPartEntity getParentBody() {
        Entity parent = level().getEntity(entityData.get(PARENT_ID));
        if (!(parent instanceof SkeletonPartEntity) && parentUuid != null && level() instanceof ServerLevel serverLevel) {
            parent = serverLevel.getEntityInAnyDimension(parentUuid);
            if (parent instanceof SkeletonPartEntity) {
                entityData.set(PARENT_ID, parent.getId());
            }

        }

        return parent instanceof SkeletonPartEntity part && part.getPartType().isBody() && !part.isRemoved() ? part : null;
    }

    private void moveTo(SkeletonPartEntity parent) {
        final Vec3 position = parent.position().add(parent.getPartType().attachmentOffset(getPartType().category(), parent.getYRot()));
        if (position.distanceToSqr(position()) > 1.0E-8D) {
            setPos(position);
        }
        if (Float.compare(getYRot(), parent.getYRot()) != 0) {
            setYRot(parent.getYRot());
        }
        if (getXRot() != 0) {
            setXRot(0);
        }

    }

    private void playPlaceSound() {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.BONE_BLOCK_PLACE, SoundSource.BLOCKS, 0.9F, 0.9F + random.nextFloat() * 0.2F);
    }

    private void playBreakSound() {
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.BONE_BLOCK_BREAK, SoundSource.BLOCKS, 0.9F, 0.9F + random.nextFloat() * 0.2F);
    }

    private void clearAttachment(ChimeraPartCategory category, UUID expectedUuid) {
        if (expectedUuid.equals(attachments.get(category))) {
            attachments.remove(category);
        }

    }

    private void readAttachment(ValueInput input, ChimeraPartCategory category) {
        final UUID uuid = getUuidPer(input, category.name().toLowerCase());
        if (uuid != null) {
            attachments.put(category, uuid);
        }

    }

    private static UUID getUuidPer(ValueInput input, String key) {
        final String value = input.getStringOr(key, "");
        if (value.isEmpty()) {
            return null;
        }

        try {
            return UUID.fromString(value);
        }
        catch (Exception _) {
            return null;
        }

    }

}