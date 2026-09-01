package dev.xylonity.nomendubium.common.entity;

import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraFollowOwnerGoal;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraHostileRootsGoal;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraMeleeAttackGoal;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraPuffyGrowthGoal;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraRoarGoal;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraSitGoal;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraWanderGoal;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBackVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraBodyVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraPaletteVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import dev.xylonity.nomendubium.common.entity.skeleton.SkeletonPartType;
import dev.xylonity.nomendubium.registry.NomenDubiumItems;
import dev.xylonity.nomendubium.registry.NomenDubiumSounds;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/// WHY DOES HALF OF THE ENTITY LOGIC TICK ONLY ON THE CLIENT WHEN THERE IS A RIDER PRESENT OMG
/// GOALS DO NOT WORK
/// TODO: Optimization after modjam
public final class ChimeraEntity extends TamableAnimal implements PlayerRideableJumping {

    // avian: more speed, shelled: slow but swims, lanky: slow but can jump high, hulking: normal but moves entities away, puffy: slow but applies bone meal
    private static final EntityDataAccessor<Integer> BODY = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    // snarled: roar, shielded: tackle, crunching: bite, beaked: peck (applies poison), snorting: extracts resources
    private static final EntityDataAccessor<Integer> HEAD = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAIL = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BACK = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PALETTE = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HOSTILE = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> MAIN_ACTION = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ROARING = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHIELD_CHARGING = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> SHIELD_CHARGE_Y_ROT = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> CRUNCHING_BITING = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SNORTING_EXTRACTING = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BEAKED_PECKING = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> BEAKED_PECK_Y_ROT = SynchedEntityData.defineId(ChimeraEntity.class, EntityDataSerializers.FLOAT);

    private static final double SHIELDED_CHARGE_SPEED = 0.85;
    private static final int CRUNCHING_BITE_DURATION = 8;
    private static final int BEAKED_PECK_DURATION = 10;
    private static final int SNORTING_EXTRACTION_DURATION = 48;
    private static final int SNORTING_ITEM_TICK = 38;
    private static final List<Item> SNORTING_ITEMS = List.of(
            Items.COAL, Items.RAW_COPPER, Items.RAW_IRON,
            Items.RAW_GOLD, Items.REDSTONE, Items.LAPIS_LAZULI,
            Items.QUARTZ, Items.AMETHYST_SHARD, Items.CARROT,
            Items.ANDESITE, Items.APPLE, Items.STICK, Items.CHARCOAL
    );
    private static final List<BlockState> SNORTING_PARTICLE_STATES = List.of(
            Blocks.STONE.defaultBlockState(), Blocks.DEEPSLATE.defaultBlockState(),
            Blocks.TUFF.defaultBlockState(), Blocks.GRAVEL.defaultBlockState()
    );

    public static final int ACTION_SIT = 0;
    public static final int ACTION_FOLLOW = 1;
    public static final int ACTION_WANDER = 2;
    private static final int MAIN_ACTION_COUNT = 3;

    private float playerJumpPendingScale;

    private float sitAnimation;
    private float sitAnimationO;

    private float jumpAnimation;
    private float jumpAnimationO;

    private float roarAnimation;
    private float roarAnimationO;

    private float shieldChargeAnimation;
    private float shieldChargeAnimationO;
    private final Set<Integer> shieldChargeHitEntities = new HashSet<>();
    private int shieldChargeEndTick;
    private int nextShieldChargeTick;

    private float crunchingBiteAnimation;
    private float crunchingBiteAnimationO;
    private int crunchingBiteStartTick;
    private int crunchingBiteEndTick;
    private int nextCrunchingBiteTick;
    private boolean crunchingBiteDamageApplied;

    private float snortingExtractionAnimation;
    private float snortingExtractionAnimationO;
    private int snortingExtractionStartTick;
    private int snortingExtractionEndTick;
    private int nextSnortingExtractionTick;
    private boolean snortingItemPopped;

    private float beakedPeckAnimation;
    private float beakedPeckAnimationO;
    private int beakedPeckStartTick;
    private int beakedPeckEndTick;
    private int nextBeakedPeckTick;
    private boolean beakedPeckDamageApplied;

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
        this.goalSelector.addGoal(1, new ChimeraSitGoal(this));
        this.goalSelector.addGoal(2, new ChimeraRoarGoal(this));
        this.goalSelector.addGoal(3, new ChimeraMeleeAttackGoal(this));
        this.goalSelector.addGoal(4, new ChimeraFollowOwnerGoal(this));
        this.goalSelector.addGoal(5, new ChimeraPuffyGrowthGoal(this));
        this.goalSelector.addGoal(5, new ChimeraHostileRootsGoal(this));
        this.goalSelector.addGoal(6, new ChimeraWanderGoal(this));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, false,
            (target, _) -> this.isValidHostileTarget(target)
        ));

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
        entityData.define(MAIN_ACTION, ACTION_FOLLOW);
        entityData.define(ROARING, false);
        entityData.define(SHIELD_CHARGING, false);
        entityData.define(SHIELD_CHARGE_Y_ROT, 0.0F);
        entityData.define(CRUNCHING_BITING, false);
        entityData.define(SNORTING_EXTRACTING, false);
        entityData.define(BEAKED_PECKING, false);
        entityData.define(BEAKED_PECK_Y_ROT, 0.0F);
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
        this.tickRoarAnimation();
        this.tickShieldChargeAnimation();
        this.tickCrunchingBiteAnimation();
        this.tickSnortingExtractionAnimation();
        this.tickBeakedPeckAnimation();
        if (!this.level().isClientSide()) {
            this.tickShieldCharge();
            this.tickCrunchingBite();
            this.tickSnortingExtraction();
            this.tickBeakedPeck();
        }

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

    private void tickRoarAnimation() {
        if (!this.isRoaring()) {
            this.roarAnimation = 0;
            this.roarAnimationO = 0;
            return;
        }

        this.roarAnimationO = this.roarAnimation;
        this.roarAnimation = Math.min(this.roarAnimation + 1.0F, ChimeraRoarGoal.DURATION_TICKS);
    }

    public float getRoarAnimation(float partialTick) {
        return Mth.lerp(partialTick, this.roarAnimationO, this.roarAnimation);
    }

    private void tickShieldChargeAnimation() {
        this.shieldChargeAnimationO = this.shieldChargeAnimation;
        final float target = this.isShieldCharging() ? 1.0F : 0.0F;
        final float step = target > this.shieldChargeAnimation ? 0.28F : 0.20F;
        this.shieldChargeAnimation += Mth.clamp(target - this.shieldChargeAnimation, -step, step);
    }

    public float getShieldChargeAnimation(float partialTick) {
        return Mth.lerp(partialTick, this.shieldChargeAnimationO, this.shieldChargeAnimation);
    }

    private void tickCrunchingBiteAnimation() {
        if (!this.isCrunchingBiting()) {
            this.crunchingBiteAnimation = 0;
            this.crunchingBiteAnimationO = 0;
            return;
        }

        this.crunchingBiteAnimationO = this.crunchingBiteAnimation;
        this.crunchingBiteAnimation = Math.min(this.crunchingBiteAnimation + 1f, CRUNCHING_BITE_DURATION);
    }

    public float getCrunchingBiteProgress(float partialTick) {
        return Mth.lerp(partialTick, this.crunchingBiteAnimationO, this.crunchingBiteAnimation) / CRUNCHING_BITE_DURATION;
    }

    private void tickSnortingExtractionAnimation() {
        if (!this.isSnortingExtracting()) {
            this.snortingExtractionAnimation = 0;
            this.snortingExtractionAnimationO = 0;
            return;
        }

        this.snortingExtractionAnimationO = this.snortingExtractionAnimation;
        this.snortingExtractionAnimation = Math.min(this.snortingExtractionAnimation + 1, SNORTING_EXTRACTION_DURATION);
    }

    public float getSnortingExtractionProgress(float partialTick) {
        return Mth.lerp(partialTick, this.snortingExtractionAnimationO, this.snortingExtractionAnimation) / SNORTING_EXTRACTION_DURATION;
    }

    private void tickBeakedPeckAnimation() {
        if (!this.isBeakedPecking()) {
            this.beakedPeckAnimation = 0;
            this.beakedPeckAnimationO = 0;
            return;
        }

        this.beakedPeckAnimationO = this.beakedPeckAnimation;
        this.beakedPeckAnimation = Math.min(this.beakedPeckAnimation + 1, BEAKED_PECK_DURATION);
    }

    public float getBeakedPeckProgress(float partialTick) {
        return Mth.lerp(partialTick, this.beakedPeckAnimationO, this.beakedPeckAnimation) / BEAKED_PECK_DURATION;
    }

    public boolean isRoaring() {
        return this.entityData.get(ROARING);
    }

    public void setRoaring(boolean roaring) {
        this.entityData.set(ROARING, roaring);
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
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.setMainAction(ACTION_SIT, player);
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
            // Cycles through wander, sit and follow, like Companions!
            // https://github.com/Xylonity/Companions/blob/v1.20.1/common/src/main/java/dev/xylonity/companions/common/entity/CompanionEntity.java
            if (player.isSecondaryUseActive()) {
                if (!this.level().isClientSide()) {
                    this.setMainAction(this.getMainAction() + 1, player);
                }

                return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

            // Rides the chimera on normal interaction
            if (!this.isVehicle()) {
                if (!this.level().isClientSide()) {
                    this.navigation.stop();
                    this.setInSittingPose(false);
                    setMainAction(ACTION_FOLLOW, player);
                    player.startRiding(this);
                }

                return this.level().isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
            }

        }

        return super.mobInteract(player, hand);
    }

    public int getMainAction() {
        return this.entityData.get(MAIN_ACTION);
    }

    /// Derived from my own implementation
    /// https://github.com/Xylonity/Companions/blob/v1.20.1/common/src/main/java/dev/xylonity/companions/common/entity/CompanionEntity.java
    public void setMainAction(int action, @Nullable Player player) {
        final int newAction = Math.floorMod(action, MAIN_ACTION_COUNT);
        final boolean sitting = newAction == ACTION_SIT;

        this.entityData.set(MAIN_ACTION, newAction);
        this.navigation.stop();
        this.ejectPassengers();
        this.setOrderedToSit(sitting);
        this.setInSittingPose(sitting);
        if (sitting) {
            this.setTarget(null);
        }

        if (player != null) {
            final String messageKey = switch (newAction) {
                case ACTION_SIT -> "main_action.nomendubium.client_message.is_sitting";
                case ACTION_WANDER -> "main_action.nomendubium.client_message.is_wandering";
                default -> "main_action.nomendubium.client_message.is_following";
            };

            player.sendOverlayMessage(Component.translatable(messageKey, this.getName()));
        }

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

        // On interaction (for some reason SERVER GOALS DO NOT WORK WHILE RIDING) selection of a specific attack logic
        if (!this.level().isClientSide() && player.swinging && player.swingTime == -1) {
            this.tryUseMountedHeadAbility();
        }

        // Extra charge movement to the front
        if (this.isShieldCharging()) {
            final Vec3 movement = this.getDeltaMovement();
            final Vec3 direction = this.getShieldChargeDirection();
            this.setDeltaMovement(direction.x * SHIELDED_CHARGE_SPEED, movement.y, direction.z * SHIELDED_CHARGE_SPEED);
            this.needsSync = true;
        }

        // Short charge to the front
        if (this.isBeakedPecking()) {
            final float progress = Mth.clamp(this.beakedPeckAnimation / BEAKED_PECK_DURATION, 0.0F, 1.0F);
            final double speed = 0.42 * Mth.sin(progress * Mth.PI);
            final Vec3 movement = this.getDeltaMovement();
            final Vec3 direction = this.getBeakedPeckDirection();
            this.setDeltaMovement(direction.x * speed, movement.y, direction.z * speed);
            this.needsSync = true;
        }

        // Jump
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
        if (this.isShieldCharging() || this.isSnortingExtracting() || this.isBeakedPecking()) {
            return Vec3.ZERO;
        }

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

    private void tryUseMountedHeadAbility() {
        switch (this.getHeadVariant()) {
            case SHIELDED -> this.tryStartShieldCharge();
            case CRUNCHING -> this.tryStartCrunchingBite();
            case SNORTING -> this.tryStartSnortingExtraction();
            case BEAKED -> this.tryStartBeakedPeck();
            default -> {
                ;;
            }

        }

    }

    private void tryStartCrunchingBite() {
        if (this.tickCount < this.nextCrunchingBiteTick || this.isCrunchingBiting()) {
            return;
        }

        this.entityData.set(CRUNCHING_BITING, true);
        this.crunchingBiteStartTick = this.tickCount;
        this.crunchingBiteEndTick = this.tickCount + CRUNCHING_BITE_DURATION;
        this.nextCrunchingBiteTick = this.tickCount + 14;
        this.crunchingBiteDamageApplied = false;
    }

    private void tickCrunchingBite() {
        if (!this.isCrunchingBiting()) {
            return;
        }

        if (!(this.getControllingPassenger() instanceof Player) || this.getHeadVariant() != ChimeraHeadVariant.CRUNCHING || this.tickCount >= this.crunchingBiteEndTick) {
            this.stopCrunchingBite();
            return;
        }

        if (!this.crunchingBiteDamageApplied && this.tickCount >= this.crunchingBiteStartTick + 6) {
            this.crunchingBiteDamageApplied = true;
            this.performCrunchingBite();
        }

    }

    private void performCrunchingBite() {
        final ServerLevel level = (ServerLevel) this.level();
        final float rotation = this.getYRot() * Mth.DEG_TO_RAD;
        final Vec3 direction = new Vec3(-Mth.sin(rotation), 0, Mth.cos(rotation));
        final AABB hitbox = this.getBoundingBox().expandTowards(direction.scale(2.2)).inflate(0.4, 0.3, 0.4);

        for (final LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, hitbox, entity ->
                !this.isChimeraAlly(entity) && !this.hasPassenger(entity)
                        && direction.dot(entity.position().subtract(this.position()).multiply(1, 0, 1)) > 0
        )) {
            this.doHurtTarget(level, target);
        }

        this.playSound(SoundEvents.PHANTOM_BITE, 1.0F, 0.88F + this.getRandom().nextFloat() * 0.12F);
    }

    private void stopCrunchingBite() {
        this.entityData.set(CRUNCHING_BITING, false);
        this.crunchingBiteDamageApplied = false;
    }

    private boolean isCrunchingBiting() {
        return this.entityData.get(CRUNCHING_BITING);
    }

    private void tryStartSnortingExtraction() {
        if (this.tickCount < this.nextSnortingExtractionTick || this.isSnortingExtracting()) {
            return;
        }

        this.entityData.set(SNORTING_EXTRACTING, true);
        this.snortingExtractionStartTick = this.tickCount;
        this.snortingExtractionEndTick = this.tickCount + SNORTING_EXTRACTION_DURATION;
        this.nextSnortingExtractionTick = this.tickCount + 200;
        this.snortingItemPopped = false;
        this.getNavigation().stop();
    }

    private void tickSnortingExtraction() {
        if (!this.isSnortingExtracting()) {
            return;
        }

        if (!(this.getControllingPassenger() instanceof Player) || this.getHeadVariant() != ChimeraHeadVariant.SNORTING || this.tickCount >= this.snortingExtractionEndTick) {
            this.stopSnortingExtraction();
            return;
        }

        final int elapsed = this.tickCount - this.snortingExtractionStartTick;
        if (elapsed >= 3 && elapsed <= SNORTING_ITEM_TICK && elapsed % 3 == 0) {
            this.snortingParticles();
        }

        if (!this.snortingItemPopped && elapsed >= SNORTING_ITEM_TICK) {
            this.snortingItemPopped = true;
            this.popSnortingItem();
        }

    }

    private void snortingParticles() {
        final ServerLevel level = (ServerLevel) this.level();
        final double radius = this.getBbWidth() * 0.375;
        final double y = this.getBoundingBox().minY + 0.05;

        for (int i = 0; i < 10; i++) {
            final double angle = this.getRandom().nextDouble() * Mth.TWO_PI;
            final double distance = Math.sqrt(this.getRandom().nextDouble()) * radius;
            final double x = Mth.cos((float) angle) * distance;
            final double z = Mth.sin((float) angle) * distance;
            final Vec3 pos = new Vec3(this.getX() + x, y, this.getZ() + z);
            final BlockState particleState = SNORTING_PARTICLE_STATES.get(this.getRandom().nextInt(SNORTING_PARTICLE_STATES.size()));
            final BlockParticleOption particle = new BlockParticleOption(ParticleTypes.BLOCK, particleState);

            final Vec3 speed = new Vec3(x, 0, z).normalize().scale(0.25 + this.getRandom().nextDouble() * 0.25).add(0, 0.45 + this.getRandom().nextDouble() * 0.35, 0);
            level.sendParticles(particle, pos.x, pos.y, pos.z, 0, speed.x, speed.y, speed.z, 0.18);
        }

    }

    private void popSnortingItem() {
        final ServerLevel level = (ServerLevel) this.level();
        final float rotation = this.getYRot() * Mth.DEG_TO_RAD;
        final Vec3 forward = new Vec3(-Mth.sin(rotation), 0, Mth.cos(rotation));
        final Vec3 origin = new Vec3(this.getX(), this.getEyeY(), this.getZ()).add(forward.scale(this.getBbWidth() * 0.5 + 1));
        final Item item = SNORTING_ITEMS.get(this.getRandom().nextInt(SNORTING_ITEMS.size()));
        final ItemEntity drop = new ItemEntity(level, origin.x, origin.y, origin.z, new ItemStack(item));
        drop.setDeltaMovement(forward.scale(0.35).add(0, 0.28, 0));
        drop.setDefaultPickUpDelay();
        level.addFreshEntity(drop);
        this.playSound(SoundEvents.SNIFFER_DROP_SEED, 1, 1.05F + this.getRandom().nextFloat() * 0.1F);
    }

    private void stopSnortingExtraction() {
        this.entityData.set(SNORTING_EXTRACTING, false);
        this.snortingItemPopped = false;
    }

    private boolean isSnortingExtracting() {
        return this.entityData.get(SNORTING_EXTRACTING);
    }

    private void tryStartBeakedPeck() {
        if (this.tickCount < this.nextBeakedPeckTick || this.isBeakedPecking()) {
            return;
        }

        this.entityData.set(BEAKED_PECK_Y_ROT, this.getYRot());
        this.entityData.set(BEAKED_PECKING, true);
        this.beakedPeckStartTick = this.tickCount;
        this.beakedPeckEndTick = this.tickCount + BEAKED_PECK_DURATION;
        this.nextBeakedPeckTick = this.tickCount + 16;
        this.beakedPeckDamageApplied = false;
        this.getNavigation().stop();
    }

    private void tickBeakedPeck() {
        if (!this.isBeakedPecking()) {
            return;
        }

        if (!(this.getControllingPassenger() instanceof Player) || this.getHeadVariant() != ChimeraHeadVariant.BEAKED || this.tickCount >= this.beakedPeckEndTick) {
            this.stopBeakedPeck();
            return;
        }

        if (!this.beakedPeckDamageApplied && this.tickCount >= this.beakedPeckStartTick + 5) {
            this.beakedPeckDamageApplied = true;
            this.performBeakedPeck();
        }
    }

    /// TODO: refactor equivalent methods
    private void performBeakedPeck() {
        final ServerLevel level = (ServerLevel) this.level();
        final Vec3 direction = this.getBeakedPeckDirection();
        final AABB hitbox = this.getBoundingBox().expandTowards(direction.scale(2.8)).inflate(0.75, 0.5, 0.75);

        for (final LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, hitbox, entity ->
                !this.isChimeraAlly(entity) && !this.hasPassenger(entity)
                        && direction.dot(entity.position().subtract(this.position()).multiply(1, 0, 1)) > 0
        )) {
            target.hurtServer(level, level.damageSources().mobAttack(this), 3.5f);
            this.applyBeakedPoison(target);
        }

        this.playSound(SoundEvents.FOX_BITE, 0.9F, 1.25F + this.getRandom().nextFloat() * 0.15F);
    }

    private void stopBeakedPeck() {
        this.entityData.set(BEAKED_PECKING, false);
        this.beakedPeckDamageApplied = false;
    }

    private boolean isBeakedPecking() {
        return this.entityData.get(BEAKED_PECKING);
    }

    private Vec3 getBeakedPeckDirection() {
        final float rotation = this.entityData.get(BEAKED_PECK_Y_ROT) * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(rotation), 0.0D, Mth.cos(rotation));
    }

    private void tryStartShieldCharge() {
        final boolean correctHead = this.getHeadVariant() == ChimeraHeadVariant.SHIELDED;
        final boolean cooldownReady = this.tickCount >= this.nextShieldChargeTick;
        final boolean accepted = correctHead && cooldownReady && !this.isShieldCharging();
        if (!accepted) {
            return;
        }

        this.entityData.set(SHIELD_CHARGE_Y_ROT, this.getYRot());
        this.entityData.set(SHIELD_CHARGING, true);
        this.shieldChargeEndTick = this.tickCount + 8;
        this.nextShieldChargeTick = this.tickCount + 30;
        this.shieldChargeHitEntities.clear();
        this.getNavigation().stop();
        this.playSound(SoundEvents.RAVAGER_ATTACK, 0.85F, 1.15F);
    }

    private void tickShieldCharge() {
        if (!this.isShieldCharging()) {
            return;
        }

        final LivingEntity passenger = this.getControllingPassenger();
        if (!(passenger instanceof Player)) {
            this.stopShieldCharge();
            return;
        }
        if (this.getHeadVariant() != ChimeraHeadVariant.SHIELDED) {
            this.stopShieldCharge();
            return;
        }
        if (this.tickCount >= this.shieldChargeEndTick) {
            this.stopShieldCharge();
            return;
        }
        if (this.horizontalCollision) {
            this.stopShieldCharge();
            return;
        }

        final Vec3 direction = this.getShieldChargeDirection();
        final Vec3 movement = this.getDeltaMovement();
        this.setDeltaMovement(direction.x * SHIELDED_CHARGE_SPEED, movement.y, direction.z * SHIELDED_CHARGE_SPEED);
        this.needsSync = true;

        final ServerLevel level = (ServerLevel) this.level();
        final AABB hitbox = this.getBoundingBox().expandTowards(direction.scale(0.8D)).inflate(0.35D, 0.2D, 0.35D);
        for (final LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, hitbox, entity ->
                !this.isChimeraAlly(entity) && !this.hasPassenger(entity) && this.shieldChargeHitEntities.add(entity.getId())
        )) {
            final boolean hurt = this.doHurtTarget(level, target);
            if (hurt) {
                this.playSound(SoundEvents.GOAT_RAM_IMPACT, 1.0F, 0.9F + this.getRandom().nextFloat() * 0.12F);
            }

        }

    }

    private void stopShieldCharge() {
        final Vec3 movement = this.getDeltaMovement();
        this.entityData.set(SHIELD_CHARGING, false);
        this.setDeltaMovement(movement.x * 0.35D, movement.y, movement.z * 0.35D);
        this.shieldChargeHitEntities.clear();
    }

    private boolean isShieldCharging() {
        return this.entityData.get(SHIELD_CHARGING);
    }

    private Vec3 getShieldChargeDirection() {
        final float rotation = this.entityData.get(SHIELD_CHARGE_Y_ROT) * Mth.DEG_TO_RAD;
        return new Vec3(-Mth.sin(rotation), 0.0D, Mth.cos(rotation));
    }

    @Override
    protected void updateWalkAnimation(float movementDistance) {
        final float speed = (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
        final float animationSpeed = speed > 0 ? Mth.clamp(movementDistance / speed, 0, 1) : 0;
        this.walkAnimation.update(animationSpeed, 0.4F, 0.85f);
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
        if (this.getHeadVariant() == ChimeraHeadVariant.SNARLED) {
            return false;
        }

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
            case CRUNCHING, SNARLED, SNORTING -> { ;; }
            case SHIELDED -> target.knockback(1.6, this.getX() - target.getX(), this.getZ() - target.getZ());
            case BEAKED -> this.applyBeakedPoison(target);
        }

    }

    private void applyBeakedPoison(LivingEntity target) {
        target.forceAddEffect(new MobEffectInstance(MobEffects.POISON, 40 + this.getRandom().nextInt(145), 0), this);
        target.forceAddEffect(new MobEffectInstance(MobEffects.BLINDNESS, 40 + this.getRandom().nextInt(100), 0), this);
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

    public boolean isValidHostileTarget(LivingEntity target) {
        return this.isHostile() && target != this && target.isAlive() && !this.hasPassenger(target) && !(target instanceof ChimeraEntity chimera && chimera.isHostile());
    }

    public boolean isChimeraAlly(Entity entity) {
        return entity == this || this.considersEntityAsAlly(entity) || this.isHostile() && entity instanceof ChimeraEntity chimera && chimera.isHostile();
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return this.isHostile() ? this.isValidHostileTarget(target) && super.canAttack(target) : super.canAttack(target);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return NomenDubiumSounds.CHIMERA_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return NomenDubiumSounds.CHIMERA_DEATH.get();
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
        final int legacyMainAction = this.isOrderedToSit() ? ACTION_SIT : ACTION_FOLLOW;
        this.setMainAction(input.getIntOr("mainaction", legacyMainAction), null);
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
        output.putInt("mainaction", this.getMainAction());
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
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.22);
                this.setAttributeBase(Attributes.ARMOR, 4.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.75);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 0.48);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.25);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 4.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.1);
            }
            case SHELLED -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.145);
                this.setAttributeBase(Attributes.ARMOR, 10.0);
                this.setAttributeBase(Attributes.KNOCKBACK_RESISTANCE, 0.90);
                this.setAttributeBase(Attributes.JUMP_STRENGTH, 0.42);
                this.setAttributeBase(Attributes.STEP_HEIGHT, 1.0);
                this.setAttributeBase(Attributes.SAFE_FALL_DISTANCE, 4.0);
                this.setAttributeBase(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.5);
            }
            case AVIAN -> {
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.36);
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
                this.setAttributeBase(Attributes.MOVEMENT_SPEED, 0.22);
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
        double attackDamage = 6;
        attackDamage += switch (this.getHeadVariant()) {
            case CRUNCHING -> 2;
            case SHIELDED -> 1;
            case SNARLED, BEAKED -> 0;
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

}
