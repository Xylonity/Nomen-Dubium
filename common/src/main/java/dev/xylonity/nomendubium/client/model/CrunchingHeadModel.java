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

public final class CrunchingHeadModel extends EntityModel<ModularCreatureRenderState> {

    public CrunchingHeadModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition entireHead = root.addOrReplaceChild(
            "entire_head",
            CubeListBuilder.create(),
            ModelConnections.alignHead(0.0F, -11.0F, 22.0F)
        );
        PartDefinition head = entireHead.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 8.0F));
        head.addOrReplaceChild(
            "Cranium",
            CubeListBuilder.create().texOffs(0, 47)
                .addBox(-3.0F, -19.0F, -29.0F, 6.0F, 5.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(52, 82).addBox(-8.0F, -19.0F, 1.0F, 16.0F, 10.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(72, 47).addBox(-3.0F, -14.0F, -29.0F, 6.0F, 5.0F, 30.0F, new CubeDeformation(0.05F))
                .texOffs(0, 104).addBox(-3.0F, -14.0F, -29.0F, 6.0F, 5.0F, 30.0F, new CubeDeformation(-0.05F)),
            PartPose.offset(0.0F, 10.0F, -6.0F)
        );
        head.addOrReplaceChild(
            "Jaw",
            CubeListBuilder.create().texOffs(76, 35)
                .addBox(-8.0F, 0.0F, 0.0F, 16.0F, 6.0F, 5.0F, new CubeDeformation(0.05F))
                .texOffs(0, 82).addBox(-3.0F, 4.0F, -20.0F, 6.0F, 2.0F, 20.0F, new CubeDeformation(0.0F))
                .texOffs(94, 82).addBox(-3.0F, 4.0F, -29.0F, 6.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -1.0F, -32.0F, 6.0F, 15.0F, 32.0F, new CubeDeformation(0.0F))
                .texOffs(119, 4).addBox(-3.0F, -1.0F, -32.0F, 6.0F, 15.0F, 32.0F, new CubeDeformation(-0.05F)),
            PartPose.offset(0.0F, 1.0F, -5.0F)
        );
        entireHead.addOrReplaceChild(
            "neck",
            CubeListBuilder.create().texOffs(76, 0)
                .addBox(-10.0F, -11.0F, -14.0F, 20.0F, 21.0F, 14.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -10.0F, 22.0F)
        );

        entireHead.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -11.0F, 22.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

}
