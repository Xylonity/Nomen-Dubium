package dev.xylonity.nomendubium.client.entity.model.chimera.skeleton.body;

import dev.xylonity.nomendubium.client.projectile.renderer.SkeletonPartRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class HulkingSkeletonModel extends EntityModel<SkeletonPartRenderState> {

    public HulkingSkeletonModel(ModelPart root) {
        super(root);
    }

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition torso = Body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, -14.0F, -23.0F, 30.0F, 23.0F, 35.0F, new CubeDeformation(0.0F))
		.texOffs(0, 58).addBox(-1.0F, -16.0F, -24.0F, 2.0F, 3.0F, 47.0F, new CubeDeformation(0.0F))
		.texOffs(98, 58).addBox(0.0F, -19.0F, -23.0F, 0.0F, 3.0F, 44.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -30.0F, 1.0F));

		PartDefinition R_front_leg = Body.addOrReplaceChild("R_front_leg", CubeListBuilder.create().texOffs(0, 108).mirror().addBox(-6.0F, -6.0F, -6.0F, 12.0F, 25.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(48, 108).addBox(3.1F, -24.0F, -9.0F, 0.0F, 18.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -19.0F, -14.0F));

		PartDefinition R_back_leg = Body.addOrReplaceChild("R_back_leg", CubeListBuilder.create().texOffs(98, 105).mirror().addBox(-7.0F, -6.0F, -9.0F, 13.0F, 19.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(130, 0).mirror().addBox(-7.0F, 7.0F, 0.0F, 11.0F, 18.0F, 11.0F, new CubeDeformation(0.025F)).mirror(false), PartPose.offset(15.0F, -25.0F, 12.0F));

		PartDefinition L_front_leg = Body.addOrReplaceChild("L_front_leg", CubeListBuilder.create().texOffs(0, 108).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 25.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(48, 108).addBox(-3.1F, -24.0F, -9.0F, 0.0F, 18.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.0F, -19.0F, -14.0F));

		PartDefinition L_back_leg = Body.addOrReplaceChild("L_back_leg", CubeListBuilder.create().texOffs(98, 105).addBox(-6.0F, -6.0F, -9.0F, 13.0F, 19.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(0, 148).addBox(0.0F, -22.0F, -4.9F, 30.0F, 16.0F, 17.0F, new CubeDeformation(0.0F))
		.texOffs(130, 0).addBox(-4.0F, 7.0F, 0.0F, 11.0F, 18.0F, 11.0F, new CubeDeformation(0.025F)), PartPose.offset(-15.0F, -25.0F, 12.0F));

		PartDefinition head_connection = Body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -44.5F, -23.0F));

		PartDefinition tail_connection = Body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -44.5F, 24.0F));

		PartDefinition extra_connection = Body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -48.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
