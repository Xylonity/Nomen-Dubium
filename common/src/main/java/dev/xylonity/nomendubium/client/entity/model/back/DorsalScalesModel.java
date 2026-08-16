package dev.xylonity.nomendubium.client.entity.model.back;

import dev.xylonity.nomendubium.client.entity.model.ModularDinoModelConnections;
import dev.xylonity.nomendubium.client.render.ModularDinoRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public final class DorsalScalesModel extends EntityModel<ModularDinoRenderState> {

	public DorsalScalesModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition dorsal_scales = partdefinition.addOrReplaceChild("dorsal_scales", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -32.0F, -13.0F, 0.0F, 32.0F, 18.0F, new CubeDeformation(0.0F)), ModularDinoModelConnections.alignToConnection(0.0F, 0.0F, 0.0F));

		PartDefinition body_connection = dorsal_scales.addOrReplaceChild("body_connection", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

}
