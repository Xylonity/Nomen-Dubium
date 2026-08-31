package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import dev.xylonity.nomendubium.common.entity.ai.chimera.ChimeraRoarGoal;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class SnarledHeadModel extends ChimeraHeadModel {

    public SnarledHeadModel(ModelPart root) {
        super(root, "head_control", 0.85F, "Head", "Jaw");
    }

    @Override
    public void setupAnim(ChimeraRenderState state) {
        super.setupAnim(state);
        if (state.roarAnimation <= 0.0F) {
            return;
        }

        final float ticks = Mth.clamp(state.roarAnimation, 0.0F, ChimeraRoarGoal.DURATION_TICKS);
        final float in = smoothstep(Mth.clamp(ticks / 6.0F, 0.0F, 1.0F));
        final float out = smoothstep(Mth.clamp((ChimeraRoarGoal.DURATION_TICKS - ticks) / 8.0F, 0.0F, 1.0F));
        final float roar = Math.min(in, out);

        this.jaw.xRot += 0.62F * roar;
        this.head.xRot -= 0.10F * roar;
        this.head.yRot += Mth.sin(ticks * 0.75F) * 0.28F * roar;
        this.head.zRot += Mth.sin(ticks * 0.48F + 0.8F) * 0.09F * roar;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition entireHead = root.addOrReplaceChild(
            "entire_head",
            CubeListBuilder.create(),
            ChimeraModelConnections.alignToConnection(0.0F, -4.0F, 8.0F)
        );
        PartDefinition headControl = entireHead.addOrReplaceChild("head_control", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 8.0F));
        PartDefinition head = headControl.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -8.0F));
        head.addOrReplaceChild(
            "Cranium",
            CubeListBuilder.create().texOffs(0, 26)
                .addBox(-9.0F, -5.0F, -20.0F, 17.0F, 5.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(0, 51).addBox(-9.0F, 0.0F, -20.0F, 17.0F, 3.0F, 20.0F, new CubeDeformation(0.05F))
                .texOffs(0, 96).addBox(-9.0F, 0.0F, -20.0F, 17.0F, 3.0F, 20.0F, new CubeDeformation(-0.01F))
                .texOffs(74, 65).addBox(-4.0F, -9.0F, -20.0F, 7.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.5F, -46.0F, 2.0F)
        );
        head.addOrReplaceChild(
            "Jaw",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-9.0F, -4.0F, -20.0F, 17.0F, 6.0F, 20.0F, new CubeDeformation(0.05F))
                .texOffs(54, 76).addBox(-9.0F, -4.0F, -20.0F, 17.0F, 6.0F, 20.0F, new CubeDeformation(-0.01F))
                .texOffs(0, 74).addBox(-9.0F, 2.0F, -20.0F, 17.0F, 2.0F, 20.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.5F, -46.0F, 2.0F)
        );
        headControl.addOrReplaceChild(
            "Neck",
            CubeListBuilder.create().texOffs(74, 49)
                .addBox(-4.0F, -8.0F, 0.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(74, 0).addBox(-4.0F, -42.0F, -7.0F, 8.0F, 42.0F, 7.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 4.0F, -8.0F)
        );

        entireHead.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 8.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

}
