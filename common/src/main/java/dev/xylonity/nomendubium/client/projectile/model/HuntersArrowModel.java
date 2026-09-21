package dev.xylonity.nomendubium.client.projectile.model;

import dev.xylonity.nomendubium.client.entity.model.NomenDubiumEntityModel;
import dev.xylonity.nomendubium.common.entity.HuntersArrowEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public final class HuntersArrowModel extends NomenDubiumEntityModel<HuntersArrowEntity> {

    public HuntersArrowModel(ModelPart root) {
        super(root, RenderType::entityCutout);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
            "back",
            CubeListBuilder.create()
                .texOffs(0, 7)
                .addBox(0.0F, -3.5F, -3.0F, 0.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(-7.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "cross_1",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-15.0F, -3.5F, 0.5F, 21.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -2.3562F, 0.0F, 0.0F)
        );
        root.addOrReplaceChild(
            "cross_2",
            CubeListBuilder.create()
                .texOffs(0, 0)
                .addBox(-15.0F, -3.0F, 0.0F, 21.0F, 7.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F)
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

}
