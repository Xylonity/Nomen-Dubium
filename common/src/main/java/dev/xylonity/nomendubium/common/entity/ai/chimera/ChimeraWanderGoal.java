package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public final class ChimeraWanderGoal extends RandomStrollGoal {

    private final ChimeraEntity chimera;

    public ChimeraWanderGoal(ChimeraEntity chimera) {
        super(chimera, 0.85);
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return this.canWander() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.canWander() && super.canContinueToUse();
    }

    private boolean canWander() {
        return !this.chimera.isRoaring() && (!this.chimera.isTame() || this.chimera.getMainAction() == ChimeraEntity.ACTION_WANDER);
    }

}
