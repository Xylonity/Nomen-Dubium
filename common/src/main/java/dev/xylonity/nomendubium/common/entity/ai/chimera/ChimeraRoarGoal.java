package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.registry.NomenDubiumSounds;
import java.util.EnumSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public final class ChimeraRoarGoal extends Goal {

    public static final int DURATION_TICKS = 50;
    private static final int COOLDOWN_TICKS = 200;

    private final ChimeraEntity chimera;

    private int nextRoarTick;
    private int roarEndTick;
    private int ownerAttackTimestamp;

    private LivingEntity roarTarget;

    public ChimeraRoarGoal(ChimeraEntity chimera) {
        this.chimera = chimera;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.chimera.getHeadVariant() != ChimeraHeadVariant.SNARLED || this.chimera.isRoaring() || this.chimera.tickCount < this.nextRoarTick) {
            return false;
        }

        this.roarTarget = this.findRoarTarget();

        return this.roarTarget != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.chimera.isRoaring() && this.chimera.getHeadVariant() == ChimeraHeadVariant.SNARLED && this.chimera.tickCount < this.roarEndTick;
    }

    @Override
    public void start() {
        this.roarEndTick = this.chimera.tickCount + DURATION_TICKS;
        this.nextRoarTick = this.chimera.tickCount + COOLDOWN_TICKS;

        this.chimera.setTarget(this.roarTarget);
        this.chimera.setRoaring(true);
        this.chimera.getNavigation().stop();
        this.chimera.playSound(NomenDubiumSounds.CHIMERA_ROAR.get(), 1.2F, 0.95F + this.chimera.getRandom().nextFloat() * 0.1F);

        final ServerLevel level = getServerLevel(this.chimera);
        for (final LivingEntity ally : level.getEntitiesOfClass(LivingEntity.class, this.chimera.getBoundingBox()
                .inflate(10), entity -> this.chimera.isChimeraAlly(entity)
        )) {
            ally.addEffect(new MobEffectInstance(MobEffects.STRENGTH, COOLDOWN_TICKS, 0), this.chimera);
        }

    }

    @Override
    public void stop() {
        this.chimera.setRoaring(false);
        this.roarTarget = null;
    }

    @Override
    public void tick() {
        this.chimera.getNavigation().stop();
        if (this.roarTarget != null && this.roarTarget.isAlive()) {
            this.chimera.getLookControl().setLookAt(this.roarTarget, 30.0F, 30.0F);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private LivingEntity findRoarTarget() {
        if (this.chimera.isTame()) {
            final LivingEntity owner = this.chimera.getOwner();
            if (owner != null) {
                final int timestamp = owner.getLastHurtMobTimestamp();
                if (timestamp != this.ownerAttackTimestamp) {
                    this.ownerAttackTimestamp = timestamp;

                    final LivingEntity ownerTarget = owner.getLastHurtMob();
                    if (ownerTarget != null && ownerTarget.isAlive() && this.chimera.wantsToAttack(ownerTarget, owner)) {
                        return ownerTarget;
                    }

                }

            }

        }

        final LivingEntity target = this.chimera.getTarget();
        return target != null && target.isAlive() ? target : null;
    }

}
