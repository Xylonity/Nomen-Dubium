package dev.xylonity.nomendubium.client.entity.model.chimera.normal.head;

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

public final class SnortingHeadModel extends EntityModel<ChimeraRenderState> {

	public SnortingHeadModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition entire_head = partdefinition.addOrReplaceChild(
			"entire_head",
			CubeListBuilder.create(),
			ChimeraModelConnections.alignToConnection(0.0F, -12.0F, -20.0F)
		);

		PartDefinition right_ear = entire_head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(0, 50).addBox(-1.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.51F, -20.0F, -24.0F));

		PartDefinition left_ear = entire_head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(48, 0).addBox(0.0F, 0.0F, -3.0F, 1.0F, 19.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(6.51F, -20.0F, -24.0F));

		PartDefinition nose = entire_head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(44, 40).addBox(-6.5F, -2.0F, -9.0F, 13.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, -31.0F));

		PartDefinition lower_beak = entire_head.addOrReplaceChild("lower_beak", CubeListBuilder.create().texOffs(0, 29).addBox(-6.5F, -7.0F, -8.0F, 13.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.0F, -32.0F));

		PartDefinition head = entire_head.addOrReplaceChild("head", CubeListBuilder.create().texOffs(44, 29).addBox(-6.5F, 7.5F, -11.5F, 13.0F, 0.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-6.5F, -7.5F, -11.5F, 13.0F, 18.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.5F, -19.5F));

		PartDefinition body_connection = entire_head.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -20.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
