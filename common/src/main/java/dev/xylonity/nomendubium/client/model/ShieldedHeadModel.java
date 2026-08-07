package dev.xylonity.nomendubium.client.model;

import dev.xylonity.nomendubium.client.render.ModularCreatureRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ShieldedHeadModel extends EntityModel<ModularCreatureRenderState> {

    public ShieldedHeadModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition entireHead = root.addOrReplaceChild(
            "entire_head",
            CubeListBuilder.create(),
            ModelConnections.alignHead(0.0F, -19.0F, 16.0F)
        );
        entireHead.addOrReplaceChild(
            "Neck",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-8.0F, -30.0F, 3.0F, 16.0F, 23.0F, 13.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, 0.0F)
        );
        PartDefinition head = entireHead.addOrReplaceChild(
            "Head",
            CubeListBuilder.create().texOffs(0, 36)
                .addBox(-8.0F, -23.0F, -13.0F, 16.0F, 9.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(66, 46).addBox(5.0F, -14.0F, -13.0F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(66, 60).addBox(5.0F, -14.0F, -3.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.05F))
                .texOffs(80, 67).addBox(5.0F, -14.0F, -7.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 46).mirror()
                .addBox(-8.0F, -14.0F, -13.0F, 3.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 67).mirror()
                .addBox(-8.0F, -14.0F, -7.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(66, 60).mirror()
                .addBox(-8.0F, -14.0F, -3.0F, 3.0F, 8.0F, 4.0F, new CubeDeformation(0.05F)).mirror(false),
            PartPose.offset(0.0F, -7.0F, 2.0F)
        );
        head.addOrReplaceChild(
            "Jaw",
            CubeListBuilder.create().texOffs(58, 0)
                .addBox(-8.0F, 0.0F, -14.0F, 16.0F, 3.0F, 14.0F, new CubeDeformation(0.05F))
                .texOffs(66, 72).mirror()
                .addBox(-8.0F, -6.0F, -8.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 35).addBox(-5.0F, -8.0F, -14.0F, 10.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(66, 72).addBox(5.0F, -6.0F, -8.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 59).addBox(-1.0F, 3.0F, -8.0F, 2.0F, 9.0F, 19.0F, new CubeDeformation(0.0F))
                .texOffs(58, 17).addBox(0.0F, 12.0F, -7.0F, 0.0F, 1.0F, 17.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -6.0F, 1.0F)
        );
        head.addOrReplaceChild(
            "L_plate",
            CubeListBuilder.create().texOffs(42, 59).mirror()
                .addBox(-12.0F, -23.0F, -2.0F, 9.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 46).mirror()
                .addBox(-13.0F, -23.0F, -0.6F, 1.0F, 11.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(80, 60).mirror()
                .addBox(-7.0F, -27.0F, -2.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(-1.0F, -3.0F, 0.0F)
        );
        head.addOrReplaceChild(
            "R_plate",
            CubeListBuilder.create().texOffs(42, 59)
                .addBox(4.0F, -23.0F, -2.0F, 9.0F, 20.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(60, 46).addBox(13.0F, -23.0F, -0.6F, 1.0F, 11.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(80, 60).addBox(4.0F, -27.0F, -2.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -3.0F, 0.0F)
        );

        entireHead.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -19.0F, 16.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

}