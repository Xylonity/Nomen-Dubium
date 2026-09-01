package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public final class BeakedHeadModel extends ChimeraHeadModel {

    public BeakedHeadModel(ModelPart root) {
        super(root, "head", 1, "jaw");
    }

    @Override
    public void setupAnim(ChimeraRenderState state) {
        super.setupAnim(state);
        final float progress = Mth.clamp(state.beakedPeckProgress, 0, 1);
        final float sin = Mth.sin(progress * Mth.PI);
        this.head.z -= sin * 5;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition entireHead = root.addOrReplaceChild(
            "entire_head",
            CubeListBuilder.create(),
            ChimeraModelConnections.alignToConnection(0, -8, 3)
        );
        entireHead.addOrReplaceChild(
            "neck",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-5.0F, -20.0F, -8.0F, 11.0F, 28.0F, 14.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-0.5F, -8.0F, 1.0F)
        );
        PartDefinition head = entireHead.addOrReplaceChild(
            "head",
            CubeListBuilder.create().texOffs(54, 33)
                .addBox(-15.0F, -24.0F, 3.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(54, 33).mirror()
                .addBox(6.0F, -24.0F, 3.0F, 10.0F, 32.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(-0.5F, -18.1F, -6.0F)
        );
        head.addOrReplaceChild(
            "cranium",
            CubeListBuilder.create().texOffs(0, 42)
                .addBox(-7.0F, 0.0F, -12.0F, 15.0F, 7.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(72, 65).addBox(-7.0F, 7.0F, -5.0F, 15.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(72, 73).addBox(-6.9F, 0.0F, -11.0F, 0.0F, 15.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 71).addBox(-2.0F, 0.0F, -23.0F, 5.0F, 6.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(74, 43).addBox(-2.0F, 0.0F, -27.0F, 5.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(74, 33).addBox(-4.0F, 6.0F, -22.0F, 2.0F, 0.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(74, 33).mirror()
                .addBox(3.0F, 6.099F, -22.0F, 2.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(0.0F, -10.0F, 3.0F)
        );
        head.addOrReplaceChild(
            "jaw",
            CubeListBuilder.create().texOffs(50, 18)
                .addBox(-7.0F, -3.0F, -9.0F, 15.0F, 8.0F, 7.0F, new CubeDeformation(0.05F))
                .texOffs(40, 65).addBox(-5.0F, -3.9F, -20.0F, 3.0F, 4.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(50, 0).addBox(-5.0F, 0.0F, -22.0F, 11.0F, 3.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(0, 61).addBox(-7.0F, 0.0F, -2.0F, 15.0F, 5.0F, 5.0F, new CubeDeformation(0.05F))
                .texOffs(40, 65).mirror()
                .addBox(3.0F, -3.9F, -20.0F, 3.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        entireHead.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, 3.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

}
