package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;

public final class ChimeraFollowOwnerGoal extends FollowOwnerGoal {

    private final ChimeraEntity chimera;

    public ChimeraFollowOwnerGoal(ChimeraEntity chimera) {
        super(chimera, 1.1, 8.0F, 3.0F);
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return this.shouldFollow() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return this.shouldFollow() && super.canContinueToUse();
    }

    private boolean shouldFollow() {
        return !this.chimera.isRoaring() && this.chimera.getMainAction() == ChimeraEntity.ACTION_FOLLOW;
    }

}
