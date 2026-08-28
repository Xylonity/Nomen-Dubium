package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Shared fast procedural animation wrapper for the different chimera heads
 */
public abstract class ChimeraHeadModel extends EntityModel<ChimeraRenderState> {

    private final ModelPart head;
    private final float lookWeight;

    protected ChimeraHeadModel(ModelPart root, String headBone, float lookWeight) {
        super(root);
        this.head = root.getChild("entire_head").getChild(headBone);
        this.lookWeight = lookWeight;
    }

    @Override
    public void setupAnim(ChimeraRenderState state) {
        super.setupAnim(state);

        final float movement = Mth.clamp(state.walkAnimationSpeed, 0.0F, 1.0F);
        final float yaw = Mth.clamp(state.headYaw, -50.0F, 50.0F) * Mth.DEG_TO_RAD;
        final float pitch = Mth.clamp(state.headPitch, -35.0F, 35.0F) * Mth.DEG_TO_RAD;

        this.head.yRot += yaw * 0.65F * this.lookWeight;
        this.head.xRot += pitch * 0.55F * this.lookWeight;
        this.head.xRot += Mth.sin(state.ageInTicks * 0.075F) * 0.018F;
        this.head.zRot += Mth.sin(state.ageInTicks * 0.055F) * 0.012F;
        this.head.xRot += Mth.cos(state.walkAnimationPos * 1.2F) * 0.035F * movement;
    }

}