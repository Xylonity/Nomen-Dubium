package dev.xylonity.nomendubium.client.entity.model.chimera.normal.tail;

import dev.xylonity.nomendubium.client.entity.model.chimera.ChimeraModelConnections;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class SpearedTailModel extends ChimeraTailModel {

	public SpearedTailModel(ModelPart root) {
		super(root, "tail_tip", 1.1F);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition tail = partdefinition.addOrReplaceChild(
			"tail",
			CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 9.0F, 30.0F, new CubeDeformation(0.0F)),
			ChimeraModelConnections.alignToConnection(0.0F, 4.0F, 3.0F, 0.5236F, 0.0F, 0.0F)
		);

		PartDefinition tail_tip = tail.addOrReplaceChild("tail_tip", CubeListBuilder.create().texOffs(32, 55).addBox(-2.0F, -15.0F, 0.0F, 4.0F, 15.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(32, 39).addBox(-2.0F, -19.0F, -5.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 23.0F));

		PartDefinition stinger = tail_tip.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(0, 39).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(54, 55).addBox(-1.0F, 5.0F, -4.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 56).addBox(0.0F, 9.0F, -2.0F, 0.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, -5.0F));

		PartDefinition body_connection = tail.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 3.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

}
