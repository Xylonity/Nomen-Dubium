package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class SnarledHeadModel extends ChimeraHeadModel {

    public SnarledHeadModel(ModelPart root) {
        super(root, "head_control", 0.85F);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition entireHead = root.addOrReplaceChild(
            "entire_head",
            CubeListBuilder.create(),
            ChimeraModelConnections.alignToConnection(0.0F, -4.0F, 8.0F)
        );
        PartDefinition headControl = entireHead.addOrReplaceChild("head_control", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = headControl.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
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
            PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        entireHead.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, 8.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

}
