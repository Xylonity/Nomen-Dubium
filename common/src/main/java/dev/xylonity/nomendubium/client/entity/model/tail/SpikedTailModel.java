package dev.xylonity.nomendubium.client.entity.model.tail;

import dev.xylonity.nomendubium.client.entity.model.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.render.ChimeraRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class SpikedTailModel extends EntityModel<ChimeraRenderState> {

    public SpikedTailModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition tail = root.addOrReplaceChild(
            "tail",
            CubeListBuilder.create().texOffs(0, 43)
                .addBox(-6.0F, -6.0F, 0.0F, 12.0F, 13.0F, 21.0F, new CubeDeformation(0.0F)),
            ChimeraModelConnections.alignToConnection(0.0F, 0.0F, 0.0F)
        );
        tail.addOrReplaceChild(
            "tail_tip",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-3.0F, -3.0F, -4.0F, 6.0F, 7.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(66, 51).addBox(3.0F, -3.0F, 26.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 43).addBox(3.0F, -3.0F, 18.0F, 14.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(66, 51).mirror()
                .addBox(-13.0F, -3.0F, 26.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(66, 43).mirror()
                .addBox(-17.0F, -3.0F, 18.0F, 14.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(0.0F, -1.0F, 22.0F)
        );

        tail.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(mesh, 128, 128);
    }

}
