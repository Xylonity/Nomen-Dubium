package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPartCategory;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import dev.xylonity.nomendubium.common.item.FruitOfLifeItem;
import dev.xylonity.nomendubium.common.item.fossil.FossilItem;
import dev.xylonity.nomendubium.registry.NomenDubiumEntities;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
    private static final EntityDataAccessor<Integer> REVIVAL_TICKS = SynchedEntityData.defineId(SkeletonPartEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> REVIVAL_PALETTE = SynchedEntityData.defineId(SkeletonPartEntity.class, EntityDataSerializers.INT);

    public static final int REVIVAL_DURATION = 60;

    private final Map<ChimeraPartCategory, UUID> attachments = new EnumMap<>(ChimeraPartCategory.class);

    private UUID parentUuid;
    private boolean dismantling;
    private int clientRevivalTicks;

    public SkeletonPartEntity(EntityType<? extends SkeletonPartEntity> type, Level level) {
        super(type, level);
        noPhysics = true;
        setNoGravity(true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        entityData.define(PART_TYPE, SkeletonPartType.HULKING_BODY.index());
        entityData.define(PARENT_ID, -1);
        entityData.define(REVIVAL_TICKS, 0);
        entityData.define(REVIVAL_PALETTE, ChimeraPaletteVariant.NORMAL.index());
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            if (getPartType().isBody()) {
                tickClientRevival();
            }
            return;
        }

        if (level() instanceof ServerLevel serverLevel) {
            setDeltaMovement(Vec3.ZERO);
            if (getPartType().isBody()) {
                tickRevival(serverLevel);
            }
            else {
                final SkeletonPartEntity parent = getParentBody();
                if (parent != null) {
                    moveTo(parent);
                }

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
        else if (REVIVAL_TICKS.equals(dataAccessor) && level().isClientSide()) {
            final int syncedTicks = entityData.get(REVIVAL_TICKS);
            if (syncedTicks <= 0) {
                clientRevivalTicks = 0;
            }
            else if (clientRevivalTicks <= 0) {
                clientRevivalTicks = syncedTicks;
            }

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
        final SkeletonPartEntity body = getPartType().isBody() ? this : getParentBody();

        if (heldItem.is(NomenDubiumItems.FRUIT_OF_LIFE.get())) {
            if (body == null) {
                return InteractionResult.FAIL;
            }
            if (level().isClientSide()) {
                return InteractionResult.SUCCESS;
            }

            return body.beginRevival(player, heldItem);
        }

        if (body != null && body.isReviving()) {
            return level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
        }

        final SkeletonPartType heldPart = heldItem.is(NomenDubiumItems.FOSSIL.get()) ? SkeletonPartType.byFossilPart(FossilItem.getPart(heldItem)) : null;

        // Interacting with a part that's not a body
        if (heldPart != null && !heldPart.isBody()) {
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
        if (isRevivingAssembly()) {
            return true;
        }

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
        if (isRevivingAssembly()) {
            return false;
        }

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
        entityData.set(REVIVAL_TICKS, input.getIntOr("revival_ticks", 0));
        entityData.set(REVIVAL_PALETTE, input.getIntOr("revival_palette", ChimeraPaletteVariant.NORMAL.index()));

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

        if (isReviving()) {
            output.putInt("revival_ticks", entityData.get(REVIVAL_TICKS));
            output.putInt("revival_palette", entityData.get(REVIVAL_PALETTE));
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

    public int getRevivalTicks() {
        final SkeletonPartEntity body = getPartType().isBody() ? this : getParentBody();
        if (body == null) {
            return 0;
        }

        return body.level().isClientSide() ? body.clientRevivalTicks : body.entityData.get(REVIVAL_TICKS);
    }

    public Vec3 getRevivalPivotOffset() {
        if (getPartType().isBody()) {
            return Vec3.ZERO;
        }

        final SkeletonPartEntity body = getParentBody();
        return body == null ? Vec3.ZERO : body.position().subtract(position());
    }

    private boolean isReviving() {
        return getPartType().isBody() && entityData.get(REVIVAL_TICKS) > 0;
    }

    private boolean isRevivingAssembly() {
        final SkeletonPartEntity body = getPartType().isBody() ? this : getParentBody();
        return body != null && body.isReviving();
    }

    private void tickClientRevival() {
        final int syncedTicks = entityData.get(REVIVAL_TICKS);
        if (syncedTicks <= 0) {
            clientRevivalTicks = 0;
        }
        else if (clientRevivalTicks <= 0) {
            clientRevivalTicks = syncedTicks;
        }
        else if (clientRevivalTicks < REVIVAL_DURATION) {
            clientRevivalTicks++;
        }

    }

    private InteractionResult beginRevival(Player player, ItemStack fruit) {
        if (!getPartType().isBody() || isReviving()) {
            return InteractionResult.FAIL;
        }

        if (getAttachedPart(ChimeraPartCategory.HEAD) == null || getAttachedPart(ChimeraPartCategory.TAIL) == null) {
            player.sendOverlayMessage(Component.translatable("message.nomendubium.fruit_of_life.incomplete"));
            return InteractionResult.FAIL;
        }

        ChimeraPaletteVariant palette = FruitOfLifeItem.getPalette(fruit);
        if (palette == null) {
            palette = ChimeraPaletteVariant.random(random);
        }

        entityData.set(REVIVAL_PALETTE, palette.index());
        entityData.set(REVIVAL_TICKS, 1);
        if (!player.hasInfiniteMaterials()) {
            fruit.shrink(1);
        }

        if (level() instanceof ServerLevel serverLevel) {
            final double centerY = getY() + getBoundingBox().getYsize() * 0.5D;
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, getX(), centerY, getZ(), 24, 0.8D, 1.0D, 0.8D, 0.08D);
            serverLevel.sendParticles(ParticleTypes.END_ROD, getX(), centerY, getZ(), 12, 0.5D, 0.8D, 0.5D, 0.04D);
            serverLevel.playSound(null, blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.NEUTRAL, 1.0F, 1.25F);
        }

        return InteractionResult.SUCCESS_SERVER;
    }

    private void tickRevival(ServerLevel level) {
        final int ticks = entityData.get(REVIVAL_TICKS);
        if (ticks <= 0) {
            return;
        }

        final double centerY = getY() + getBoundingBox().getYsize() * 0.5D;
        if (ticks % 4 == 0) {
            final double radius = 0.6D + ticks / (double) REVIVAL_DURATION;
            final double angle = ticks * 0.55D;
            level.sendParticles(ParticleTypes.END_ROD, getX() + Math.cos(angle) * radius, centerY + Math.sin(angle * 0.5D) * 0.5D, getZ() + Math.sin(angle) * radius, 2, 0.08D, 0.08D, 0.08D, 0.01D);
        }

        if (ticks == 20 || ticks == 40) {
            level.playSound(null, blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 1.1F, ticks == 20 ? 0.9F : 1.25F);
        }

        if (ticks >= REVIVAL_DURATION) {
            transformIntoChimera(level);
        }
        else {
            entityData.set(REVIVAL_TICKS, ticks + 1);
        }

    }

    private void transformIntoChimera(ServerLevel level) {
        final SkeletonPartEntity head = getAttachedPart(ChimeraPartCategory.HEAD);
        final SkeletonPartEntity tail = getAttachedPart(ChimeraPartCategory.TAIL);
        final SkeletonPartEntity back = getAttachedPart(ChimeraPartCategory.BACK);
        if (head == null || tail == null) {
            entityData.set(REVIVAL_TICKS, 0);
            return;
        }

        final ChimeraEntity chimera = new ChimeraEntity(NomenDubiumEntities.CHIMERA.get(), level);
        chimera.setBodyVariant((ChimeraBodyVariant) getPartType().variant());
        chimera.setHeadVariant((ChimeraHeadVariant) head.getPartType().variant());
        chimera.setTailVariant((ChimeraTailVariant) tail.getPartType().variant());
        chimera.setBackVariant(back == null ? ChimeraBackVariant.NONE : (ChimeraBackVariant) back.getPartType().variant());
        chimera.setPaletteVariant(ChimeraPaletteVariant.index(entityData.get(REVIVAL_PALETTE)));
        chimera.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
        chimera.setHealth(chimera.getMaxHealth());

        if (!level.addFreshEntity(chimera)) {
            entityData.set(REVIVAL_TICKS, 0);
            return;
        }

        final double centerY = getY() + getBoundingBox().getYsize() * 0.5D;
        level.sendParticles(ParticleTypes.POOF, getX(), centerY, getZ(), 80, 1.2D, 1.4D, 1.2D, 0.12D);
        level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, getX(), centerY, getZ(), 50, 1.0D, 1.2D, 1.0D, 0.15D);
        level.playSound(null, blockPosition(), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 1.3F, 0.9F);

        discardAssembly();
    }

    private void discardAssembly() {
        dismantling = true;
        for (final ChimeraPartCategory category : new ChimeraPartCategory[] { ChimeraPartCategory.HEAD, ChimeraPartCategory.TAIL, ChimeraPartCategory.BACK }) {
            final SkeletonPartEntity attachment = getAttachedPart(category);
            if (attachment != null) {
                attachment.dismantling = true;
                attachment.discard();
            }

        }

        attachments.clear();

        discard();
    }

    private void refreshBoundingBox() {
        setBoundingBox(makeBoundingBox(position()));
    }

    private boolean attachOrReplace(Player player, ItemStack heldItem, SkeletonPartType newPart) {
        if (!getPartType().isBody() || newPart.isBody() || isReviving()) {
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
        if (isRemoved() || isRevivingAssembly()) {
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