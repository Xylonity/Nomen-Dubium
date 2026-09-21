package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

import dev.xylonity.knightlib.api.util.KnightLibEasings;
import dev.xylonity.nomendubium.client.entity.model.NomenDubiumEntityModel;
import dev.xylonity.nomendubium.common.entity.ChimeraEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

/**
 * Shared fast procedural animation wrapper for the different chimera heads
 */
public abstract class ChimeraHeadModel extends NomenDubiumEntityModel<ChimeraEntity> {

    protected final ModelPart head;
    protected final ModelPart jaw;

    private final float lookWeight;

    protected ChimeraHeadModel(ModelPart root, String headBone, float lookWeight, String... jawPath) {
        super(root);
        this.head = root.getChild("entire_head").getChild(headBone);
        this.lookWeight = lookWeight;

        ModelPart jawBone = this.head;
        for (final String child : jawPath) {
            jawBone = jawBone.getChild(child);
        }

        this.jaw = jawBone;
    }

    @Override
    public void setupAnim(ChimeraEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        final float partialTick = ageInTicks - entity.tickCount;

        final float sit = smoothstep(Mth.clamp(entity.getSitAnimation(partialTick), 0.0F, 1.0F));
        final float movement = Mth.clamp(limbSwingAmount, 0.0F, 1.0F) * (1.0F - sit);
        final float yaw = Mth.clamp(netHeadYaw, -50.0F, 50.0F) * Mth.DEG_TO_RAD;
        final float pitch = Mth.clamp(headPitch, -35.0F, 35.0F) * Mth.DEG_TO_RAD;

        this.head.yRot += yaw * 0.65F * this.lookWeight;
        this.head.xRot += pitch * 0.55F * this.lookWeight;
        this.head.xRot += Mth.sin(ageInTicks * 0.075F) * 0.018F;
        this.head.zRot += Mth.sin(ageInTicks * 0.055F) * 0.012F;
        this.head.xRot += Mth.cos(limbSwing * 1.2F) * 0.035F * movement;
        this.head.xRot += 0.10F * sit;
        this.head.yRot += Mth.sin(ageInTicks * 0.035F) * 0.025F * sit;

        final float jawCycle = 0.5F + 0.5F * Mth.sin(
            ageInTicks * 0.095F + Mth.sin(ageInTicks * 0.031F) * 0.65F
        );

        final float jawOpening = jawCycle * (0.040F + sit * 0.012F);
        this.jaw.xRot += jawOpening;
    }

    public static float smoothstep(float value) {
        return KnightLibEasings.SMOOTHSTEP.apply(value);
    }

}
