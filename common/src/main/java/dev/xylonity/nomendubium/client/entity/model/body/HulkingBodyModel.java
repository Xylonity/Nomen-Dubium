package dev.xylonity.nomendubium.client.entity.model.body;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class HulkingBodyModel extends ChimeraBodyModel {

    public HulkingBodyModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        body.addOrReplaceChild(
            "torso",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-15.0F, -18.0F, -24.0F, 30.0F, 33.0F, 47.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -30.0F, 1.0F)
        );
        body.addOrReplaceChild(
            "R_front_leg",
            CubeListBuilder.create().texOffs(60, 80).mirror()
                .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 25.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(12.0F, -19.0F, -14.0F)
        );
        body.addOrReplaceChild(
            "R_back_leg",
            CubeListBuilder.create().texOffs(0, 80).mirror()
                .addBox(-7.0F, -6.0F, -9.0F, 13.0F, 19.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(108, 80).mirror()
                .addBox(-7.0F, 7.0F, 0.0F, 11.0F, 18.0F, 11.0F, new CubeDeformation(0.025F)).mirror(false),
            PartPose.offset(15.0F, -25.0F, 12.0F)
        );
        body.addOrReplaceChild(
            "L_front_leg",
            CubeListBuilder.create().texOffs(60, 80)
                .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 25.0F, 12.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-12.0F, -19.0F, -14.0F)
        );
        body.addOrReplaceChild(
            "L_back_leg",
            CubeListBuilder.create().texOffs(0, 80)
                .addBox(-6.0F, -6.0F, -9.0F, 13.0F, 19.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(108, 80)
                .addBox(-4.0F, 7.0F, 0.0F, 11.0F, 18.0F, 11.0F, new CubeDeformation(0.025F)),
            PartPose.offset(-15.0F, -25.0F, 12.0F)
        );

        body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -32.0F, -23.0F));
        body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -30.0F, 24.0F));
        body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -48.0F, -1.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

}
