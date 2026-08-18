package dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import dev.xylonity.nomendubium.client.entity.render.chimera.ChimeraRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class StubbyTailModel extends EntityModel<ChimeraRenderState> {

    public StubbyTailModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition tail = root.addOrReplaceChild(
            "tail",
            CubeListBuilder.create().texOffs(0, 0)
                .addBox(-6.0F, -10.0F, -6.0F, 12.0F, 13.0F, 13.0F, new CubeDeformation(0.0F)),
            ChimeraModelConnections.alignToConnection(0.0F, -4.0F, -6.0F)
        );
        tail.addOrReplaceChild(
            "tail_tip",
            CubeListBuilder.create().texOffs(0, 26)
                .addBox(-3.0F, -4.0F, -1.0F, 6.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -4.0F, 8.0F)
        );

        tail.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -4.0F, -6.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

}
