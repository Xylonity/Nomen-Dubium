package dev.xylonity.nomendubium.common.entity.ai.chimera;

import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;

public final class ChimeraSitGoal extends SitWhenOrderedToGoal {

    private final ChimeraEntity chimera;

    public ChimeraSitGoal(ChimeraEntity chimera) {
        super(chimera);
        this.chimera = chimera;
    }

    @Override
    public boolean canUse() {
        return !this.chimera.isVehicle() && this.chimera.getMainAction() == ChimeraEntity.ACTION_SIT && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.chimera.isVehicle() && this.chimera.getMainAction() == ChimeraEntity.ACTION_SIT && super.canContinueToUse();
    }

}
