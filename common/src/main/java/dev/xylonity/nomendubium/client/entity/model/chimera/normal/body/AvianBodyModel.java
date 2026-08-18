package dev.xylonity.nomendubium.client.entity.model.chimera.normal.body;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class AvianBodyModel extends ChimeraBodyModel {

	public AvianBodyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -22.0F, -18.0F, 20.0F, 22.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -22.0F, 0.0F));

		PartDefinition front_plume2 = torso.addOrReplaceChild("front_plume2", CubeListBuilder.create().texOffs(106, 84).mirror().addBox(0.0F, -16.0F, -6.0F, 0.0F, 33.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -12.0F, -18.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition front_plume = torso.addOrReplaceChild("front_plume", CubeListBuilder.create().texOffs(106, 84).addBox(0.0F, -16.0F, -6.0F, 0.0F, 33.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.0F, -18.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition back_plume2 = torso.addOrReplaceChild("back_plume2", CubeListBuilder.create().texOffs(0, 90).mirror().addBox(0.0F, -8.5F, 0.0F, 0.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -15.5F, 18.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition back_plume = torso.addOrReplaceChild("back_plume", CubeListBuilder.create().texOffs(0, 90).addBox(0.0F, -8.5F, 0.0F, 0.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -15.5F, 18.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition R_arm = body.addOrReplaceChild("R_arm", CubeListBuilder.create().texOffs(64, 95).mirror().addBox(1.0F, -2.0F, 2.5F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(88, 58).mirror().addBox(0.0F, -3.0F, -2.5F, 4.0F, 15.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(78, 95).mirror().addBox(0.0F, 8.0F, -6.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(12, 90).mirror().addBox(0.0F, 12.0F, -6.5F, 4.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, -27.0F, -11.5F));

		PartDefinition L_arm = body.addOrReplaceChild("L_arm", CubeListBuilder.create().texOffs(64, 95).addBox(-4.0F, -2.0F, 2.5F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(88, 58).addBox(-4.0F, -3.0F, -2.5F, 4.0F, 15.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(78, 95).addBox(-4.0F, 8.0F, -6.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(12, 90).addBox(-4.0F, 12.0F, -6.5F, 4.0F, 7.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -27.0F, -11.5F));

		PartDefinition R_leg = body.addOrReplaceChild("R_leg", CubeListBuilder.create().texOffs(0, 58).mirror().addBox(-5.0F, -4.0F, -6.0F, 9.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.0F, -30.0F, 8.0F));

		PartDefinition R_calf = R_leg.addOrReplaceChild("R_calf", CubeListBuilder.create().texOffs(64, 58).mirror().addBox(-3.0F, -4.0F, -3.0F, 6.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 14.0F, 5.0F));

		PartDefinition R_foot = R_calf.addOrReplaceChild("R_foot", CubeListBuilder.create().texOffs(64, 84).mirror().addBox(-5.0F, 0.0F, -6.0F, 10.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(88, 78).mirror().addBox(-5.0F, 0.1F, -8.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 13.9F, -2.0F));

		PartDefinition L_leg = body.addOrReplaceChild("L_leg", CubeListBuilder.create().texOffs(0, 58).addBox(-4.0F, -4.0F, -6.0F, 9.0F, 20.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.0F, -30.0F, 8.0F));

		PartDefinition L_calf = L_leg.addOrReplaceChild("L_calf", CubeListBuilder.create().texOffs(64, 58).addBox(-3.0F, -4.0F, -3.0F, 6.0F, 20.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, 14.0F, 5.0F));

		PartDefinition L_foot = L_calf.addOrReplaceChild("L_foot", CubeListBuilder.create().texOffs(64, 84).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 2.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(88, 78).addBox(-5.0F, 0.1F, -8.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.9F, -2.0F));

		PartDefinition extra_connection = body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -44.0F, -1.0F));

		PartDefinition tail_connection = body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -36.0F, 18.0F));

		PartDefinition head_connection = body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -33.0F, -18.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
