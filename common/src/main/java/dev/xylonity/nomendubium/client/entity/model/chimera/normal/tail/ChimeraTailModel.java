package dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail;

import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Shared fast procedural animation wrapper for the different chimera tails
 */
public abstract class ChimeraTailModel extends EntityModel<ChimeraRenderState> {

    private final ModelPart tail;
    private final ModelPart tip;
    private final float weight;

    protected ChimeraTailModel(ModelPart root, String tipBone, float weight) {
        super(root);
        this.tail = root.getChild("tail");
        this.tip = this.tail.getChild(tipBone);
        this.weight = weight;
    }

    @Override
    public void setupAnim(ChimeraRenderState state) {
        super.setupAnim(state);

        final float movement = Mth.clamp(state.walkAnimationSpeed, 0.0F, 1.0F);
        final float idleWeight = 1.0F - movement;
        final float idlePhase = state.ageInTicks * 0.08F;
        final float walkPhase = state.walkAnimationPos * 0.72F;

        this.tail.yRot += Mth.sin(idlePhase) * 0.10F * idleWeight / this.weight;
        this.tail.yRot += Mth.sin(walkPhase) * 0.20F * movement / this.weight;
        this.tail.xRot += Mth.sin(idlePhase * 0.73F) * 0.025F / this.weight;

        this.tip.yRot += Mth.sin(idlePhase - 0.65F) * 0.14F * idleWeight / this.weight;
        this.tip.yRot += Mth.sin(walkPhase - 0.80F) * 0.28F * movement / this.weight;
        this.tip.xRot += Mth.sin(idlePhase * 0.73F - 0.45F) * 0.035F / this.weight;
    }

}
