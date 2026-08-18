package dev.xylonity.nomendubium.client.entity.model.chimera.normal.body;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class ShelledBodyModel extends ChimeraBodyModel {

	public ShelledBodyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition L_front_leg = body.addOrReplaceChild("L_front_leg", CubeListBuilder.create().texOffs(182, 50).mirror().addBox(-5.0F, 0.0F, -6.0F, 10.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(182, 94).mirror().addBox(-5.0F, 10.0F, 5.0F, 10.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(16.0F, -12.0F, 21.0F));

		PartDefinition L_back_leg = body.addOrReplaceChild("L_back_leg", CubeListBuilder.create().texOffs(182, 73).mirror().addBox(-5.0F, 0.0F, -6.0F, 10.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(182, 94).mirror().addBox(-5.0F, 8.0F, 5.0F, 10.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(16.0F, -10.0F, -17.0F));

		PartDefinition R_back_leg = body.addOrReplaceChild("R_back_leg", CubeListBuilder.create().texOffs(182, 73).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 10.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(182, 94).addBox(-5.0F, 8.0F, 5.0F, 10.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.0F, -10.0F, -17.0F));

		PartDefinition R_front_leg = body.addOrReplaceChild("R_front_leg", CubeListBuilder.create().texOffs(182, 50).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 12.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(182, 94).addBox(-5.0F, 10.0F, 5.0F, 10.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-16.0F, -12.0F, 21.0F));

		PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 147).addBox(-18.0F, -19.0F, -30.0F, 36.0F, 18.0F, 47.0F, new CubeDeformation(0.0F))
		.texOffs(0, 81).addBox(-20.0F, -34.0F, -32.0F, 40.0F, 15.0F, 51.0F, new CubeDeformation(0.0F))
		.texOffs(182, 26).addBox(-20.0F, -19.0F, 5.0F, 10.0F, 10.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(166, 147).addBox(-20.0F, -19.0F, -18.0F, 10.0F, 15.0F, 23.0F, new CubeDeformation(0.0F))
		.texOffs(182, 0).addBox(-20.0F, -19.0F, -32.0F, 10.0F, 12.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-20.0F, -34.0F, -32.0F, 40.0F, 30.0F, 51.0F, new CubeDeformation(0.5F))
		.texOffs(182, 26).mirror().addBox(10.0F, -19.0F, 5.0F, 10.0F, 10.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(166, 147).mirror().addBox(10.0F, -19.0F, -18.0F, 10.0F, 15.0F, 23.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(182, 0).mirror().addBox(10.0F, -19.0F, -32.0F, 10.0F, 12.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -5.0F, 8.0F));

		PartDefinition head_connection = body.addOrReplaceChild("head_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 25.0F));

		PartDefinition tail_connection = body.addOrReplaceChild("tail_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -8.0F, -21.0F));

		PartDefinition extra_connection = body.addOrReplaceChild("extra_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -38.0F, 2.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

}
