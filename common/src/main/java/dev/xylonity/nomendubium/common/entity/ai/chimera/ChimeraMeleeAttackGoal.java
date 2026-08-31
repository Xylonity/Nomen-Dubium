package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraHeadVariant;
import dev.xylonity.nomendubium.common.entity.variant.ChimeraTailVariant;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public final class ChimeraMeleeAttackGoal extends MeleeAttackGoal {

    private final ChimeraEntity chimera;

    public ChimeraMeleeAttackGoal(ChimeraEntity chimera) {
        super(chimera, 1.15, true);
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return this.canAttack() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canAttack() && super.canContinueToUse();
    }

    @Override
    protected int getAttackInterval() {
        return this.chimera.getTailVariant() == ChimeraTailVariant.STUBBY ? 14 : 20;
    }

    private boolean canAttack() {
        return !this.chimera.isRoaring() && this.chimera.getHeadVariant() != ChimeraHeadVariant.SNARLED;
    }

}
