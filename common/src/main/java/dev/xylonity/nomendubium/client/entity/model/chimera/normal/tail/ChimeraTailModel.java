package dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail;

import dev.xylonity.nomendubium.client.entity.model.NomenDubiumEntityModel;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Shared fast procedural animation wrapper for the different chimera tails
 */
public abstract class ChimeraTailModel extends NomenDubiumEntityModel<ChimeraEntity> {

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
    public void setupAnim(ChimeraEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        final float partialTick = ageInTicks - entity.tickCount;

        final float sit = smoothstep(Mth.clamp(entity.getSitAnimation(partialTick), 0.0F, 1.0F));
        final float movement = Mth.clamp(limbSwingAmount, 0.0F, 1.0F) * (1.0F - sit);
        final float idleWeight = 1.0F - movement;
        final float idlePhase = ageInTicks * 0.08F;
        final float walkPhase = limbSwing * 0.72F;

        this.tail.yRot += Mth.sin(idlePhase) * 0.10F * idleWeight / this.weight;
        this.tail.yRot += Mth.sin(walkPhase) * 0.20F * movement / this.weight;
        this.tail.xRot += Mth.sin(idlePhase * 0.73F) * 0.025F / this.weight;

        this.tip.yRot += Mth.sin(idlePhase - 0.65F) * 0.14F * idleWeight / this.weight;
        this.tip.yRot += Mth.sin(walkPhase - 0.80F) * 0.28F * movement / this.weight;
        this.tip.xRot += Mth.sin(idlePhase * 0.73F - 0.45F) * 0.035F / this.weight;

        this.tail.xRot += 0.18F * sit;
        this.tail.yRot += Mth.sin(ageInTicks * 0.045F) * 0.08F * sit / this.weight;
        this.tip.xRot += 0.22F * sit;
    }

    private static float smoothstep(float value) {
        return value * value * (3.0F - 2.0F * value);
    }

}
