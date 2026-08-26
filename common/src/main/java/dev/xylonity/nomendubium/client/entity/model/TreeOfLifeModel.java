package dev.xylonity.nomendubium.client.entity.model;

import dev.xylonity.nomendubium.NomenDubium;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public final class TreeOfLifeModel extends EntityModel<LivingEntityRenderState> {

    public static final ModelLayerLocation LAYER_LOCATION =
        new ModelLayerLocation(NomenDubium.of("tree_of_life"), "main");

    public TreeOfLifeModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        PartDefinition trunk = root.addOrReplaceChild("trunk", CubeListBuilder.create()
            .texOffs(40, 48).addBox(-5.0F, -16.0F, -5.0F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(40, 74).addBox(-3.0F, -66.0F, -3.0F, 6.0F, 18.0F, 6.0F, new CubeDeformation(0.0F))
            .texOffs(0, 48).addBox(-5.0F, -89.0F, -7.0F, 10.0F, 23.0F, 10.0F, new CubeDeformation(0.0F))
            .texOffs(57, 99).addBox(5.0F, -115.0F, -5.0F, 16.0F, 28.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(84, 70).addBox(-12.0F, -89.0F, -7.0F, 7.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
            .texOffs(80, 58).addBox(0.0F, -89.0F, 3.0F, 5.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
            .texOffs(64, 74).addBox(-12.0F, -100.0F, -7.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
            .texOffs(104, 0).addBox(-14.0F, -122.0F, -4.5F, 12.0F, 22.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(104, -12).addBox(-8.0F, -122.0F, -10.5F, 0.0F, 22.0F, 12.0F, new CubeDeformation(0.0F))
            .texOffs(104, -12).addBox(1.0F, -127.0F, 1.5F, 0.0F, 27.0F, 12.0F, new CubeDeformation(0.0F))
            .texOffs(104, 0).addBox(-5.0F, -127.0F, 7.5F, 12.0F, 27.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(101, 37).addBox(0.0F, -102.0F, -12.0F, 0.0F, 14.0F, 9.0F, new CubeDeformation(0.0F))
            .texOffs(101, 46).addBox(-5.0F, -102.0F, -6.0F, 9.0F, 14.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(0, 97).addBox(10.0F, -99.0F, 0.5F, 19.0F, 22.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(0, 78).addBox(23.0F, -99.0F, -7.0F, 0.0F, 22.0F, 19.0F, new CubeDeformation(0.0F))
            .texOffs(0, 81).addBox(0.0F, -100.0F, 5.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
            .texOffs(0, 0).addBox(-8.0F, -48.0F, -8.0F, 16.0F, 32.0F, 16.0F, new CubeDeformation(0.0F))
            .texOffs(64, 0).addBox(-8.0F, -48.0F, -7.5F, 16.0F, 32.0F, 0.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 24.0F, 0.0F));

        trunk.addOrReplaceChild("left_branch", CubeListBuilder.create()
            .texOffs(80, 42).addBox(-19.0F, -13.4F, -2.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F))
            .texOffs(90, 80).addBox(-26.0F, -29.4F, 0.5F, 18.0F, 26.0F, 0.0F, new CubeDeformation(0.0F))
            .texOffs(90, 62).addBox(-16.0F, -29.4F, -8.5F, 0.0F, 26.0F, 18.0F, new CubeDeformation(0.0F))
            .texOffs(64, 32).addBox(-19.0F, -2.4F, -2.0F, 19.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-5.0F, -77.6F, 0.0F));

        trunk.addOrReplaceChild("right_branch", CubeListBuilder.create()
            .texOffs(80, 42).mirror().addBox(14.0F, -13.4F, -2.0F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
            .texOffs(64, 32).mirror().addBox(0.0F, -2.4F, -2.0F, 19.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false),
            PartPose.offset(5.0F, -72.6F, 0.0F));

        return LayerDefinition.create(meshDefinition, 128, 128);
    }

}
