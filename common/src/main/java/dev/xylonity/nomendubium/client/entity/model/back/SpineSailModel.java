package dev.xylonity.nomendubium.client.entity.model.back;

import dev.xylonity.nomendubium.client.entity.model.ModularDinoModelConnections;
import dev.xylonity.nomendubium.client.render.ModularCreatureRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class SpineSailModel extends EntityModel<ModularCreatureRenderState> {

	public SpineSailModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sail = partdefinition.addOrReplaceChild("sail", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -11.0F, -17.0F, 2.0F, 11.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(36, 20).addBox(0.0F, -12.0F, -16.0F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(22, 46).addBox(0.0F, -13.0F, 8.0F, 0.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(22, 31).addBox(0.0F, -16.0F, -7.0F, 0.0F, 1.0F, 14.0F, new CubeDeformation(0.0F))
		.texOffs(0, 31).addBox(-1.0F, -12.0F, 8.0F, 2.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-1.0F, -15.0F, -8.0F, 2.0F, 15.0F, 16.0F, new CubeDeformation(0.0F)), ModularDinoModelConnections.alignToConnection(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = sail.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}
