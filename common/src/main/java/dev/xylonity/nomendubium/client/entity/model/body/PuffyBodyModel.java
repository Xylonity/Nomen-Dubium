package dev.xylonity.nomendubium.client.entity.model.body;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class PuffyBodyModel extends ModularDinoBodyModel {

	public PuffyBodyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition front_left_leg = body.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(163, 44).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -9.0F, -15.0F));

		PartDefinition front_right_leg = body.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(131, 44).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -9.0F, -15.0F));

		PartDefinition middle_left_leg = body.addOrReplaceChild("middle_left_leg", CubeListBuilder.create().texOffs(163, 62).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -9.0F, 0.0F));

		PartDefinition middle_right_leg = body.addOrReplaceChild("middle_right_leg", CubeListBuilder.create().texOffs(131, 62).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -9.0F, 0.0F));

		PartDefinition back_left_leg = body.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(163, 80).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(7.5F, -9.0F, 15.0F));

		PartDefinition back_right_leg = body.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(131, 79).addBox(-3.5F, -1.0F, -4.0F, 7.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.5F, -9.0F, 15.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(65, 0).addBox(-12.5F, 12.0F, -20.0F, 25.0F, 0.0F, 40.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-12.5F, -14.0F, -20.0F, 25.0F, 29.0F, 40.0F, new CubeDeformation(0.0F))
		.texOffs(0, 69).addBox(-12.5F, -14.0F, -20.0F, 25.0F, 24.0F, 40.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -19.0F, 0.0F));

		PartDefinition extra_connection = body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -34.0F, 1.0F));

		PartDefinition tail_connection = body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 20.0F));

		PartDefinition head_connection = body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, -20.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
