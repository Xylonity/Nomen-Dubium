package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.registry.NomenDubiumBlocks;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class ChimeraEntity extends TamableAnimal implements PlayerRideableJumping {

    // avian: more speed, shelled: slow but swims, lanky: slow but can jump high, hulking: normal but moves entities away, puffy: slow but applies bone meal
    private static final EntityDataAccessor<Integer> BODY = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    // snarled: roar, shielded: tackle, crunching: bite, beaked: peck (applies poison), snorting: extracts resources
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAIL = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BACK = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PALETTE = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int HOSTILE_ROOT_RADIUS = 8;

    private int hostileRootCooldown = 200;
    private int puffyGrowthCooldown;
    private int snortingCooldown = 200;
    private int roarCooldown;
    private float playerJumpPendingScale;
    private float sitAnimation;
    private float sitAnimationO;
    private float jumpAnimation;
    private float jumpAnimationO;

    public ChimeraEntity(EntityType<? extends ChimeraEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 50.0)
            .add(Attributes.MOVEMENT_SPEED, 0.30)
            .add(Attributes.ATTACK_DAMAGE, 10.0)
            .add(Attributes.ATTACK_KNOCKBACK, 1.2)
            .add(Attributes.ARMOR, 4.0)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.75)
            .add(Attributes.FOLLOW_RANGE, 32.0)
            .add(Attributes.JUMP_STRENGTH, 0.48)
            .add(Attributes.STEP_HEIGHT, 1.25)
            .add(Attributes.SAFE_FALL_DISTANCE, 4.0)
            .add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new ChimeraMeleeAttackGoal(this));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.1, 8.0F, 3.0F));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.85));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, false, (target, _) -> this.isValidHostileTarget(target)));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(BODY, ChimeraBodyVariant.HULKING.index());
        entityData.define(HEAD, ChimeraHeadVariant.CRUNCHING.index());
        entityData.define(TAIL, ChimeraTailVariant.SPIKED.index());
        entityData.define(BACK, ChimeraBackVariant.NONE.index());
        entityData.define(PALETTE, ChimeraPaletteVariant.NORMAL.index());
        entityData.define(HOSTILE, false);
    }

    @Override
    public void onSyncedDataUpdated(@NonNull EntityDataAccessor<?> dataAccessor) {
        super.onSyncedDataUpdated(dataAccessor);
        if (BODY.equals(dataAccessor)) {
            this.refreshDimensions();
        }

    }

    @Override
    public void tick() {
        super.tick();

        this.tickSitAnimation();
        this.tickJumpAnimation();

        if (!(this.level() instanceof ServerLevel serverLevel) || !this.isAlive()) {
            return;
        }

        if (this.roarCooldown > 0) {
            this.roarCooldown--;
        }

        this.tickPuffyGrowth(serverLevel);
        this.tickHostileRoots(serverLevel);
        this.tickSnorting(serverLevel);
    }

    private void tickSitAnimation() {
        this.sitAnimationO = this.sitAnimation;
        final float target = this.isInSittingPose() ? 1.0F : 0.0F;
        this.sitAnimation += Mth.clamp(target - this.sitAnimation, -0.1F, 0.1F);
    }

    public float getSitAnimation(float partialTick) {
        return Mth.lerp(partialTick, this.sitAnimationO, this.sitAnimation);
    }

    private void tickJumpAnimation() {
        this.jumpAnimationO = this.jumpAnimation;
        final boolean lankyAirborne = this.getBodyVariant() == ChimeraBodyVariant.LANKY && !this.onGround();
        final float target = lankyAirborne ? Mth.clamp(0.45F + (float) Math.abs(this.getDeltaMovement().y) * 0.65F, 0.0F, 1.0F) : 0.0F;
        final float step = target > this.jumpAnimation ? 0.18F : 0.22F;
        this.jumpAnimation += Mth.clamp(target - this.jumpAnimation, -step, step);
    }

    public float getJumpAnimation(float partialTick) {
        return Mth.lerp(partialTick, this.jumpAnimationO, this.jumpAnimation);
    }

    private void tickPuffyGrowth(ServerLevel level) {
        if (this.getBodyVariant() != ChimeraBodyVariant.PUFFY || this.getDeltaMovement().horizontalDistanceSqr() < 0.0025) {
            return;
        }

        if (this.puffyGrowthCooldown-- > 0) {
            return;
        }

        this.puffyGrowthCooldown = 200;
        final BlockPos center = this.blockPosition().below();
        // Bonemeals the block below
        for (int attempt = 0; attempt < 3; attempt++) {
            final BlockPos pos = center.offset(this.random.nextInt(3) - 1, 0, this.random.nextInt(3) - 1);
            final ItemStack boneMeal = new ItemStack(Items.BONE_MEAL);
            final boolean should = BoneMealItem.growCrop(boneMeal, level, pos);
            final BlockPos growthPos = should ? pos : pos.above();
            if (!should && !BoneMealItem.growCrop(new ItemStack(Items.BONE_MEAL), level, growthPos)) {
                continue;
            }

            BoneMealItem.addGrowthParticles(level, growthPos, 8);
        }

    }

    private void tickHostileRoots(ServerLevel level) {
        if (!this.isHostile()) {
            return;
        }

        if (this.hostileRootCooldown-- > 0) {
            return;
        }

        this.hostileRootCooldown = 400 + this.random.nextInt(201);
        final BlockPos center = this.blockPosition();
        int nearbyRoots = 0;
        for (final BlockPos pos : BlockPos.betweenClosed(center.offset(-HOSTILE_ROOT_RADIUS, -2, -HOSTILE_ROOT_RADIUS), center.offset(HOSTILE_ROOT_RADIUS, 2, HOSTILE_ROOT_RADIUS))) {
            if (level.getBlockState(pos).is(NomenDubiumBlocks.ROOT_OF_LIFE.get()) && ++nearbyRoots >= 6) {
                return;
            }
        }

        // Spawns root of life blocks in the surroundings
        final BlockState root = NomenDubiumBlocks.ROOT_OF_LIFE.get().defaultBlockState();
        for (int attempt = 0; attempt < 12; attempt++) {
            final BlockPos target = center.offset(
                this.random.nextInt(HOSTILE_ROOT_RADIUS * 2 + 1) - HOSTILE_ROOT_RADIUS,
                this.random.nextInt(4) - 2,
                this.random.nextInt(HOSTILE_ROOT_RADIUS * 2 + 1) - HOSTILE_ROOT_RADIUS
            );

            if (level.isEmptyBlock(target) && root.canSurvive(level, target)) {
                level.setBlockAndUpdate(target, root);
                return;
            }

        }

    }

    // TODO: change to manual variant
    private void tickSnorting(ServerLevel level) {
        if (this.getHeadVariant() != ChimeraHeadVariant.SNORTING || this.getTarget() != null || !this.onGround()) {
            return;
        }

        if (this.snortingCooldown-- > 0) {
            return;
        }

        final BlockState ground = level.getBlockState(this.blockPosition().below());
        if (!ground.is(BlockTags.DIRT) && !ground.is(BlockTags.BASE_STONE_OVERWORLD)) {
            this.snortingCooldown = 100;
            return;
        }

        this.snortingCooldown = 500 + this.random.nextInt(301);
        final int rand = this.random.nextInt(100);
        final Item resource = rand < 50 ? Items.FLINT
            : rand < 75 ? Items.COAL
            : rand < 90 ? Items.RAW_COPPER
            : rand < 98 ? Items.RAW_IRON
            : Items.RAW_GOLD;
        this.spawnAtLocation(level, resource);
        this.playSound(SoundEvents.SNIFFER_DIGGING, 1.0F, 0.85F + this.random.nextFloat() * 0.2F);
    }

    @Override
    public @NonNull InteractionResult mobInteract(Player player, @NonNull InteractionHand hand) {
        final ItemStack heldItem = player.getItemInHand(hand);
        if (this.isHostile()) {
            return super.mobInteract(player, hand);
        }

        // Sap of life tames the entity
        if (heldItem.is(NomenDubiumItems.SAP_OF_LIFE.get())) {
            if (!this.isTame()) {
                if (this.level().isClientSide()) {
                    return InteractionResult.SUCCESS;
                }

                this.usePlayerItem(player, hand, heldItem);
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte) 7);
                return InteractionResult.SUCCESS_SERVER;
            }

            // Or heals the chimera
            if (this.isOwnedBy(player) && this.getHealth() < this.getMaxHealth()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, heldItem);
                    this.heal(10);
                }

                return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

        }


        if (this.isTame() && this.isOwnedBy(player) && hand == InteractionHand.MAIN_HAND) {
            // Cycles the sit status
            if (player.isSecondaryUseActive()) {
                if (!this.level().isClientSide()) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.setInSittingPose(this.isOrderedToSit());
                    this.navigation.stop();
                    this.ejectPassengers();
                }

                return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

            // Rides the chimera on normal interaction
            if (!this.isVehicle()) {
                if (!this.level().isClientSide()) {
                    this.setOrderedToSit(false);
                    this.setInSittingPose(false);
                    player.startRiding(this);
                }

                return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        return this.getBodySkeletonType().entityDimensions();
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        final Entity passenger = this.getFirstPassenger();
        if (!this.isHostile() && this.isTame() && passenger instanceof Player player && this.isOwnedBy(player)) {
            return player;
        }

        return null;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty() && !this.isHostile() && this.isTame() && passenger instanceof Player player && this.isOwnedBy(player);
    }

    @Override
    protected void tickRidden(@NonNull Player player, @NonNull Vec3 travelVector) {
        super.tickRidden(player, travelVector);

        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.yHeadRot = this.getYRot();

        if (this.isLocalInstanceAuthoritative() && this.playerJumpPendingScale > 0.0F && this.onGround()) {
            final double jumpPower = this.getJumpPower(this.playerJumpPendingScale);
            final Vec3 movement = this.getDeltaMovement();
            this.setDeltaMovement(movement.x, jumpPower, movement.z);
            if (travelVector.z > 0.0) {
                final float rotation = this.getYRot() * Mth.DEG_TO_RAD;
                this.setDeltaMovement(this.getDeltaMovement().add(-0.35F * Mth.sin(rotation) * this.playerJumpPendingScale, 0, 0.35F * Mth.cos(rotation) * this.playerJumpPendingScale));
            }

            this.needsSync = true;
            this.playerJumpPendingScale = 0.0F;
        }

    }

    @Override
    protected Vec3 getRiddenInput(Player player, Vec3 travelVector) {
        float forward = player.zza;
        if (forward <= 0.0F) {
            forward *= 0.25F;
        }

        return new Vec3(player.xxa * 0.5F, 0.0, forward);
    }

    @Override
    protected float getRiddenSpeed(Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if (jumpPower >= 0) {
            this.playerJumpPendingScale = this.getPlayerJumpPendingScale(Mth.clamp(jumpPower, 0, 90));
        }

    }

    @Override
    public boolean canJump() {
        return this.getControllingPassenger() != null;
    }

    @Override
    public void handleStartJump(int jumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public void travel(Vec3 travelVector) {
        super.travel(travelVector);
        if (this.getBodyVariant() == ChimeraBodyVariant.SHELLED && (this.isInWater() || this.isInLava())) {
            final Vec3 movement = this.getDeltaMovement();
            final double horizontalBoost = this.isInLava() ? 1.65 : 1.15;
            this.setDeltaMovement(movement.x * horizontalBoost, Math.max(movement.y, -0.08) + 0.02, movement.z * horizontalBoost);
        }

    }

    @Override
    protected float getWaterSlowDown() {
        return this.getBodyVariant() == ChimeraBodyVariant.SHELLED ? 0.95F : super.getWaterSlowDown();
    }

    @Override
    public boolean fireImmune() {
        return this.getBodyVariant() == ChimeraBodyVariant.SHELLED || super.fireImmune();
    }

    @Override
    public void lavaHurt() {
        if (this.getBodyVariant() != ChimeraBodyVariant.SHELLED) {
            super.lavaHurt();
        }

    }

    @Override
    public void push(Entity other) {
        super.push(other);
        if (this.level().isClientSide() || this.getBodyVariant() != ChimeraBodyVariant.HULKING || this.getDeltaMovement().horizontalDistanceSqr() < 0.0025 || this.hasPassenger(other) || this.considersEntityAsAlly(other)) {
            return;
        }

        final Vec3 away = other.position().subtract(this.position()).multiply(1.0, 0.0, 1.0);
        if (away.lengthSqr() > 1.0E-4) {
            final Vec3 force = away.normalize().scale(0.8);
            other.push(force.x, 0.15, force.z);
        }

    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        final boolean hurt;
        if (this.getTailVariant() == ChimeraTailVariant.SPEARED && target instanceof LivingEntity living) {
            hurt = living.hurtServer(level, level.damageSources().indirectMagic(this, this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            if (hurt) {
                this.playAttackSound();
            }

        }
        else {
            hurt = super.doHurtTarget(level, target);
        }

        if (!hurt || !(target instanceof LivingEntity living)) {
            return hurt;
        }

        this.applyHeadAttack(level, living);
        this.applyTailAttack(level, living);

        return true;
    }

    private void applyHeadAttack(ServerLevel level, LivingEntity target) {
        switch (this.getHeadVariant()) {
            case CRUNCHING, SNORTING -> { ;; }
            case SHIELDED -> target.knockback(1.6, this.getX() - target.getX(), this.getZ() - target.getZ());
            case SNARLED -> this.roar(level);
            case BEAKED -> target.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0), this);
        }

    }

    private void applyTailAttack(ServerLevel level, LivingEntity primaryTarget) {
        if (this.getTailVariant() != ChimeraTailVariant.FAN) {
            return;
        }

        final AABB sweepArea = primaryTarget.getBoundingBox().inflate(4.0, 1.5, 4.0);
        final float sweepDamage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.45F;
        for (final LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, sweepArea, entity ->
            entity != this && entity != primaryTarget && !this.considersEntityAsAlly(entity) && (this.isHostile() || entity.getType() == primaryTarget.getType())
        )) {
            target.hurtServer(level, level.damageSources().mobAttack(this), sweepDamage);
        }

    }

    private void roar(ServerLevel level) {
        if (this.roarCooldown > 0) {
            return;
        }

        this.roarCooldown = 200;
        this.playSound(SoundEvents.RAVAGER_ROAR, 1.2F, 0.85F + this.random.nextFloat() * 0.15F);
        for (final LivingEntity ally : level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.0), entity ->
            entity == this || this.considersEntityAsAlly(entity) || this.isHostile() && entity instanceof ChimeraEntity chimera && chimera.isHostile()
        )) {
            ally.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 200, 0), this);
        }

    }

    private boolean isValidHostileTarget(LivingEntity target) {
        return this.isHostile() && target != this && target.isAlive() && !this.hasPassenger(target) && !(target instanceof ChimeraEntity chimera && chimera.isHostile());
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.isHostile() ? this.isValidHostileTarget(target) && super.canAttack(target) : super.canAttack(target);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setBodyVariant(ChimeraBodyVariant.index(input.getIntOr("bodyvariant", ChimeraBodyVariant.HULKING.index())));
        this.setHeadVariant(ChimeraHeadVariant.index(input.getIntOr("headvariant", ChimeraHeadVariant.CRUNCHING.index())));
        this.setTailVariant(ChimeraTailVariant.index(input.getIntOr("tailvariant", ChimeraTailVariant.SPIKED.index())));
        this.setBackVariant(ChimeraBackVariant.index(input.getIntOr("backvariant", ChimeraBackVariant.NONE.index())));
        final int legacyPalette = input.getBooleanOr("junglepalette", false) ? ChimeraPaletteVariant.JUNGLE.index() : ChimeraPaletteVariant.NORMAL.index();
        this.setPaletteVariant(ChimeraPaletteVariant.index(input.getIntOr("palettevariant", legacyPalette)));
        this.setHostile(input.getBooleanOr("hostile", false));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("bodyvariant", this.getBodyVariant().index());
        output.putInt("headvariant", this.getHeadVariant().index());
        output.putInt("tailvariant", this.getTailVariant().index());
        output.putInt("backvariant", this.getBackVariant().index());
        output.putInt("palettevariant", this.getPaletteVariant().index());
        output.putBoolean("hostile", this.isHostile());
    }

    public ChimeraBodyVariant getBodyVariant() {
        return ChimeraBodyVariant.index(this.entityData.get(BODY));
    }

    public void setBodyVariant(ChimeraBodyVariant variant) {
        this.entityData.set(BODY, variant.index());
        this.applyBodyAttributes();
        this.refreshDimensions();
    }

    public ChimeraHeadVariant getHeadVariant() {
        return ChimeraHeadVariant.index(this.entityData.get(HEAD));
    }

    public void setHeadVariant(ChimeraHeadVariant variant) {
        this.entityData.set(HEAD, variant.index());
        this.applyCombatAttributes();
    }

    public ChimeraTailVariant getTailVariant() {
        return ChimeraTailVariant.index(this.entityData.get(TAIL));
    }

    public void setTailVariant(ChimeraTailVariant variant) {
        this.entityData.set(TAIL, variant.index());
        this.applyCombatAttributes();
    }

    public ChimeraBackVariant getBackVariant() {
        return ChimeraBackVariant.index(this.entityData.get(BACK));
    }

    public void setBackVariant(ChimeraBackVariant variant) {
        this.entityData.set(BACK, variant.index());
    }

    public ChimeraPaletteVariant getPaletteVariant() {
        return ChimeraPaletteVariant.index(this.entityData.get(PALETTE));
    }

    public void setPaletteVariant(ChimeraPaletteVariant variant) {
        this.entityData.set(PALETTE, variant.index());
    }

    public boolean isHostile() {
        return this.entityData.get(HOSTILE);
    }

    public void setHostile(boolean hostile) {
        this.entityData.set(HOSTILE, hostile);
        if (hostile) {
            this.setOrderedToSit(false);
            this.setInSittingPose(false);
            this.setTame(false, true);
            this.setOwnerReference(null);
            this.ejectPassengers();
        }

    }

    private void applyBodyAttributes() {
        switch (this.getBodyVariant()) {
            case HULKING -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.30);
                this.setAttributeBase(Attributes.ARMOR, 4.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.75);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 0.48);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.25);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 4.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.1);
            }
            case SHELLED -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.20);
                this.setAttributeBase(Attributes.ARMOR, 10.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.90);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 0.42);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.0);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 4.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 1.0);
            }
            case AVIAN -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.42);
                this.setAttributeBase(Attributes.ARMOR, 2.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.20);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 0.55);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.0);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 5.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.1);
            }
            case LANKY -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.22);
                this.setAttributeBase(Attributes.ARMOR, 2.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.20);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 1.05);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.5);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 12.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.1);
            }
            case PUFFY -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.30);
                this.setAttributeBase(Attributes.ARMOR, 3.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.35);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 0.45);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.0);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 4.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.2);
            }

        }

        this.applyCombatAttributes();
    }

    private void applyCombatAttributes() {
        double attackDamage = 6.0;
        attackDamage += switch (this.getHeadVariant()) {
            case CRUNCHING -> 2.0;
            case SHIELDED -> 1.0;
            case SNARLED, BEAKED -> 0.0;
            case SNORTING -> -1.0;
        };
        attackDamage += switch (this.getTailVariant()) {
            case SPIKED -> 2.0;
            case SPEARED -> 1.0;
            case STUBBY, CLUBBED, FAN -> 0.0;
        };

        double attackKnockback = this.getBodyVariant() == ChimeraBodyVariant.HULKING ? 1.2 : 0.4;
        if (this.getHeadVariant() == ChimeraHeadVariant.SHIELDED) {
            attackKnockback += 0.8;
        }
        if (this.getTailVariant() == ChimeraTailVariant.CLUBBED) {
            attackKnockback += 1.2;
        }

        this.setAttributeBase(Attributes.ATTACK_DAMAGE, attackDamage);
        this.setAttributeBase(Attributes.ATTACK_KNOCKBACK, attackKnockback);
    }

    private void setAttributeBase(Holder<Attribute> attribute, double value) {
        final AttributeInstance instance = this.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }

    }

    private SkeletonPartType getBodySkeletonType() {
        final SkeletonPartType type = SkeletonPartType.byFossilPart(this.getBodyVariant().fossilPart());
        return type == null ? SkeletonPartType.HULKING_BODY : type;
    }

    private static final class ChimeraMeleeAttackGoal extends MeleeAttackGoal {

        private final ChimeraEntity chimera;

        private ChimeraMeleeAttackGoal(ChimeraEntity chimera) {
            super(chimera, 1.15, true);
            this.chimera = chimera;
        }

        @Override
        protected int getAttackInterval() {
            return this.chimera.getTailVariant() == ChimeraTailVariant.STUBBY ? 14 : 20;
        }

    }

}
