package dev.xylonity.nomendubium.client.projectile.model;

import dev.xylonity.nomendubium.client.projectile.renderer.HuntersArrowRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

public final class HuntersArrowModel extends EntityModel<HuntersArrowRenderState> {

    public HuntersArrowModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutCull);
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

    @Override
    public void setupAnim(HuntersArrowRenderState state) {
        super.setupAnim(state);
        if (state.shake > 0.0F) {
            float shakeRotation = -Mth.sin(state.shake * 3.0F) * state.shake;
            root.zRot += shakeRotation * Mth.DEG_TO_RAD;
        }

    }

}
